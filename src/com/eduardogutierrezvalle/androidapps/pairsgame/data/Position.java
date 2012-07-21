package com.eduardogutierrezvalle.androidapps.pairsgame.data;

// position of a card in the board
public class Position {

	@Override
	public String toString() {
		return "Position [row=" + row + ", column=" + column + ", id=" + id
				+ "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (column != other.column)
			return false;
		if (id != other.id)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	public int row;
	public int column;
	public int id;
	
	public Position(int row, int column, int id) {
		this.row = row; this.column = column; this.id = id;
	}
	public Position(Position p) {
		this.row = p.row; this.column = p.column; this.id = p.id;
	}
	
}
