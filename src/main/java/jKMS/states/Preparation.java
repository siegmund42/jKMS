package jKMS.states;

import jKMS.Amount;
import jKMS.Kartoffelmarktspiel;
import jKMS.cards.BuyerCard;
import jKMS.cards.Card;
import jKMS.cards.SellerCard;
import jKMS.LogicHelper;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;

public class Preparation extends State{
	public Preparation(Kartoffelmarktspiel kms){
		this.kms = kms;
	}
	
	// load StandardConfiguration into kms
	public void loadStandardDistribution(){

		Map<Integer, Amount> bDistribution = new TreeMap<>();
		bDistribution.put(70, new Amount(20, 0));
		bDistribution.put(65, new Amount(16, 0));
		bDistribution.put(60, new Amount(16, 0));
		bDistribution.put(55, new Amount(16, 0));
		bDistribution.put(50, new Amount(16, 0));
		bDistribution.put(45, new Amount(16, 0));
		kms.getConfiguration().setbDistribution(bDistribution);
		
		Map<Integer, Amount> sDistribution = new TreeMap<>();
		sDistribution.put(63, new Amount(10, 0));
		sDistribution.put(58, new Amount(18, 0));
		sDistribution.put(53, new Amount(18, 0));
		sDistribution.put(48, new Amount(18, 0));
		sDistribution.put(43, new Amount(18, 0));
		sDistribution.put(38, new Amount(18, 0));
		kms.getConfiguration().setsDistribution(sDistribution);
		
		kms.getConfiguration().setGroupCount(6);
		
	}
	
	
	//setBasicConfig
	//setter method for the number of players and assistants
	public void setBasicConfig(int playerCount, int assistantCount){
		kms.getConfiguration().setPlayerCount(playerCount);
		kms.getConfiguration().setAssistantCount(assistantCount);
	}
	
	public String createPDF(){ return ""; }
	public boolean save(String path){ return false; }
	
	public void load(){}
	
	// generateCardSet
	// Generate an ordered, random Set of Cards using
	// bDistribution and sDistribution
	
	public void generateCards() {
		// DECLARATION
		
		//for put seller and buyer distribution
		Random random = new Random();
		
		List<Integer> bKeys = new ArrayList<Integer>(kms.getConfiguration().getbDistribution().keySet());
		List<Integer> sKeys = new ArrayList<Integer>(kms.getConfiguration().getsDistribution().keySet());
		Map<Integer, Amount> bTemp = new HashMap<Integer, Amount>();
		Map<Integer, Amount> sTemp = new HashMap<Integer, Amount>();
		
		int id = kms.getConfiguration().getFirstID();
		int randomKey, randomListEntry;
		
		//for put packages
		int i,ide;
		int[] packdistribution =LogicHelper.getPackageDistribution(kms.getPlayerCount(),kms.getAssistantCount());

		

		bTemp = kms.getConfiguration().getbDistribution();
		sTemp = kms.getConfiguration().getsDistribution();
		
		//clear Card Set
		kms.getCards().clear();

		// IMPLEMENTATION
		
		//put seller and buyer distribution and put packages
		i =0;
		ide = kms.getConfiguration().getFirstID() + packdistribution[0];
		
		while (!bTemp.isEmpty() && !sTemp.isEmpty()) {
			// Create Buyer Card
			randomListEntry = random.nextInt(bKeys.size());
			randomKey = bKeys.get(randomListEntry);
			
			if(id < ide){
				kms.getCards().add(new BuyerCard(id, randomKey, LogicHelper.IntToPackage(i)));
			}else {
				i++;
				ide = ide + packdistribution[i];
				kms.getCards().add(new BuyerCard(id, randomKey, LogicHelper.IntToPackage(i)));
			}


		
			bTemp.put(randomKey, new Amount(bTemp.get(randomKey).getRelative(), bTemp.get(randomKey).getAbsolute() - 1)); 
			if (bTemp.get(randomKey).getAbsolute() == 0) {
				bTemp.remove(randomKey);
				bKeys.remove(randomListEntry);
			}

			id++;

			// Create Seller Card
			randomListEntry = random.nextInt(sKeys.size());
			randomKey = sKeys.get(randomListEntry);

			if(id < ide){
				kms.getCards().add(new SellerCard(id, randomKey, LogicHelper.IntToPackage(i)));
			}else {
				i++;
				ide = ide + packdistribution[i];
				kms.getCards().add(new SellerCard(id, randomKey, LogicHelper.IntToPackage(i)));
			}

			sTemp.put(randomKey, new Amount(sTemp.get(randomKey).getRelative(), sTemp.get(randomKey).getAbsolute() - 1));
			if (sTemp.get(randomKey).getAbsolute() == 0) {
				sTemp.remove(randomKey);
				sKeys.remove(randomListEntry);
			}

			id++;
		}
		
		
	}			

	// newGroup
	// Creates a new entry for the bDistribution or sDistribution Map,
	// depending if isBuyer is true or false.
	public void newGroup(boolean isBuyer, int price, int relativeNumber, int absoluteNumber) {
		if (isBuyer)
			kms.getConfiguration().getbDistribution().put(price, new Amount(absoluteNumber, relativeNumber));
		else
			kms.getConfiguration().getsDistribution().put(price, new Amount(absoluteNumber, relativeNumber));
	}
}
