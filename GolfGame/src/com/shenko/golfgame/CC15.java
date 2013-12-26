package com.shenko.golfgame;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class CC15 extends GolfGameLevel {

	public CC15(GolfGameScreen inScreen)
	{
		Screen = inScreen;
		
        Map = new TmxMapLoader().load("data/maps/cc15.tmx");
        TerrainLayer = (TiledMapTileLayer)Map.getLayers().get(0);
        Objects = Map.getLayers().get(1).getObjects();
        Trees = Map.getLayers().get(2).getObjects();
        TileSet = Map.getTileSets().getTileSet(0);
        TreeTileSet = Map.getTileSets().getTileSet(1);
        
        Par = 4;
        Yards = 352;
        
        FindTeeAndHole();
        SpawnBall();      
        InitTrees();
	}

}
