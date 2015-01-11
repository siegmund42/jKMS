package jKMS;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

public class ConfigurationTest {
	private Configuration config, defaultConfig;
	
	@Before
	public void setUp(){
		defaultConfig = new Configuration();
		config = new Configuration();
		
		config.setFirstID(1257);
		config.setPlayerCount(10);
		config.setAssistantCount(5);
		
		Map<Integer, Amount> bDist = new TreeMap<>();
		Map<Integer, Amount> sDist = new TreeMap<>();
		
		bDist.put(11, new Amount(10, 1));
		bDist.put(22, new Amount(10, 1));
		bDist.put(33, new Amount(10, 1));
		bDist.put(44, new Amount(10, 1));
		bDist.put(55, new Amount(10, 1));
		bDist.put(66, new Amount(10, 1));
		sDist.put(22, new Amount(10, 1));
		sDist.put(33, new Amount(10, 1));
		sDist.put(44, new Amount(10, 1));
		sDist.put(55, new Amount(10, 1));
		config.setbDistribution(bDist);
		config.setsDistribution(sDist);
	}
	
	@Test
	public void testDefautlConfig(){
		assertEquals(1001, defaultConfig.getFirstID());
	}
	
	@Test
	public void testGetter(){
		assertEquals("FirstID not right!", 1257, config.getFirstID());
		assertEquals("PlayerCount not right!", 10, config.getPlayerCount());
		assertEquals("AssistantCount not right!", 5, config.getAssistantCount());
		assertEquals("GroupCount [Buyer] is not right!", 6, config.getGroupCount("b"));
		assertEquals("GroupCount [Seller] is not right!", 4, config.getGroupCount("s"));
	}
	
	//TODO testSave()
}
