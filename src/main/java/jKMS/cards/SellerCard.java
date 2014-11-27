package jKMS.cards;

public class SellerCard extends Card {
	int cost;

	public SellerCard(int idNumber, int cost, char pack) {
		super(idNumber,pack);
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "SellerCard [idNumber=" + idNumber + ", " + "cost=" + cost + "]";
	}
	
	public int getCost() {
		return cost;
	}
}
