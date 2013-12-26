package com.shenko.golfgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class GolfGameUI {

	public GolfGameScreen Screen;
	public GolfGameLevel Level;
	
	public Texture LieTexture;
	public Texture ClubTexture;
	public Texture SwingTexture;
	public Texture SwingBarTexture;
	
	private Texture ClubIcon_Driver;
	private Texture ClubIcon_1Iron;
	private Texture ClubIcon_3Iron;
	private Texture ClubIcon_5Iron;
	private Texture ClubIcon_7Iron;
	private Texture ClubIcon_9Iron;
	private Texture ClubIcon_SW;
	private Texture ClubIcon_Putter;
	
	private Texture ClubSelect_Driver;
	private Texture ClubSelect_1Iron;
	private Texture ClubSelect_3Iron;
	private Texture ClubSelect_5Iron;
	private Texture ClubSelect_7Iron;
	private Texture ClubSelect_9Iron;
	private Texture ClubSelect_SW;
	private Texture ClubSelect_Putter;
	
	private Texture LieBunker;
	private Texture LieFairway;
	private Texture LieGreen;
	private Texture LieRough;
	private Texture LieTee;
	
	private Texture YardIndicator;
	private Texture ScoreIndicator;
	
    public BitmapFont Font, Font2;
	
	enum EMode { HoleOpen, HoleClose, Idle, SwingingUp, SwingingDown, ChooseClub, Scorecard };
	public EMode Mode;
	
	public boolean HoldLeft, HoldRight;
	private float Swing, Backswing, Downswing;
	
	private float OpenHoldTime, OpenAlpha;
	
	private String ShotMessage;
	private float ShotMessageTime;
	
	private SpriteBatch Batch;
	private ShapeRenderer Shape;
	
	private Scorecard Card;
	
	public GolfGameUI(GolfGameLevel inLevel, GolfGameScreen inScreen)
	{
		Level = inLevel;
		Screen = inScreen;
		Batch = new SpriteBatch();
		Shape = new ShapeRenderer();
		
		if (Screen.CurrentHole < 10 
				&& ( Screen.MatchMode.toString() == "CCF" ||  Screen.MatchMode.toString() == "CCB" || Screen.MatchMode.toString() == "CC18"))
		{
			Card = new Scorecard("CCF", Screen);
		}
		else if (Screen.CurrentHole < 10 
				&& ( Screen.MatchMode.toString() == "LSF" ||  Screen.MatchMode.toString() == "LSB" || Screen.MatchMode.toString() == "LS18"))
		{
			Card = new Scorecard("LSF", Screen);
		}
		else if (Screen.CurrentHole < 18 && Screen.CurrentHole > 9 
				&& ( Screen.MatchMode.toString() == "CCF" ||  Screen.MatchMode.toString() == "CCB" || Screen.MatchMode.toString() == "CC18"))
		{
			Card = new Scorecard("CCB", Screen);
		}
		else if (Screen.CurrentHole < 17 && Screen.CurrentHole > 9 
				&& ( Screen.MatchMode.toString() == "LSF" ||  Screen.MatchMode.toString() == "LSB" || Screen.MatchMode.toString() == "LS18"))
		{
			Card = new Scorecard("LSB", Screen);
		}
		
		Mode = EMode.HoleOpen;
		
		Swing = 0f;
		Backswing = 0f;
		Downswing = 0f;
		
		OpenHoldTime = 0f;
		OpenAlpha = 1f;
		
		HoldLeft = false;
		HoldRight = false;
		
		LieTexture = new Texture(Gdx.files.internal("data/ui/lie_tee.png"));
		ClubTexture = new Texture(Gdx.files.internal("data/ui/club_driver.png"));
		SwingTexture = new Texture(Gdx.files.internal("data/ui/swing_button.png"));
		SwingBarTexture = new Texture(Gdx.files.internal("data/ui/swingbar_frame.png"));
		
		ClubIcon_Driver = new Texture(Gdx.files.internal("data/ui/club_driver.png"));
		ClubIcon_1Iron = new Texture(Gdx.files.internal("data/ui/club_1iron.png"));
		ClubIcon_3Iron = new Texture(Gdx.files.internal("data/ui/club_3iron.png"));
		ClubIcon_5Iron = new Texture(Gdx.files.internal("data/ui/club_5iron.png"));
		ClubIcon_7Iron = new Texture(Gdx.files.internal("data/ui/club_7iron.png"));
		ClubIcon_9Iron = new Texture(Gdx.files.internal("data/ui/club_9iron.png"));
		ClubIcon_SW = new Texture(Gdx.files.internal("data/ui/club_sw.png"));
		ClubIcon_Putter = new Texture(Gdx.files.internal("data/ui/club_putter.png"));

		ClubSelect_Driver = new Texture(Gdx.files.internal("data/ui/cs_driver.png"));
		ClubSelect_1Iron = new Texture(Gdx.files.internal("data/ui/cs_1iron.png"));
		ClubSelect_3Iron = new Texture(Gdx.files.internal("data/ui/cs_3iron.png"));
		ClubSelect_5Iron = new Texture(Gdx.files.internal("data/ui/cs_5iron.png"));
		ClubSelect_7Iron = new Texture(Gdx.files.internal("data/ui/cs_7iron.png"));
		ClubSelect_9Iron = new Texture(Gdx.files.internal("data/ui/cs_9iron.png"));
		ClubSelect_SW = new Texture(Gdx.files.internal("data/ui/cs_sw.png"));
		ClubSelect_Putter = new Texture(Gdx.files.internal("data/ui/cs_putter.png"));
		
		LieBunker = new Texture(Gdx.files.internal("data/ui/lie_bunker.png")); 
		LieFairway = new Texture(Gdx.files.internal("data/ui/lie_fairway.png"));
		LieGreen = new Texture(Gdx.files.internal("data/ui/lie_green.png"));
		LieRough = new Texture(Gdx.files.internal("data/ui/lie_rough.png"));
		LieTee = new Texture(Gdx.files.internal("data/ui/lie_tee.png"));
		
		YardIndicator = new Texture(Gdx.files.internal("data/uiyard.png"));
		ScoreIndicator = new Texture(Gdx.files.internal("data/uiscore.png"));
		
        Font = new BitmapFont( Gdx.files.internal("data/VOYAGEFANT-32.fnt"), Gdx.files.internal("data/GolfFont.png"), false );
        Font2 = new BitmapFont( Gdx.files.internal("data/VOYAGEFANT-18.fnt"), Gdx.files.internal("data/GolfFont.png"), false );
	}
	
	public void Render(float Delta)
	{
		// Handle swing logic
		if (Mode == EMode.SwingingUp)
		{
			// Increase swing amount
			Swing += 1f;
			if (Swing > 100)
			{
				// Missed the swing
				Mode = EMode.Idle;
				Swing = 0f;
				Backswing = 0f;
				Downswing = 0f;
			}
		}
		else if (Mode == EMode.SwingingDown)
		{
			// Decrease swing amount
			Swing -= 1f;
			if (Swing < 0)
			{
				// Missed the swing
				float Power = Clamp(40f, 100f, (float)((Backswing - 12.4) / 87.6));
				float Accuracy = Clamp(-10f, 10f, (float)((0 - 12.4) / 12.4));
				Screen.HitBall(Power, Accuracy);
				
				Mode = EMode.Idle;
				Swing = 0f;
				Downswing = 0f;
			}
		}
		
		if (Mode != EMode.Scorecard)
		{
			Vector3 LieLoc = new Vector3(10, Screen.Renderer.Height - 10, 0);
			Vector3 ClubLoc = new Vector3(494, Screen.Renderer.Height - 10, 0);
			Vector3 SwingLoc = new Vector3(172, Screen.Renderer.Height - 10, 0);
			
			Vector3 ClubSelect8Loc = new Vector3(494, Screen.Renderer.Height - 116, 0);
			Vector3 ClubSelect7Loc = new Vector3(494, Screen.Renderer.Height - 150, 0);
			Vector3 ClubSelect6Loc = new Vector3(494, Screen.Renderer.Height - 184, 0);
			Vector3 ClubSelect5Loc = new Vector3(494, Screen.Renderer.Height - 218, 0);
			Vector3 ClubSelect4Loc = new Vector3(494, Screen.Renderer.Height - 252, 0);
			Vector3 ClubSelect3Loc = new Vector3(494, Screen.Renderer.Height - 286, 0);
			Vector3 ClubSelect2Loc = new Vector3(494, Screen.Renderer.Height - 320, 0);
			Vector3 ClubSelect1Loc = new Vector3(494, Screen.Renderer.Height - 354, 0);
			
			Vector3 StrokeLoc = new Vector3(20, 20, 0);
			Vector3 YardIndicatorLoc = new Vector3(Screen.Renderer.Width - 96, 20, 0);
			
			Screen.Renderer.Camera.unproject(LieLoc);
			Screen.Renderer.Camera.unproject(ClubLoc);
			Screen.Renderer.Camera.unproject(SwingLoc); 
			Screen.Renderer.Camera.unproject(StrokeLoc);
			Screen.Renderer.Camera.unproject(YardIndicatorLoc);
			
			Batch.setProjectionMatrix(Screen.Renderer.Camera.combined);		
			Batch.begin();
			
			if (ShotMessage != null)
			{
				// Display message
				ShotMessageTime += Delta;
				if (ShotMessageTime < 5)
				{
					Vector3 ShotMessageLoc = new Vector3( (Screen.Renderer.Width - Font.getBounds(ShotMessage).width) / 2, 200, 0 );
					Screen.Renderer.Camera.unproject(ShotMessageLoc);
					Font.draw(Batch, ShotMessage, ShotMessageLoc.x, ShotMessageLoc.y);
				}
				else
				{
					ClearMessage();
				}
			}
			
			if (Mode == EMode.HoleOpen)
			{
				String OpenString1 = "Hole " + Screen.CurrentHole;
				String OpenString2 = Level.Yards + " Yards - Par " + Level.Par;			
				Vector3 OpenLine1 = new Vector3( (Screen.Renderer.Width - Font.getBounds(OpenString1).width) / 2, 200, 0 );
				Vector3 OpenLine2 = new Vector3( (Screen.Renderer.Width - Font2.getBounds(OpenString2).width) / 2, 230, 0 );
				Screen.Renderer.Camera.unproject(OpenLine1);
				Screen.Renderer.Camera.unproject(OpenLine2);
				
				Font.draw(Batch, OpenString1, OpenLine1.x, OpenLine1.y);
				Font2.draw(Batch, OpenString2, OpenLine2.x, OpenLine2.y);
				
				OpenHoldTime += Delta;			
				if (OpenHoldTime > 5)
				{
					Mode = EMode.Idle;
				}
			}
			
			if (Mode == EMode.HoleClose)
			{
				String CloseString1 = new String();
				String CloseString2 = new String();
				
				switch (Screen.Stroke - Level.Par)
				{
				case -3:
					CloseString1 = "Albatross!";
					CloseString2 = "Three under par!";
					break;
				case -2:
					CloseString1 = "Eagle!";
					CloseString2 = "Two under par!";
					break;
				case -1:
					CloseString1 = "Birdie!";
					CloseString2 = "One under par!";
					break;
				case 0:
					CloseString1 = "Par!";
					CloseString2 = "Not bad!";
					break;
				case 1:
					CloseString1 = "Bogey!";
					CloseString2 = "It could have been worse.";
					break;
				case 2:
					CloseString1 = "Double Bogey!";
					CloseString2 = "Not so hot.";
					break;
				case 3:
					CloseString1 = "Triple Bogey!";
					CloseString2 = "You can do better.";
					break;
				default:
					CloseString1 = "Hole Finished!";
					CloseString2 = "Well that was painful.";
				}
				
				Vector3 CloseLine1 = new Vector3( (Screen.Renderer.Width - Font.getBounds(CloseString1).width) / 2, 200, 0 );
				Vector3 CloseLine2 = new Vector3( (Screen.Renderer.Width - Font2.getBounds(CloseString2).width) / 2, 230, 0 );
				Screen.Renderer.Camera.unproject(CloseLine1);
				Screen.Renderer.Camera.unproject(CloseLine2);
				
				Font.draw(Batch, CloseString1, CloseLine1.x, CloseLine1.y);
				Font2.draw(Batch, CloseString2, CloseLine2.x, CloseLine2.y);
				
				OpenHoldTime += Delta;			
				if (OpenHoldTime > 5)
				{
					// Tell screen we're done
					Screen.HoleFinished();
				}
			}
			
			if (Mode == EMode.ChooseClub)
			{			
				Screen.Renderer.Camera.unproject(ClubSelect8Loc);
				Screen.Renderer.Camera.unproject(ClubSelect7Loc);
				Screen.Renderer.Camera.unproject(ClubSelect6Loc);
				Screen.Renderer.Camera.unproject(ClubSelect5Loc);
				Screen.Renderer.Camera.unproject(ClubSelect4Loc);
				Screen.Renderer.Camera.unproject(ClubSelect3Loc);
				Screen.Renderer.Camera.unproject(ClubSelect2Loc);
				Screen.Renderer.Camera.unproject(ClubSelect1Loc);			
			}
			
			if (Mode == EMode.SwingingUp || Mode == EMode.SwingingDown)
			{
				// Draw swing bar
				Shape.setProjectionMatrix(Screen.Renderer.Camera.combined);
				Shape.begin(ShapeType.Filled);
				Shape.setColor(1f, 0f, 0f, 1f);
				Vector3 BarLoc = new Vector3( (Screen.Renderer.Width / 2) - 120, Screen.Renderer.Height - 153, 0 );
				Screen.Renderer.Camera.unproject(BarLoc);
				Shape.rect(BarLoc.x, BarLoc.y, (int)(Swing * 2.41), 21);
				Shape.end();
				
				Shape.begin(ShapeType.Line);
				Shape.setColor(0f, 1f, 0f, 1f);
				Shape.line(BarLoc.x + 30, BarLoc.y + 24, BarLoc.x + 30, BarLoc.y + 2);
				Shape.end();			
			}
			
			Batch.draw(LieTexture, LieLoc.x, LieLoc.y, 96, 96);
			Batch.draw(ClubTexture, ClubLoc.x, ClubLoc.y, 96, 96);
			Batch.draw(SwingTexture, SwingLoc.x, SwingLoc.y);
			
			if (Mode == EMode.ChooseClub)
			{	
				Batch.draw(ClubSelect_Putter, ClubSelect8Loc.x, ClubSelect8Loc.y, 96, 24);
				Batch.draw(ClubSelect_SW, ClubSelect7Loc.x, ClubSelect7Loc.y, 96, 24);
				Batch.draw(ClubSelect_9Iron, ClubSelect6Loc.x, ClubSelect6Loc.y, 96, 24);
				Batch.draw(ClubSelect_7Iron, ClubSelect5Loc.x, ClubSelect5Loc.y, 96, 24);
				Batch.draw(ClubSelect_5Iron, ClubSelect4Loc.x, ClubSelect4Loc.y, 96, 24);
				Batch.draw(ClubSelect_3Iron, ClubSelect3Loc.x, ClubSelect3Loc.y, 96, 24);
				Batch.draw(ClubSelect_1Iron, ClubSelect2Loc.x, ClubSelect2Loc.y, 96, 24);
				Batch.draw(ClubSelect_Driver, ClubSelect1Loc.x, ClubSelect1Loc.y, 96, 24);
			}
			
			if (Mode == EMode.SwingingUp || Mode == EMode.SwingingDown)
			{
				// Draw swing frame
				GridPoint2 FrameLoc = Unproject(new GridPoint2((int)((Screen.Renderer.Width / 2) - 128), (int)(Screen.Renderer.Height - 148)));
				
				Batch.draw(SwingBarTexture, FrameLoc.x, FrameLoc.y);
			}
			
			Batch.draw(ScoreIndicator, StrokeLoc.x + 2, StrokeLoc.y);			
			Font2.setColor(1f, 1f, 1f, 1f);
			Font2.draw(Batch, "Stroke: " + Screen.Stroke, StrokeLoc.x,  StrokeLoc.y);
			
			Batch.draw(YardIndicator, YardIndicatorLoc.x, YardIndicatorLoc.y);
			int DistToPin = (int)(Level.Ball.Location.dst(Level.Hole.x, Level.Hole.y, 0) / Screen.Clubs.YardFactor);
			Font2.draw(Batch, new String(DistToPin + " Yards"), YardIndicatorLoc.x - 8, YardIndicatorLoc.y - 2);
			
			Batch.end();
		}
		else
		{
			// Scorecard mode
			Batch.begin();
			Matrix4 UIMatrix = Screen.Renderer.Camera.combined.cpy();
			UIMatrix.setToOrtho2D(0, 0, Screen.Renderer.Width, Screen.Renderer.Height);
			Batch.setProjectionMatrix(UIMatrix);
			Card.Render(Batch);
			Batch.end();
		}
	}
	
	public float Clamp(float Min, float Max, float Val)
	{
		return Math.max(Min, Math.min(Max, Val));
	}
	
	public void HoleFinished()
	{
		OpenHoldTime = 0f;
		Mode = EMode.HoleClose;		
	}
	
	public void AddMessage(String newMessage)
	{
		ShotMessage = newMessage;
		ShotMessageTime = 0f;
	}
	
	public void ClearMessage()
	{
		ShotMessage = null;
		ShotMessageTime = 0f;
	}
	
	public void SetLie(String inLie)
	{
		if (inLie == "Rough")
		{
			LieTexture = LieRough;
		}
		else if (inLie == "Fairway")
		{
			LieTexture = LieFairway;
		}
		else if (inLie == "Green")
		{
			LieTexture = LieGreen;
		}
		else if (inLie == "Bunker")
		{
			LieTexture = LieBunker;
		}
		else if (inLie == "Tee")
		{
			LieTexture = LieTee;
		}
	}
	
	public void HandleTouch(int inX, int inY)
	{
		inY = (int) (Screen.Renderer.Height - inY);
	//	System.out.print("UI handling touch - X:" + inX + " Y:" + inY + "\n");
		
		if (Mode != EMode.Scorecard)
		{		
			if (Mode != EMode.ChooseClub && Screen.GameMode.toString() != "Resolving")
			{
				if (inX > 494 && inX < 590 && inY > 10 && inY < 106)
				{
					// Clicked club icon
					Mode = EMode.ChooseClub;
				}
				else if (inX > 172 && inX < 238 && inY > 10 && inY < 79)
				{
					// Clicked left aim arrow
					HoldLeft = true;
					Screen.StartAim();
				}
				else if (inX > 361 && inX < 428 && inY > 10 && inY < 79)
				{
					// Clicked right aim arrow
					HoldRight = true;
					Screen.StartAim();
				}
				else if (inX > 18 && inX < 93 && inY > 776 && inY < 807)
				{
					// Clicked score button
					Mode = EMode.Scorecard;
				}
				else if (inX > 238 && inY < 362 && inY > 10 && inY < 138)
				{
				//	System.out.print("Clicked swing ball\n");
					// Clicked swing
					//Screen.HitBall();
					if (Mode == EMode.SwingingDown)
					{
						// Set downswing
						Downswing = Swing;
						float Power = (float)((Backswing - 12.4) / 80);
						float Accuracy = (float)((Downswing - 12.4) / 24.8);
						Screen.HitBall(Power, Accuracy);
						
						Mode = EMode.Idle;
						Swing = 12.4f;
						Backswing = 0f;
						Downswing = 0f;
					}
					else if (Mode == EMode.SwingingUp)
					{
						// Set backswing
					//	System.out.print("Starting downswing\n");
						Mode = EMode.SwingingDown;
						Backswing = Swing;					
					}
					else if (Mode == EMode.Idle)
					{
					//	System.out.print("Starting swing\n");
						Mode = EMode.SwingingUp;
						Swing = 12.4f;
						Backswing = 0f;
						Downswing = 0f;
						
						ClearMessage();
					}
				}
			}
			else if (Mode == EMode.ChooseClub)
			{
				if (inX > 494 && inX < 590 && inY > 116 && inY < 140 )
				{
					// Clicked club 8
					Screen.ChooseClub(8);
					ClubTexture = ClubIcon_Putter;
					Mode = EMode.Idle;
					Screen.StartAim();
				}
				else if (inX > 494 && inX < 590 && inY > 150 && inY < 174 )
				{
					// Clicked club 7
					Screen.ChooseClub(7);
					ClubTexture = ClubIcon_SW;
					Mode = EMode.Idle;
					Screen.StartAim();
				}
				else if (inX > 494 && inX < 590 && inY > 184 && inY < 208 )
				{
					// Clicked club 6
					Screen.ChooseClub(6);
					ClubTexture = ClubIcon_9Iron;
					Mode = EMode.Idle;
					Screen.StartAim();
				}
				else if (inX > 494 && inX < 590 && inY > 218 && inY < 242 )
				{
					// Clicked club 5
					Screen.ChooseClub(5);
					ClubTexture = ClubIcon_7Iron;
					Mode = EMode.Idle;
					Screen.StartAim();
				}
				else if (inX > 494 && inX < 590 && inY > 252 && inY < 276 )
				{
					// Clicked club 4
					Screen.ChooseClub(4);
					ClubTexture = ClubIcon_5Iron;
					Mode = EMode.Idle;
					Screen.StartAim();
				}
				else if (inX > 494 && inX < 590 && inY > 286 && inY < 310 )
				{
					// Clicked club 3
					Screen.ChooseClub(3);
					ClubTexture = ClubIcon_3Iron;
					Mode = EMode.Idle;
					Screen.StartAim();
				}
				else if (inX > 494 && inX < 590 && inY > 320 && inY < 344 )
				{
					// Clicked club 2
					Screen.ChooseClub(2);
					ClubTexture = ClubIcon_1Iron;
					Mode = EMode.Idle;
					Screen.StartAim();
				}
				else if (inX > 494 && inX < 590 && inY > 354 && inY < 378 )
				{
					// Clicked club 1
					Screen.ChooseClub(1);
					ClubTexture = ClubIcon_Driver;
					Mode = EMode.Idle;
					Screen.StartAim();
				}
			}
		}
		else
		{
			// Handle scorecard touch
			Mode = EMode.Idle;
		}
	}
	
	public void HandleUntouch(int inX, int inY)
	{
		HoldLeft = false;
		HoldRight = false;
		Screen.StopAim();
	}
	
	private GridPoint2 Unproject(GridPoint2 inGrid)
	{
		Vector3 V = new Vector3(inGrid.x, inGrid.y, 0);
		Screen.Renderer.Camera.unproject(V);
		GridPoint2 R = new GridPoint2((int)V.x, (int)V.y);
		return R;
	}
	

}
