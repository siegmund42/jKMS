package jKMS;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigurationTest {
	private Configuration config, defaultConfig;
	
	@Before
	public void setUp(){
		defaultConfig = new Configuration();
		config = new Configuration();
		
		config.setFirstID(1257);
		config.setPlayerCount(10);
		config.setAssistantCount(5);
		config.setGroupCount(3);
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
		assertEquals("GroupCount not right!", 3, config.getGroupCount());
	}
	
	//TODO testSave()
}
