package com.shenko.golfgame;

import com.badlogic.gdx.Game;
import com.shenko.golfgame.GolfGameScreen.EMatchMode;

public class GolfGame extends Game {
	
	public int[] Scores;
	private String LastMode;
	
	@Override
	public void create() {
		setScreen(new TitleScreen(this));
	//	NewGame("CCF");
	}

	@Override
	public void render() {
		super.render();
	}
	
	public void MatchOver()
	{
		setScreen(new MatchOverScreen(this, LastMode));
	}
	
	public void UpdateScores(int newScore, int Index)
	{
		Scores[Index] = newScore;
	}
	
	public void TitleEnd()
	{
		setScreen(new MainMenu(this));
	}
	
	public void NewGame(String Match)
	{
        Scores = new int[19];
        
        for (int i=1; i < 19; i++)
        {
        	Scores[i] = 0;
        }
        
		if (Match == "CCF")
		{
			setScreen(new GolfGameScreen(this, "CCF"));
			LastMode = "CCF";
		}
		else if (Match == "CCB")
		{
			setScreen(new GolfGameScreen(this, "CCB"));
			LastMode = "CCB";
		}
		else if (Match == "CC18")
		{
			setScreen(new GolfGameScreen(this, "CC18"));
			LastMode = "CCF";
		}
		else if (Match == "LSF")
		{
			setScreen(new GolfGameScreen(this, "LSF"));
			LastMode = "LSF";
		}
		else if (Match == "LSB")
		{
			setScreen(new GolfGameScreen(this, "LSB"));
			LastMode = "LSB";
		}
		else if (Match == "LS18")
		{
			setScreen(new GolfGameScreen(this, "LSB"));
			LastMode = "LSF";
		}
	}
}
