package jKMS;


import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

@Component
public class Configuration {
	

	private int playerCount;
	private int assistantCount;
	private int firstID;
	private Map<Integer, Amount> bDistribution;
	private Map<Integer, Amount> sDistribution;
	
	public Configuration(){
		
		setbDistribution(new TreeMap<Integer, Amount>());
		setsDistribution(new TreeMap<Integer, Amount>());
		setFirstID(1001);
	}
	
	@Override
	public String toString()	{
		String str = ""; 
		str += "Buyer: ";
		for(Integer price : bDistribution.keySet())	{
			str += price + " , " + this.bDistribution.get(price).getRelative() + " , " + this.bDistribution.get(price).getAbsolute() + " | ";
		}
		str += System.getProperty("line.separator") + "Seller: ";
		for(Integer price : sDistribution.keySet())	{
			str += price + " , " + this.sDistribution.get(price).getRelative() + " , " + this.sDistribution.get(price).getAbsolute() + " | ";
		}
		return str;
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

	public int getGroupCount(String type) {
		if(type.equals("s"))
			return this.sDistribution.size();
		if(type.equals("b"))
			return this.bDistribution.size();
		throw new IllegalArgumentException("Argument " + type + "is not allowed for getGroupCount!");
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
