package jKMS.cards;

public abstract class Card {
	protected int idNumber, value;
	protected char pack;

	Card(int idNumber, int value, char pack) {
		this.idNumber = idNumber;
		this.pack = pack;
		this.value = value;
	}

	@Override
	public String toString() {
		return "Card [idNumber=" + idNumber + "Package="+ pack +"]";
	}
	
	public char getPackage(){
		return this.pack;
	}
	
	public void setPackage(char pack){
		this.pack =pack;
	}
	
	public int getValue(){
		return value;
	}
	
	public int getId(){
		return this.idNumber;
	}
}
