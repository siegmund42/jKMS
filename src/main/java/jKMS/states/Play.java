package jKMS.states;

import jKMS.Contract;
import jKMS.Kartoffelmarktspiel;
import jKMS.cards.BuyerCard;
import jKMS.cards.Card;
import jKMS.cards.SellerCard;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class Play extends State {
	
	public Play(Kartoffelmarktspiel kms)	{
		this.kms = kms;
	}
	
	@Override
	public int addContract(int id1,int id2,int price, String uri){ 
		// returns errorCode: 0 -> everything fine 1 -> double buyer/seller 2 -> Card not available 3 -> already dealt
		Set<Card> gehandeltCards = new LinkedHashSet<Card>();
	    Iterator<Contract> citer = kms.getContracts().iterator();
	    while(citer.hasNext()){
	    	Contract gehandeltContract = citer.next();
	    	gehandeltCards.add(gehandeltContract.getBuyer());
	    	gehandeltCards.add(gehandeltContract.getSeller());
	    }
		Iterator<Card> iter = kms.getCards().iterator();
	    Card card1 = null;
    	Card card2 = null;
	    while(iter.hasNext()){	
	    	Card card = iter.next();
	    	if (card.getId() == id1){
	    		card1 = card;
	    	}
	    	else if(card.getId() == id2){
	    		card2 = card;
	    	}
	    }
	    if(card1 == null || card2 == null){
	    	return 2;
	    }else if((card1 instanceof BuyerCard && card2 instanceof BuyerCard) 
	    		|| (card1 instanceof SellerCard && card2 instanceof SellerCard)){
	    	return 1;
	    }else if(gehandeltCards.contains(card1) || gehandeltCards.contains(card2)){
	    	return 3;
	    }else{
	    	Contract contract;
	    	if(card1 instanceof BuyerCard) {
	    		contract = new Contract((BuyerCard)card1,(SellerCard)card2,price,uri);
	    	}else{
	    		contract = new Contract((BuyerCard)card2,(SellerCard)card1,price,uri);
	    	}
	    	kms.getContracts().add(contract);
	    	System.out.println("Added contract: " + contract.toString());
	    	return 0;
	    }  
	}
}
