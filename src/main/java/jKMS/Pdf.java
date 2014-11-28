package jKMS;

import jKMS.cards.Card;
import jKMS.cards.SellerCard;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
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

	
	public Pdf(){
	}

    public Document createPdfCardsSeller(Document cardsSeller,Set<Card> Cards,int assistancount,int firstID) throws DocumentException{ 
    	//Author: Justus (Timon with the good idea)
    	
    	//at every paper are 2 cards --> 2 sets one for top one for bottom
        Set<Card> printcards = new LinkedHashSet<Card>(); // add first all cards + package idendifikationsites in one Set
        Set<Card> topcards = new LinkedHashSet<Card>();
        Set<Card> bottomcards = new LinkedHashSet<Card>();
        int[] packdis = LogicHelper.getPackageDistribution(Cards.size(), assistancount);
        int packID = -1;
        int ide = firstID;
        int i = 0;
        
        for(Card iter : Cards){
        	
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
        top.setSpacingAfter(100);
        top.setSpacingBefore(100);
        top.setAlignment(Element.ALIGN_CENTER);
        Chunk sellerT = new Chunk("seller",catFont);
        top.add(sellerT);
        top.add(Chunk.NEWLINE);
        Chunk costsT = new Chunk("costs: " + topcard.getValue() +"€");
        top.add(costsT);
        top.add(Chunk.NEWLINE);
        Chunk idT = new Chunk("id :" +topcard.getId());
        top.add(idT);
        top.add(Chunk.NEWLINE);
        top.add(Chunk.NEWLINE);
        top.add(Chunk.NEWLINE);
        Paragraph paT = new Paragraph(String.valueOf(topcard.getPackage()),redFont);
        paT.setAlignment(Element.ALIGN_LEFT);
        top.add(paT);
        	
        //bottom
        bottomcard = (Card) iterbot.next();
        
        Paragraph bottom = new Paragraph(); 
        bottom.setAlignment(Element.ALIGN_CENTER);
        bottom.setSpacingBefore(130);
        Chunk sellerB = new Chunk("seller",catFont);
        bottom.add(sellerB);
        bottom.add(Chunk.NEWLINE);
        Chunk costsB = new Chunk("costs: " + bottomcard.getValue() +"€");
        bottom.add(costsB);
        bottom.add(Chunk.NEWLINE);
        Chunk idB = new Chunk("id: " +bottomcard.getId());
        bottom.add(idB);
        bottom.add(Chunk.NEWLINE);
        bottom.add(Chunk.NEWLINE);
        bottom.add(Chunk.NEWLINE);
        Paragraph paB = new Paragraph(String.valueOf(bottomcard.getPackage()),redFont);
        paB.setAlignment(Element.ALIGN_LEFT);
        bottom.add(paB);
        
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
        
        
        return (cardsSeller);
        
    } 
}
