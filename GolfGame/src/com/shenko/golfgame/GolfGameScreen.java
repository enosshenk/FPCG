package com.shenko.golfgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.GridPoint3;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.shenko.golfgame.GolfBall.ELie;

public class GolfGameScreen implements Screen {
	
	private GolfGame Game;
	
    public GolfGameLevel Level;
    public GolfGameRenderer Renderer;
    public GolfClubs Clubs;

	enum EGameMode { Loading, Idle, Aiming, Swinging, Resolving, PostShot };
	public EGameMode GameMode;
	
	public int CurrentHole;
	public int[] Scores;
	enum EMatchMode { CCF, CCB, CC18, LSF, LSB, LS18 };
	public EMatchMode MatchMode;
	
	private float AimTime;
	private float AimTimeElapsed = 0;
	
	public float AimDegree;
	public GridPoint2 AimPoint;
	
	public int MaxDistance = 500;
	public int ShotAngle = 5;
	
	enum EClubs { D, I1, I3, I5, I7, I9, SW, P };
	
	public EClubs ActiveClub;
	public int Stroke;
	
	public Vector3 StartLoc;
	
	private Sound CHappy, CDissapoint, CShot, CAngry;
	
	public GolfGameScreen(GolfGame inGame, String Match)
	{
		Game = inGame;
		
		if (Match == "CCF")
		{
			MatchMode = EMatchMode.CCF;
		}
		else if (Match == "CCB")
		{
			MatchMode = EMatchMode.CCB;
		}
		else if (Match == "CC18")
		{
			MatchMode = EMatchMode.CC18;
		}
		else if (Match == "LSF")
		{
			MatchMode = EMatchMode.LSF;
		}
		else if (Match == "LSB")
		{
			MatchMode = EMatchMode.LSB;
		}
		else if (Match == "LS18")
		{
			MatchMode = EMatchMode.LS18;
		}
		
        Scores = new int[19];
        
        for (int i=1; i < 19; i++)
        {
        	Scores[i] = 0;
        }
        
        Clubs = new GolfClubs();
        
        GolfKeyProcessor InputProcessor = new GolfKeyProcessor();
        Gdx.input.setInputProcessor(InputProcessor);
	}
    
