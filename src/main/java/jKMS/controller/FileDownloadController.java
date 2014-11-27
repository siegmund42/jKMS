package jKMS.controller;
 
import jKMS.Pdf;

import java.io.ByteArrayOutputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
 
@Controller
public class FileDownloadController extends AbstractServerController {
     
    @RequestMapping(value = "/pdf/cards/{type}")
    public ResponseEntity<byte[]> downloadPDF(@PathVariable String type)	{
		Pdf pdf = new Pdf();

		Document document = new Document();
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		try {
			PdfWriter.getInstance(document, outstream); 
			document.open();
			if(type.equals("customer"))
				pdf.createPdfCardsSeller(document);
			else
				pdf.createPdfCardsSeller(document);
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	    byte[] contents = outstream.toByteArray();
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("application/pdf"));
	    String filename;
	    if(type.equals("customer"))
	    	filename = "KäuferKarten.pdf";
	    else
	    	filename = "VerkäuferKarten.pdf";
	    headers.setContentDispositionFormData(filename, filename);
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
	    return response;
    }
    
    @RequestMapping(value = "/pdf/export")
    public ResponseEntity<byte[]> exportPDF()	{
		Pdf pdf = new Pdf();

		Document document = new Document();
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		try {
			PdfWriter.getInstance(document, outstream); 
			document.open();
			pdf.createPdfCardsSeller(document);
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	    byte[] contents = outstream.toByteArray();
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("application/pdf"));
	    String filename = "Export.pdf";
	    headers.setContentDispositionFormData(filename, filename);
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
	    return response;
    }
    
    @RequestMapping(value = "/config")
    // public ResponseEntity<byte[]> downloadConfig()	{
     public void saveConfig(){
     	kms.getState().save("config.txt");
     	
// 			Pdf pdf = new Pdf();
 //	
// 			Document document = new Document();
// 			ByteArrayOutputStream outstream = new ByteArrayOutputStream();
// 			try {
// 				PdfWriter.getInstance(document,outstream); 
// 				document.open();
// 				pdf.createPdfCardsSeller(document);
// 				document.close();
// 			} catch (Exception e) {
// 				e.printStackTrace();
// 			}
// 		
// 		    byte[] contents = outstream.toByteArray();
// 		    
// 		    HttpHeaders headers = new HttpHeaders();
// 		    headers.setContentType(MediaType.parseMediaType("application/pdf"));
// 		    String filename = "Config.pdf";
// 		    headers.setContentDispositionFormData(filename, filename);
// 		    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
// 		    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
// 		    return response;
     }

    
}
