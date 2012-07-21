package com.eduardogutierrezvalle.androidapps.pairsgame.application;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.eduardogutierrezvalle.androidapps.pairsgame.data.Position;

public class GameState {


	int rows, columns;

	int player1Score, player2Score;

	public enum Turn {
		player1(0), player2(1);

		private int mValue;

		private Turn(int value) {
			mValue = value;
		}

		public int getValue() {
			return mValue;
		}

		public static Turn fromInt(int i) {
			for (Turn s : values()) {
				if (s.getValue() == i) {
					return s;
				}
			}
			return player1;
		}

	}

	Turn turn;

	int pending;
	boolean cardsDiscovered[][];
	int board[][];

	public boolean gameOver;

	boolean isCardDiscovered(Position card) {
		return cardsDiscovered[card.row][card.column];
	}

	boolean isCardDiscovered(int row, int column) {
		return cardsDiscovered[row][column];
	}

	void setCardDiscovered(int row, int column) {
	    cardsDiscovered[row][column] = true;
	}

	void setCardNotDiscovered(int row, int column) {
	    cardsDiscovered[row][column] = false;
	}

	
	public void setAllDiscovered(boolean discover) {
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				cardsDiscovered[i][j] = discover;
	}

	public GameState(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		cardsDiscovered = new boolean[rows][columns];
		board = new int[rows][columns];
		pending = rows * columns;
	}

	// generates a random board
	public void generateRandomBoard() {
		final Random myRandom = new Random();
		List<Integer> list = new LinkedList<Integer>();
		for (int i = 0; i < rows * columns / 2; i++) {
			list.add(i);
			list.add(i);
		}
		int total = 0;
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++) {
				int location = myRandom.nextInt(rows * columns - total);
				board[i][j] = list.remove(location);
				total++;
			}
	}
	
	// generates sequential board
	// for testing purposes
	public void generateSequentialBoard() {
		int total = 0;
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++) {
				board[i][j] = total / 2;
				total++;
			}
	}
	

}