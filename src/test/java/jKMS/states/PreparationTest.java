package jKMS.states;

import java.io.FileOutputStream;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import jKMS.Amount;
import jKMS.Application;
import jKMS.Kartoffelmarktspiel;
import jKMS.Pdf;
import jKMS.cards.BuyerCard;
import jKMS.cards.Card;
import jKMS.cards.SellerCard;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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
		
		bDistrib.put(3, new Amount(33, 2));
		bDistrib.put(4, new Amount(16, 1));
		sDistrib.put(3, new Amount(33, 2));
		sDistrib.put(4, new Amount(16, 1));
		
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
		
	}
	
	@Test
	public void testCreatePdfCardsSeller(){
		
		Map<Integer, Amount> bDistrib = new TreeMap<Integer, Amount>();
		Map<Integer, Amount> sDistrib = new TreeMap<Integer, Amount>();
		
		bDistrib.put(5, new Amount(33, 5));
		bDistrib.put(4, new Amount(16, 5));
		sDistrib.put(3, new Amount(33, 5));
		sDistrib.put(2, new Amount(16, 5));
		
		kms.getConfiguration().setbDistribution(bDistrib);
		kms.getConfiguration().setsDistribution(sDistrib);
		
		kms.getConfiguration().setFirstID(1000);
		
		kms.getState().setBasicConfig(20, 5);
		
		kms.getState().generateCards();
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
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	//TODO testSave
	public void testSave(){
		
	}
	
	@Test
	//Tests if the generated Set is the same three consecutive times
	public void testGenerateCardsRandom(){
		Set<Card> s1, s2;
		s1 = new LinkedHashSet<Card>();
		s2 = new LinkedHashSet<Card>();
		int i = 0;
		
		do{
			kms.getState().generateCards();
			s1 = kms.getCards();
			kms.getState().generateCards();
			s2 = kms.getCards();
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
