package jKMS;

import org.junit.Before;
import org.junit.Test;

import jKMS.cards.BuyerCard;
import jKMS.cards.SellerCard;

import static org.junit.Assert.*;

public class ContractTest {
	private BuyerCard buyer;
	private SellerCard seller;
	private Contract con;
	
	@Before
	public void setUp(){
		buyer = new BuyerCard(1207, 2, 'A');
		seller = new SellerCard(1624, 4, 'C');
		con = new Contract(buyer, seller, 3);
	}
	
	@Test
	public void testContract(){
		assertEquals(buyer, con.getBuyer());
		assertEquals(seller, con.getSeller());
		assertEquals(3, con.getPrice());
	}
}
