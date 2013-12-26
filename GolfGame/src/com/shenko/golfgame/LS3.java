package com.shenko.golfgame;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class LS3 extends GolfGameLevel {

	public LS3(GolfGameScreen inScreen)
	{
		Screen = inScreen;
		
        Map = new TmxMapLoader().load("data/maps/ls3.tmx");
        TerrainLayer = (TiledMapTileLayer)Map.getLayers().get(0);
        Objects = Map.getLayers().get(1).getObjects();
        Trees = Map.getLayers().get(2).getObjects();
        TileSet = Map.getTileSets().getTileSet(0);
        TreeTileSet = Map.getTileSets().getTileSet(1);
        
        Par = 3;
        Yards = 155;
        
        FindTeeAndHole();
        SpawnBall();      
        InitTrees();
	}

}
