package jKMS.states;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import jKMS.Contract;
import jKMS.Kartoffelmarktspiel;

public class Evaluation extends State{
	
	public Evaluation(Kartoffelmarktspiel kms){
		this.kms = kms;
	}
	
	//returns statistic data of all contracts - min, max, average, variance, standard deviation
	@Override
	public Map<String,Float> getStatistics(){ 
		Set<Contract> contracts = kms.getContracts();
		Map<String,Float> statistics = new HashMap<String, Float>();
		int sum = 0;
		int max = 0;
		int tempPrice = 0;
		
		Iterator<Contract> i = contracts.iterator();
		while(i.hasNext()){
			tempPrice = i.next().getPrice();
			
			//maximum
			if(tempPrice > max){
				max = tempPrice;
			}
			
			//sum
			sum += tempPrice;
		}
		
		float averagePrice = sum/contracts.size();
		statistics.put("maximum",(float)max);
		statistics.put("averagePrice", averagePrice);
		
		int min = max;
		float sumOfSquares = 0;
		float variance = 0;
		double standardDeviation = 0;
		
		Iterator<Contract> k = contracts.iterator();
		while(k.hasNext()){
			tempPrice = i.next().getPrice();
			
			//minimum
			if(tempPrice < min){
				min = tempPrice;
			}
			
			//variance
			sumOfSquares += (tempPrice - averagePrice)*(tempPrice-averagePrice);
		}
		
		variance = sumOfSquares/contracts.size();
		standardDeviation = Math.sqrt(variance);
		statistics.put("minimum", (float) min);
		statistics.put("variance", variance);
		statistics.put("standardDeviation",(float) standardDeviation);
		
		return statistics; 
	}
	
	//choose random "winner"-contract
	@Override
	public Contract pickWinner(){ 
		Set<Contract> contracts = kms.getContracts();
		
		if(contracts == null) throw new NullPointerException();
		
		Random rand = new Random();
		int randomInt = rand.nextInt(contracts.size());
		System.out.println(contracts.size());
		System.out.println(randomInt);
		int i = 0;
		Iterator<Contract> k = contracts.iterator();
		
		while(k.hasNext() && i < randomInt){
			k.next();
			i++;
		}
		
		Contract winner = k.next();
		
		return winner; 
	}
	
	@Override
	public int buyerProfit(Contract con){ 
		int wtp = con.getBuyer().getValue();
		return (wtp - con.getPrice()); 
	}
	
	@Override
	public int sellerProfit(Contract con){ 
		int cost = con.getSeller().getValue();
		return (con.getPrice() - cost); 
	}
	
	
}
