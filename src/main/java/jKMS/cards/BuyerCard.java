package jKMS.cards;

import jKMS.Package;

public class BuyerCard extends Card{
	
	public BuyerCard(int idNumber, int value, Package pack) {
		super(idNumber, value, pack);
	}

	@Override
	public String toString() {
		return "BuyerCard " + this.pack + " [ID = " + idNumber + ", wtp = " + value + "]";
	}
}
