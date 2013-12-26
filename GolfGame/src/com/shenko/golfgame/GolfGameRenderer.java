package com.shenko.golfgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector3;

public class GolfGameRenderer {
	
	private GolfGameScreen Screen;
	private TiledMapRenderer Renderer;
	public GolfGameUI UI;
	private GolfGameLevel Level;
    public OrthographicCamera Camera;
    private SpriteBatch Batch;
    private ShapeRenderer Shape;
    private Texture Cursor;
    public float Width, Height;
    private Vector3 ProjCamLoc;
    public GridPoint2 CamLoc;
    public GridPoint2 DesiredCamLoc;
	public GridPoint2 CursorLoc;
	
	private Texture Dot, Hole, Trees, Golfer, GolferPutt, Flag, Splash, Sand;
	private TextureRegion[][] TreeRegion;
	private TextureRegion[][] GolferRegion;
	private TextureRegion[] GolferFrames;
	private TextureRegion GolferFrame;
	private TextureRegion[][] GolferPuttRegion;
	private TextureRegion[] GolferPuttFrames;
	private TextureRegion GolferPuttFrame;
	private TextureRegion[][] SplashRegion;
	private TextureRegion[] SplashFrames;
	private TextureRegion SplashFrame;
	private TextureRegion[][] SandRegion;
	private TextureRegion[] SandFrames;
	private TextureRegion SandFrame;
	
	private Texture Background;
	
	private Animation GolferSwing, GolferPuttSwing, SplashAnim, SandAnim;
	
	public float ScaleFactor = 1f;
	private float SwingTime = 0f;
	private float SplashTime = 0f;
	
	private boolean Debug = false;
	
    public GolfGameRenderer(GolfGameLevel inLevel, GolfGameScreen inScreen)
    {
    	Screen = inScreen;
    	Level = inLevel;
    	
    	Renderer = new OrthogonalTiledMapRenderer(Level.Map, ScaleFactor);
    	
        Width = Gdx.graphics.getWidth();
        Height = Gdx.graphics.getHeight();
        
        Camera = new OrthographicCamera();
        Camera.setToOrtho(false, Width, Height);
        CamLoc = new GridPoint2((int)(Width / 2), (int)(Height / 2));
        DesiredCamLoc = new GridPoint2((int)Level.Ball.Location.x, (int)Level.Ball.Location.y);
        SetCamLoc(DesiredCamLoc);
        Camera.translate(0, 0);
        Camera.update();	
        
        Batch = new SpriteBatch();
        Shape = new ShapeRenderer();
        
        UI = new GolfGameUI(Level, Screen);
        
        Cursor = new Texture(Gdx.files.internal("data/cursor.tga"));
        Dot = new Texture(Gdx.files.internal("data/ui/dot.png"));
        Hole = new Texture(Gdx.files.internal("data/hole.png"));
        Flag = new Texture(Gdx.files.internal("data/flag.png"));
        Trees = new Texture(Gdx.files.internal("data/tree_tiles.png"));
        TreeRegion = TextureRegion.split(Trees, 256, 256);
        Golfer = new Texture(Gdx.files.internal("data/drivesheet.png"));
        GolferPutt = new Texture(Gdx.files.internal("data/golferputt.png"));
        Splash = new Texture(Gdx.files.internal("data/splash.tga"));
        Sand = new Texture(Gdx.files.internal("data/sand.png"));
        
        Background = new Texture(Gdx.files.internal("data/background.png"));
        
        InitGolferFrames();
        InitPuttFrames();
        InitSplashFrames();
        InitSandFrames();
    }
    
