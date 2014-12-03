package jKMS.controller;
 
import jKMS.Pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jni.File;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    
    @RequestMapping(value = "/pdf/export")
    public ResponseEntity<byte[]> exportPDF()	{
		Pdf pdf = new Pdf();

		Document document = new Document();
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		try {
			PdfWriter.getInstance(document, outstream); 
			document.open();
			pdf.createPdfCardsSeller(document,kms.getCards(),kms.getAssistantCount(),kms.getConfiguration().getFirstID());
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
    
    @RequestMapping(value = "/config", method = RequestMethod.GET)
     public void saveConfig(@RequestParam("path") String fileName, HttpServletResponse response) {
    	    try {
    	      // get your file as InputStream
    	    	Resource resource = new FileSystemResource(fileName);
    	    	InputStream is = resource.getInputStream();
    	      // copy it to response's OutputStream
    	      IOUtils.copy(is, response.getOutputStream());
    	      response.setContentType("application/txt");      
    	      response.setHeader("Content-Disposition", "attachment; filename=config.txt");
    	      response.flushBuffer();
    	    } catch (IOException ex) {
    	      ex.printStackTrace();
    	      throw new RuntimeException("IOError writing file to output stream");
    	    }
     }
    
}
