package com.github.hummel.sb.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.hummel.sb.SeaBattle;
import com.github.hummel.sb.bean.ButtonPanel;
import com.github.hummel.sb.bean.Cell;
import com.github.hummel.sb.iface.ScreenPlayable;
import com.github.hummel.sb.util.ShipPlacement;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class GameScreen implements Screen, ScreenPlayable {
	private final Cell[][] field = new Cell[ShipPlacement.NUM_CELLS_X][ShipPlacement.NUM_CELLS_Y];

	private final Texture emptyCellTexture;
	private final Texture shootCellTexture;
	private final Texture shipCellTexture;

	private final Texture emptyCellTextureS;
	private final Texture shootCellTextureS;
	private final Texture shipCellTextureS;

	private final Texture regenTexture;
	private final Texture shaderTexture;
	private final Texture exitTexture;

	private final Texture leftTexture;
	private final Texture rightTexture;
	private final Texture topTexture;
	private final Texture bottomTexture;

	private final Texture plusTexture;
	private final Texture minusTexture;

	private final Texture maskTexture;

	private final int cellSize;

	private final int fieldX;
	private final int fieldY;

	private int maskPosX = Gdx.graphics.getWidth() / 2;
	private int maskPosY = Gdx.graphics.getHeight() / 2;
	private int maskScale = 300;

	private final Collection<ButtonPanel> buttonPanels = new HashSet<>();

	private boolean fieldGenerated;
	private boolean buttonPressed;
	private boolean shaderEnabled;

	private final SeaBattle seaBattle;

	public GameScreen(SeaBattle seaBattle) {
		this.seaBattle = seaBattle;

		emptyCellTexture = new Texture("textures/empty.png");
		shootCellTexture = new Texture("textures/shoot.png");
		shipCellTexture = new Texture("textures/ship.png");

		emptyCellTextureS = new Texture("textures/empty_s.png");
		shootCellTextureS = new Texture("textures/shoot_s.png");
		shipCellTextureS = new Texture("textures/ship_s.png");

		regenTexture = new Texture("textures/regen.png");
		shaderTexture = new Texture("textures/shader.png");
		exitTexture = new Texture("textures/exit.png");

		leftTexture = new Texture("textures/left.png");
		rightTexture = new Texture("textures/right.png");
		topTexture = new Texture("textures/top.png");
		bottomTexture = new Texture("textures/bottom.png");

		plusTexture = new Texture("textures/plus.png");
		minusTexture = new Texture("textures/minus.png");

		maskTexture = new Texture("textures/mask.png");
		maskTexture.bind(1);

		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);

		cellSize = Math.min(Gdx.graphics.getWidth() / ShipPlacement.NUM_CELLS_X, Gdx.graphics.getHeight() / ShipPlacement.NUM_CELLS_Y);
		fieldX = (Gdx.graphics.getWidth() - ShipPlacement.NUM_CELLS_X * cellSize) / 2;
		fieldY = (Gdx.graphics.getHeight() - ShipPlacement.NUM_CELLS_Y * cellSize) / 2;

		buttonPanels.add(new ButtonPanel(this, 250, true,
				Arrays.asList(regenTexture, shaderTexture, exitTexture),
				Arrays.asList(() -> {
					if (!buttonPressed) {
						ShipPlacement.generateField(this, field);
						buttonPressed = true;
					}
				}, () -> {
					if (!buttonPressed) {
						seaBattle.switchShader();
						shaderEnabled = !shaderEnabled;
						buttonPressed = true;
					}
				}, () -> {
					seaBattle.switchShader();
					seaBattle.setScreen(new MainScreen(seaBattle));
					dispose();
				})));
		buttonPanels.add(new ButtonPanel(this, 600, false,
				Arrays.asList(plusTexture, minusTexture),
				Arrays.asList(
						() -> {
							if (shaderEnabled) {
								maskScale += 10;
							}
						},
						() -> {
							if (shaderEnabled) {
								maskScale -= 10;
							}
						}
				)));
		buttonPanels.add(new ButtonPanel(this, 400, false,
				Collections.singletonList(topTexture),
				Collections.singletonList(
						() -> {
							if (shaderEnabled) {
								maskPosY += 10;
							}
						}
				)));
		buttonPanels.add(new ButtonPanel(this, 300, false,
				Arrays.asList(leftTexture, rightTexture),
				Arrays.asList(
						() -> {
							if (shaderEnabled) {
								maskPosX -= 10;
							}
						},
						() -> {
							if (shaderEnabled) {
								maskPosX += 10;
							}
						}
				)));
		buttonPanels.add(new ButtonPanel(this, 200, false,
				Collections.singletonList(bottomTexture),
				Collections.singletonList(
						() -> {
							if (shaderEnabled) {
								maskPosY -= 10;
							}
						}
				)));
	}

	@Override
	public void render(float f) {
		ScreenUtils.clear((float) 107 / 255, (float) 125 / 255, (float) 175 / 255, 1);

		if (shaderEnabled) {
			seaBattle.getShaderMasked().bind();
			seaBattle.getShaderMasked().setUniformi("u_texture", 0);
			seaBattle.getShaderMasked().setUniformi("u_mask", 1);
			seaBattle.getShaderMasked().setUniformf("u_maskPosition", maskPosX, maskPosY);
			seaBattle.getShaderMasked().setUniformf("u_maskScale", maskScale, maskScale);
		} else {
			seaBattle.getShaderTransparent().bind();
		}

		seaBattle.getBatch().begin();

		if (!fieldGenerated) {
			ShipPlacement.generateField(this, field);
			fieldGenerated = true;
		}

		for (int i = 0; i < ShipPlacement.NUM_CELLS_X; i++) {
			for (int j = 0; j < ShipPlacement.NUM_CELLS_Y; j++) {
				field[i][j].displayWith(seaBattle.getBatch(), false);
			}
		}

		for (ButtonPanel buttonPanel : buttonPanels) {
			buttonPanel.displayWith(seaBattle.getBatch());
		}

		seaBattle.getBatch().end();

		seaBattle.getBatchS().begin();

		for (int i = 0; i < ShipPlacement.NUM_CELLS_X; i++) {
			for (int j = 0; j < ShipPlacement.NUM_CELLS_Y; j++) {
				field[i][j].displayWith(seaBattle.getBatchS(), true);
			}
		}

		seaBattle.getBatchS().end();

		if (Gdx.input.isTouched()) {
			int touchX = Gdx.input.getX();
			int touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

			for (ButtonPanel buttonPanel : buttonPanels) {
				buttonPanel.executeIfPressed(touchX, touchY);
			}
		} else {
			buttonPressed = false;
		}
	}

	@Override
	public void dispose() {
		emptyCellTexture.dispose();
		shootCellTexture.dispose();
		shipCellTexture.dispose();

		regenTexture.dispose();
		shaderTexture.dispose();
		exitTexture.dispose();

		leftTexture.dispose();
		rightTexture.dispose();
		topTexture.dispose();
		bottomTexture.dispose();

		plusTexture.dispose();
		minusTexture.dispose();

		maskTexture.dispose();
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

	@Override
	public int getCellSize() {
		return cellSize;
	}

	@Override
	public int getButtonSize() {
		return cellSize;
	}

	@Override
	public int getFieldX() {
		return fieldX;
	}

	@Override
	public int getFieldY() {
		return fieldY;
	}

	@Override
	public Texture getEmptyCellTexture() {
		return emptyCellTexture;
	}

	@Override
	public Texture getEmptyCellTextureS() {
		return emptyCellTextureS;
	}

	@Override
	public Texture getShipCellTexture() {
		return shipCellTexture;
	}

	@Override
	public Texture getShipCellTextureS() {
		return shipCellTextureS;
	}

	@Override
	public Texture getShootCellTexture() {
		return shootCellTexture;
	}

	@Override
	public Texture getShootCellTextureS() {
		return shootCellTextureS;
	}
}