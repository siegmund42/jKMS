package jKMS.states;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

import jKMS.Amount;
import jKMS.Contract;
import jKMS.Kartoffelmarktspiel;
import jKMS.cards.Card;
import jKMS.exceptionHelper.EmptyFileException;
import jKMS.exceptionHelper.WrongAssistantCountException;
import jKMS.exceptionHelper.WrongFirstIDException;
import jKMS.exceptionHelper.WrongPlayerCountException;
import jKMS.exceptionHelper.WrongRelativeDistributionException;

public abstract class State {
	protected Kartoffelmarktspiel kms;
	
	public void loadStandardDistribution(){} // LUKAS - DONE
	public void generateCards() throws WrongFirstIDException, WrongAssistantCountException, WrongPlayerCountException, WrongRelativeDistributionException{} // JUSTUS - DONE
	public void newGroup(boolean isBuyer, int price, int relativeNumber, int absoluteNumber){} // DONE
	public void setBasicConfig(int playerCount, int assistantCount){} // DOMINIK DONE
	public void createPdf(boolean isBuyer, Document doc) throws DocumentException,IOException{} // JUSTUS
	public boolean removeCard(char pack, int lastId) throws WrongPlayerCountException, WrongAssistantCountException, WrongFirstIDException, WrongRelativeDistributionException{ return false; } //DOMNINIK DONE
	public int addContract(int id1, int id2, int price){ return 0; } // XINYU
																			
	public float equilibriumPrice(){ return 0; } // TIMON
	public float equilibriumSet(){ return 0; } // TIMON
	public Map<String,Float> getStatistics(){ return null; } // DONE
	public Contract pickWinner(){ return null; } // DONE
	public int buyerProfit(Contract contract){ return 0; } // DONE
	public int sellerProfit(Contract contract){ return 0; } // DONE
	public boolean save(String path) throws IOException{ return false; }
	public void load(MultipartFile file) throws NumberFormatException, IOException, EmptyFileException{}
}   
