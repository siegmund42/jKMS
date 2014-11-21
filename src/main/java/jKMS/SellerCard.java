package jKMS;

public class SellerCard extends Card {
	int cost;

	SellerCard(int idNumber, int cost) {
		super(idNumber);
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "SellerCard [idNumber=" + idNumber + ", " + "cost=" + cost + "]";
	}
}
