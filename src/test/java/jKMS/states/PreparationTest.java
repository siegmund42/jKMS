package jKMS.states;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
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

import jKMS.Amount;
import jKMS.Application;
import jKMS.Kartoffelmarktspiel;
import jKMS.Pdf;
import jKMS.cards.BuyerCard;
import jKMS.cards.Card;
import jKMS.cards.SellerCard;
import jKMS.exceptionHelper.EmptyFileException;
import jKMS.exceptionHelper.WrongAssistantCountException;
import jKMS.exceptionHelper.WrongFirstIDException;
import jKMS.exceptionHelper.WrongPlayerCountException;
import jKMS.exceptionHelper.WrongRelativeDistributionException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class PreparationTest {
	@Autowired
	private Kartoffelmarktspiel kms;
	
	@Before
	public void setUp(){
		kms.prepare();
		
		//Setup Player/Assistant No 
		kms.getState().setBasicConfig(6, 1);
		
		//Setup Distribution
		Map<Integer, Amount> bDistrib = new TreeMap<Integer, Amount>();
		Map<Integer, Amount> sDistrib = new TreeMap<Integer, Amount>();
		
		bDistrib.put(3, new Amount(50, 2));
		bDistrib.put(4, new Amount(50, 1));
		sDistrib.put(3, new Amount(50, 2));
		sDistrib.put(4, new Amount(50, 1));
		
		kms.getConfiguration().setbDistribution(bDistrib);
		kms.getConfiguration().setsDistribution(sDistrib);
	}
	
	@Test
	//TODO testStandardDistribution
	public void testStandardDistribution(){
		
	}
	
	@Test
	//TODO testLoad
	public void testLoad(){
		ServletContext servletContext = null;
		int unexpectedPlayerCount = 8;
		int unexpectedAssistantCount = 2;
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
		
		String pathFile = servletContext.getRealPath(".").concat("file.txt");
		
	    String line = System.getProperty("line.separator");
		   StringBuffer str = new StringBuffer();
		   FileWriter fw = null;
		try {
			fw = new FileWriter(pathFile, false);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		   str.append("PlayerCount:").append(kms.getConfiguration().getPlayerCount()).append(line)
		   .append("AssistantCount:").append(kms.getConfiguration().getAssistantCount()).append(line)
		   .append("GroupCount:").append(kms.getConfiguration().getGroupCount()).append(line)
		   .append("FirstID:").append(kms.getConfiguration().getFirstID()).append(line);
		   
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
	    String name = "file.txt";
	    String originalFileName = "file.txt";
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
	   
	    try {
			kms.getState().load(configTest);
		} catch (NumberFormatException | IOException | EmptyFileException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	   
	    
	    assertEquals("the PlayerCount should be 6,system should not load 8 in state preparation"
	    		, unexpectedPlayerCount, kms.getConfiguration().getPlayerCount());
		assertEquals("the AssistantCount should be 1,system should not load 2 in state preparation"
				, unexpectedAssistantCount, kms.getConfiguration().getAssistantCount());
		assertEquals("system did not load the right PlayerCount in StateLoad", 6, kms.getConfiguration().getPlayerCount());
		assertEquals("system did not load the right AssistantCount in StateLoad", 1, kms.getConfiguration().getAssistantCount());
		assertEquals("system did not load the right GroupCount", expectedGroupCount, kms.getConfiguration().getGroupCount());
		assertEquals("system did not load the right FirstID", expectedFirstID, kms.getConfiguration().getFirstID());
		assertEquals("system did not load the right bDistributionCount", expectedbDistribution.size(), kms.getConfiguration().getbDistribution().size());
		assertEquals("system did not load the right sDistributionCount", expectedsDistribution, kms.getConfiguration().getsDistribution().size());
		assertEquals("system did not load the right bDistributionContent", expectedbDistribution.toString(), kms.getConfiguration().getbDistribution().toString());
		assertEquals("system did not load the right sDistributionContent", expectedsDistribution.toString(), kms.getConfiguration().getsDistribution().toString());
		assertEquals("system did not load the right CardNumber", expectedCardSet.size(), kms.getCards().size());
		assertEquals("system did not load the right CardSetContent", expectedCardSet.toString(), kms.getCards().toString());

	}

	@Test
	public void testCreatePdfCardsSeller(){
		
		Map<Integer, Amount> bDistrib = new TreeMap<Integer, Amount>();
		Map<Integer, Amount> sDistrib = new TreeMap<Integer, Amount>();
		
		bDistrib.put(5, new Amount(50, 5));
		bDistrib.put(4, new Amount(50, 5));
		sDistrib.put(3, new Amount(50, 5));
		sDistrib.put(2, new Amount(50, 5));
		
		kms.getConfiguration().setbDistribution(bDistrib);
		kms.getConfiguration().setsDistribution(sDistrib);
		
		kms.getConfiguration().setFirstID(1000);
		
		kms.getState().setBasicConfig(20, 5);
		
		try{
		kms.getState().generateCards();
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());		
		}
		
		Pdf pdf = new Pdf();
		Document documentSeller = new Document();
		Document documentBuyer = new Document();
		try {
			
			PdfWriter.getInstance(documentSeller, new FileOutputStream("/home/justus/documentseller.pdf")); 
			documentSeller.open();
			pdf.createPdfCardsSeller(documentSeller,kms.getCards(),kms.getAssistantCount(),kms.getConfiguration().getFirstID());
			documentSeller.close();
			PdfWriter.getInstance(documentBuyer, new FileOutputStream("/home/justus/documentbuyer.pdf")); 
			documentBuyer.open();
			pdf.createPdfCardsBuyer(documentBuyer,kms.getCards(),kms.getAssistantCount(),kms.getConfiguration().getFirstID());
			documentBuyer.close();
			
		}catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			
		}
	}
	@Test
	//TODO testSave
	public void testSave(){
		ServletContext servletContext = null;
		int expectedPlayerCount = 0;
		int expectedAssistantCount = 0;
		int expectedGroupCount = 0;
    	int expectedFirstID = 0;
    	Set<Card> expectedCardSet = new LinkedHashSet<Card>();
    	Map<Integer, Amount> expectedbDistribution = new TreeMap<>();
		Map<Integer, Amount> expectedsDistribution = new TreeMap<>();
		try {
			kms.getCards().clear();
			kms.getState().generateCards();
		} catch (WrongFirstIDException | WrongAssistantCountException
				| WrongPlayerCountException
				| WrongRelativeDistributionException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		String path = servletContext.getRealPath(".").concat("configTest.txt");
		try {
			kms.getState().save(path);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		File file =new File(path);
		if (!(file.exists())) {
       	 BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
       	 String buf = "";
       	 int count = 0;
       	 try {
			while ((buf=br.readLine()) != null && count < 4) {
				 buf=buf.trim();
				 String[] sa = buf.split(":|\\s");
				 if(count == 0){
					 expectedPlayerCount = Integer.valueOf(sa[1].trim());
					 count = count + 1;
					 continue;
				 }
				 else if(count == 1){
					 expectedAssistantCount = Integer.valueOf(sa[1].trim());
					 count = count + 1;
					 continue;
				 }
				 else if(count == 2){
					 expectedGroupCount = Integer.valueOf(sa[1].trim());
					 count = count + 1;
					 continue;
				 }
				 else if(count == 3){
					 expectedFirstID = Integer.valueOf(sa[1].trim());
					 count = count + 1;
					 break;
				 }
			 }
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
       	 while ( count >=4 && count < expectedGroupCount+4){
       		 try {
				if( (buf=br.readLine()) != null){
					 buf=buf.trim();
				         String[] sa = buf.split(":|\\s");
				         int bpreis = Integer.valueOf(sa[1].trim());
				         Amount bAmount =  new Amount(Integer.valueOf(sa[2].trim()),Integer.valueOf(sa[3].trim()));
				         // int banteil = Integer.valueOf(sa[1]);
				         int spreis = Integer.valueOf(sa[5].trim());
				         Amount sAmount = new Amount(Integer.valueOf(sa[6].trim()),Integer.valueOf(sa[7].trim()));
				         //int santeil = Integer.valueOf(sa[3]);
				        
				         expectedbDistribution.put(bpreis, bAmount);
				         expectedsDistribution.put(spreis, sAmount);
				         count = count + 1;
				 }else {
					 throw new EmptyFileException("The GroupCount is not enough!");
				 }
			} catch (NumberFormatException | IOException | EmptyFileException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
       	 }
			try {
				while (count >= expectedGroupCount +4 && (buf=br.readLine()) != null){
					 Card card;
					 buf=buf.trim();
					 String[] sa = buf.split(":|\\s");
					 if((Integer.valueOf(sa[1])%2) == 0){
						card = new BuyerCard(Integer.valueOf(sa[1].trim()),Integer.valueOf(sa[2].trim()),sa[3].trim().charAt(0));
					 }else {
						card = new SellerCard(Integer.valueOf(sa[1].trim()),Integer.valueOf(sa[2].trim()),sa[3].trim().charAt(0));
					 }
					 expectedCardSet.add(card);
				 }
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			assertEquals("system did not save the right PlayerCount", expectedPlayerCount, kms.getConfiguration().getPlayerCount());
			assertEquals("system did not save the right AssistantCount", expectedAssistantCount, kms.getConfiguration().getAssistantCount());
			assertEquals("system did not save the right GroupCount", expectedGroupCount, kms.getConfiguration().getGroupCount());
			assertEquals("system did not save the right FirstID", expectedFirstID, kms.getConfiguration().getFirstID());
			assertEquals("system did not save the right bDistributionCount", expectedbDistribution.size(), kms.getConfiguration().getbDistribution().size());
			assertEquals("system did not save the right sDistributionCount", expectedsDistribution, kms.getConfiguration().getsDistribution().size());
			assertEquals("system did not save the right bDistributionContent", expectedbDistribution.toString(), kms.getConfiguration().getbDistribution().toString());
			assertEquals("system did not save the right sDistributionContent", expectedsDistribution.toString(), kms.getConfiguration().getsDistribution().toString());
			assertEquals("system did not save the right CardNumber", expectedCardSet.size(), kms.getCards().size());
			assertEquals("system did not save the right CardSetContent", expectedCardSet.toString(), kms.getCards().toString());

		}

	}
	
	@Test
	//Tests if the generated Set is the same three consecutive times
	public void testGenerateCardsRandom(){
		Set<Card> s1, s2;
		s1 = new LinkedHashSet<Card>();
		s2 = new LinkedHashSet<Card>();
		int i = 0;
		
		do{
			
			try{
				kms.getState().generateCards();
				}catch (Exception e) {
					e.printStackTrace();
					System.out.println(e.getMessage());		
				}
			
			s1.addAll(kms.getCards());
			
			try{
				kms.getState().generateCards();
				}catch (Exception e) {
					e.printStackTrace();
					System.out.println(e.getMessage());		
				}
			
			s2.addAll(kms.getCards());
			i++;
			
		} while(s1 == s2 && i<3);
		
		assertTrue("Bad random generation of cardSet!", s1 != s2);
	}
	
	@Test
	//Tests if cards are generated in an alternating order (Buyer, Seller, Buyer, ...)
	public void testGenerateCardsAlternating(){
		int i = 1;
		for(Card iter : kms.getCards()){
			if(i==1){
				assertTrue(iter instanceof BuyerCard);
				i=2;
			}
			else{
				assertTrue(iter instanceof SellerCard);
				i=1;
			}
		}
	}
	
	@Test
	public void testNewGroup(){
		Set<Integer> expectedBKeys = new LinkedHashSet<Integer>();
		Set<Integer> expectedSKeys = new LinkedHashSet<Integer>();
		
		
		expectedBKeys.add(3);
		expectedBKeys.add(4);
		expectedSKeys.add(3);
		expectedSKeys.add(4);
		
		assertEquals(expectedBKeys, kms.getbDistribution().keySet());
		assertEquals(expectedSKeys, kms.getsDistribution().keySet());
		
		//Test adding new keys to the set
		kms.getState().newGroup(true, 2, 0, 1);
		expectedBKeys.add(2);
		assertEquals("bDistribution does not add a new key when newGroup is called for a buyer", expectedBKeys, kms.getbDistribution().keySet());
		assertEquals("sDistribution must not add a new key when newGroup is called for a buyer", expectedSKeys, kms.getsDistribution().keySet());
		
		kms.getState().newGroup(false, 5, 0, 1);
		expectedSKeys.add(5);
		assertEquals("sDistribution does not add a new key when newGroup is called for a seller", expectedSKeys, kms.getsDistribution().keySet());
		assertEquals("bDistribution must not add a new key when newGroup is called for a seller", expectedBKeys, kms.getbDistribution().keySet());
		
		//Test increasing value of already existing keys
		kms.getState().newGroup(true, 3, 0, 1);
		assertEquals("sDistribution must not increase when newGroup is called for an already existing buyer-key", 3, kms.getbDistribution().keySet().size());
		assertEquals("bDistribution does not update correctly when newGroup is called for an already existing key", 3, kms.getbDistribution().get(3).getAbsolute());
	}
}
