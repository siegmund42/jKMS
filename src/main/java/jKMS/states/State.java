package jKMS.states;

import com.itextpdf.text.Document;

import jKMS.Contract;
import jKMS.Kartoffelmarktspiel;

public abstract class State {
	protected Kartoffelmarktspiel kms;
	
	public void loadStandardDistribution(){} // LUKAS
	public void generateCards(){} // JUSTUS
	public void newGroup(boolean isBuyer, int price, int relativeNumber){} // 
	public void setBasicConfig(int playerCount, int assistantCount){} // DOMINIK
	public String createPDF(Document document, boolean isBuyer){ return ""; } // 
	public boolean removeCard(){ return false; } //Memo: Update Verteilungsset
	public boolean addContract(int id1, int id2, int price){ return false; } // XINYU
																			// TIMON implementiert Contract Klasse
	public float equilibriumPrice(){ return 0; } // TIMON
	public float equilibriumSet(){ return 0; } // TIMON
	public float averagePrice(){ return 0; } // TIMON
	public Contract pickWinner(){ return null; } // TIMON
	public int buyerProfit(){ return 0; } // TIMON
	public int sellerProfit(){ return 0; } // TIMON
	public boolean save(String path){ return false; }
	public void load(){}
}
