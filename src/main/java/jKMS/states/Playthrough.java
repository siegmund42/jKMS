package jKMS.states;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import jKMS.Amount;
import jKMS.Contract;
import jKMS.Kartoffelmarktspiel;
import jKMS.LogicHelper;
import jKMS.cards.Card;
import jKMS.cards.BuyerCard;
import jKMS.cards.SellerCard;
import jKMS.exceptionHelper.WrongAssistantCountException;
import jKMS.exceptionHelper.WrongFirstIDException;
import jKMS.exceptionHelper.WrongPlayerCountException;
import jKMS.exceptionHelper.WrongRelativeDistributionException;

public class Playthrough extends State{
	
	public Playthrough(Kartoffelmarktspiel kms){
		this.kms = kms;
	}
	
	//removeCard
	//removes all cards from the given package (pack)
	//beginning with lastId up to its size
	public boolean removeCard(char pack, int lastId) throws WrongPlayerCountException, WrongAssistantCountException, WrongFirstIDException, WrongRelativeDistributionException{
		Set<Card> oldSet = new LinkedHashSet<Card>(kms.getCards());
		Map<Integer, Amount> distrib;
		Integer key;
		
		//test is there a conform configuration?
		if(kms.getPlayerCount() != (LogicHelper.getAbsoluteSum(kms.getbDistribution()) +  LogicHelper.getAbsoluteSum(kms.getsDistribution())))throw new WrongPlayerCountException();
		if(kms.getAssistantCount() <= 0)throw new WrongAssistantCountException();
		if(kms.getConfiguration().getFirstID() < 0)throw new WrongFirstIDException();
		if((LogicHelper.getRelativeSum(kms.getbDistribution()) +  LogicHelper.getRelativeSum(kms.getsDistribution())) != 100) throw new WrongRelativeDistributionException();
		
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
	

	public int addContract(int id1,int id2,int price){ 
		// TODO return errorCode 0 -> everything fine 1 -> double buyer/seller 2 -> Card not available 3 -> already dealt
		// TODO Reihenfolge der Eingabe egal --> id2 kann auch buyer sein
		// TODO Es dürfen nicht beide Käufer/verkäufer sein
		// TODO Es muss Käufer/Verkäufer geben (dürfen nicht ausgetragen sein)
		// TODO Weder Käufer noch Verkäufer darf bisher gehandelt haben
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
	    	return 0;
	    }else{
	    	return 2;
	    }
	}

	public void load(){}
}
