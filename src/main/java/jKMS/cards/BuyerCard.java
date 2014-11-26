package jKMS.cards;

public class BuyerCard extends Card {
	int wtp;

	public BuyerCard(int idNumber, int wtp, char pack) {
		super(idNumber,pack);
		this.wtp = wtp;
	}

	@Override
	public String toString() {
		return "BuyerCard [idNumber=" + idNumber + ", " + "wtp=" + wtp + "]";
	}
	
}