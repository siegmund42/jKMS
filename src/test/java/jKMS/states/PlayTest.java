package jKMS.states;

import java.util.TreeMap;

import jKMS.Amount;
import jKMS.Application;
import jKMS.Kartoffelmarktspiel;

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
public class PlayTest {
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
		
		//Setup Distribution pro distribution 100 % because eingabemaske
		kms.getState().newGroup(true, 2, 20, 2);
		kms.getState().newGroup(true, 3, 20, 2);
		kms.getState().newGroup(true, 4, 60, 1);
		kms.getState().newGroup(false, 2, 60, 1);
		kms.getState().newGroup(false, 3, 20, 2);
		kms.getState().newGroup(false, 4, 20, 2);
		
		try{
			kms.getState().generateCards();
		}catch (Exception e) {
			e.printStackTrace();	
		}
		
		
		kms.load();
		kms.play();
	}
	
	@Test
	public void testAddContract(){
		
	}
	
	@Test
	public void testLoad(){
		
	}
	

}
