package jKMS;

public class BuyerCard extends Card {
	int wtp;

	BuyerCard(int idNumber, int wtp) {
		super(idNumber);
		this.wtp = wtp;
	}

	@Override
	public String toString() {
		return "BuyerCard [idNumber=" + idNumber + ", " + "wtp=" + wtp + "]";
	}
}