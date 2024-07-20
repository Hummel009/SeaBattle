package com.github.hummel.sb.iface;

import com.badlogic.gdx.graphics.Texture;

@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
public interface ScreenPlayable {
	int getCellSize();

	int getButtonSize();

	int getFieldX();

	int getFieldY();

	Texture getEmptyCellTexture();

	Texture getEmptyCellTextureS();

	Texture getShipCellTexture();

	Texture getShipCellTextureS();

	Texture getShootCellTexture();

	Texture getShootCellTextureS();
}