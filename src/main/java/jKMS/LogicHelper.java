package jKMS;

import java.util.Set;

public class LogicHelper {// have static function to help implementation logic
	
	static int PackageToInt(char pack){ // package to int A = 0  Z = 25 
		
		switch(pack){
		case 'A':
			return 0;
		case 'B':
			return 1;
		case 'C':
			return 2;
		case 'D':
			return 3;
		case 'E':
			return 4;
		case 'F':
			return 5;
		case 'G':
			return 6;
		case 'H':
			return 7;
		case 'I':
			return 8;
		case 'J':
			return 9;
		case 'K':
			return 10;
		case 'L':
			return 11;
		case 'M':
			return 12;
		case 'N':
			return 13;
		case 'O':
			return 14;
		case 'P':
			return 15;
		case 'Q':
			return 16;
		case 'R':
			return 17;
		case 'S':
			return 18;
		case 'T':
			return 19;
		case 'U':
			return 20;
		case 'V':
			return 21;
		case 'W':
			return 22;
		case 'x':
			return 23;
		case 'Y':
			return 24;
		case 'Z':
			return 25;
		}
		
	return(42);
	}
	
	static Set getPackageDistribution(int playercount,int assistentCount){ //TODO
		return null;
	}
}