	public void Render(float delta)
	{
        Gdx.gl.glClearColor(0f, 0.3f, 0f, 1f);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
       
        
        if (Screen.GameMode.toString() == "Aiming" || Screen.GameMode.toString() == "Idle")
        {
        	if (DesiredCamLoc != CamLoc)
        	{
        		GridPoint2 NewCamLoc = new GridPoint2();
        		
        		NewCamLoc = Lerp(CamLoc, DesiredCamLoc, 0.1f);
        		SetCamLoc(NewCamLoc);
        	}
        	else
        	{
        		SetCamLoc(new GridPoint2((int)Level.Ball.Location.x + Screen.AimPoint.x, (int)Level.Ball.Location.y + Screen.AimPoint.y));
        	}
        }
        else
        {
        	SetCamLoc(new GridPoint2((int)Level.Ball.Location.x, (int)Level.Ball.Location.y));
        }
         
        Camera.update();
        
        Batch.setProjectionMatrix(Camera.combined);
        Batch.begin();        
        // Draw background
        Vector3 BackgroundLoc = new Vector3(0, 0, 0);
        Camera.unproject(BackgroundLoc);
        Batch.draw(Background, BackgroundLoc.x, BackgroundLoc.y, 1024, 1024);
        Batch.end();
        
        Renderer.setView(Camera);
        Renderer.render();           

        Batch.begin();
        // Render hole
        Batch.draw(Hole, Level.Hole.x - 16, Level.Hole.y - 16);
        
        // Render flag
        if (!Level.Ball.OnGreen)
        {
        	Batch.draw(Flag, Level.Hole.x - 8, Level.Hole.y - 8, 16, 128);
        }
        
        // Render the golfer
        if (Screen.GameMode.toString() == "Aiming" || Screen.GameMode.toString() == "Idle")
        {
        	if (Screen.ActiveClub.toString() != "P")
        	{
        		Batch.draw(GolferRegion[0][0], Level.Ball.Location.x - 32, Level.Ball.Location.y - 96, 256, 256);
        	}
        	else
        	{
        		Batch.draw(GolferPuttRegion[0][0], Level.Ball.Location.x - 32, Level.Ball.Location.y - 96, 256, 256);
        	}
        }
        else if (Screen.GameMode.toString() == "Resolving")
        {
        	// Render sand explosion
        	if (Level.Ball.Lie.toString() == "Bunker")
        	{
	        	SplashTime += delta;
	        	if (SplashTime < 0.5f)
	        	{
	        		SandFrame = SandAnim.getKeyFrame(SplashTime);
	        		Batch.draw(SandFrame, Screen.StartLoc.x - 32, Screen.StartLoc.y - 32);
	        	}
        	}
        	
        	if (Screen.ActiveClub.toString() != "P")
        	{
	        	SwingTime += delta;
	        	if (SwingTime < 0.4f)
	        	{
	        		GolferFrame = GolferSwing.getKeyFrame(SwingTime);        		
	        	}
	        	else
	        	{
	        		GolferFrame = GolferRegion[3][3];
	        	}
	        	Batch.draw(GolferFrame, Screen.StartLoc.x - 32, Screen.StartLoc.y - 96, 256, 256);
        	}
        	else
        	{
	        	SwingTime += delta;
	        	if (SwingTime < 0.25f)
	        	{
	        		GolferPuttFrame = GolferPuttSwing.getKeyFrame(SwingTime);        		
	        	}
	        	else
	        	{
	        		GolferPuttFrame = GolferPuttFrames[9];
	        	}
	        	Batch.draw(GolferPuttFrame, Screen.StartLoc.x - 32, Screen.StartLoc.y - 96, 256, 256);
        	}
        }        
        
        
        // Render trees maybe
        for (int i = 0; i < Level.Trees.getCount(); i++)
        {
        	RectangleMapObject Tree = (RectangleMapObject)Level.Trees.get(i);
        	GridPoint2 TreeLoc = new GridPoint2((int)Tree.getRectangle().x, (int)Tree.getRectangle().y);
        	
        	if (Level.Trees.get(i).getProperties().get("gid", int.class) == 257)
        	{
        		Batch.draw(TreeRegion[0][0], TreeLoc.x, TreeLoc.y);
        	}
        	else if (Level.Trees.get(i).getProperties().get("gid", int.class) == 258)
        	{
        		Batch.draw(TreeRegion[0][1], TreeLoc.x, TreeLoc.y);
        	}
        	else if (Level.Trees.get(i).getProperties().get("gid", int.class) == 259)
        	{
        		Batch.draw(TreeRegion[1][0], TreeLoc.x, TreeLoc.y);
        	}
        	else if (Level.Trees.get(i).getProperties().get("gid", int.class) == 260)
        	{
        		Batch.draw(TreeRegion[1][1], TreeLoc.x, TreeLoc.y);
        	}
        }  
        
        if (Level.Ball != null && !Level.Ball.InHole)
        {
        	if (Level.Ball.InWater)
        	{
        		SplashTime += delta;
        		if (SplashTime < 0.6f)
        		{
        			SplashFrame = SplashAnim.getKeyFrame(SplashTime);  
        			Batch.draw(SplashFrame, Level.Ball.Location.x - 16, Level.Ball.Location.y - 16);
        		}
        		else
        		{
        			// Splash is done playing
        			Screen.HazardReset();
        		}
        	}
        	else
        	{
        		Batch.draw(Level.Ball.BallTexture, Level.Ball.Location.x - (Level.Ball.Scale / 2), Level.Ball.Location.y - (Level.Ball.Scale / 2), Level.Ball.Scale, Level.Ball.Scale);
        	}
        }        
        
        if (Screen.GameMode.toString() == "Aiming")
        {
        	GridPoint2 AimGlobal = new GridPoint2();
        	AimGlobal.x = Screen.AimPoint.x + (int)Level.Ball.Location.x;
        	AimGlobal.y = Screen.AimPoint.y + (int)Level.Ball.Location.y;
        //	System.out.print("AimGlobal X:" + AimGlobal.x + " Y:" + AimGlobal.y + "\n");
        	
        	// Draw dotted aim line
        	float Distance = Vector3.dst(AimGlobal.x, AimGlobal.y, 0, Level.Ball.Location.x, Level.Ball.Location.y, 0);
        	int NumDots = (int)(Distance / 32);
        	
        	float XLen = Level.Ball.Location.x - AimGlobal.x;
        	float YLen = Level.Ball.Location.y - AimGlobal.y;
        	float XPeriod = Math.abs(XLen / 32);
        	float YPeriod = Math.abs(YLen / 32);
        	
        	for (int i=1; i < NumDots; i++)
        	{        		
        		int DotX, DotY;
        		if (XLen > 0)
        		{
        			DotX = (int)(Level.Ball.Location.x - (i * XPeriod));
        		}
        		else
        		{
        			DotX = (int)(Level.Ball.Location.x + (i * XPeriod));
        		}
        		
        		if (YLen > 0)
        		{
        			DotY = (int)(Level.Ball.Location.y - (i * YPeriod));
        		}
        		else
        		{
        			DotY = (int)(Level.Ball.Location.y + (i * YPeriod));
        		}
        	
        		Batch.draw(Dot, DotX - 8, DotY - 8);
        	}
        	
        	Batch.draw(Cursor, AimGlobal.x - 32, AimGlobal.y - 32);
        	
        }
        
        Batch.end();
        
        // Debug
        if (Debug)
        {
        	Shape.setProjectionMatrix(Camera.combined);
        	Shape.begin(ShapeType.Line);
        	
        	if (Level.Ball != null)
        	{
        		Shape.circle(Level.Ball.BallCircle.x, Level.Ball.BallCircle.y, Level.Ball.BallCircle.radius);
        		Shape.circle(Level.HoleCircle.x, Level.HoleCircle.y, Level.HoleCircle.radius);
        		
        		for (int i=0; i < Level.TreeCircle.length; i++)
        		{
        			Shape.circle(Level.TreeCircle[i].x, Level.TreeCircle[i].y, Level.TreeCircle[i].radius);
        		}
        	}
        	
        	Shape.end();
        }
        
        UI.Render(delta);        
	}
	
