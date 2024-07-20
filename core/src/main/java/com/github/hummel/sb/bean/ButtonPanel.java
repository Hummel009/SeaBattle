package com.github.hummel.sb.bean;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.github.hummel.sb.iface.ScreenPlayable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ButtonPanel {
	private final int quantity;

	private final int iconSize;

	private final List<Texture> textures;
	private final List<Runnable> actions;

	private final List<Integer> xCoords = new ArrayList<>();
	private final List<Integer> yCoords = new ArrayList<>();

	public ButtonPanel(ScreenPlayable screenPlayable, int margin, boolean fromTop, List<Texture> textures, List<Runnable> actions) {
		this.textures = textures;
		this.actions = actions;

		quantity = textures.size();

		if (textures.size() != actions.size()) {
			throw new IllegalArgumentException("Button panel error!");
		}

		iconSize = screenPlayable.getButtonSize();

		for (int i = 0; i < quantity; i++) {
			xCoords.add((Gdx.graphics.getWidth() - iconSize) / (textures.size() + 1) * (i + 1));
			yCoords.add(fromTop ? Gdx.graphics.getHeight() - iconSize - margin : margin - iconSize);
		}
	}

	public void displayWith(Batch batch) {
		for (int i = 0; i < quantity; i++) {
			batch.draw(textures.get(i), xCoords.get(i), yCoords.get(i), iconSize, iconSize);
		}
	}

	public void executeIfPressed(int touchX, int touchY) {
		for (int i = 0; i < quantity; i++) {
			if (touchX >= xCoords.get(i) && touchX <= xCoords.get(i) + iconSize && touchY >= yCoords.get(i) && touchY <= yCoords.get(i) + iconSize) {
				actions.get(i).run();
			}
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ButtonPanel that = (ButtonPanel) o;
		return quantity == that.quantity && iconSize == that.iconSize && Objects.equals(textures, that.textures) && Objects.equals(actions, that.actions) && xCoords.equals(that.xCoords) && yCoords.equals(that.yCoords);
	}

	@Override
	public int hashCode() {
		return Objects.hash(quantity, iconSize, textures, actions, xCoords, yCoords);
	}

	@Override
	public String toString() {
		return "ButtonPanel{" + "quantity=" + quantity + ", iconSize=" + iconSize + ", textures=" + textures + ", actions=" + actions + ", xCoords=" + xCoords + ", yCoords=" + yCoords + '}';
	}
}