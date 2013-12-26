package com.shenko.golfgame;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class LS5 extends GolfGameLevel {

	public LS5(GolfGameScreen inScreen)
	{
		Screen = inScreen;
		
        Map = new TmxMapLoader().load("data/maps/ls5.tmx");
        TerrainLayer = (TiledMapTileLayer)Map.getLayers().get(0);
        Objects = Map.getLayers().get(1).getObjects();
        Trees = Map.getLayers().get(2).getObjects();
        TileSet = Map.getTileSets().getTileSet(0);
        TreeTileSet = Map.getTileSets().getTileSet(1);
        
        Par = 3;
        Yards = 160;
        
        FindTeeAndHole();
        SpawnBall();      
        InitTrees();
	}

}
