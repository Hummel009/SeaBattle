package com.github.hummel.sb.util;

import com.github.hummel.sb.bean.Cell;
import com.github.hummel.sb.iface.ScreenPlayable;

import java.util.Random;

public class ShipPlacement {
	public static final int NUM_CELLS_X = 10;
	public static final int NUM_CELLS_Y = 10;

	@SuppressWarnings("TypeMayBeWeakened")
	private static final Random RANDOM = new Random();

	private ShipPlacement() {
	}

	public static void generateField(ScreenPlayable screenPlayable, Cell[][] field) {
		for (int i = 0; i < NUM_CELLS_X; i++) {
			for (int j = 0; j < NUM_CELLS_Y; j++) {
				field[i][j] = new Cell(screenPlayable, i, j);
			}
		}

		placeRandomShip(field, 4);

		placeRandomShip(field, 3);
		placeRandomShip(field, 3);

		placeRandomShip(field, 2);
		placeRandomShip(field, 2);
		placeRandomShip(field, 2);

		placeRandomShip(field, 1);
		placeRandomShip(field, 1);
		placeRandomShip(field, 1);
		placeRandomShip(field, 1);

		for (int i = 0; i < NUM_CELLS_X; i++) {
			for (int j = 0; j < NUM_CELLS_Y; j++) {
				if (field[i][j].isEmpty() && RANDOM.nextDouble() < 0.19) {
					field[i][j].setIsShoot();
				}
			}
		}
	}

	private static void placeRandomShip(Cell[][] field, int shipSize) {
		while (true) {
			int randomI = RANDOM.nextInt(NUM_CELLS_X);
			int randomJ = RANDOM.nextInt(NUM_CELLS_Y);

			boolean canPlaceShip = false;

			int dir = -1;
			int shipType = shipSize - 1;

			for (int i = 0; i <= 3; i++) {
				if (!canPlaceShip) {
					dir = i;
					canPlaceShip = getPrimaryPlaceCondition(field, shipType, randomI, randomJ, dir);
				}
			}

			if (!canPlaceShip) {
				continue;
			}

			canPlaceShip = getSecondaryPlaceCondition(field, shipType, randomI, randomJ, dir);

			if (!canPlaceShip) {
				continue;
			}

			for (int[] shifts : RelativeCoords.SHIP[shipType][dir]) {
				try {
					field[randomI + shifts[0]][randomJ + shifts[1]].setIsShip();
				} catch (ArrayIndexOutOfBoundsException ignored) {
				}
			}

			for (int[] shifts : RelativeCoords.BORDER[shipType][dir]) {
				try {
					field[randomI + shifts[0]][randomJ + shifts[1]].setIsBorder();
				} catch (ArrayIndexOutOfBoundsException ignored) {
				}
			}

			break;
		}
	}

	private static boolean getPrimaryPlaceCondition(Cell[][] field, int shipType, int randomI, int randomJ, int rule) {
		boolean canPlaceShip = true;
		for (int[] shifts : RelativeCoords.SHIP[shipType][rule]) {
			try {
				if (!field[randomI + shifts[0]][randomJ + shifts[1]].isEmptyAndNotBorder()) {
					canPlaceShip = false;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				canPlaceShip = false;
			}
		}
		return canPlaceShip;
	}

	private static boolean getSecondaryPlaceCondition(Cell[][] field, int shipType, int randomI, int randomJ, int rule) {
		boolean canPlaceShip = true;
		for (int[] shifts : RelativeCoords.BORDER[shipType][rule]) {
			try {
				if (!field[randomI + shifts[0]][randomJ + shifts[1]].isEmpty()) {
					canPlaceShip = false;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				canPlaceShip = true;
			}
		}
		return canPlaceShip;
	}
}
