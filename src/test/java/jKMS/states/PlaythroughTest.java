package jKMS.states;

import jKMS.Amount;
import jKMS.Application;
import jKMS.Kartoffelmarktspiel;
import jKMS.cards.BuyerCard;
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
		kms.prepare();
		kms.getCards().clear();
		kms.getConfiguration().setbDistribution(new TreeMap<Integer, Amount>());
		kms.getConfiguration().setsDistribution(new TreeMap<Integer, Amount>());
		
		//Setup Player/Assistant No 
		kms.getState().setBasicConfig(10, 1);
		kms.getConfiguration().setFirstID(1001);
		
		//Setup Distribution
		kms.getState().newGroup(true, 2, 20, 2);
		kms.getState().newGroup(true, 3, 20, 2);
		kms.getState().newGroup(true, 4, 10, 1);
		kms.getState().newGroup(false, 2, 10, 1);
		kms.getState().newGroup(false, 3, 20, 2);
		kms.getState().newGroup(false, 4, 20, 2);
		
		kms.getState().generateCards();
		
		kms.play();
	}
	
	@Test
	public void testAddContract(){
		
	}
	
	@Test
	public void testLoad(){
		
	}
	
	@Test
	public void testRemoveCard(){
		Set<Card> expectedSet;
		Map<Integer, Amount> expectedBDistrib;
		Map<Integer, Amount> expectedSDistrib;
		Map<Integer, Amount> distrib;
		int key;
		
		expectedBDistrib = new TreeMap<Integer, Amount>();
		expectedSDistrib = new TreeMap<Integer, Amount>();
		
		//Setup expected values
		expectedSet = new LinkedHashSet<Card>();
		
		for(Card iter : kms.getCards()){
			if(iter.getId() < 1004){
				expectedSet.add(iter);
				
				if(iter instanceof BuyerCard) distrib = expectedBDistrib;
				else distrib = expectedSDistrib;
				
				key = iter.getValue();
						
				//If key does not exist in map - create a new Amount
				if(!distrib.containsKey(key))
					distrib.put(key, new Amount(0, 1));
				//Else increase absolute of its Amount
				else
					distrib.get(key).setAbsolute(distrib.get(key).getAbsolute()+1);
			}
		}
		
		//Test
		assertTrue("removeCard should return True, if cards are removed", kms.getState().removeCard('A', 1004));
		assertEquals("Cards are not removed successfully", expectedSet, kms.getCards());
		assertEquals("removeCard does not updates the number of players", expectedSet.size(), kms.getPlayerCount());
		assertEquals("removeCard does not update the (Buyer)-Distribution correctly", expectedBDistrib.size(), kms.getbDistribution().size());
		assertEquals("removeCard does not update the (Seller)-Distribution correctly", expectedSDistrib.size(), kms.getsDistribution().size());
	}
}
