package com.eduardogutierrezvalle.androidapps.pairsgame.application;

import java.util.LinkedList;
import java.util.Random;
import android.util.Log;

import com.eduardogutierrezvalle.androidapps.pairsgame.data.Position;

public class ComputerPlayer {

	private LinkedList<Position> computerMemory;
	private GameState board;
	public int card1Row;
	public int card1Column;
	public int card1Id;
	public int card2Row;
	public int card2Column;
	public int card2Id;
	
	
	public ComputerPlayer(GameState board) {
		computerMemory = new LinkedList<Position>();
		this.board = board;
	}
	
    public void computerPlay(Position lastPlayerCard1, Position lastPlayerCard2) {
		boolean foundPair;

		// clean discovered cards from computer memory
		cleanDiscoveredCardsFromMemory();
		// adds other player's last two cards if they are not a discovered
		// pair
		Log.d("AUX", dumpComputerMemory());
		Log.d("AUX", lastPlayerCard1.toString());
		Log.d("AUX", lastPlayerCard2.toString());
		if (!board.isCardDiscovered(lastPlayerCard1)) {
			if (!hasCardInMemory(lastPlayerCard1)) {
				computerMemory.add(lastPlayerCard1);
			}
			Log.d("AUX", dumpComputerMemory());
			if (!hasCardInMemory(lastPlayerCard2)) {
				computerMemory.add(lastPlayerCard2);
			}
			Log.d("AUX", dumpComputerMemory());
		}

		// tries to find a pair among the cards the computer remembers

		foundPair = findPairInComputerMemory();
		if (foundPair)
			Log.d("AUX", "Pair found!");

		if (!foundPair) {
			Log.d("AUX", "No Pair found");
			// in case the computer doesn't know
			// a pair of cards, he will choose randomly.
			final Random myRandom = new Random();
			boolean validCard = false;
			int cRow = -1;
			int cColumn = -1;
			while (!validCard) {
				cRow = myRandom.nextInt(board.rows);
				cColumn = myRandom.nextInt(board.columns);
				if (!board.cardsDiscovered[cRow][cColumn])
					validCard = true;
			}
			card1Row = cRow;
			card1Column = cColumn;
			card1Id = board.board[cRow][cColumn];
			Position carta1 = new Position(cRow, cColumn,
					board.board[cRow][cColumn]);

			validCard = false;
			while (!validCard) {
				cRow = myRandom.nextInt(board.rows);
				cColumn = myRandom.nextInt(board.columns);
				if (!board.cardsDiscovered[cRow][cColumn]
						&& (card1Row != cRow || card1Column != cColumn))
					validCard = true;
			}
			card2Row = cRow;
			card2Column = cColumn;
			card2Id = board.board[cRow][cColumn];
			Position carta2 = new Position(card2Row, card2Column,
					board.board[card2Row][card2Column]);

			// if computer doesn't get lucky adds its selection to its memory
			if (carta1.id != carta2.id) {
				if (!hasCardInMemory(carta1))
					computerMemory.add(carta1);
				if (!hasCardInMemory(carta2))
					computerMemory.add(carta2);
			}

			// limits computer memory to make the game not perfect
			if (computerMemory.size() > 5) {
				computerMemory.removeFirst();
				computerMemory.removeFirst();
			}
		}
    }
    
	private void cleanDiscoveredCardsFromMemory() {
		int i = 0;
		while (i < computerMemory.size()) {
			if (board.isCardDiscovered(computerMemory.get(i))) {
				computerMemory.remove(i);
			} else
				i++;
		}
	}

	private boolean hasCardInMemory(Position carta) {
		for (Position c : computerMemory)
			if (c.equals(carta))
				return true;
		return false;
	}

	private Boolean findPairInComputerMemory() {
		boolean foundPair = false;
		Position p1, p2;

		for (int i = 0; i < computerMemory.size() && !foundPair; i++) {
			for (int j = i + 1; j < computerMemory.size(); j++) {
				if (computerMemory.get(i).id == computerMemory.get(j).id) {
					foundPair = true;
					p2 = computerMemory.remove(j);
					p1 = computerMemory.remove(i);
					card1Row = p1.row;
					card1Column = p1.column;
					card1Id = board.board[card1Row][card1Column];
					card2Row = p2.row;
					card2Column = p2.column;
					card2Id = board.board[card2Row][card2Column];
					break;
				}
			}
		}

		return foundPair;
	}

	private String dumpComputerMemory() {
		String data = "";
		for (Position p : computerMemory)
			data += "(" + p.row + "," + p.column + ":" + p.id + ")";
		return data;
	}
    
}
