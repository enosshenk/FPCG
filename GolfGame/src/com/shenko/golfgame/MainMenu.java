package com.shenko.golfgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainMenu implements Screen {
	
	private GolfGame Game;
	private SpriteBatch Batch;
	
	private Texture MainMenuTexture;
	
	public MainMenu(GolfGame inGame)
	{
		Game = inGame;
	}

	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
		Batch.begin();
		Batch.draw(MainMenuTexture, 0, 0, 600, 812);
		Batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		Batch = new SpriteBatch();
		
		MainMenuTexture = new Texture(Gdx.files.internal("data/ui/menu.png"));		
		MainMenuTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
        MenuKeyProcessor InputProcessor = new MenuKeyProcessor();
        Gdx.input.setInputProcessor(InputProcessor);
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
	
	private class MenuKeyProcessor implements InputProcessor
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
			
			if (screenX > 83 && screenX < 231 && screenY > 505 && screenY < 558)
			{
				// CC F
			//	System.out.print("CC F\n");
				Game.NewGame("CCF");
			}
			else if (screenX > 79 && screenX < 234 && screenY > 578 && screenY < 633)
			{
				// CC B
			//	System.out.print("CC B\n");
				Game.NewGame("CCB");
			}
			else if (screenX > 90 && screenX < 224 && screenY > 658 && screenY < 715)
			{
				// CC 18
			//	System.out.print("CC 18\n");
				Game.NewGame("CC18");
			}
			else if (screenX > 366 && screenX < 519 && screenY > 506 && screenY < 554)
			{
				// LS F
			//	System.out.print("LS F\n");
				Game.NewGame("LSF");
			}
			else if (screenX > 362 && screenX < 516 && screenY > 577 && screenY < 636)
			{
				// LS B
			//	System.out.print("LS B\n");
				Game.NewGame("LSB");
			}
			else if (screenX > 368 && screenX < 514 && screenY > 660 && screenY < 716)
			{
				// LS 18
			//	System.out.print("LS 18\n");
				Game.NewGame("LSB");
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
