package jKMS.states;

import java.util.LinkedHashSet;
import java.util.Set;

import jKMS.Kartoffelmarktspiel;
import jKMS.cards.Card;

public class Playthrough extends State{
	
	public Playthrough(Kartoffelmarktspiel kms){
		this.kms = kms;
	}
	
	//removeCard
	//removes all cards from the given package (pack)
	//beginning with lastId up to its size
	public boolean removeCard(char pack, int lastId){
		Set<Card> oldSet = new LinkedHashSet<Card>(kms.getCards());
		
		for(Card iter : oldSet){
			if(iter.getPackage() == pack && iter.getId() >= lastId) kms.getCards().remove(iter);
		}
		
		if(kms.getCards() == oldSet) return false;
		else return true;
	}
	
	public boolean addContract(){ return false; }
	public void load(){}
}
