package jKMS.controller;
 
import jKMS.Pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
 
@Controller
public class FileDownloadController extends AbstractServerController {
	byte[] pdfBytes = null;
	
    @RequestMapping(value = "/pdf/cards/{type}")
    public ResponseEntity<byte[]> downloadPDF(@PathVariable String type)	{
		Pdf pdf = new Pdf();
		
		Document document = new Document();
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		try {
			PdfWriter.getInstance(document, outstream); 
			document.open();
			if(type.equals("customer"))
				pdf.createPdfCardsSeller(document,kms.getCards(),kms.getAssistantCount(),kms.getConfiguration().getFirstID());
			else
				pdf.createPdfCardsSeller(document,kms.getCards(),kms.getAssistantCount(),kms.getConfiguration().getFirstID());
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
    
    //catch ajax-request when evaluate.html is ready, prepare export-pdf for download
    @RequestMapping(value = "/pdfExport",
    				method = RequestMethod.POST)
    public void exportPDF(@RequestParam("image") MultipartFile image)	{
    	byte[] imageBytes = null;

    	if(!image.isEmpty()){
    		try{
    			imageBytes = image.getBytes();
    		} catch(Exception e){}
    	}
    	
    	Pdf pdf = new Pdf();
    	Document document = new Document(PageSize.A4.rotate());
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		
		try {
			PdfWriter.getInstance(document, outstream); 
			Image pdfImage = Image.getInstance(imageBytes);
			
			document.open();
			document = pdf.createExportPdf(document, pdfImage);
			document.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		pdfBytes = outstream.toByteArray();

    }
    
    //download export-pdf 
    @RequestMapping(value = "/pdfDownload")
    public ResponseEntity<byte[]> downloadPdf(){

	    if(pdfBytes != null){
	    	byte[] contents = pdfBytes;
		    
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.parseMediaType("application/pdf"));
		    //TODO timestamp am Dateinamen
		    String filename = "Export.pdf";
		    headers.setContentDispositionFormData(filename, filename);
		    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
		    return response;
	    }
	    else return null;
    	
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
