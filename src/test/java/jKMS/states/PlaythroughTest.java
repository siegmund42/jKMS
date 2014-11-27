package jKMS.states;

import jKMS.Amount;
import jKMS.Application;
import jKMS.Kartoffelmarktspiel;
import jKMS.cards.Card;
import jKMS.cards.BuyerCard;
import jKMS.cards.SellerCard;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class PlaythroughTest {
	@Autowired
	private Kartoffelmarktspiel kms;
	Set<Card> expectedSet;
	Map<Integer, Amount> expectedBDistrib;
	Map<Integer, Amount> expectedSDistrib;
	
	@Before
	public void setUp(){
		//Setup Player/Assistant No 
		kms.getState().setBasicConfig(6, 1);
		
		//Setup Distribution
		kms.getConfiguration().getbDistribution().put(3, new Amount(0, 2));
		kms.getConfiguration().getsDistribution().put(3, new Amount(0, 2));
		kms.getConfiguration().getbDistribution().put(4, new Amount(0, 1));
		kms.getConfiguration().getsDistribution().put(4, new Amount(0, 1));
		
		//Setup Cards
		Card c1 = new BuyerCard(1001, 3, 'A');
		Card c2 = new SellerCard(1002, 3, 'A');
		Card c3 = new BuyerCard(1003, 3, 'A');
		Card c4 = new SellerCard(1004, 3, 'A');
		Card c5 = new BuyerCard(1005, 4, 'A');
		Card c6 = new SellerCard(1006, 4, 'A');
		
		//Setup package A
		kms.getCards().add(c1);
		kms.getCards().add(c2);
		kms.getCards().add(c3);
		kms.getCards().add(c4);
		kms.getCards().add(c5);
		kms.getCards().add(c6);
		
		//Setup expected values
		expectedSet = new LinkedHashSet<Card>();
		expectedSet.add(c1);
		expectedSet.add(c2);
		expectedSet.add(c3);
		
		expectedBDistrib = new TreeMap<Integer, Amount>();
		expectedSDistrib = new TreeMap<Integer, Amount>();
		expectedBDistrib.put(3, new Amount(0, 2));
		expectedSDistrib.put(3, new Amount(0, 1));
		
		kms.play();
	}
	
	@Test
	public void testRemoveCard(){
		assertTrue("removeCard should return True, if cards are removed", kms.getState().removeCard('A', 1004));
		assertEquals("Cards are not removed successfully", expectedSet, kms.getCards());
		assertEquals("removeCard does not updates the number of players", expectedSet.size(), kms.getPlayerCount());
		assertEquals("removeCard does not update the (Buyer)-Distribution correctly", expectedBDistrib.size(), kms.getbDistribution().size());
		assertEquals("removeCard does not update the (Seller)-Distribution correctly", expectedSDistrib.size(), kms.getsDistribution().size());
	}
}
