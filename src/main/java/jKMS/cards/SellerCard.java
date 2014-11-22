package jKMS.cards;

public class SellerCard extends Card {
	int cost;

	public SellerCard(int idNumber, int cost) {
		super(idNumber);
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "SellerCard [idNumber=" + idNumber + ", " + "cost=" + cost + "]";
	}
}
