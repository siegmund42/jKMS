package jKMS.cards;

import jKMS.Package;

public class SellerCard extends Card  {
	public SellerCard(int idNumber, int value, Package pack) {
		super(idNumber, value, pack);
	}

	@Override
	public String toString() {
		return "SellerCard " + this.pack + " [ID = " + idNumber + ", cost = " + value + "]";
	}
}
