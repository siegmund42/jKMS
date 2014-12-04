package jKMS;

import jKMS.cards.BuyerCard;
import jKMS.cards.Card;
import jKMS.cards.SellerCard;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;


public class Pdf {
	
	 //define Fonts
	private static Font catFont = new Font(Font.FontFamily.COURIER, 18,
		      Font.BOLD);
	private static Font redFont = new Font(Font.FontFamily.COURIER, 12,
		      Font.NORMAL);
	
	private String cardtitle;
	private String value;
	private String id;
	private Properties propertie;
	
	
	public Pdf(){// to catch crashes
		this.propertie = new Properties();
		this.cardtitle = "Default";
		this.value = "Default";
		this.id = "Default";
	}

	public void createPdfCardsSeller(Document cardsSeller,Set<Card> cards,int assistancount,int firstID) throws DocumentException,IOException{ 
    	//Author: Justus (Timon with the good idea)
    	//----------------------DEFINATIONS-----------------------------------
    	//PRINT
		
		
		//get language
        this.setProperetie();
        
		cardtitle = this.propertie.getProperty("PDFSeller.cardtitle");
        value = this.propertie.getProperty("PDFSeller.value") + ": ";
		id = this.propertie.getProperty("PDF.id") + ": ";
		
    	//LOGIC
    	//at every paper are 2 cards --> 2 sets one for top one for bottom
        Set<Card> printcards = new LinkedHashSet<Card>(); // add first all cards + package idendifikationsites in one Set
        Set<Card> topcards = new LinkedHashSet<Card>();
        Set<Card> bottomcards = new LinkedHashSet<Card>();
        int[] packdis = LogicHelper.getPackageDistribution(cards.size(), assistancount);
        int packID = -1;
        int ide = firstID;
        int i = 0;
        
        //----------------------IMPLEMENTATION-------------------------------
        for(Card iter : cards){
        	
        	if(iter instanceof SellerCard){ //seller or buyer?
        		//seller
        			if(iter.getId() < ide){ //is there a new package ?
        			//no
        				printcards.add(new SellerCard(iter.getId(),iter.getValue(),iter.getPackage()));
        			}else{
        			//yes
        				packID++;
        				ide = ide + packdis[packID];
        				printcards.add(new SellerCard(-42,0,LogicHelper.IntToPackage(packID))); //add card for package idedifikation
        				printcards.add(new SellerCard(iter.getId(),iter.getValue(),iter.getPackage()));
        			}
        	}
        	
        }
        	
       if((printcards.size() % 2) == 0){ // split printcards in top and bottom
           for(Card iter : printcards){
        	   if(i < printcards.size()/2){
        		   topcards.add(iter);
        		   i++;
        	   }else{
        		   bottomcards.add(iter);
        		   i++;
        	   	}   
           }
       }else{
    	   //size of printcards is uneven --> topcards.size = bottomcards.size + 1 --> add withepage to bottomcards
           for(Card iter : printcards){
        	   if(i < (printcards.size()/2) + 1){
        		   topcards.add(iter);
        		   i++;
        	   }else{
        		   bottomcards.add(iter);
        		   i++;
        	    }   
           } 
           bottomcards.add(new SellerCard(-42,0,' '));// add wihtepage
       }
       
       //PRINT
        
        //MetaData
       
        cardsSeller.addTitle("Seller-Cards");
        cardsSeller.addAuthor("Kartoffelmarkspiel");
        cardsSeller.addCreator("KMS");
        
        Iterator<Card> itertop = topcards.iterator();
        Iterator<Card> iterbot = bottomcards.iterator();
        Card topcard = new SellerCard(0,0,' ');
        Card bottomcard = new SellerCard(0,0,' ');
        
        //Content       
        
        //TODO Titlepage
        
        while(itertop.hasNext()){
	        	
	       //Top
        	topcard = itertop.next();
        	Paragraph top = createCard(topcard, true);
          
        	//bottom
        	bottomcard = (Card) iterbot.next();
        	Paragraph bottom = createCard(bottomcard, false);
               
        	//set Content togeser ;-)
        
        	PdfPCell topcell = new PdfPCell();
        	topcell.addElement(new Paragraph(" "));//Leerzeile, damit top paragraph funzt
        	topcell.addElement(top);
        	topcell.addElement(new Paragraph(" "));//Leerzeile, damit top paragraph funzt
        	topcell.setBorder(Rectangle.BOTTOM);
        
        	PdfPCell bottomcell = new PdfPCell();
        	bottomcell.addElement(new Paragraph(" "));//Leerzeile, damit bottom paragraph funzt
        	bottomcell.addElement(bottom);
        	bottomcell.addElement(new Paragraph(" "));//Leerzeile, damit bottom paragraph funzt
        	bottomcell.setBorder(Rectangle.NO_BORDER);
        
        	PdfPTable myTable = new PdfPTable(1);
        	myTable.setWidthPercentage(100.0f);
        	myTable.addCell(topcell);
        	myTable.addCell(bottomcell);
        
        
        	cardsSeller.add(myTable);
        	cardsSeller.newPage();
        } 
  
    }
    

    
	public void createPdfCardsBuyer(Document cardsSeller,Set<Card> cards,int assistancount,int firstID) throws DocumentException,IOException{ 
    	//Author: Justus (Timon with the good idea)
    	//----------------------DEFINATIONS-----------------------------------
    	//PRINT
		
		//get language
        this.setProperetie();
        
        //get Strings
		cardtitle = this.propertie.getProperty("PDFBuyer.cardtitle");
        value = this.propertie.getProperty("PDFBuyer.value") + ": ";
		id = this.propertie.getProperty("PDF.id") + ": ";
		
    	//LOGIC
    	//at every paper are 2 cards --> 2 sets one for top one for bottom
        Set<Card> printcards = new LinkedHashSet<Card>(); // add first all cards + package idendifikationsites in one Set
        Set<Card> topcards = new LinkedHashSet<Card>();
        Set<Card> bottomcards = new LinkedHashSet<Card>();
        int[] packdis = LogicHelper.getPackageDistribution(cards.size(), assistancount);
        int packID = -1;
        int ide = firstID;
        int i = 0;
        
        //----------------------IMPLEMENTATION-------------------------------
        for(Card iter : cards){
        	
        	if(iter instanceof BuyerCard){ //seller or buyer?
        		//buyer
        			if(iter.getId() < ide){ //is there a new package ?
        			//no
        				printcards.add(new BuyerCard(iter.getId(),iter.getValue(),iter.getPackage()));
        			}else{
        			//yes
        				packID++;
        				ide = ide + packdis[packID];
        				printcards.add(new BuyerCard(-42,0,LogicHelper.IntToPackage(packID))); //add card for package idedifikation
        				printcards.add(new BuyerCard(iter.getId(),iter.getValue(),iter.getPackage()));
        			}
        	}
        	
        }
        	
       if((printcards.size() % 2) == 0){ // split printcards in top and bottom
           for(Card iter : printcards){
        	   if(i < printcards.size()/2){
        		   topcards.add(iter);
        		   i++;
        	   }else{
        		   bottomcards.add(iter);
        		   i++;
        	   	}   
           }
       }else{
           for(Card iter : printcards){
        	   if(i < (printcards.size()/2) + 1){
        		   topcards.add(iter);
        		   i++;
        	   }else{
        		   bottomcards.add(iter);
        		   i++;
        	   	}   
           } 
           bottomcards.add(new SellerCard(-42,0,' '));// add wihteside
       }
       
       //PRINT
        
        //MetaData
       
        cardsSeller.addTitle("Buyer-Cards");
        cardsSeller.addAuthor("Kartoffelmarkspiel");
        cardsSeller.addCreator("KMS");
        
        Iterator<Card> itertop = topcards.iterator();
        Iterator<Card> iterbot = bottomcards.iterator();
        Card topcard = new SellerCard(0,0,' ');
        Card bottomcard = new SellerCard(0,0,' ');
        
        //Content       
        
        //TODO Titlepage
        
        while(itertop.hasNext()){
        	
        	//Top
        	topcard = itertop.next();
        	Paragraph top = createCard(topcard, true);
          
        	//bottom
        	bottomcard = (Card) iterbot.next();
        	Paragraph bottom = createCard(bottomcard, false);
               
        	//set Content togeser ;-)
        
        	PdfPCell topcell = new PdfPCell();
        	topcell.addElement(new Paragraph(" "));//Leerzeile, damit top paragraph funzt
        	topcell.addElement(top);
        	topcell.addElement(new Paragraph(" "));//Leerzeile, damit top paragraph funzt
        	topcell.setBorder(Rectangle.BOTTOM);
        
        	PdfPCell bottomcell = new PdfPCell();
        	bottomcell.addElement(new Paragraph(" "));//Leerzeile, damit bottom paragraph funzt
        	bottomcell.addElement(bottom);
        	bottomcell.addElement(new Paragraph(" "));//Leerzeile, damit bottom paragraph funzt
        	bottomcell.setBorder(Rectangle.NO_BORDER);
        
        	PdfPTable myTable = new PdfPTable(1);
        	myTable.setWidthPercentage(100.0f);
        	myTable.addCell(topcell);
        	myTable.addCell(bottomcell);
        
        
        	cardsSeller.add(myTable);
        	cardsSeller.newPage();
        }
  
    }

