package jKMS;


import jKMS.states.Evaluation;
import jKMS.states.Playthrough;
import jKMS.states.Preparation;
import jKMS.states.State;


import java.util.Set;
import java.util.LinkedHashSet;

public class Kartoffelmarktspiel {

	private Set<Contract> contracts;
	private State state;
	private Configuration configuration;


	private Kartoffelmarktspiel instance;

	// DEFAULT CONSTRUCTOR
	private Kartoffelmarktspiel() {
		// load default settings from file
		instance = this;


		contracts = new LinkedHashSet<Contract>();
		state = new Preparation(instance);
		

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

	public Kartoffelmarktspiel getInstance() {
		return instance;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	

}
