package com.shenko.golfgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TitleScreen implements Screen {
	
	private GolfGame Game;
	private Texture TitleTexture;
	private float TitleFade;
	private SpriteBatch Batch;
	
	private enum EMode { FadeIn, Wait, FadeOut };
	public EMode Mode;

	public TitleScreen(GolfGame inGame)
	{
		Game = inGame;
	}
	
	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
        if (Mode == EMode.FadeIn)
        {
        	TitleFade += 0.01f;
        	
        	if (TitleFade > 1)
        	{
        		TitleFade = 1f;
        		Mode = EMode.Wait;
        	}
        }
        else if (Mode == EMode.FadeOut)
        {
        	TitleFade -= 0.01f;
        	
        	if (TitleFade < 0)
        	{
        		// End fade out
        		TitleFade = 0f;
        		Game.TitleEnd();
        	}
        }
        
        Batch.setColor(1f, 1f, 1f, TitleFade);
        Batch.begin();
        TitleTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        Batch.draw(TitleTexture, 0, 0, 600, 812);
        Batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		Batch = new SpriteBatch();
		TitleFade = 0f;
		TitleTexture = new Texture(Gdx.files.internal("data/titlescreen.png"));
		
		Mode = EMode.FadeIn;
		
        TitleKeyProcessor InputProcessor = new TitleKeyProcessor();
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
		TitleTexture.dispose();
		
	}
	
	private class TitleKeyProcessor implements InputProcessor
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
			
			if (Mode == EMode.Wait)
			{
				Mode = EMode.FadeOut;
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
