package jKMS.cards;

public abstract class Card {
	protected int idNumber;

	Card(int idNumber) {
		this.idNumber = idNumber;
	}

	@Override
	public String toString() {
		return "Card [idNumber=" + idNumber + "]";
	}
}