	@Override
	public void render(float delta) {
		
		if (Renderer.UI.HoldLeft)
		{
			AimDegree -= 1;
			RecalcAim();
		}
		
		if (Renderer.UI.HoldRight)
		{
			AimDegree += 1;
			RecalcAim();
		}
		
		if (GameMode == EGameMode.Resolving)
		{
			Level.Ball.Update(delta);
		}
		else if (GameMode == EGameMode.Aiming && (!Renderer.UI.HoldLeft || !Renderer.UI.HoldRight))
		{
			AimTimeElapsed += delta;
			if (AimTimeElapsed > AimTime)
			{
				GameMode = EGameMode.Idle;
				Renderer.SetDesiredCamLoc(new GridPoint2((int)Level.Ball.Location.x, (int)Level.Ball.Location.y));
			}
		}		
		
		Renderer.Render(delta);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() { 
      //  Level = new CC1(this);
       // Renderer = new GolfGameRenderer(Level, this);
        
        ActiveClub = EClubs.D;
        
        GameMode = EGameMode.Loading;
        AimDegree = 0;
        AimPoint = new GridPoint2();        
        
        AimTime = 2f;
        Stroke = 0;
        
        if (MatchMode == EMatchMode.CCB || MatchMode == EMatchMode.LSB)
        {
        	CurrentHole = 10;
        }
        else
        {
        	CurrentHole = 1;
        }
        
        StartLoc = new Vector3();
        
        CHappy = Gdx.audio.newSound(Gdx.files.internal("data/sound/crowd_happy.wav"));  
        CDissapoint = Gdx.audio.newSound(Gdx.files.internal("data/sound/crowd_dissapoint.wav")); 
        CAngry = Gdx.audio.newSound(Gdx.files.internal("data/sound/crowd_angry.wav")); 
        CShot = Gdx.audio.newSound(Gdx.files.internal("data/sound/crowd_shot.wav")); 
        
        // Load hole
        LoadHole(CurrentHole);
   //     InitialAim();
	}
	
	public void ChooseClub(int Club)
	{
		switch (Club)
		{
		case 1:
			ActiveClub = EClubs.D;
			RecalcAim();
			break;
		case 2:
			ActiveClub = EClubs.I1;
			RecalcAim();
			break;
		case 3:
			ActiveClub = EClubs.I3;
			RecalcAim();
			break;
		case 4:
			ActiveClub = EClubs.I5;
			RecalcAim();
			break;
		case 5:
			ActiveClub = EClubs.I7;
			RecalcAim();
			break;
		case 6:
			ActiveClub = EClubs.I9;
			RecalcAim();
			break;
		case 7:
			ActiveClub = EClubs.SW;
			RecalcAim();
			break;
		case 8:
			ActiveClub = EClubs.P;
			RecalcAim();
			break;
		}
	}
	
	public GridPoint2 WorldPixelToTile(GridPoint2 inPixels)
	{
		GridPoint2 OutPoints = new GridPoint2();
		
		OutPoints.x = inPixels.x % 64;
		OutPoints.y = inPixels.y % 64;
		
		return OutPoints;
	}
	
	public GridPoint2 WorldPixelToTile(int inX, int inY)
	{
		GridPoint2 OutPoints = new GridPoint2();
		
		OutPoints.x = inX % 64;
		OutPoints.y = inY % 64;
		
		return OutPoints;
	}
	
	public GridPoint2 PixelToGrid(GridPoint2 inPixels)
	{
		GridPoint2 OutPoints = new GridPoint2();
		
		OutPoints.x = (int)Math.floor(inPixels.x / 64);
		OutPoints.y = (int)Math.floor(inPixels.y / 64);
		
		return OutPoints;
	}
	
	public GridPoint2 GridToPixel(GridPoint2 inGrid)
	{
		GridPoint2 OutPoints = new GridPoint2();
		
		OutPoints.x = (int)Math.floor(inGrid.x * 64);
		OutPoints.y = (int)Math.floor(inGrid.y * 64);
		
		return OutPoints;		
	}
	
	public GridPoint2 PixelToGrid(float inX, float inY)
	{
		GridPoint2 OutPoints = new GridPoint2();
		
		OutPoints.x = (int)Math.floor(inX / 64);
		OutPoints.y = (int)Math.floor(inY / 64);
		
		return OutPoints;
	}
	
	public GridPoint2 GridToPixel(float inX, float inY)
	{
		GridPoint2 OutPoints = new GridPoint2();
		
		OutPoints.x = (int)Math.floor(inX * 64);
		OutPoints.y = (int)Math.floor(inY * 64);
		
		return OutPoints;		
	}
	
	public GridPoint2 PixelToGrid(Vector3 inPixels)
	{
		GridPoint2 OutPoints = new GridPoint2();
		
		OutPoints.x = (int)Math.floor(inPixels.x / 64);
		OutPoints.y = (int)Math.floor(inPixels.y / 64);
		
		return OutPoints;
	}
	
	public void RecalcAim()
	{
		AimPoint.x = Clubs.GetClubDistance(ActiveClub.toString());
		AimPoint.y = 0;
		
		AimPoint.x = (int)(Clubs.GetClubDistance(ActiveClub.toString()) * Math.sin((double)(AimDegree * 0.0174532925)));
		AimPoint.y = (int)(Clubs.GetClubDistance(ActiveClub.toString()) * Math.cos((double)(AimDegree * 0.0174532925)));
		
		Renderer.SetDesiredCamLoc(new GridPoint2((int)Level.Ball.Location.x + AimPoint.x, (int)Level.Ball.Location.y + AimPoint.y));
		
	//	System.out.print("New aimpoint angle: " + AimDegree + "\n");
	}
	
	public void InitialAim()
	{
		AimPoint.x = Clubs.GetClubDistance(ActiveClub.toString());
		AimPoint.y = 0;
		
		AimPoint.x = (int)(Clubs.GetClubDistance(ActiveClub.toString()) * Math.sin((double)(AimDegree * 0.0174532925)));
		AimPoint.y = (int)(Clubs.GetClubDistance(ActiveClub.toString()) * Math.cos((double)(AimDegree * 0.0174532925)));
	}
	
	public void HitBall(float inPower, float inSpin)
	{
		StartLoc.x = Level.Ball.Location.x;
		StartLoc.y = Level.Ball.Location.y;
		StartLoc.z = 0;
		
		if (ActiveClub == EClubs.P)
		{
			Level.Ball.HitBall((int)AimDegree, (int)(Clubs.GetPower(ActiveClub.toString()) * inPower), 0, 0, true);
		}
		else
		{
			Level.Ball.HitBall((int)AimDegree, (int)(Clubs.GetPower(ActiveClub.toString()) * inPower), Clubs.GetAngle(ActiveClub.toString()), inSpin, false);
		}
		
		GameMode = EGameMode.Resolving;	
		Stroke += 1;
	}
	
	public void BallFinished(boolean OutOfBounds, boolean WaterHazard)
	{
		if (!OutOfBounds && !WaterHazard)
		{
			Renderer.SetDesiredCamLoc(new GridPoint2((int)Level.Ball.Location.x, (int)Level.Ball.Location.y));
			GameMode = EGameMode.Idle;
		//	System.out.print("Ball traveled " + StartLoc.dst(Level.Ball.Location.x, Level.Ball.Location.y, Level.Ball.Location.z) + "\n");
			if (ActiveClub != EClubs.P)
			{
				Renderer.UI.AddMessage("Flight Distance: " 
			+ (int)(StartLoc.dst(Level.Ball.Location.x, Level.Ball.Location.y, Level.Ball.Location.z) / Clubs.YardFactor)
					+ " yards!");
			}
			
			if (Level.Ball.Lie.toString() == "Bunker" || Level.Ball.Lie.toString() == "Rough")
			{
				CAngry.play();
			}
			else if (Level.Ball.Lie.toString() == "Green")
			{
				CHappy.play();
			}
			else
			{
				if (Math.random() < 0.3)
				{
					CShot.play();
				}
			}
		}
		else if (OutOfBounds && !WaterHazard)
		{
			// Went out of bounds
			CDissapoint.play();
			Renderer.UI.AddMessage("Out of Bounds!");
			HazardReset();
		}
		else if (!OutOfBounds && WaterHazard)
		{
			// Went in the drink
			CDissapoint.play();
			Renderer.UI.AddMessage("Water Hazard!");
			GameMode = EGameMode.PostShot;
		}
		
		Renderer.UI.HoldLeft = false;
		Renderer.UI.HoldRight = false;
		InitialAim();
	}
	
	public void HoleFinished()
	{
		Scores[CurrentHole] = Stroke;
		Game.UpdateScores(Stroke, CurrentHole);
		Stroke = 0;
		
		if (MatchMode == EMatchMode.CCF)
		{
			if (CurrentHole != 9)
			{
				CurrentHole += 1;
				LoadHole(CurrentHole);
			}
			else
			{
				// Return to menu
				Game.MatchOver();
			}
		}
		else if (MatchMode == EMatchMode.CCB)
		{
			if (CurrentHole != 18)
			{
				CurrentHole += 1;
				LoadHole(CurrentHole);
			}
			else
			{
				Game.MatchOver();
			}
		}
		else if (MatchMode == EMatchMode.CC18)
		{
			if (CurrentHole != 18)
			{
				CurrentHole += 1;
				LoadHole(CurrentHole);
			}
			else
			{
				Game.MatchOver();
			}
		}
		if (MatchMode == EMatchMode.LSF)
		{
			if (CurrentHole != 9)
			{
				CurrentHole += 1;
				LoadHole(CurrentHole);
			}
			else
			{
				Game.MatchOver();
			}
		}
		else if (MatchMode == EMatchMode.LSB)
		{
			if (CurrentHole != 18)
			{
				CurrentHole += 1;
				LoadHole(CurrentHole);
			}
			else
			{
				Game.MatchOver();
			}
		}
		else if (MatchMode == EMatchMode.LS18)
		{
			if (CurrentHole != 18)
			{
				CurrentHole += 1;
				LoadHole(CurrentHole);
			}
			else
			{
				Game.MatchOver();
			}
		}
	}
	
	private void LoadHole(int inHole)
	{
		if (MatchMode == EMatchMode.CCF || MatchMode == EMatchMode.CCB || MatchMode == EMatchMode.CC18)
		{
			// Load cedar creek holes
			if (Level != null)
			{
				Level.LevelDone();
				Level = null;
			}
			if (Renderer != null)
			{
				Renderer = null;
			}
			
			switch (inHole)
			{
			case 1:
				Level = new CC1(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 2:
				Level = new CC2(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 3:
				Level = new CC3(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 4:
				Level = new CC4(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 5:
				Level = new CC5(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 6:
				Level = new CC6(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 7:
				Level = new CC7(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 8:
				Level = new CC8(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 9:
				Level = new CC9(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 10:
				Level = new CC10(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 11:
				Level = new CC11(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 12:
				Level = new CC12(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 13:
				Level = new CC13(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 14:
				Level = new CC14(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 15:
				Level = new CC15(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 16:
				Level = new CC16(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 17:
				Level = new CC17(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 18:
				Level = new CC18(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			}
		}
		else if (MatchMode == EMatchMode.LSF || MatchMode == EMatchMode.LSB || MatchMode == EMatchMode.LS18)
		{
			// Load lakeside holes
			if (Level != null)
			{
				Level = null;
			}
			if (Renderer != null)
			{
				Renderer = null;
			}
			
			switch (inHole)
			{
			case 1:
				Level = new LS1(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 2:
				Level = new LS2(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 3:
				Level = new LS3(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 4:
				Level = new LS4(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 5:
				Level = new LS5(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 6:
				Level = new LS6(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 7:
				Level = new LS7(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 8:
				Level = new LS8(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 9:
				Level = new LS9(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 10:
				Level = new LS10(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 11:
				Level = new LS11(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 12:
				Level = new LS12(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 13:
				Level = new LS13(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 14:
				Level = new LS14(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 15:
				Level = new LS15(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 16:
				Level = new LS16(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 17:
				Level = new LS17(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			case 18:
				Level = new LS18(this);
				InitialAim();		
				Renderer = new GolfGameRenderer(Level, this);
				GameMode = EGameMode.Idle;
				break;
			}
		}
	}
	
	public void HazardReset()
	{
		Stroke += 1;
		Level.Ball.SetLocation(new GridPoint3((int)StartLoc.x, (int)StartLoc.y, 0));
	//	System.out.print("Hazard reset x" + StartLoc.x + " y" + StartLoc.y + "\n");
		Level.Ball.InWater = false;
		GameMode = EGameMode.Idle;
	}
	
	public void StartAim()
	{
		GameMode = EGameMode.Aiming;
		AimTimeElapsed = 0;
	}
	
	public void StopAim()
	{
		AimTimeElapsed = 0;
	}
	
	public void ResetBall()
	{
		Level.Ball.Location.x = Level.Tee.x;
		Level.Ball.Location.y = Level.Tee.y;
		Level.Ball.Location.z = 0;
		
		Level.Ball.Velocity.x = 0;
		Level.Ball.Velocity.y = 0;
		Level.Ball.Velocity.z = 0;
		
		GameMode = EGameMode.Idle;
		Renderer.UI.SetLie("Tee");
		InitialAim();
		Stroke += 1;
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		
		CHappy.dispose();
		CAngry.dispose();
		CDissapoint.dispose();
		CShot.dispose();		
	}
	
	private class GolfKeyProcessor implements InputProcessor
	{

		@Override
		public boolean keyDown(int keycode) {
			if (keycode == Keys.R)
			{
				ResetBall();
			}
			else if (keycode == Keys.DOWN)
			{

			}
			else if (keycode == Keys.SPACE)
			{

			}
			
			
			return true;
		}

		@Override
		public boolean keyUp(int keycode) {	
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			
		/*	Vector3 ProjClickLoc = new Vector3(screenX, screenY, 0);
			Renderer.Camera.unproject(ProjClickLoc);
			System.out.print("Clicked at x" + screenX + " y" + screenY + " - Project x" + ProjClickLoc.x + " y" + ProjClickLoc.y + "\n");
			Renderer.SetCamLoc(new GridPoint2((int)ProjClickLoc.x, (int)ProjClickLoc.y)); */
			
			Renderer.UI.HandleTouch(screenX, screenY);
			
			return true;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			
			Renderer.UI.HandleUntouch(screenX, screenY);
			
			return true;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean scrolled(int amount) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

}
