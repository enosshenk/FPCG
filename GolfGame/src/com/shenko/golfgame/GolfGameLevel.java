package com.shenko.golfgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.GridPoint2;

public class GolfGameLevel {
	
	public TiledMap Map;
	public TiledMapTileLayer TerrainLayer;
	public MapObjects Objects;
	public MapObjects Trees;
	public TiledMapTileSet TileSet;
	public TiledMapTileSet TreeTileSet;
	public GolfGameScreen Screen;
	public int MapWidth;
	public int MapHeight;
	
	public GolfBall Ball;
	public GridPoint2 Tee = new GridPoint2();
	public GridPoint2 Hole = new GridPoint2();
	public Circle HoleCircle;
	public Circle[] TreeCircle;
    
    public int Par, Yards;
    
	private Sound AmbientSound;
    
    public int GetTileID(int inX, int inY)
    {
    	return TerrainLayer.getCell(inX, inY).getTile().getId();
    }
    
    public int GetWidth()
    {
    	return Map.getProperties().get("width", int.class);
    }
    
    public int GetHeight()
    {
    	return Map.getProperties().get("height", int.class);
    }
    
    public String GetTerrain(GridPoint2 inWorldPixel)
    {
    	GridPoint2 WorldTile = Screen.PixelToGrid(inWorldPixel);
    	int Tile = GetTileID(WorldTile.x, WorldTile.y);
    //	System.out.print("Getting tile for x" + inWorldPixel.x + " y" + inWorldPixel.y + " = Tile ID " + Tile + "\n");
    	
    	GridPoint2 TilePixel = Screen.WorldPixelToTile(inWorldPixel);
    	
    	return GetTileTerrain(Tile, TilePixel);
    }
    
    public boolean CheckOOB(GridPoint2 inWorldPixel)
    {
    	GridPoint2 WorldTile = Screen.PixelToGrid(inWorldPixel);
    	int Tile = GetTileID(WorldTile.x, WorldTile.y);
    	
    	if (Tile == 3)
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }

	public String GetTileTerrain(int inTile, GridPoint2 inPixel)
	{
		int i = 0;
		TiledMapTile TheTile = TileSet.getTile(inTile);
		String[] TileTerrain = TheTile.getProperties().get("terrain", String.class).split(",");
		
		if (inPixel.x < 32 && inPixel.y > 32)
		{
			i = Integer.parseInt(TileTerrain[0]);
		}
		else if (inPixel.x > 32 && inPixel.y > 32)
		{
			i = Integer.parseInt(TileTerrain[1]);
		}
		else if (inPixel.x < 32 && inPixel.y < 32)
		{
			i = Integer.parseInt(TileTerrain[2]);
		}
		else if (inPixel.x > 32 && inPixel.y < 32)
		{
			i = Integer.parseInt(TileTerrain[3]);
		}
		
		String s = new String();
		
		switch (i)
		{
		case 0:
			s = "Rough";
			break;
		case 1:
			s = "Fairway";
			break;
		case 2:
			s = "Green";
			break;
		case 3:
			s = "Bunker";
			break;
		case 4:
			s = "Water";
			break;
		}
		
		return s;
	}
	
	public void FindTeeAndHole()
	{
		AmbientSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/amb_outside.ogg"));
		AmbientSound.loop();
		
		for (int i = 0; i < Objects.getCount(); i++)
		{
			if (Objects.get(i).getName().equalsIgnoreCase("tee"))
			{
				EllipseMapObject e = (EllipseMapObject)Objects.get(i);
				Tee.x = (int)e.getEllipse().x;
				Tee.y = (int)e.getEllipse().y;
			}
			
			if (Objects.get(i).getName().equalsIgnoreCase("hole"))
			{
				EllipseMapObject e = (EllipseMapObject)Objects.get(i);
				Hole.x = (int)e.getEllipse().x;
				Hole.y = (int)e.getEllipse().y;
				
				HoleCircle = new Circle(Hole.x, Hole.y, 10);
			}
		}		
	}
	
	public void InitTrees()
	{
		TreeCircle = new Circle[Trees.getCount()];
				
		for (int i=0; i < Trees.getCount(); i++)
		{
			RectangleMapObject t = (RectangleMapObject)Trees.get(i);
			GridPoint2 tl = new GridPoint2((int)t.getRectangle().x, (int)t.getRectangle().y);
						
			if (Trees.get(i).getProperties().get("gid", int.class) == 257)
			{				
				TreeCircle[i] = new Circle(tl.x + 125, tl.y + 104, 63);
			}
			if (Trees.get(i).getProperties().get("gid", int.class) == 258)
			{				
				TreeCircle[i] = new Circle(tl.x + 133, tl.y + 112, 59);
			}
			if (Trees.get(i).getProperties().get("gid", int.class) == 259)
			{				
				TreeCircle[i] = new Circle(tl.x + 128, tl.y + 105, 70);
			}
			if (Trees.get(i).getProperties().get("gid", int.class) == 260)
			{				
				TreeCircle[i] = new Circle(tl.x + 133, tl.y + 96, 51);
			}			
			
		}
	}
	
	public void SpawnBall()
	{
		Ball = new GolfBall(this, Screen);
		Ball.SetLocation(Tee.x, Tee.y, 0);
		
//		Screen.Renderer.SetCamLoc( new GridPoint2(Hole.x, Hole.y) );
//		Screen.Renderer.SetDesiredCamLoc( new GridPoint2(Tee.x, Tee.y) );
	}
	
	public float GetBounceFactor(String inTerrain)
	{
		float ReturnFactor = 0;
		
		if (inTerrain == "Rough")
		{
			ReturnFactor = 0.3f;
		}
		else if (inTerrain == "Fairway")
		{
			ReturnFactor = 0.5f;
		}
		else if (inTerrain == "Green")
		{
			ReturnFactor = 0.5f;
		}
		else if (inTerrain == "Bunker")
		{
			ReturnFactor = 0.1f;
		}
		
		return ReturnFactor;
	}
	
	public float GetFrictionFactor(String inTerrain)
	{
		float ReturnFactor = 0;
		
		if (inTerrain == "Rough")
		{
			ReturnFactor = 0.3f;
		}
		else if (inTerrain == "Fairway")
		{
			ReturnFactor = 0.5f;
		}
		else if (inTerrain == "Green")
		{
			ReturnFactor = 0.6f;
		}
		else if (inTerrain == "Bunker")
		{
			ReturnFactor = 0.3f;
		}
		
		return ReturnFactor;
	}
	
	public float GetPuttFactor(String inTerrain)
	{
		float ReturnFactor = 0;
		
		if (inTerrain == "Rough")
		{
			ReturnFactor = 0.5f;
		}
		else if (inTerrain == "Fairway")
		{
			ReturnFactor = 0.92f;
		}
		else if (inTerrain == "Green")
		{
			ReturnFactor = 0.97f;
		}
		else if (inTerrain == "Bunker")
		{
			ReturnFactor = 0.2f;
		}
		
		return ReturnFactor;		
	}
	
	public void LevelDone()
	{
		AmbientSound.stop();
		AmbientSound.dispose();
	}
}
