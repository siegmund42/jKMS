package jKMS;

import jKMS.cards.Card;
import jKMS.states.Evaluation;
import jKMS.states.Playthrough;
import jKMS.states.Preparation;
import jKMS.states.State;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.LinkedHashSet;

public class Kartoffelmarktspiel {
	private Set<Card> cards;
	private Set<Contract> contracts;
	private State state;

	private int playerCount;
	private int assistantCount;
	private int groupCount;
	private Map<Integer, Integer> bDistribution;
	private Map<Integer, Integer> sDistribution;
	private Kartoffelmarktspiel instance;

	// DEFAULT CONSTRUCTOR
	private Kartoffelmarktspiel() {
		// load default settings from file
		instance = this;

		cards = new LinkedHashSet<Card>();
		contracts = new LinkedHashSet<Contract>();
		state = new Preparation(instance);
		
		bDistribution = new HashMap<Integer, Integer>();
		sDistribution = new HashMap<Integer, Integer>();
	}
	
	//STATE SETTERS
	void prepare(){
		state = new Preparation(instance);
	}
	
	void play(){
		state = new Playthrough(instance);
	}
	
	void evaluate(){
		state = new Evaluation(instance);
	}

	// GETTERS AND SETTERS
	public int getPlayerCount() {
		return playerCount;
	}

	public void setPlayerCount(int playerCount) {
		this.playerCount = playerCount;
	}

	public int getAssistantCount() {
		return assistantCount;
	}

	public void setAssistantCount(int assistantCount) {
		this.assistantCount = assistantCount;
	}

	public int getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(int groupCount) {
		this.groupCount = groupCount;
	}
	
	public Map<Integer, Integer> getbDistribution() {
		return bDistribution;
	}

	public Map<Integer, Integer> getsDistribution() {
		return sDistribution;
	}

	public Kartoffelmarktspiel getInstance() {
		return instance;
	}
	
	public Set<Card> getCards(){
		return cards;
	}
}
