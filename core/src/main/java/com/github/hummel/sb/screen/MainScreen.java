package com.github.hummel.sb.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.hummel.sb.SeaBattle;

public class MainScreen implements Screen {
	private final Texture buttonTexture;

	private final SeaBattle seaBattle;

	public MainScreen(SeaBattle seaBattle) {
		this.seaBattle = seaBattle;

		buttonTexture = new Texture("textures/start.png");
	}

	@Override
	public void render(float v) {
		ScreenUtils.clear((float) 107 / 255, (float) 125 / 255, (float) 175 / 255, 1);

		seaBattle.getBatch().begin();

		int cellSize = Math.min(Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 5);

		int buttonX = (Gdx.graphics.getWidth() - cellSize) / 2;
		int buttonY = (Gdx.graphics.getHeight() - cellSize) / 2;

		seaBattle.getBatch().draw(buttonTexture, buttonX, buttonY, cellSize, cellSize);

		GlyphLayout layout1 = new GlyphLayout(seaBattle.getBitmapFont(), "Tap swords");
		int topLabelX = (int) (buttonX + (cellSize - layout1.width) / 2);
		int topLabelY = (int) (buttonY + (cellSize + layout1.height) / 2 + cellSize);
		seaBattle.getBitmapFont().draw(seaBattle.getBatch(), layout1, topLabelX, topLabelY);

		GlyphLayout layout2 = new GlyphLayout(seaBattle.getBitmapFont(), "to continue");
		int bottomLabelX = (int) (buttonX + (cellSize - layout2.width) / 2);
		int bottomLabelY = (int) (buttonY + (cellSize + layout2.height) / 2 - cellSize);
		seaBattle.getBitmapFont().draw(seaBattle.getBatch(), layout2, bottomLabelX, bottomLabelY);

		seaBattle.getBatch().end();

		if (Gdx.input.isTouched()) {
			int touchX = Gdx.input.getX();
			int touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

			if (touchX >= buttonX && touchX <= buttonX + cellSize && touchY >= buttonY && touchY <= buttonY + cellSize) {
				seaBattle.setScreen(new GameScreen(seaBattle));
				dispose();
			}
		}
	}

	@Override
	public void dispose() {
		buttonTexture.dispose();
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void resize(int i, int i1) {
	}
}