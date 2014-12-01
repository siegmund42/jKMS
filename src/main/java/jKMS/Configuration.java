package jKMS;


import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

@Component
public class Configuration {
	

	private int playerCount;
	private int assistantCount;
	private int groupCount;
	private int firstID;
	private Map<Integer, Amount> bDistribution;
	private Map<Integer, Amount> sDistribution;
	//TODO discuss: should we save the random-seed, so that the exactly same game can be played twice??
	
	public Configuration(){
		
		setbDistribution(new TreeMap<Integer, Amount>());
		setsDistribution(new TreeMap<Integer, Amount>());
		// TODO Make FirstID editable for User
		setFirstID(1001);
	}

	public Boolean save(){ //TODO
		return false; 
	}
	
// getter and setter make the world better ;)
	
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

	public Map<Integer, Amount> getbDistribution() {
		return bDistribution;
	}

	public void setbDistribution(Map<Integer, Amount> bDistribution) {
		this.bDistribution = bDistribution;
	}

	public Map<Integer, Amount> getsDistribution() {
		return sDistribution;
	}

	public void setsDistribution(Map<Integer, Amount> sDistribution) {
		this.sDistribution = sDistribution;
	}

	public int getFirstID() {
		return firstID;
	}

	public void setFirstID(int firstID) {
		this.firstID = firstID;
	}

}
