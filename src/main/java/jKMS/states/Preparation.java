package jKMS.states;

import jKMS.Amount;
import jKMS.Kartoffelmarktspiel;
import jKMS.cards.BuyerCard;
import jKMS.cards.SellerCard;
import jKMS.LogicHelper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Preparation extends State{
	public Preparation(Kartoffelmarktspiel kms){
		this.kms = kms;
	}
	
	//	Loads StandardConfiguration into kms.
	//	This method only loads the relative values for displaying in Web Interface.
	//	Absolute Values are calculated by Javascript and stored using the newGroup-Method.
	public void loadStandardDistribution(){

		// Load Buyer Distribution
		Map<Integer, Amount> bDistribution = new TreeMap<>();
		bDistribution.put(70, new Amount(20, 0));
		bDistribution.put(65, new Amount(16, 0));
		bDistribution.put(60, new Amount(16, 0));
		bDistribution.put(55, new Amount(16, 0));
		bDistribution.put(50, new Amount(16, 0));
		bDistribution.put(45, new Amount(16, 0));
		kms.getConfiguration().setbDistribution(bDistribution);
		
		// Load Seller Distribution
		Map<Integer, Amount> sDistribution = new TreeMap<>();
		sDistribution.put(63, new Amount(10, 0));
		sDistribution.put(58, new Amount(18, 0));
		sDistribution.put(53, new Amount(18, 0));
		sDistribution.put(48, new Amount(18, 0));
		sDistribution.put(43, new Amount(18, 0));
		sDistribution.put(38, new Amount(18, 0));
		kms.getConfiguration().setsDistribution(sDistribution);
		
		// Set Amount of Groups
		kms.getConfiguration().setGroupCount(6);
		
	}
	
	
	//setBasicConfig
	//setter method for the number of players and assistants
	public void setBasicConfig(int playerCount, int assistantCount){
		kms.getConfiguration().setPlayerCount(playerCount);
		kms.getConfiguration().setAssistantCount(assistantCount);
	}
	
	
	//load Implementieren
	
		public void load(String fileurl) throws NumberFormatException, IOException{
			 BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileurl)));
			 String buf = "";
			 Map<Integer, Amount> bDistributionLoad = new TreeMap<>();
			 Map<Integer, Amount> sDistributionLoad = new TreeMap<>();
			 int count = 0;
			 while ((buf=br.readLine()) != null) {
				 	buf=buf.trim();
		            String[] sa = buf.split(":|\\s");
		            int bpreis = Integer.valueOf(sa[0]);
		            Amount bAmount =  new Amount(Integer.valueOf(sa[1]),Integer.valueOf(sa[2]));
		           // int banteil = Integer.valueOf(sa[1]);
		            int spreis = Integer.valueOf(sa[3]);
		            Amount sAmount = new Amount(Integer.valueOf(sa[4]),Integer.valueOf(sa[5]));
		            //int santeil = Integer.valueOf(sa[3]);
		            
		            bDistributionLoad.put(bpreis, bAmount);
		            sDistributionLoad.put(spreis, sAmount);
		            count = count + 1;
		     }
			 kms.getConfiguration().setbDistribution(bDistributionLoad);
			 kms.getConfiguration().setsDistribution(sDistributionLoad);
			 kms.getConfiguration().setGroupCount(count);
		}
		
		//defalt path:Users/yangxinyu/git/jKMS
		public boolean save(String path){
			 Map<Integer, Amount> bDistributionSave = new TreeMap<>();
			 Map<Integer, Amount> sDistributionSave = new TreeMap<>();
			 bDistributionSave = kms.getConfiguration().getbDistribution();
			 sDistributionSave = kms.getConfiguration().getsDistribution();
			 if(bDistributionSave.isEmpty() || sDistributionSave.isEmpty())
				 return false;
			 else{
				 try {
					   String line = System.getProperty("line.separator");
					   StringBuffer str = new StringBuffer();
					   FileWriter fw = new FileWriter(path, true);
					   Set bSet = bDistributionSave.entrySet();
					   Set sSet = sDistributionSave.entrySet();
					   Iterator bIter = bSet.iterator();
					   Iterator sIter = sSet.iterator();
					   while(bIter.hasNext() && sIter.hasNext()){
						   Map.Entry bEntry = (Map.Entry)bIter.next(); 
						   Map.Entry sEntry = (Map.Entry)sIter.next(); 
					    
						   str.append(bEntry.getKey()+":"+((Amount) bEntry.getValue()).getRelative()+":"+((Amount) bEntry.getValue()).getAbsolute()+
								   " "+sEntry.getKey()+":"+((Amount)sEntry.getValue()).getRelative()+":"+((Amount)sEntry.getValue()).getAbsolute()).append(line);
					   }
					   fw.write(str.toString());
					   fw.close();
					  } catch (IOException e) {
					   // TODO Auto-generated catch block
					   e.printStackTrace();
					  }
				 return true;
			 }
		}

	
	// generateCardSet
	// Generate an ordered, random Set of Cards using
	// bDistribution and sDistribution
	
	public void generateCards() {
		// DECLARATION
		
		//for put seller and buyer distribution
		Random random = new Random();
		
		List<Integer> bKeys = new ArrayList<Integer>(kms.getConfiguration().getbDistribution().keySet());
		List<Integer> sKeys = new ArrayList<Integer>(kms.getConfiguration().getsDistribution().keySet());
		Map<Integer, Amount> bTemp = new TreeMap<Integer, Amount>(kms.getConfiguration().getbDistribution());
		Map<Integer, Amount> sTemp = new TreeMap<Integer, Amount>(kms.getConfiguration().getsDistribution());
		
		int id = kms.getConfiguration().getFirstID();
		int randomKey, randomListEntry;
		
		//for put packages
		int i,ide;
		int[] packdistribution =LogicHelper.getPackageDistribution(kms.getPlayerCount(),kms.getAssistantCount());
		
		//clear Card Set
		kms.getCards().clear();

		// IMPLEMENTATION
		
		//put seller and buyer distribution and put packages
		i =0;
		ide = kms.getConfiguration().getFirstID() + packdistribution[0];
		
		while (!bTemp.isEmpty() && !sTemp.isEmpty()) {
			// Create Buyer Card
			randomListEntry = random.nextInt(bKeys.size());
			randomKey = bKeys.get(randomListEntry);
			
			if(id < ide){
				kms.getCards().add(new BuyerCard(id, randomKey, LogicHelper.IntToPackage(i)));
			}else {
				if(id < kms.getConfiguration().getFirstID() + kms.getPlayerCount()){
					i++;
					ide = ide + packdistribution[i];
					kms.getCards().add(new BuyerCard(id, randomKey, LogicHelper.IntToPackage(i)));
				}
			}


		
			bTemp.put(randomKey, new Amount(bTemp.get(randomKey).getRelative(), bTemp.get(randomKey).getAbsolute() - 1)); 
			if (bTemp.get(randomKey).getAbsolute() == 0) {
				bTemp.remove(randomKey);
				bKeys.remove(randomListEntry);
			}

			id++;

			// Create Seller Card
			randomListEntry = random.nextInt(sKeys.size());
			randomKey = sKeys.get(randomListEntry);

			if(id < ide){
				kms.getCards().add(new SellerCard(id, randomKey, LogicHelper.IntToPackage(i)));
			}else {
				if(id < kms.getConfiguration().getFirstID() + kms.getPlayerCount()){
						i++;
					ide = ide + packdistribution[i];
					kms.getCards().add(new SellerCard(id, randomKey, LogicHelper.IntToPackage(i)));
			    }
			}

			sTemp.put(randomKey, new Amount(sTemp.get(randomKey).getRelative(), sTemp.get(randomKey).getAbsolute() - 1));
			if (sTemp.get(randomKey).getAbsolute() == 0) {
				sTemp.remove(randomKey);
				sKeys.remove(randomListEntry);
			}

			id++;
		}
		
		
	}			

	// newGroup
	// Creates a new entry for the bDistribution or sDistribution Map,
	// depending if isBuyer is true or false.
	public void newGroup(boolean isBuyer, int price, int relativeNumber, int absoluteNumber) {
		Map<Integer, Amount> distrib;
		
		if (isBuyer) distrib = kms.getConfiguration().getbDistribution();
		else distrib = kms.getConfiguration().getsDistribution();
		
		if(!distrib.containsKey(price))
			distrib.put(price,  new Amount(relativeNumber, absoluteNumber));
		else 
			distrib.get(price).setAbsolute(distrib.get(price).getAbsolute() + 1);
	}
}
