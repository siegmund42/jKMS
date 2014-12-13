package jKMS.controller;
 
import jKMS.Pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import au.com.bytecode.opencsv.CSVWriter;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
 
@Controller
public class FileDownloadController extends AbstractServerController {
	byte[] pdfBytes = null;
	
	// Downloading Seller-/BuyerCardsPDF
    @RequestMapping(value = "/pdf/cards/{type}")
    public ResponseEntity<byte[]> downloadPDF(Model model, @PathVariable String type) throws Exception	{
		
    	// Create new Document
		Document document = new Document();
		// Get a new Outputstream for the PDF Library
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		try {
			// Very Handwaving itext-PdfWriter.getInstance method
			PdfWriter.getInstance(document, outstream); 
			// Open document to write in it
			document.open();
			
			// Create PDF for Buyer/Seller
			kms.getState().createPdf(type.equals("customer"), document);
			
			// Close document
			document.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: Think about how to handle this error -.-
			Exception ex = new RuntimeException("Something went wrong while creating Cards.");
			throw ex;
		}
	
		// Get a byte Array of the pdf
	    byte[] contents = outstream.toByteArray();
	    
	    // Write Headers
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("application/pdf"));
	    
	    // Set the download-Filename
	    String filename;
	    // TODO internationalize! Generate nice Name using timestamp or similar
	    switch(type)	{
	    case("customer"):
	    	filename = "KäuferKarten.pdf";
	    	break;
	    default:
	    	filename = "VerkäuferKarten.pdf";
	    }
	    
	    headers.setContentDispositionFormData(filename, filename);
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    // Serve Data to the browser
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
			Map<String,Float> stats = kms.getState().getStatistics();
			
			document.open();
			document = pdf.createExportPdf(document, pdfImage, stats);
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
    
    @RequestMapping(value = "/config", method = RequestMethod.GET)
     public void saveConfig(@RequestParam("path") String fileName, HttpServletResponse response) {
    	    try {
    	      // get your file as InputStream
    	    	Resource resource = new FileSystemResource(fileName);
    	    	InputStream is = resource.getInputStream();
    	      // copy it to response's OutputStream
    	      IOUtils.copy(is, response.getOutputStream());
    	      response.setContentType("application/txt");
    	      // TODO Dateinamen ordentlich machen
    	      response.setHeader("Content-Disposition", "attachment; filename=config.txt");
    	      response.flushBuffer();
    	    } catch (IOException ex) {
    	      ex.printStackTrace();
    	      throw new RuntimeException("IOError writing file to output stream");
    	    }
     }
    
    @RequestMapping(value = "/csv")
    public ResponseEntity<byte[]> downloadCsv() throws Exception{ 
    	
    	ByteArrayOutputStream outstream = new ByteArrayOutputStream();
    	CSVWriter writer = new CSVWriter(new OutputStreamWriter(outstream));
    	
		try {
			// Create CSV
			kms.getState().generateCSV(writer);
			
			// Close document
			writer.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: Think about how to handle this error -.-
			Exception ex = new RuntimeException("Something went wrong while export CSV.");
			throw ex;
		}
		
		// Get a byte Array of the csv
	    byte[] contents = outstream.toByteArray();
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("text/csv"));
	    //TODO timestamp for filename
	    String filename = "Export.csv";
	    headers.setContentDispositionFormData(filename, filename);
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
	    return response;
	      
    }
    
}
