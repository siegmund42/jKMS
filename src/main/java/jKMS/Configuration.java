package jKMS;


import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

@Component
public class Configuration {
	

	private int playerCount;
	private int assistantCount;
	private int groupCount;
	private Map<Integer, Integer> bDistribution;
	private Map<Integer, Integer> sDistribution;
	
	public Configuration(){
		
		setbDistribution(new TreeMap<Integer, Integer>());
		setsDistribution(new TreeMap<Integer, Integer>());
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

	public Map<Integer, Integer> getbDistribution() {
		return bDistribution;
	}

	public void setbDistribution(Map<Integer, Integer> bDistribution) {
		this.bDistribution = bDistribution;
	}

	public Map<Integer, Integer> getsDistribution() {
		return sDistribution;
	}

	public void setsDistribution(Map<Integer, Integer> sDistribution) {
		this.sDistribution = sDistribution;
	}

}
