package jKMS.states;

import jKMS.Amount;
import jKMS.Kartoffelmarktspiel;
import jKMS.LogicHelper;
import jKMS.cards.BuyerCard;
import jKMS.cards.Card;
import jKMS.exceptionHelper.WrongAssistantCountException;
import jKMS.exceptionHelper.WrongFirstIDException;
import jKMS.exceptionHelper.WrongPlayerCountException;
import jKMS.exceptionHelper.WrongRelativeDistributionException;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Load extends State {
	
	public Load(Kartoffelmarktspiel kms){
		this.kms = kms;
	}

	public void load(){}
	
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
		if((LogicHelper.getRelativeSum(kms.getbDistribution()) +  LogicHelper.getRelativeSum(kms.getsDistribution())) != 200) throw new WrongRelativeDistributionException();
		
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
				
				System.out.println("Excluded Card: " + iter.getId());
			}
		}
		
		if(kms.getCards() == oldSet) return false;
		else return true;
	}
	
	
	
}
