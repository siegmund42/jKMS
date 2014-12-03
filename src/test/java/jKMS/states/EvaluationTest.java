package jKMS.states;

import java.util.LinkedHashSet;
import java.util.Set;

import jKMS.Application;
import jKMS.Kartoffelmarktspiel;
import jKMS.cards.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.cypher.internal.compiler.v2_1.ast.rewriters.addUniquenessPredicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class EvaluationTest {
	@Autowired
	Kartoffelmarktspiel kms;
	
	@Before
	public void setUp(){
		Set<Card> c;
		
		//Preparation
		kms.prepare();
		
		kms.getState().setBasicConfig(800, 10);
		kms.getState().loadStandardDistribution();
		
		try{
			kms.getState().generateCards();
			}catch (Exception e) {
				e.printStackTrace();	
			}
		
		
		//Playthrough
		kms.play();
		
		c = new LinkedHashSet<Card>(kms.getCards());
		
		for(Card id1 : kms.getCards()){
			if(id1 instanceof BuyerCard){
				for (Card id2 : c){
					if(id2 instanceof SellerCard){
						kms.getState().addContract(id1.getId(), id2.getId(), Math.min(id1.getValue(), id2.getValue()) + Math.abs(id1.getValue() - id2.getValue())/2);
						c.remove(id2);
						
						break;
					}
				}
			}
		}
	}
	
	@Test
	//TODO testGetStatistics
	public void testGetStatistics(){
		
	}
	
	@Test
	//TODO testPickWinner
	public void testPickWinner(){
		
	}
}
