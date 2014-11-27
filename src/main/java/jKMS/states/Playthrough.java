package jKMS.states;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import jKMS.Amount;
import jKMS.Kartoffelmarktspiel;
import jKMS.cards.Card;
import jKMS.cards.BuyerCard;
import jKMS.cards.SellerCard;

public class Playthrough extends State{
	
	public Playthrough(Kartoffelmarktspiel kms){
		this.kms = kms;
	}
	
	//removeCard
	//removes all cards from the given package (pack)
	//beginning with lastId up to its size
	public boolean removeCard(char pack, int lastId){
		Set<Card> oldSet = new LinkedHashSet<Card>(kms.getCards());
		Map<Integer, Amount> distrib;
		Integer key;
		
		for(Card iter : oldSet){
			//Check if card must be removed (Id is higher than lasdId)
			if(iter.getPackage() == pack && iter.getId() >= lastId){
				kms.getCards().remove(iter);
			
				//Update player count
				kms.getConfiguration().setPlayerCount(kms.getConfiguration().getPlayerCount()-1);
				
				//Update distribution-map if Card is a Buyer
				if(iter instanceof BuyerCard) distrib = kms.getConfiguration().getbDistribution();
				else distrib = kms.getConfiguration().getsDistribution();
				
				key = iter.getValue();
				
				if(distrib.get(key).getAbsolute() == 1) distrib.remove(key);
				else distrib.get(key).setAbsolute(distrib.get(key).getAbsolute()-1);
			}
		}
		
		if(kms.getCards() == oldSet) return false;
		else return true;
	}
	
	public boolean addContract(){ return false; }
	public void load(){}
}
