package jKMS;

public class Playthrough extends State{
	Playthrough(Kartoffelmarktspiel kms){
		this.kms = kms;
	}
	
	public boolean removeCard(){ return false; }
	public boolean addContract(){ return false; }
	public void load(){}
}
