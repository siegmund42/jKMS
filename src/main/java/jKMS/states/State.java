package jKMS.states;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

import jKMS.Contract;
import jKMS.Kartoffelmarktspiel;
import jKMS.exceptionHelper.EmptyFileException;
import jKMS.exceptionHelper.NoContractsException;
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
																			
	public float equilibriumPrice() throws IllegalStateException	{
		throw new IllegalStateException("It seems you did not follow the workflow correctly. Please try again from the beginning!");
	} // TIMON
	public float equilibriumSet() throws IllegalStateException	{
		throw new IllegalStateException("It seems you did not follow the workflow correctly. Please try again from the beginning!");
	} // TIMON
	public Map<String,Float> getStatistics() throws NoContractsException, IllegalStateException{
		throw new IllegalStateException("It seems you did not follow the workflow correctly. Please try again from the beginning!");
	} // DONE
	public Contract pickWinner() throws NoContractsException,IllegalStateException	{
		throw new IllegalStateException("It seems you did not follow the workflow correctly. Please try again from the beginning!");
	} // DONE
	public int buyerProfit(Contract contract) throws IllegalStateException	{
		throw new IllegalStateException("It seems you did not follow the workflow correctly. Please try again from the beginning!");
	} // DONE
	public int sellerProfit(Contract contract) throws IllegalStateException	{
		throw new IllegalStateException("It seems you did not follow the workflow correctly. Please try again from the beginning!");
	} // DONE
	public boolean save(String path) throws IOException, IllegalStateException	{
		throw new IllegalStateException("It seems you did not follow the workflow correctly. Please try again from the beginning!");
	}
	public void load(MultipartFile file) throws NumberFormatException, IOException, EmptyFileException, IllegalStateException	{
		throw new IllegalStateException("It seems you did not follow the workflow correctly. Please try again from the beginning!");
	}
}   
