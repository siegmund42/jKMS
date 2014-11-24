package jKMS.states;

import jKMS.Kartoffelmarktspiel;
import jKMS.cards.BuyerCard;
import jKMS.cards.SellerCard;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

public class Preparation extends State{
	public Preparation(Kartoffelmarktspiel kms){
		this.kms = kms;
	}
	
	public void loadStandardDistribution(){}
	public void setBasicConfig(int playerCount, int assistantCount){}
	public String createPDF(){ return ""; }
	public boolean save(String path){ return false; }
	public void load(){}
	
	// generateCardSet
	// Generate an ordered, random Set of Cards using
	// bDistribution and sDistribution
	public void generateCards() {
		// DECLARATION
		Random random = new Random();

		List<Integer> bKeys = new ArrayList<Integer>(kms.getConfiguration().getbDistribution().keySet());
		List<Integer> sKeys = new ArrayList<Integer>(kms.getConfiguration().getsDistribution().keySet());
		Map<Integer, Integer> bTemp = new HashMap<Integer, Integer>();
		Map<Integer, Integer> sTemp = new HashMap<Integer, Integer>();

		int id = 1001;
		int randomKey, randomListEntry;

		bTemp = kms.getConfiguration().getbDistribution();
		sTemp = kms.getConfiguration().getsDistribution();
		
		//clear Card Set
		kms.getConfiguration().getCards().clear();

		// IMPLEMENTATION
		while (!bTemp.isEmpty() && !sTemp.isEmpty()) {
			// Create Buyer Card
			randomListEntry = random.nextInt(bKeys.size());
			randomKey = bKeys.get(randomListEntry);

			kms.getConfiguration().getCards().add(new BuyerCard(id, randomKey, 'A'));

			bTemp.put(randomKey, bTemp.get(randomKey) - 1);
			if (bTemp.get(randomKey) == 0) {
				bTemp.remove(randomKey);
				bKeys.remove(randomListEntry);
			}

			id++;

			// Create Seller Card
			randomListEntry = random.nextInt(sKeys.size());
			randomKey = sKeys.get(randomListEntry);

			kms.getConfiguration().getCards().add(new SellerCard(id, randomKey,'A'));

			sTemp.put(randomKey, sTemp.get(randomKey) - 1);
			if (sTemp.get(randomKey) == 0) {
				sTemp.remove(randomKey);
				sKeys.remove(randomListEntry);
			}

			id++;
		}
	}

	// newGroup
	// Creates a new entry for the bDistribution or sDistribution Map,
	// depending if isBuyer is true or false.
	public void newGroup(boolean isBuyer, int price, int relativeNumber) {
		if (isBuyer)
			kms.getConfiguration().getbDistribution().put(price, relativeNumber);
		else
			kms.getConfiguration().getsDistribution().put(price, relativeNumber);
	}
}
