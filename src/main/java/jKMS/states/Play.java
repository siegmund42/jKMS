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
	    //getCardset for card check
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
	    // return 2 when Card not available
	    if(card1 == null || card2 == null){
	    	System.out.println("addContract() not successful,because Card not available");
	    	return 2;
	    }else if((card1 instanceof BuyerCard && card2 instanceof BuyerCard) 
	    		|| (card1 instanceof SellerCard && card2 instanceof SellerCard)){
	    	System.out.println("addContract() not successful,because deal with double buyer or seller");
	    	return 1;
	    //return 1 when double buyer or seller
	    }else if(gehandeltCards.contains(card1) || gehandeltCards.contains(card2)){
	    	System.out.println("addContract() not successful,because Card already dealt");
	    	return 3;
	    //return 3 when card already dealt
	    }else{
	    	Contract contract;
	    	if(card1 instanceof BuyerCard) {
	    		contract = new Contract((BuyerCard)card1,(SellerCard)card2,price,uri);
	    	}else{
	    		contract = new Contract((BuyerCard)card2,(SellerCard)card1,price,uri);
	    	}
	    	kms.getContracts().add(contract);
	    	System.out.println("Added contract: " + contract.toString());
	    	System.out.println("addContract() successful");
	    	return 0;
	    //return 0 when everything fine	
	    }  
	}
}
