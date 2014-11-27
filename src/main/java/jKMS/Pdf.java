package jKMS;

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
	
	 //Fonts definieren damit's schön aussieht  
	private static Font catFont = new Font(Font.FontFamily.COURIER, 18,
		      Font.BOLD);
	private static Font redFont = new Font(Font.FontFamily.COURIER, 12,
		      Font.NORMAL);

	
	public Pdf(){
	}

    public Document createPdfCardsSeller(Document cardsSeller) throws DocumentException{ 
    	
    	
        
        //MetaData
        cardsSeller.addTitle("Seller-Cards");
        cardsSeller.addAuthor("Kartoffelmarkspiel");
        cardsSeller.addCreator("KMS");
        
        //Content
        	//Top
        Paragraph top = new Paragraph(); 
        top.setSpacingAfter(100);
        top.setSpacingBefore(100);
        top.setAlignment(Element.ALIGN_CENTER);
        Chunk sellerT = new Chunk("seller",catFont);
        top.add(sellerT);
        top.add(Chunk.NEWLINE);
        Chunk costsT = new Chunk("costs: 34€");
        top.add(costsT);
        top.add(Chunk.NEWLINE);
        Chunk idT = new Chunk("id:1342");
        top.add(idT);
        top.add(Chunk.NEWLINE);
        top.add(Chunk.NEWLINE);
        top.add(Chunk.NEWLINE);
        Paragraph paT = new Paragraph("A",redFont);
        paT.setAlignment(Element.ALIGN_LEFT);
        top.add(paT);
        	
        	//bottom
        Paragraph bottom = new Paragraph(); 
        bottom.setAlignment(Element.ALIGN_CENTER);
        bottom.setSpacingBefore(130);
        Chunk sellerB = new Chunk("seller",catFont);
        bottom.add(sellerB);
        bottom.add(Chunk.NEWLINE);
        Chunk costsB = new Chunk("costs: 72€");
        bottom.add(costsB);
        bottom.add(Chunk.NEWLINE);
        Chunk idB = new Chunk("id:4242");
        bottom.add(idB);
        bottom.add(Chunk.NEWLINE);
        bottom.add(Chunk.NEWLINE);
        bottom.add(Chunk.NEWLINE);
        Paragraph paB = new Paragraph("B",redFont);
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
        
        
        return (cardsSeller);

    } 
}