	public void StartSwing()
	{
		SwingTime = 0f;
		SplashTime = 0f;
	}
	
	public void SetCamLoc(GridPoint2 NewLoc)
	{
	//	System.out.print("SetCamLoc called, inX" + NewLoc.x + " inY" + NewLoc.y + " - Translating by x" + (NewLoc.x - CamLoc.x) + " y" + (NewLoc.y - CamLoc.y) + "\n");
		Camera.translate((float)(NewLoc.x - CamLoc.x), (float)(NewLoc.y - CamLoc.y));
		CamLoc = NewLoc;
	}
	
	public void SetDesiredCamLoc(GridPoint2 NewLoc)
	{
		DesiredCamLoc = NewLoc;
	}
	
	private int Lerp(int v0, int v1, float alpha)
	{
		return (int)(v0 + (v1 - v0) * alpha);
	}	
	
	private GridPoint2 Lerp(GridPoint2 v0, GridPoint2 v1, float alpha)
	{
		GridPoint2 ReturnGrid = new GridPoint2();
		
		ReturnGrid.x = (int)(v0.x + (v1.x - v0.x) * alpha);
		ReturnGrid.y = (int)(v0.y + (v1.y - v0.y) * alpha);
		
		return ReturnGrid;
	}
	
	private void InitGolferFrames()
	{
        GolferRegion = TextureRegion.split(Golfer, 256, 256);
        GolferFrames = new TextureRegion[17];
        GolferFrame = new TextureRegion();
        
        // Build golfer animation frames
        int i = 0;
        for (int x=0; x < 4; x++)
        {
        	for (int y=0; y < 4; y++)
        	{
        		GolferFrames[i++] = GolferRegion[x][y];
        	}
        }
        
        GolferSwing = new Animation(0.025f, GolferFrames);
	}
	
	private void InitPuttFrames()
	{
        GolferPuttRegion = TextureRegion.split(GolferPutt, 256, 256);
        GolferPuttFrames = new TextureRegion[17];
        GolferPuttFrame = new TextureRegion();
        
        // Build golfer animation frames
        int i = 0;
        for (int x=0; x < 4; x++)
        {
        	for (int y=0; y < 4; y++)
        	{
        			GolferPuttFrames[i++] = GolferPuttRegion[x][y];
        	}
        }
        
        GolferPuttSwing = new Animation(0.025f, GolferPuttFrames);
	}
	
	private void InitSplashFrames()
	{
		SplashRegion = TextureRegion.split(Splash, 32, 32);
		SplashFrames = new TextureRegion[6];
		SplashFrame = new TextureRegion();
		
		// Build splash animation frames
		int i = 0;
		for (int x=0; x < 4; x++)
		{
			for (int y=0; y < 4; y++)
			{
				if (i < 6)
				{
					SplashFrames[i++] = SplashRegion[x][y];
				} 
				else
				{
					break;
				}
					
			}
		}
		
		SplashAnim = new Animation(0.1f, SplashFrames);
	}
	
	private void InitSandFrames()
	{
		SandRegion = TextureRegion.split(Sand, 64, 64);
		SandFrames = new TextureRegion[5];
		SandFrame = new TextureRegion();
		
		// Build splash animation frames
		int i = 0;
		for (int x=0; x < 4; x++)
		{
			for (int y=0; y < 4; y++)
			{
				if (i < 5)
				{
					SandFrames[i++] = SandRegion[x][y];
				} 
				else
				{
					break;
				}
					
			}
		}
		
		SandAnim = new Animation(0.1f, SandFrames);
	}
}
