package jKMS;

import jKMS.cards.BuyerCard;
import jKMS.cards.SellerCard;

import java.util.Date;

public class Contract {
	private BuyerCard buyer;
	private SellerCard seller;

	private int price;
	private Date time;

	public Contract(BuyerCard buyer, SellerCard seller, int price) {
		this.buyer = buyer;
		this.seller = seller;
		this.price = price;
		time = new Date();
	}

	public BuyerCard getBuyer() {
		return buyer;
	}

	public SellerCard getSeller() {
		return seller;
	}

	public int getPrice() {
		return price;
	}

	public Date getTime() {
		return time;
	}
}
