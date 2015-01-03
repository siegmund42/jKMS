package jKMS.states;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import au.com.bytecode.opencsv.CSVWriter;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

import jKMS.Contract;
import jKMS.Kartoffelmarktspiel;
import jKMS.exceptionHelper.EmptyFileException;
import jKMS.exceptionHelper.FalseLoadFileException;
import jKMS.exceptionHelper.NoContractsException;
import jKMS.exceptionHelper.NoIntersectionException;
import jKMS.exceptionHelper.WrongAssistantCountException;
import jKMS.exceptionHelper.WrongFirstIDException;
import jKMS.exceptionHelper.WrongPlayerCountException;
import jKMS.exceptionHelper.WrongRelativeDistributionException;

public abstract class State {
	protected Kartoffelmarktspiel kms;
	
	public void loadStandardDistribution() throws IllegalStateException	{
		throw new IllegalStateException("It seems you did not follow the workflow correctly. Please try again from the beginning!");
	} // LUKAS - DONE
	public void generateCards() throws WrongFirstIDException, WrongAssistantCountException, WrongPlayerCountException, WrongRelativeDistributionException, IllegalStateException	{
		throw new IllegalStateException("It seems you did not follow the workflow correctly. Please try again from the beginning!");
	} // JUSTUS - DONE
	public void newGroup(boolean isBuyer, int price, int relativeNumber, int absoluteNumber) throws IllegalStateException{
		throw new IllegalStateException("It seems you did not follow the workflow correctly. Please try again from the beginning!");
	} // DONE
	public void setBasicConfig(int playerCount, int assistantCount) throws IllegalStateException	{
		throw new IllegalStateException("It seems you did not follow the workflow correctly. Please try again from the beginning!");
	} // DOMINIK DONE
	public void createPdf(boolean isBuyer, Document doc) throws DocumentException,IOException,IllegalStateException	{
		throw new IllegalStateException("It seems you did not follow the workflow correctly. Please try again from the beginning!");
	} // JUSTUS
	public boolean removeCard(char pack, int lastId) throws WrongPlayerCountException, WrongAssistantCountException, WrongFirstIDException, WrongRelativeDistributionException{ return false; } //DOMNINIK DONE
	public int addContract(int id1, int id2, int price, String uri) throws IllegalStateException	{
		throw new IllegalStateException("It seems you did not follow the workflow correctly. Please try again from the beginning!");
	} // XINYU
																			
	public float[] getEquilibrium() throws NoIntersectionException, IllegalStateException	{
		throw new IllegalStateException("It seems you did not follow the workflow correctly. Please try again from the beginning!");
	} // DONE
	public Map<String,Float> getStatistics() throws NoContractsException, NoIntersectionException, IllegalStateException{
		throw new IllegalStateException("It seems you did not follow the workflow correctly. Please try again from the beginning!");
	} // DONE
	public Contract pickWinner(boolean repeat) throws NoContractsException,IllegalStateException	{
		throw new IllegalStateException("It seems you did not follow the workflow correctly. Please try again from the beginning!");
	} // DONE
	public int buyerProfit(Contract contract) throws IllegalStateException	{
		throw new IllegalStateException("It seems you did not follow the workflow correctly. Please try again from the beginning!");
	} // DONE
	public int sellerProfit(Contract contract) throws IllegalStateException	{
		throw new IllegalStateException("It seems you did not follow the workflow correctly. Please try again from the beginning!");
	} // DONE
	public boolean save(OutputStream o) throws IOException, IllegalStateException	{
		throw new IllegalStateException("It seems you did not follow the workflow correctly. Please try again from the beginning!");
	}
	public void load(MultipartFile file) throws NumberFormatException, IOException, EmptyFileException, IllegalStateException, FalseLoadFileException	{
		throw new IllegalStateException("It seems you did not follow the workflow correctly. Please try again from the beginning!");
	}
	
	public void generateCSV(CSVWriter writer) throws IllegalStateException	{
		throw new IllegalStateException("It seems you did not follow the workflow correctly. Please try again from the beginning!");
	}
}   
