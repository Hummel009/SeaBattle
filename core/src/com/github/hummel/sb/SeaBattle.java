package com.github.hummel.sb;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.github.hummel.sb.screen.MainScreen;

public class SeaBattle extends Game {
	public static final ApplicationListener INSTANCE = new SeaBattle();

	private Batch batch;
	private Batch batchS;

	private FreeTypeFontGenerator freeTypeFontGenerator;
	private BitmapFont bitmapFont;

	private ShaderProgram shaderMasked;
	private ShaderProgram shaderTransparent;

	private SeaBattle() {
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		batchS = new SpriteBatch();

		freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/courier_new.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 65;
		bitmapFont = freeTypeFontGenerator.generateFont(parameter);

		ShaderProgram.pedantic = false;

		shaderMasked = new ShaderProgram(Gdx.files.internal("shaders/default.vert"), Gdx.files.internal("shaders/masked.frag"));
		shaderTransparent = new ShaderProgram(Gdx.files.internal("shaders/default.vert"), Gdx.files.internal("shaders/transparent.frag"));

		if (!shaderMasked.isCompiled() || !shaderTransparent.isCompiled()) {
			throw new IllegalArgumentException("Shader error!");
		}

		batchS.setShader(shaderTransparent);

		setScreen(new MainScreen(this));
	}

	@Override
	@SuppressWarnings("EmptyMethod")
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
		batchS.dispose();

		freeTypeFontGenerator.dispose();
		bitmapFont.dispose();

		shaderMasked.dispose();
		shaderTransparent.dispose();
	}

	public Batch getBatch() {
		return batch;
	}

	public Batch getBatchS() {
		return batchS;
	}

	public BitmapFont getBitmapFont() {
		return bitmapFont;
	}

	public ShaderProgram getShaderMasked() {
		return shaderMasked;
	}

	public ShaderProgram getShaderTransparent() {
		return shaderTransparent;
	}

	public void switchShader() {
		batchS.setShader(batchS.getShader() == shaderMasked ? shaderTransparent : shaderMasked);
	}
}