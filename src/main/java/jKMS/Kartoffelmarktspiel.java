package jKMS;


import jKMS.cards.Card;
import jKMS.states.Evaluation;
import jKMS.states.Load;
import jKMS.states.Play;
import jKMS.states.Preparation;
import jKMS.states.State;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Kartoffelmarktspiel {

	private Set<Contract> contracts;
	private State state;
	@Autowired
	private Configuration configuration;
	private Set<Card> cards;
	private Kartoffelmarktspiel instance;
	private Date begin;
	private Date end;
    
	// DEFAULT CONSTRUCTOR
	private Kartoffelmarktspiel() {
		// load default settings from file

		instance = this;
		contracts = new LinkedHashSet<Contract>();
		cards = new LinkedHashSet<Card>();
		prepare();
	}

	// STATE SETTERS
	public void prepare() {
		state = new Preparation(instance);
		LogicHelper.print("Preparing..");
		//Application.gui.setReady(LogicHelper.getLocalizedMessage("preparation"));
		//gui.setReady(LogicHelper.getLocalizedMessage("preparation"));
	}

	public void load() {
		state = new Load(instance);
		LogicHelper.print("Loading..");
		//Application.gui.setReady(LogicHelper.getLocalizedMessage("loading"));
	}
	
	public void play() {
		// Start game
		begin = new Date();
		state = new Play(instance);
		SimpleDateFormat ft = new SimpleDateFormat ("HH:mm:ss");
		LogicHelper.print("Playing since: " + ft.format(begin));
	}

	public void evaluate() {
		// Stop game
		end = new Date();
		state = new Evaluation(instance);
		SimpleDateFormat ft = new SimpleDateFormat ("HH:mm:ss");
		LogicHelper.print("Game stopped at: " + ft.format(end));
		LogicHelper.print("Evaluating..");
	}

	// GETTERS AND SETTERS

	public Configuration getConfiguration() {
		return configuration;
	}

	public Set<Card> getCards() {
		return cards;
	}
	
	public Set<Contract> getContracts(){
		return contracts;
	}
	
	public State getState()	{
		return state;
	}

	public void setCards(Set<Card> cards) {
		this.cards = cards;
	}
	
	// GETTERS FOR CONFIGURATION
	
	public int getPlayerCount() {
		return configuration.getPlayerCount();
	}
	
	public int getLastId()	{
		int last = 0;
		for(Card card : cards)	{
			if(card.getId() > last)
				last = card.getId();
		}
		return last;
	}

	public int getAssistantCount() {
		return configuration.getAssistantCount();
	}

	public int getGroupCount(String type) {
		return configuration.getGroupCount(type);
	}
	
	public Map<Integer, Amount> getbDistribution() {
		return configuration.getbDistribution();
	}

	public Map<Integer, Amount> getsDistribution() {
		return configuration.getsDistribution();
	}
	
	public Date getBegin() {
		return begin;
	}

	public Date getEnd() {
		return end;
	}

	
}
