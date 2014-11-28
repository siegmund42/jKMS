package jKMS.cards;

import org.junit.Test;

import static org.junit.Assert.*;

public class SellerCardTest {
	
	@Test
	public void testSellerCard(){
		SellerCard seller = new SellerCard(2345, 8, 'X');
		
		assertEquals(2345, seller.getId());
		assertEquals(8, seller.getValue());
		assertEquals('X', seller.getPackage());
	}
}