    private Paragraph createCard(Card card,boolean isTop){
    	
    	Paragraph content = new Paragraph();
        content.setAlignment(Element.ALIGN_CENTER);
        
    	if(isTop){
    		content.setSpacingBefore(100);
            content.setSpacingAfter(150);
    		if(card.getId() == -42 && card.getValue() == 0){
                content.setSpacingAfter(200);
    		}
    	}else{
            content.setSpacingBefore(130);
    	}
    	
        if((card.getId() != -42) && (card.getValue() != 0)){
        	Chunk cTitle = new Chunk(cardtitle,catFont);
        	content.add(cTitle);
        	content.add(Chunk.NEWLINE);
        	Chunk cValue = new Chunk(this.value + card.getValue() +"€");
        	content.add(cValue);
        	content.add(Chunk.NEWLINE);
        	Chunk cID = new Chunk(this.id + card.getId());
        	content.add(cID);
        	content.add(Chunk.NEWLINE);
        	content.add(Chunk.NEWLINE);
        	content.add(Chunk.NEWLINE);
        	Paragraph cPa = new Paragraph(String.valueOf(card.getPackage()),redFont);
        	cPa.setAlignment(Element.ALIGN_LEFT);
        	content.add(cPa);
        }else{
        	Chunk cPack = new Chunk(String.valueOf(card.getPackage()),catFont);
        	content.add(cPack);
        }
        	
    	return content;
    }
   
    private void setProperetie(){

        Locale locale = LocaleContextHolder.getLocale(); // get lang.
        try {
        	this.propertie.load(ClassLoader.getSystemResourceAsStream("messages_"+locale.getLanguage()+".properties"));//get rigth propertie
        	}
        	catch (IOException ioe) {
        		System.out.println(ioe);
        		try {
        			this.propertie.load(ClassLoader.getSystemResourceAsStream("messages_en.properties"));
        			}
        			catch (IOException ioe1) {
        				System.out.println(ioe1);
        			}
        	}
    }
    
    
    
    public Document createExportPdf(Document doc, Image pdfImage) throws DocumentException{
    	//TODO Statistikdaten abfragen und einfügen
    	float chartWidth = doc.getPageSize().getWidth() - doc.leftMargin() - doc.rightMargin();
    	float chartHeight = doc.getPageSize().getHeight();
		pdfImage.scaleToFit(chartWidth, chartHeight);
		doc.add(pdfImage);
		
		return doc;
    }
}
