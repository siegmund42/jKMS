package jKMS;

public abstract class State {
	protected Kartoffelmarktspiel kms;
	
	public void loadStandardDistribution(){}
	public void generateCards(){}
	public void newGroup(boolean isBuyer, int price, int relativeNumber){}
	public void setBasicConfig(int playerCount, int assistantCount){}
	public String createPDF(){ return ""; }
	public boolean removeCard(){ return false; } //Memo: Update Verteilungsset
	public boolean addContract(){ return false; }
	public float equilibriumPrice(){ return 0; }
	public float equilibriumSet(){ return 0; }
	public float averagePrice(){ return 0; }
	public Contract pickWinner(){ return null; }
	public int buyerProfit(){ return 0; }
	public int sellerProfit(){ return 0; }
	public boolean save(String path){ return false; }
	public void load(){}
}
