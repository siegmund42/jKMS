package jKMS.cards;

import jKMS.Package;

public abstract class Card implements Comparable<Card>	{
	protected int idNumber, value;
	protected Package pack;

	Card(int idNumber, int value, Package pack) {
		this.idNumber = idNumber;
		this.pack = pack;
		this.value = value;
		pack.add(this);
	}

	@Override
	public int compareTo(Card card) {
		if(this.idNumber < card.getId())
			return -1;
		if(this == card)
			return 0;
		if(this.idNumber > card.getId())
			return 1;
		return 42;
	}

	@Override
	public String toString() {
		return "Card [idNumber=" + idNumber + "Package="+ pack +"]";
	}
	
	public Package getPackage(){
		return this.pack;
	}
	
	public void setPackage(Package pack){
		this.pack = pack;
	}
	
	public int getValue(){
		return value;
	}
	
	public int getId(){
		return this.idNumber;
	}
}
