package jKMS.controller;
 
import jKMS.LogicHelper;
import jKMS.Pdf;
import jKMS.exceptionHelper.NoContractsException;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
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

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
 
@Controller
public class FileDownloadController extends AbstractServerController {
	byte[] pdfBytes = null;
	
	/*
	 *  Downloading Seller-/BuyerCardsPDF
	 */
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
			
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
			// Throw new Exception because were not able to return an Error page at this moment
			throw new RuntimeException(LogicHelper.getLocalizedMessage("error.PDF.cards"));
		}
	
		// Get a byte Array of the pdf
	    byte[] contents = outstream.toByteArray();
	    
	    // Write Headers
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("application/pdf"));
	    
	    // Set the download-Filename
	    String filename;
	    switch(type)	{
	    case("customer"):
	    	filename = LogicHelper.getLocalizedMessage("filename.PDF.buyer") + "_" + ControllerHelper.getNiceDate();
	    	break;
	    default:
	    	filename = LogicHelper.getLocalizedMessage("filename.PDF.buyer") + "_" + ControllerHelper.getNiceDate();
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
    		try	{
    			imageBytes = image.getBytes();
    		} catch(IOException e){
    			e.printStackTrace();
    			throw new RuntimeException(LogicHelper.getLocalizedMessage("error.PDF.export"));
    		}
    	}

		Image pdfImage = null;
		Map<String, Float> stats = null;
		try {
			pdfImage = Image.getInstance(imageBytes);
			stats = kms.getState().getStatistics();
		} catch (BadElementException | IOException e2) {
			e2.printStackTrace();
			throw new RuntimeException(LogicHelper.getLocalizedMessage("error.PDF.export"));
		}	catch(NoContractsException e)	{
			e.printStackTrace();
			throw new RuntimeException(LogicHelper.getLocalizedMessage("error.noContracts"));
		}
		
    	// Save byteArray ########################################
    	Pdf pdf = new Pdf();
    	Document document = new Document(PageSize.A4.rotate());
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		
		try {
			PdfWriter.getInstance(document, outstream); 
			
			document.open();
			document = pdf.createExportPdf(document, pdfImage, stats);
			document.close();
			
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException(LogicHelper.getLocalizedMessage("error.PDF.export"));
		}
		
		pdfBytes = outstream.toByteArray();
		
		// Write to File ######################################
    	FileOutputStream fos;
    	String path = ControllerHelper.getApplicationFolder() + ControllerHelper.getExportFolderName() + "/" + ControllerHelper.getNiceDate() + ".pdf";
		try {
			fos = new FileOutputStream(path);
			PdfWriter.getInstance(document, fos);
				
			document.open();
			document = pdf.createExportPdf(document, pdfImage, stats);
			document.close();
		} catch (FileNotFoundException | DocumentException e1) {
			e1.printStackTrace();
			throw new RuntimeException(LogicHelper.getLocalizedMessage("error.PDF.export"));
		}

    }
    
    //download export-pdf 
    @RequestMapping(value = "/pdfDownload")
    public ResponseEntity<byte[]> downloadPdf(){

	    if(pdfBytes != null){
	    	byte[] contents = pdfBytes;

		    // Define filename for download
		    String filename = LogicHelper.getLocalizedMessage("filename.PDF.export") + "_" + ControllerHelper.getNiceDate() + ".pdf";
		    
		    HttpHeaders headers = new HttpHeaders();
		    // Write headers
		    headers.setContentType(MediaType.parseMediaType("application/pdf"));
		    headers.setContentDispositionFormData(filename, filename);
		    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		    
		    return new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
	    }	else	{
	    	throw new RuntimeException(LogicHelper.getLocalizedMessage("error.PDF.export.download"));
	    }
    	
    }
    
    /*
     * Gets the in (hopefully) "path" pre-saved Config File for download.
     */
    @RequestMapping(value = "/config", method = RequestMethod.GET)
     public void saveConfig(@RequestParam("path") String fileName, HttpServletResponse response) {
    	    try {
    	      // get your file as InputStream
    	    	Resource resource = new FileSystemResource(fileName);
    	    	InputStream is = resource.getInputStream();
    	      // copy it to response's OutputStream
    	      IOUtils.copy(is, response.getOutputStream());
    	      response.setContentType("application/txt");
    	      String filename = LogicHelper.getLocalizedMessage("filename.config") + "_" + ControllerHelper.getNiceDate() + ".txt";
    	      response.setHeader("Content-Disposition", "attachment; filename=" + filename);
    	      response.flushBuffer();
    	    } catch (IOException ex) {
    	      ex.printStackTrace();
    	      throw new RuntimeException(LogicHelper.getLocalizedMessage("error.config.download"));
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
			
		} catch (IOException e) {
			e.printStackTrace();
			// Throw new Exception because were not able to return an Error page at this moment
			throw new RuntimeException(LogicHelper.getLocalizedMessage("error.csv.download"));
		}
		
		// Get a byte Array of the csv
	    byte[] contents = outstream.toByteArray();
	    // Define Filename for download
	    String filename = LogicHelper.getLocalizedMessage("filename.csv") + "_" + ControllerHelper.getNiceDate() + ".csv";
	    // Write headers
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("text/csv"));
	    headers.setContentDispositionFormData(filename, filename);
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    
	    return new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
    }
    
}
