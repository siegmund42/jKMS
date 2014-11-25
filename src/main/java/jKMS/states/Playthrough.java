package jKMS.states;

import jKMS.Kartoffelmarktspiel;

public class Playthrough extends State{
	public Playthrough(Kartoffelmarktspiel kms){
		this.kms = kms;
	}
	
	public boolean removeCard(){return false; }
	public boolean addContract(){ return false; }
	public void load(){}
}
