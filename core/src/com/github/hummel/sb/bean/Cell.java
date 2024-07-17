package com.github.hummel.sb.bean;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.github.hummel.sb.iface.ScreenPlayable;

import java.util.Objects;

public class Cell {
	private final ScreenPlayable screenPlayable;

	private final int iconSize;

	private Texture texture;
	private Texture textureS;

	private boolean border;

	private final int x;
	private final int y;

	public Cell(ScreenPlayable screenPlayable, int i, int j) {
		this.screenPlayable = screenPlayable;

		iconSize = screenPlayable.getCellSize();

		x = screenPlayable.getFieldX() + i * iconSize;
		y = screenPlayable.getFieldY() + j * iconSize;

		texture = screenPlayable.getEmptyCellTexture();
		textureS = screenPlayable.getEmptyCellTextureS();
	}

	public void setIsShip() {
		texture = screenPlayable.getShipCellTexture();
		textureS = screenPlayable.getShipCellTextureS();
	}

	public void setIsShoot() {
		texture = screenPlayable.getShootCellTexture();
		textureS = screenPlayable.getShootCellTextureS();
	}

	public void setIsBorder() {
		border = true;
	}

	public boolean isEmpty() {
		return screenPlayable.getEmptyCellTexture().equals(texture);
	}

	public boolean isEmptyAndNotBorder() {
		return screenPlayable.getEmptyCellTexture().equals(texture) && !border;
	}

	public void displayWith(Batch batch, boolean s) {
		batch.draw(s ? textureS : texture, x, y, iconSize, iconSize);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Cell cell = (Cell) o;
		return iconSize == cell.iconSize && border == cell.border && x == cell.x && y == cell.y && Objects.equals(screenPlayable, cell.screenPlayable) && Objects.equals(texture, cell.texture) && Objects.equals(textureS, cell.textureS);
	}

	@Override
	public int hashCode() {
		return Objects.hash(screenPlayable, iconSize, texture, textureS, border, x, y);
	}

	@Override
	public String toString() {
		return "Cell{" + "screenPlayable=" + screenPlayable + ", iconSize=" + iconSize + ", texture=" + texture + ", textureS=" + textureS + ", border=" + border + ", x=" + x + ", y=" + y + '}';
	}
}