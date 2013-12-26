package com.shenko.golfgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Scorecard {
	
	private GolfGameScreen Screen;
	private String Mode;	
	private Texture CCS1, CCS2, LSS1, LSS2;
	private BitmapFont Font;
	
	public Scorecard (String inMode, GolfGameScreen inScreen)
	{
		Mode = inMode;
		Screen = inScreen;
		Font = new BitmapFont( Gdx.files.internal("data/GABRIOLA-32.fnt"), Gdx.files.internal("data/ScoreFont.png"), false );
		
		CCS1 = new Texture(Gdx.files.internal("data/ui/ccscorecard1.png"));
		CCS2 = new Texture(Gdx.files.internal("data/ui/ccscorecard2.png"));
		LSS1 = new Texture(Gdx.files.internal("data/ui/lsscorecard1.png"));
		LSS2 = new Texture(Gdx.files.internal("data/ui/lsscorecard2.png"));
	}
	
	public void SwitchMode(String newMode)
	{
		Mode = newMode;
	}
	
	public void Render(SpriteBatch Batch)
	{
		Font.setColor(0f, 0f, 0f, 1f);
		
		if (Mode == "CCF")
		{
			Batch.draw(CCS1, 44, 144, 512, 512);
			
			Font.draw(Batch, "Fred", 66, 545);
			
			if (Screen.Scores[1] != 0)
			{
				Font.draw(Batch, Screen.Scores[1]+"", 182, 545);
			}
			if (Screen.Scores[2] != 0)
			{
				Font.draw(Batch, Screen.Scores[2]+"", 209, 545);
			}
			if (Screen.Scores[3] != 0)
			{
				Font.draw(Batch, Screen.Scores[3]+"", 234, 545);
			}
			if (Screen.Scores[4] != 0)
			{
				Font.draw(Batch, Screen.Scores[4]+"", 264, 545);
			}
			if (Screen.Scores[5] != 0)
			{
				Font.draw(Batch, Screen.Scores[5]+"", 291, 545);
			}
			if (Screen.Scores[6] != 0)
			{
				Font.draw(Batch, Screen.Scores[6]+"", 316, 545);
			}
			if (Screen.Scores[7] != 0)
			{
				Font.draw(Batch, Screen.Scores[7]+"", 349, 545);
			}
			if (Screen.Scores[8] != 0)
			{
				Font.draw(Batch, Screen.Scores[8]+"", 376, 545);
			}
			if (Screen.Scores[9] != 0)
			{
				Font.draw(Batch, Screen.Scores[9]+"", 400, 545);
			}			
		}
		else if (Mode == "CCB")
		{
			Batch.draw(CCS2, 44, 144, 512, 512);
			
			if (Screen.Scores[10] != 0)
			{
				Font.draw(Batch, Screen.Scores[10]+"", 189, 539);
			}
			if (Screen.Scores[11] != 0)
			{
				Font.draw(Batch, Screen.Scores[11]+"", 218, 539);
			}
			if (Screen.Scores[12] != 0)
			{
				Font.draw(Batch, Screen.Scores[12]+"", 242, 539);
			}
			if (Screen.Scores[13] != 0)
			{
				Font.draw(Batch, Screen.Scores[13]+"", 270, 539);
			}
			if (Screen.Scores[14] != 0)
			{
				Font.draw(Batch, Screen.Scores[14]+"", 300, 539);
			}
			if (Screen.Scores[15] != 0)
			{
				Font.draw(Batch, Screen.Scores[15]+"", 331, 539);
			}
			if (Screen.Scores[16] != 0)
			{
				Font.draw(Batch, Screen.Scores[16]+"", 355, 539);
			}
			if (Screen.Scores[17] != 0)
			{
				Font.draw(Batch, Screen.Scores[17]+"", 384, 539);
			}
			if (Screen.Scores[18] != 0)
			{
				Font.draw(Batch, Screen.Scores[18]+"", 410, 539);
			}	
		}
		else if (Mode == "LSF")
		{
			Batch.draw(LSS1, 44, 144, 512, 512);
			
			Font.draw(Batch, "Fred", 52, 431);
			
			if (Screen.Scores[1] != 0)
			{
				Font.draw(Batch, Screen.Scores[1]+"", 206, 431);
			}
			if (Screen.Scores[2] != 0)
			{
				Font.draw(Batch, Screen.Scores[2]+"", 242, 431);
			}
			if (Screen.Scores[3] != 0)
			{
				Font.draw(Batch, Screen.Scores[3]+"", 276, 431);
			}
			if (Screen.Scores[4] != 0)
			{
				Font.draw(Batch, Screen.Scores[4]+"", 309, 431);
			}
			if (Screen.Scores[5] != 0)
			{
				Font.draw(Batch, Screen.Scores[5]+"", 340, 431);
			}
			if (Screen.Scores[6] != 0)
			{
				Font.draw(Batch, Screen.Scores[6]+"", 376, 431);
			}
			if (Screen.Scores[7] != 0)
			{
				Font.draw(Batch, Screen.Scores[7]+"", 411, 431);
			}
			if (Screen.Scores[8] != 0)
			{
				Font.draw(Batch, Screen.Scores[8]+"", 442, 431);
			}
			if (Screen.Scores[9] != 0)
			{
				Font.draw(Batch, Screen.Scores[9]+"", 476, 431);
			}	
		}
		else if (Mode == "LSB")
		{
			Batch.draw(LSS2, 44, 144, 512, 512);
			
			if (Screen.Scores[10] != 0)
			{
				Font.draw(Batch, Screen.Scores[10]+"", 76, 431);
			}
			if (Screen.Scores[11] != 0)
			{
				Font.draw(Batch, Screen.Scores[11]+"", 109, 431);
			}
			if (Screen.Scores[12] != 0)
			{
				Font.draw(Batch, Screen.Scores[12]+"", 141, 431);
			}
			if (Screen.Scores[13] != 0)
			{
				Font.draw(Batch, Screen.Scores[13]+"", 173, 431);
			}
			if (Screen.Scores[14] != 0)
			{
				Font.draw(Batch, Screen.Scores[14]+"", 206, 431);
			}
			if (Screen.Scores[15] != 0)
			{
				Font.draw(Batch, Screen.Scores[15]+"", 238, 431);
			}
			if (Screen.Scores[16] != 0)
			{
				Font.draw(Batch, Screen.Scores[16]+"", 271, 431);
			}
			if (Screen.Scores[17] != 0)
			{
				Font.draw(Batch, Screen.Scores[17]+"", 303, 431);
			}
			if (Screen.Scores[18] != 0)
			{
				Font.draw(Batch, Screen.Scores[18]+"", 337, 431);
			}
		}
	}
}
