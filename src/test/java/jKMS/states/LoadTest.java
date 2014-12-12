package jKMS.states;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import jKMS.Amount;
import jKMS.Application;
import jKMS.Kartoffelmarktspiel;
import jKMS.cards.BuyerCard;
import jKMS.cards.Card;
import jKMS.cards.SellerCard;
import jKMS.exceptionHelper.EmptyFileException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.multipart.MultipartFile;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class LoadTest {
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
	}
	
	@Test
	public void testRemoveCard(){
		Set<Card> expectedSet;
		Map<Integer, Amount> expectedBDistrib;
		Map<Integer, Amount> expectedSDistrib;
		Map<Integer, Amount> distrib;
		int key;
		boolean test = false;
		
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
		
		try{
		
			test = kms.getState().removeCard('A', 1004);
		
		}catch (Exception e) {
			e.printStackTrace();	
		}
		assertTrue("removeCard should return True, if cards are removed",test );
		assertEquals("Cards are not removed successfully", expectedSet, kms.getCards());
		assertEquals("removeCard does not updates the number of players", expectedSet.size(), kms.getPlayerCount());
		assertEquals("removeCard does not update the (Buyer)-Distribution correctly", expectedBDistrib.size(), kms.getbDistribution().size());
		assertEquals("removeCard does not update the (Seller)-Distribution correctly", expectedSDistrib.size(), kms.getsDistribution().size());
	}
	
	@Test
	public void testLoad(){
		int expectedPlayerCount = 8;
		int expectedAssistantCount = 2;
		int expectedGroupCount = 3;
    	int expectedFirstID = 1001;
    	Map<Integer, Amount> expectedbDistribution = new TreeMap<>();
		Map<Integer, Amount> expectedsDistribution = new TreeMap<>();
		Set<Card> expectedCardSet = new LinkedHashSet<Card>();
		expectedbDistribution.put(56,new Amount(25,1));
		expectedbDistribution.put(65,new Amount(25,1));
		expectedbDistribution.put(66,new Amount(50,2));
		expectedsDistribution.put(56,new Amount(25,1));
		expectedsDistribution.put(65,new Amount(25,1));
		expectedsDistribution.put(66,new Amount(50,2));
		expectedCardSet.add(new BuyerCard(1001,56,'A'));
		expectedCardSet.add(new BuyerCard(1003,65,'A'));
		expectedCardSet.add(new BuyerCard(1005,66,'B'));
		expectedCardSet.add(new BuyerCard(1007,66,'B'));
		expectedCardSet.add(new SellerCard(1002,56,'A'));
		expectedCardSet.add(new SellerCard(1004,65,'A'));
		expectedCardSet.add(new SellerCard(1006,66,'B'));
		expectedCardSet.add(new SellerCard(1008,66,'B'));
		
		String pathFile = "src/test/java/jKMS/states/loadTestFile.txt";
		
	    String line = System.getProperty("line.separator");
		   StringBuffer str = new StringBuffer();
		   FileWriter fw = null;
		try {
			fw = new FileWriter(pathFile, false);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		   str.append("PlayerCount:").append(String.valueOf(expectedPlayerCount)).append(line)
		   .append("AssistantCount:").append(String.valueOf(expectedAssistantCount)).append(line)
		   .append("GroupCount:").append(String.valueOf(expectedGroupCount)).append(line)
		   .append("FirstID:").append(String.valueOf(expectedFirstID)).append(line);
		   
		   Set<Entry<Integer, Amount>> bSet = expectedbDistribution.entrySet();
		   Set<Entry<Integer, Amount>> sSet = expectedsDistribution.entrySet();
		   Iterator<Entry<Integer, Amount>> bIter = bSet.iterator();
		   Iterator<Entry<Integer, Amount>> sIter = sSet.iterator();
		   while(bIter.hasNext() && sIter.hasNext()){
			   Map.Entry bEntry = (Map.Entry)bIter.next(); 
			   Map.Entry sEntry = (Map.Entry)sIter.next(); 
		    
			   str.append("bDistribution:"+bEntry.getKey()+":"+((Amount) bEntry.getValue()).getRelative()+":"+((Amount) bEntry.getValue()).getAbsolute()+
					   " "+"sDistribution:"+sEntry.getKey()+":"+((Amount)sEntry.getValue()).getRelative()+":"+((Amount)sEntry.getValue()).getAbsolute()).append(line);
		   }
		   Iterator<Card> cardIter = expectedCardSet.iterator();
		   while(cardIter.hasNext()){
			   Card card = (Card) cardIter.next();
			   str.append("Card:"+card.getId()+":"+card.getValue()+":"+card.getPackage()).append(line);
		   }
		try {
			fw.write(str.toString());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		try {
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
	    Path path = Paths.get(pathFile);
	    String name = "loadTestFile.txt";
	    String originalFileName = "loadTestFile.txt";
	    String contentType = "text/plain";
	    byte[] content = null;
	    try {
	        content = Files.readAllBytes(path);
	    } catch (final IOException e) {
	    	e.printStackTrace();
			System.out.println(e.getMessage());
	    }
	    MultipartFile configTest = new MockMultipartFile(name,
	                         originalFileName, contentType, content);
	    
//	    try {
//			BufferedReader br = new BufferedReader(new InputStreamReader(configTest.getInputStream()));
//			String buf = "";
//	   	    while ((buf=br.readLine()) !=null) {
//			   buf=buf.trim();
//			   System.out.println(buf);
//	   	    }
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace(); 
//		}                                                                             // read multipartfile
//   	    
   	   
	    try {
			kms.getState().load(configTest);
		} catch (NumberFormatException | IOException | EmptyFileException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	   
	    
	    assertEquals("the PlayerCount should be 8,system should load 8 in state Load"
	    		, expectedPlayerCount, kms.getConfiguration().getPlayerCount());
		assertEquals("the AssistantCount should be 2,system should load 2 in state Load"
				, expectedAssistantCount, kms.getConfiguration().getAssistantCount());
		assertEquals("system did not load the right GroupCount"
				, 3, kms.getConfiguration().getGroupCount());
		assertEquals("system did not load the right FirstID", expectedFirstID, kms.getConfiguration().getFirstID());
		assertEquals("system did not load the right bDistributionCount", expectedbDistribution.size(), kms.getConfiguration().getbDistribution().size());
		assertEquals("system did not load the right sDistributionCount", expectedsDistribution.size(), kms.getConfiguration().getsDistribution().size());
		
		Set bd_key1 = expectedbDistribution.keySet();
		Set bd_key2 = kms.getConfiguration().getbDistribution().keySet();
		if(bd_key1.equals(bd_key2)){
			for(int i =0;i<bd_key1.size();i++){
				assertSame("system did not load the right Absolute value bDistribution", expectedbDistribution.get(bd_key1.toArray()[i]).getAbsolute(), kms.getConfiguration().getbDistribution().get(bd_key1.toArray()[i]).getAbsolute());
				assertSame("system did not load the right Relative value bDistribution", expectedbDistribution.get(bd_key1.toArray()[i]).getRelative(), kms.getConfiguration().getbDistribution().get(bd_key1.toArray()[i]).getRelative());
			}
		}
		Set sd_key1 = expectedsDistribution.keySet();
		Set sd_key2 = kms.getConfiguration().getsDistribution().keySet();
		if(sd_key1.equals(sd_key2)){
			for(int i =0;i<sd_key1.size();i++){
				assertSame("system did not load the right Absolute value for sDistribution", expectedsDistribution.get(sd_key1.toArray()[i]).getAbsolute(), kms.getConfiguration().getsDistribution().get(sd_key1.toArray()[i]).getAbsolute());
				assertSame("system did not load the right Relative value for sDistribution", expectedsDistribution.get(sd_key1.toArray()[i]).getRelative(), kms.getConfiguration().getsDistribution().get(sd_key1.toArray()[i]).getRelative());
			}
		}
		
		assertEquals("system did not load the right CardNumber", expectedCardSet.size(), kms.getCards().size());
		assertEquals("system did not load the right CardSetContent", expectedCardSet.toString(), kms.getCards().toString());
		System.out.println("load zuo wan la ");
	

	}

}
