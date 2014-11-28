package jKMS.states;

import java.io.IOException;
import java.util.Map;

import com.itextpdf.text.Document;

import jKMS.Contract;
import jKMS.Kartoffelmarktspiel;

public abstract class State {
	protected Kartoffelmarktspiel kms;
	
	public void loadStandardDistribution(){} // LUKAS - DONE
	public void generateCards(){} // JUSTUS - DONE
	public void newGroup(boolean isBuyer, int price, int relativeNumber, int absoluteNumber){} // DONE
	public void setBasicConfig(int playerCount, int assistantCount){} // DOMINIK DONE
	public String createPDF(Document document, boolean isBuyer){ return ""; } // JUSTUS
	public boolean removeCard(char pack, int lastId){ return false; } //DOMNINIK DONE
	public boolean addContract(int id1, int id2, int price){ return false; } // XINYU
																			
	public float equilibriumPrice(){ return 0; } // TIMON
	public float equilibriumSet(){ return 0; } // TIMON
	public Map<String,Float> getStatistics(){ return null; } // DONE
	public Map<String,Integer> pickWinner(){ return null; } // DONE
	public int buyerProfit(){ return 0; } // DONE
	public int sellerProfit(){ return 0; } // DONE
	public boolean save(String path){ return false; }
	public void load(String fileurl) throws NumberFormatException, IOException{}
}
