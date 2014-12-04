package jKMS;

import jKMS.cards.Card;
import jKMS.cards.SellerCard;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

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
import com.itextpdf.text.pdf.PdfWriter;


public class Pdf {
	
	 //define Fonts
	private static Font catFont = new Font(Font.FontFamily.COURIER, 18,
		      Font.BOLD);
	private static Font redFont = new Font(Font.FontFamily.COURIER, 12,
		      Font.NORMAL);

	
	public Pdf(){
	}

    public void createPdfCardsSeller(Document cardsSeller,Set<Card> cards,int assistancount,int firstID) throws DocumentException,IOException{ 
    	//Author: Justus (Timon with the good idea)
    	
    	//at every paper are 2 cards --> 2 sets one for top one for bottom

		
		
        Set<Card> printcards = new LinkedHashSet<Card>(); // add first all cards + package idendifikationsites in one Set
        Set<Card> topcards = new LinkedHashSet<Card>();
        Set<Card> bottomcards = new LinkedHashSet<Card>();
        int[] packdis = LogicHelper.getPackageDistribution(cards.size(), assistancount);
        int packID = -1;
        int ide = firstID;
        int i = 0;
        
        for(Card iter : cards){
        	
        	if((iter.getId()-firstID) % 2 == 0){ //seller or buyer?
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
           for(Card iter : printcards){
        	   if(i <= (printcards.size()/2) + 1){
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
	        topcard = (Card) itertop.next();
	        	
	        Paragraph top = new Paragraph(); 
	        top.setSpacingBefore(100);
	        top.setAlignment(Element.ALIGN_CENTER);
	        if((topcard.getId() != -42) && (topcard.getValue() != 0)){
	            top.setSpacingAfter(100);
	        	Chunk sellerT = new Chunk("seller",catFont);
	        	top.add(sellerT);
	        	top.add(Chunk.NEWLINE);
	        	Chunk costsT = new Chunk("costs: " + topcard.getValue() +"€");
	        	top.add(costsT);
	        	top.add(Chunk.NEWLINE);
	        	Chunk idT = new Chunk("ID :" +topcard.getId());
	        	top.add(idT);
	        	top.add(Chunk.NEWLINE);
	        	top.add(Chunk.NEWLINE);
	        	top.add(Chunk.NEWLINE);
	        	Paragraph paT = new Paragraph(String.valueOf(topcard.getPackage()),redFont);
	        	paT.setAlignment(Element.ALIGN_LEFT);
	        	top.add(paT);
	        }else{
	            top.setSpacingAfter(200);
	        	Chunk paT = new Chunk(String.valueOf(topcard.getPackage()),catFont);
	        	top.add(paT);
	        }
	        
	        //bottom
	        bottomcard = (Card) iterbot.next();
	        
	        Paragraph bottom = new Paragraph(); 
	        bottom.setAlignment(Element.ALIGN_CENTER);
	        bottom.setSpacingBefore(130);
	        if((bottomcard.getId() != -42) && (bottomcard.getValue() != 0)){
	        	Chunk sellerB = new Chunk("seller",catFont);
	        	bottom.add(sellerB);
	        	bottom.add(Chunk.NEWLINE);
	        	Chunk costsB = new Chunk("costs: " + bottomcard.getValue() +"€");
	        	bottom.add(costsB);
	        	bottom.add(Chunk.NEWLINE);
	        	Chunk idB = new Chunk("ID: " +bottomcard.getId());
	        	bottom.add(idB);
	        	bottom.add(Chunk.NEWLINE);
	        	bottom.add(Chunk.NEWLINE);
	        	bottom.add(Chunk.NEWLINE);
	        	Paragraph paB = new Paragraph(String.valueOf(bottomcard.getPackage()),redFont);
	        	paB.setAlignment(Element.ALIGN_LEFT);
	        	bottom.add(paB);
	        }else{
	        	Chunk pab = new Chunk(String.valueOf(bottomcard.getPackage()),catFont);
	        	bottom.add(pab);
	        }
	        
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
    
    
    public Document createExportPdf(Document doc, Image pdfImage) throws DocumentException{
    	//TODO Statistikdaten abfragen und einfügen
    	float chartWidth = doc.getPageSize().getWidth() - doc.leftMargin() - doc.rightMargin();
    	float chartHeight = doc.getPageSize().getHeight();
		pdfImage.scaleToFit(chartWidth, chartHeight);
		doc.add(pdfImage);
		
		return doc;
    }
}
