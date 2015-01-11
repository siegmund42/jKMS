package jKMS;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AmountTest {
	private Amount amount = new Amount(10, 5);
	
	@Test
	public void testAmount(){
		assertEquals(10, amount.getRelative());
		assertEquals(5, amount.getAbsolute());
		
		amount.setRelative(20);
		amount.setAbsolute(10);
		
		assertEquals(20, amount.getRelative());
		assertEquals(10, amount.getAbsolute());
	}
}
