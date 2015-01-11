package jKMS;

import static org.junit.Assert.assertEquals;
import jKMS.cards.BuyerCard;
import jKMS.cards.SellerCard;

import org.junit.Before;
import org.junit.Test;

public class ContractTest {
	private BuyerCard buyer;
	private SellerCard seller;
	private Contract con;
	
	@Before
	public void setUp(){
		buyer = new BuyerCard(1207, 2, 'A');
		seller = new SellerCard(1624, 4, 'C');
		con = new Contract(buyer, seller, 3, "TEST");
	}
	
	@Test
	public void testContract(){
		assertEquals(buyer, con.getBuyer());
		assertEquals(seller, con.getSeller());
		assertEquals(3, con.getPrice());
	}
}
