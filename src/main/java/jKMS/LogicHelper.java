package jKMS;

import java.util.Map;
import java.util.Set;




public class LogicHelper {// have static function to help implementation logic
	
	public static int PackageToInt(char pack){ // package to int A = 0  Z = 25 
		 int iPack;
		 iPack = (int)pack-65;
		  
		 if(iPack >= 0 && iPack <= 25) return iPack;
		 else return 42;
	}
	
	public static char IntToPackage(int pack){ // package to int A = 0  Z = 25 
		 char iPack;
		 pack = pack +65;
		 iPack = (char)pack;
		 
		 if(pack >= 65 && pack <= 90) return iPack;
		 else return '#';
	}
	
	public static int[] getPackageDistribution(int playerCount,int assistantCount){ //from 0 to assitentCount-1
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
	
	//sum up all absolute players in one distribution
	public static int getAbsoluteSum(Map<Integer, Amount> distribution){
		int sum=0;
			Set<Integer> d = distribution.keySet();
			for(int i : d){
				sum = sum + distribution.get(i).getAbsolute();
				}
		return sum;
		}
	
	//sum up all relative players in one distribution
	public static int getRelativeSum(Map<Integer, Amount> distribution){
		int sum=0;
			Set<Integer> d = distribution.keySet();
			for(int i : d){
				sum = sum + distribution.get(i).getRelative();
				}
		return sum;
		}
}
