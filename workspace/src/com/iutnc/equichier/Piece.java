package com.iutnc.equichier;

public class Piece {
	private boolean blanc = true;
	private TypePiece type;
	private boolean moved = false;
	
	public Piece(boolean blanc, TypePiece type) {
		this.blanc = blanc;
		this.type = type;
	}

	public boolean estBlanc() {
		return blanc;
	}

	public TypePiece getType() {
		return type;
	}

	public boolean hasMoved() {
		return moved;
	}

	public void setMoved(boolean moved) {
		this.moved = moved;
	}
}
