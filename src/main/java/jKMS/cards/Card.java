package jKMS.cards;

public abstract class Card {
	protected int idNumber;
	protected char pack;

	Card(int idNumber, char pack) {
		this.idNumber = idNumber;
		this.pack = pack;
	}

	@Override
	public String toString() {
		return "Card [idNumber=" + idNumber + "Package="+ pack +"]";
	}
	
	public char getPackage(){
		return this.pack;
	}
	
	public int getId(){
		return this.idNumber;
	}
}
