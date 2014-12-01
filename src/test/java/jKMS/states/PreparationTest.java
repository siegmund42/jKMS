package jKMS.states;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
		//Setup Player/Assistant No 
		kms.getState().setBasicConfig(6, 1);
		
		kms.getState().newGroup(true, 2, 16, 1);
		kms.getState().newGroup(true, 3, 33, 2);
		kms.getState().newGroup(false, 3, 33, 2);
		kms.getState().newGroup(false, 4, 16, 1);

		
		/*
		//Setup Distribution
		Map<Integer, Amount> bDistrib = new TreeMap<Integer, Amount>();
		Map<Integer, Amount> sDistrib = new TreeMap<Integer, Amount>();
		
		bDistrib.put(3, new Amount(33, 2));
		bDistrib.put(4, new Amount(16, 1));
		sDistrib.put(3, new Amount(33, 2));
		sDistrib.put(4, new Amount(16, 1));
		
		kms.getConfiguration().setbDistribution(bDistrib);
		kms.getConfiguration().setsDistribution(sDistrib);*/
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
	public void testcreatePdfCardsSeller(){
		kms.getConfiguration().setFirstID(1001);
		kms.getState().generateCards();
		Pdf pdf = new Pdf();
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream("/home/justus/document.pdf")); 
			document.open();
			pdf.createPdfCardsSeller(document,kms.getCards(),kms.getAssistantCount(),kms.getConfiguration().getFirstID());
			document.close();
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
		assertEquals(2, kms.getbDistribution().keySet().size());
		assertEquals(2, kms.getsDistribution().keySet().size());
		
		kms.getState().newGroup(true, 5, 0, 1);
		assertEquals("bDistribution does not increase when newGroup is called", 3, kms.getbDistribution().keySet().size());
		assertEquals("sDistribution must not increase when newGroup is called for a buyer", 2, kms.getsDistribution().keySet().size());
		kms.getState().newGroup(false, 2, 0, 1);
		assertEquals("sDistribution does not increase when newGroup is called", 3, kms.getbDistribution().keySet().size());
		assertEquals("bDistribution must not increase when newGroup is called for a seller", 3, kms.getsDistribution().keySet().size());
		
		kms.getState().newGroup(true, 3, 0, 1);
		assertEquals("bDistribution must not increase when a new group is created for an already existing key", 3, kms.getbDistribution().keySet().size());
		//assertEquals("bDistribution does not update correctly when a new group is created for an already existing key", );
		//TODO check if Amount has updated correctly
	}
}
