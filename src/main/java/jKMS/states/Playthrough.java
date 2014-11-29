package jKMS.states;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import jKMS.Amount;
import jKMS.Contract;
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
				//Update player count
				kms.getConfiguration().setPlayerCount(kms.getConfiguration().getPlayerCount()-1);
				
				//Update distribution-map
				if(iter instanceof BuyerCard) distrib = kms.getConfiguration().getbDistribution();
				else distrib = kms.getConfiguration().getsDistribution();
				
				key = iter.getValue();
				
				distrib.get(key).setAbsolute(distrib.get(key).getAbsolute()-1);
				if(distrib.get(key).getAbsolute() == 0) distrib.remove(key);
				
				kms.getCards().remove(iter);
			}
		}
		
		if(kms.getCards() == oldSet) return false;
		else return true;
	}
	

	public boolean addContract(int id1,int id2,int price){  
		Iterator<Card> iter = kms.getCards().iterator();
	    BuyerCard card1 = null;
    	SellerCard card2 = null;
	    while(iter.hasNext()){	
	    	Card card = iter.next();
	    	if (card.getId() == id1){
	    		card1 = (BuyerCard)card;
	    	}
	    	else if(card.getId() == id2){
	    		card2 = (SellerCard)card;
	    	}else
	    		continue;
	    }
	    if(card1 != null && card2 != null){
	    	Contract contract =new Contract(card1,card2,price);
	    	kms.getContracts().add(contract);
	    	return true;
	    }else{
	    	return false;
	    }
	}

	public void load(){}
}
