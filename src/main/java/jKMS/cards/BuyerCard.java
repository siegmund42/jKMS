package jKMS.cards;

public class BuyerCard extends Card {
	public BuyerCard(int idNumber, int value, char pack) {
		super(idNumber, value, pack);
	}

	@Override
	public String toString() {
		return "BuyerCard [idNumber=" + idNumber + ", " + "wtp=" + value + "]";
	}
	
	public int getWtp() {
		return wtp;
	}
	
}
