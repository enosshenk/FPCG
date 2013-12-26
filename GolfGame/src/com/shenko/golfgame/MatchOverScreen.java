package com.shenko.golfgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MatchOverScreen implements Screen {
	
	private SpriteBatch Batch;
	private GolfGame Game;
	private FinalScorecard Card;
	private String Mode;

	public int[] Scores;
	
	public MatchOverScreen (GolfGame inGame, String lastMode)
	{
		Game = inGame;
		
		Scores = Game.Scores.clone();
		
		Batch = new SpriteBatch();
		Card = new FinalScorecard(lastMode, this);
		Mode = lastMode;
		
        MOKeyProcessor InputProcessor = new MOKeyProcessor();
        Gdx.input.setInputProcessor(InputProcessor);
	}

	@Override
	public void render(float delta) {
		Batch.begin();
		Card.Render(Batch);	
		Batch.end();
	}
	

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}
	
	private class MOKeyProcessor implements InputProcessor
	{

		@Override
		public boolean keyDown(int keycode) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer,
				int button) {

			if (screenY > 90)
			{
				if (Mode == "CCF")
				{
					if (screenX > 300)
					{
						Card.SwitchMode("CCB");
						Mode = "CCB";
					}
				}
				else if (Mode == "CCB")
				{
					if (screenX < 300)
					{
						Card.SwitchMode("CCF");
						Mode = "CCF";
					}
				}
				else if (Mode == "LSF")
				{
					if (screenX > 300)
					{
						Card.SwitchMode("LSB");
						Mode = "LSB";
					}
				}
				else if (Mode == "LSB")
				{
					if (screenX < 300)
					{
						Card.SwitchMode("LSF");
						Mode = "LSF";
					}
				}
			}
			else
			{
				Game.TitleEnd();
			}
			
			
			return true;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			// TODO Auto-generated method stub
			return false;
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
