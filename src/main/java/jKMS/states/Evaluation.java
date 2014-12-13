package jKMS.states;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import jKMS.Amount;

import java.util.TreeMap;

import au.com.bytecode.opencsv.CSVWriter;
import jKMS.Amount;
import jKMS.Contract;
import jKMS.Csv;
import jKMS.Kartoffelmarktspiel;
import jKMS.exceptionHelper.NoContractsException;

public class Evaluation extends State{
	
	public Evaluation(Kartoffelmarktspiel kms){
		this.kms = kms;
	}
	private Contract winner = null;
	
	//returns statistic data of all contracts - min, max, average, variance, standard deviation
	@Override
	public Map<String,Float> getStatistics() throws NoContractsException{ 
		Set<Contract> contracts = kms.getContracts();
		Map<String,Float> statistics = new HashMap<String, Float>();
		int sum = 0;
		int max = 0;
		int tempPrice = 0;
		
		if(contracts.size() == 0) throw new NoContractsException();
		
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
			tempPrice = k.next().getPrice();
			
			//minimum
			if(tempPrice < min){
				min = tempPrice;
			}
			
			//variance
			sumOfSquares += (tempPrice - averagePrice)*(tempPrice-averagePrice);
		}
		
		variance = sumOfSquares/contracts.size();
		variance = Math.round(variance*100)/100;
		standardDeviation = Math.sqrt(variance);
		standardDeviation = Math.round(standardDeviation*100)/100;
		statistics.put("minimum", (float) min);
		statistics.put("variance", variance);
		statistics.put("standardDeviation",(float) standardDeviation);
		
		float[] equilibrium = getEquilibrium();
		if(equilibrium  == null) throw new NullPointerException("Obviously there is no intersection of the two lines.");
		statistics.put("eqPrice",equilibrium[0]);
		statistics.put("eqQuantity",equilibrium[1]);
		return statistics; 
	}
	
	/*
	 * calculate equilibrium price and equilibrium quantity
	 */
	//TODO think about a good algorithm
	public float[] getEquilibrium(){
		TreeMap<Integer,Amount> sDistr = (TreeMap<Integer, Amount>) kms.getsDistribution();
		TreeMap<Integer,Amount> bDistr = (TreeMap<Integer, Amount>) kms.getbDistribution();
		int bCount = 0, sCount = 0;
		int sPrice = 0;
		int bPrice = 0;
		Map.Entry<Integer, Amount> sEntry = sDistr.firstEntry();
		Map.Entry<Integer, Amount> bEntry = bDistr.lastEntry();
		
		while(bDistr.lowerEntry(bEntry.getKey()) != null && sDistr.higherEntry(sEntry.getKey()) != null){
			bCount += bEntry.getValue().getAbsolute();
			bEntry = bDistr.lowerEntry(bEntry.getKey());
			bPrice = bEntry.getKey();
			
			sCount += sEntry.getValue().getAbsolute();
			sEntry = sDistr.higherEntry(sEntry.getKey());
			sPrice = sEntry.getKey();
			
					
			if(bPrice < sPrice){
				if(bCount < sCount){
					float[] result = {bPrice, sCount};
					return result;
				}else{
					float[] result = {sPrice, bCount};
					return result;
				}
			}
		}
		
		return null;
	}
	
	//choose random "winner"-contract
	@Override
	public Contract pickWinner() throws NoContractsException{ 
		if(winner == null){
			Set<Contract> contracts = kms.getContracts();
			
			if(contracts == null) throw new NoContractsException();
			
			Random rand = new Random();
			int randomInt = rand.nextInt(contracts.size());
			int i = 0;
			Iterator<Contract> k = contracts.iterator();
			
			while(k.hasNext() && i < randomInt){
				k.next();
				i++;
			}
			
			winner = k.next();
			
		}
		
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
	

	@Override
	public void generateCSV(CSVWriter writer){
		Csv csv = new Csv();
		
		csv.generateCSV(writer, kms.getCards(), kms.getContracts());
		
	}
	
}
