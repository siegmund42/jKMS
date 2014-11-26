package jKMS.states;

import jKMS.Application;
import jKMS.Kartoffelmarktspiel;
import jKMS.cards.Card;
import jKMS.cards.BuyerCard;
import jKMS.cards.SellerCard;

import java.util.LinkedHashSet;
import java.util.Set;

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
	Set<Card> removed;
	
	@Before
	public void setUp(){
		//Setup Cards
		Card c1 = new BuyerCard(1001, 3, 'A');
		Card c2 = new SellerCard(1002, 3, 'A');
		Card c3 = new BuyerCard(1003, 4, 'A');
		Card c4 = new SellerCard(1004, 4, 'A');
		Card c5 = new BuyerCard(1005, 5, 'A');
		Card c6 = new SellerCard(1006, 5, 'A');
		
		//Setup package A
		kms.getCards().add(c1);
		kms.getCards().add(c2);
		kms.getCards().add(c3);
		kms.getCards().add(c4);
		kms.getCards().add(c5);
		kms.getCards().add(c6);
		
		//Setup removed package
		removed = new LinkedHashSet<Card>();
		removed.add(c1);
		removed.add(c2);
		removed.add(c3);
		
		kms.play();
	}
	
	@Test
	public void testRemoveCard(){
		assertTrue("removeCard should return True, if cards are removed", kms.getState().removeCard('A', 1004));
		assertEquals("Cards are not removed successfully", removed, kms.getCards());
	}
}
