package com.shenko.golfgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.GridPoint3;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GolfBall {

	private GolfGameLevel Level;
	private GolfGameScreen Screen;
	
	public Vector3 Location;
	public Vector3 Velocity;
	public int Scale;
	public int FlightSpeed;
	public float Spin;
	
	enum ELie { Tee, Rough, Fairway, Green, Bunker };
	enum EMode { Flying, Rolling, Putt };
	
	public EMode Mode;	
	public ELie Lie;
	
	public Texture BallTexture;
	public Circle BallCircle;
	
	private boolean DoFriction;
	private float FrictionTime;
	private String LastTerrain;
	
	public boolean OnGreen, InWater, HitTree, InHole;
	
	private Sound SHitDriver, SHitIron, SHitSW, SHitSand, SPutt, SBounce, SInHole, SSplash, STree;
	
	public GolfBall(GolfGameLevel inLevel, GolfGameScreen inScreen)
	{
		Level = inLevel;
		Screen = inScreen;
		
		SHitDriver = Gdx.audio.newSound(Gdx.files.internal("data/sound/hit_driver.wav"));
		SHitIron = Gdx.audio.newSound(Gdx.files.internal("data/sound/hit_iron.wav"));
		SHitSW = Gdx.audio.newSound(Gdx.files.internal("data/sound/hit_sw.wav"));
		SHitSand = Gdx.audio.newSound(Gdx.files.internal("data/sound/hit_sand.wav"));
		SPutt = Gdx.audio.newSound(Gdx.files.internal("data/sound/hit_putt.wav"));
		SBounce = Gdx.audio.newSound(Gdx.files.internal("data/sound/ballbounce.wav"));
		SInHole = Gdx.audio.newSound(Gdx.files.internal("data/sound/inhole.wav"));
		SSplash = Gdx.audio.newSound(Gdx.files.internal("data/sound/splash.wav"));
		STree = Gdx.audio.newSound(Gdx.files.internal("data/sound/treehit.wav"));
		
		Location = new Vector3();
		Velocity = new Vector3();
		
		Scale = 16;
		Spin = 0;
		
		Lie = ELie.Tee;
		OnGreen = false;
		HitTree = false;
		InHole = false;
		DoFriction = true;
		FrictionTime = 0f;
		LastTerrain = "Tee";
		
		BallTexture = new Texture(Gdx.files.internal("data/golfball.tga"));
		
		BallCircle = new Circle(0, 0, 5);
	}
	
	public void Update(float Delta)
	{
		if (!InHole && !InWater)
		{
			if (Mode == EMode.Flying || Mode == EMode.Rolling)
			{
				if (LastTerrain != Level.GetTerrain(new GridPoint2((int)Location.x, (int)Location.y)))
				{
					DoFriction = true;
				}
				
				// Gravity and update for spin
				Velocity.z -= 1f;
				Velocity.x += Spin;
				
				// Update position from velocity
				Location.x += Velocity.x * Delta;
				Location.y += Velocity.y * Delta;
				Location.z += Velocity.z * Delta;
				
				// Update collision circle
				BallCircle.setPosition(Location.x, Location.y);
				
				
				if ((Location.x > Level.GetWidth() * 64 || Location.x < 0 || Location.y > Level.GetHeight() * 64 || Location.y < 0) 
						|| Level.CheckOOB( new GridPoint2( (int)Location.x, (int)Location.y ) ) )
				{
					// Off map
					//Screen.ResetBall();
					Screen.BallFinished(true, false);
				}
				
				// Update drawscale for Z height
				Scale = (int)(16 + (Location.z / 2));
				
				// Check for tree collision
				for (int i=0; i < Level.TreeCircle.length; i++)
				{
					if (Location.z > 10 && Location.z < Level.TreeCircle[i].radius * 2 && Level.TreeCircle[i].overlaps(BallCircle) && !HitTree)
					{
						// Ball hit a tree
						STree.play();
						HitTree = true;
						Velocity.x *= 0.2;
						Velocity.y *= 0.2;
						Velocity.z = -2;
						
					//	System.out.print("Hit a tree you dumbass\n");
					}
				}
				
				if (Location.z < 0)
				{
					// Check for water hazard
					if (Level.GetTerrain(new GridPoint2((int)Location.x, (int)Location.y)) == "Water")
					{
						SSplash.play();
						InWater = true;
						Screen.BallFinished(false, true);
					}
					
					// Invert Z velocity for bounce
					Location.z = 0.5f;
					Velocity.z *= -1f;
					// Reduce by bounce factor
					Velocity.z *= Level.GetBounceFactor(Level.GetTerrain(new GridPoint2((int)Location.x, (int)Location.y)));
					// Bounce sound
					SBounce.play();
					
					if (Velocity.z < 1)
					{
						Mode = EMode.Rolling;
					}
					

					if (Mode == EMode.Flying)
					{
						// Apply bouncing mode friction
						Velocity.x *= Level.GetFrictionFactor(Level.GetTerrain(new GridPoint2((int)Location.x, (int)Location.y)));
						Velocity.y *= Level.GetFrictionFactor(Level.GetTerrain(new GridPoint2((int)Location.x, (int)Location.y)));
					}
					else if (Mode == EMode.Rolling)
					{
						if (DoFriction)
						{
						// Apply rolling mode friction
						Velocity.x *= Level.GetFrictionFactor(Level.GetTerrain(new GridPoint2((int)Location.x,(int)Location.y)));
						Velocity.y *= Level.GetFrictionFactor(Level.GetTerrain(new GridPoint2((int)Location.x, (int)Location.y)));
						DoFriction = false;
						FrictionTime = 0f;
						}
						else
						{
							FrictionTime += Delta;
							if (FrictionTime > 0.05)
							{
								DoFriction = true;
							}
						}
					}
						
					
					if (Spin > 0.1)
					{
						// Reduce spin by friction
						Spin *= Level.GetFrictionFactor(Level.GetTerrain(new GridPoint2((int)Location.x, (int)Location.y)));
					}
					else
					{
						Spin = 0;
					}
					
					Vector2 Velocity2d = new Vector2(Velocity.x, Velocity.y);
					if (Velocity2d.len() <= 1 && !InWater)
					{
						// Stop ball if velocity is near nothing
						Velocity.z = 0;
						Velocity.x = 0;
						Velocity.y = 0;
						// Tell screen we're done
						Screen.BallFinished(false, false);
						// Update lies
						Screen.Renderer.UI.SetLie(Level.GetTerrain(new GridPoint2((int)Location.x, (int)Location.y)));
						SetLie(Level.GetTerrain(new GridPoint2((int)Location.x, (int)Location.y)));
						// Check to hide flag
						if (Level.GetTerrain(new GridPoint2((int)Location.x, (int)Location.y)) == "Green")
						{
							OnGreen = true;
						}
					}
				}
				if (Location.z < 5)
				{
					// Check hole collision
					if (Level.HoleCircle.contains(BallCircle))
					{
						// Ball go in the hole
					//	System.out.print("You got it in the hole\n");
						Velocity.x = 0;
						Velocity.y = 0;
						Velocity.z = 0;
						InHole = true;
						// Play sound
						SInHole.play();
						Screen.Renderer.UI.HoleFinished();
					}
				}
				
				LastTerrain = Level.GetTerrain(new GridPoint2((int)Location.x, (int)Location.y));
			}
			else
			{
				// Handle putt physics
				Location.x += Velocity.x * Delta;
				Location.y += Velocity.y * Delta;
				Location.z = 0;
				
				// Update collision
				BallCircle.setPosition(Location.x, Location.y);
				
				
				if (Location.x > Level.GetWidth() * 64 || Location.x < 0 || Location.y > Level.GetHeight() * 64 || Location.y < 0)
				{
					// Off map
					//Screen.ResetBall();
					Screen.BallFinished(true, false);
				}
				
				// Check for water hazard
				if (Level.GetTerrain(new GridPoint2((int)Location.x, (int)Location.y)) == "Water")
				{
					SSplash.play();
					InWater = true;
					Screen.BallFinished(false, true);
				}
				
				// Friction
				if (DoFriction)
				{
					Velocity.x *= Level.GetPuttFactor(Level.GetTerrain(new GridPoint2((int)Location.x,(int)Location.y)));
					Velocity.y *= Level.GetPuttFactor(Level.GetTerrain(new GridPoint2((int)Location.x, (int)Location.y)));
					DoFriction = false;
					FrictionTime = 0f;
				}
				else
				{
					FrictionTime += Delta;
					if (FrictionTime > 0.05)
					{
						DoFriction = true;
					}
				}
				
				if (Location.z < 5)
				{
					// Check hole collision
					if (Level.HoleCircle.contains(BallCircle))
					{
						// Ball go in the hole
					//	System.out.print("You got it in the hole\n");
						Velocity.x = 0;
						Velocity.y = 0;
						Velocity.z = 0;
						InHole = true;
						// Play sound
						SInHole.play();
						Screen.Renderer.UI.HoleFinished();
					}
				}
				
				Vector2 Velocity2d = new Vector2(Velocity.x, Velocity.y);
				if (Velocity2d.len() <= 1 && !InWater)
				{
					// Ball stop
					Velocity.z = 0;
					Velocity.x = 0;
					Velocity.y = 0;
					Screen.BallFinished(false, false);
					Screen.Renderer.UI.SetLie(Level.GetTerrain(new GridPoint2((int)Location.x, (int)Location.y)));
					SetLie(Level.GetTerrain(new GridPoint2((int)Location.x, (int)Location.y)));
					if (Level.GetTerrain(new GridPoint2((int)Location.x, (int)Location.y)) == "Green")
					{
						OnGreen = true;
					}
				}
			}
			
			LastTerrain = Level.GetTerrain(new GridPoint2((int)Location.x, (int)Location.y));
		}
	}
	
	public void SetLocation(GridPoint3 inLocation)
	{
		Location.x = inLocation.x;
		Location.y = inLocation.y;
		Location.z = inLocation.z;
	//	System.out.print("Ball SetLoc x" + inLocation.x + " y" + inLocation.y + "\n");
	}
	
	public void SetLocation(int inX, int inY, int inZ)
	{
		Location.x = inX;
		Location.y = inY;
		Location.z = inZ;
	}
	
	public void HitBall(int inAim, int inPower, int inRise, float inSpin, boolean inPutt)
	{
		// Generate velocity from angle
		Velocity.x = (float)(inPower * Math.sin((double)(inAim * 0.0174532925)));
		Velocity.y = (float)(inPower * Math.cos((double)(inAim * 0.0174532925)));
				
		Spin = inSpin;
		if (inPutt)
		{
			Mode = EMode.Putt;
		//	System.out.print("Putting ball\n");
			SPutt.play();
		}
		else
		{
			Mode = EMode.Flying;
			Velocity.z = (float)(inPower * Math.sin((double)(inRise * 0.0174532925)));
			// Play sound
			if (Screen.ActiveClub.toString() == "D")
			{
				SHitDriver.play();
			}
			else if (Screen.ActiveClub.toString() == "SW")
			{
				SHitSand.play();
			}
			else
			{
				SHitIron.play();
			}
		}
	}
	
	private void SetLie(String inLie)
	{
		if (inLie == "Rough")
		{
			Lie = ELie.Rough;
		}
		else if (inLie == "Fairway")
		{
			Lie = ELie.Fairway;
		}
		else if (inLie == "Green")
		{
			Lie = ELie.Green;
		}
		else if (inLie == "Bunker")
		{
			Lie = ELie.Bunker;
		}
		else if (inLie == "Tee")
		{
			Lie = ELie.Tee;
		}
	}
	
}
