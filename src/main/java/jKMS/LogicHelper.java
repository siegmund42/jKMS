package jKMS;

import java.util.Iterator;
import java.util.LinkedList;


public class LogicHelper {// have static function to help implementation logic
	
	static int PackageToInt(char pack){ // package to int A = 0  Z = 25 
		 int iPack;
		 iPack = (int)pack-65;
		  
		 if(iPack >= 0 && iPack <= 25) return iPack;
		 else return 42;
	}
	
	static int IntToPackage(int pack){ // package to int A = 0  Z = 25 
		 char iPack;
		 pack = pack +65;
		 iPack = (char)pack;
		 
		 if(pack >= 65 && pack <= 90) return iPack;
		 else return 42;
	}
	
	static int[] getPackageDistribution(int playerCount,int assistantCount){ //form 0 to assitentCount-1
		int[] packd = new int[assistantCount];
		int rest,normalSize;
		rest = playerCount % assistantCount;
		normalSize = playerCount / assistantCount;
		
		for(int i=0; i < assistantCount;i++){
			packd[i]=normalSize;
		}
			
		while(rest > 0){
			for(int i = 0; i < assistantCount;i++){
				if(rest > 0){
					rest--;
					packd[i]++;
				}else break;
			}
	
		}
		
		return packd;
	}
}
