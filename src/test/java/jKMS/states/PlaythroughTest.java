package jKMS.states;

import jKMS.Amount;
import jKMS.Application;
import jKMS.Kartoffelmarktspiel;
import jKMS.cards.Card;

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
	
	@Before
	public void setUp(){
		//Setup Player/Assistant No 
		kms.getState().setBasicConfig(6, 1);
		kms.getConfiguration().setFirstID(1001);
		
		//Setup Distribution
		
		kms.getState().newGroup(true, 2, 16, 1);
		kms.getState().newGroup(true, 3, 33, 3);
		kms.getState().newGroup(false, 3, 33, 3);
		kms.getState().newGroup(false, 4, 16, 1);
		
		//Setup Distribution

		
		kms.getState().generateCards();
		
		kms.play();
	}
	
	@Test
	public void testRemoveCard(){
		final Set<Card> expectedSet;
		final Map<Integer, Amount> expectedBDistrib;
		final Map<Integer, Amount> expectedSDistrib;
		
		//Setup expected values
		expectedSet = new LinkedHashSet<Card>();
		
		for(Card iter : kms.getCards()){
			if(iter.getId() < 1004) expectedSet.add(iter);;
		}
		
		// Test falsch du legst die Verteilung zufällig über das Set --> du musst schauen wo die verteilung liegt
		expectedBDistrib = new TreeMap<Integer, Amount>();
		expectedSDistrib = new TreeMap<Integer, Amount>();
		expectedBDistrib.put(3, new Amount(0, 2));
		expectedSDistrib.put(3, new Amount(0, 1));
		
		//Test
		assertTrue("removeCard should return True, if cards are removed", kms.getState().removeCard('A', 1004));
		assertEquals("Cards are not removed successfully", expectedSet, kms.getCards());
		assertEquals("removeCard does not updates the number of players", expectedSet.size(), kms.getPlayerCount());
		assertEquals("removeCard does not update the (Buyer)-Distribution correctly", expectedBDistrib.size(), kms.getbDistribution().size());
		assertEquals("removeCard does not update the (Seller)-Distribution correctly", expectedSDistrib.size(), kms.getsDistribution().size());
	}
}
