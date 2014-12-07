package jKMS.controller;

import jKMS.Amount;
import jKMS.Contract;
import jKMS.Kartoffelmarktspiel;
import jKMS.states.Evaluation;
import jKMS.states.Load;
import jKMS.states.Play;
import jKMS.states.Preparation;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

public class ControllerHelper {
	
	/*
	 * Responsible for state setting.
	 * Returns true if state setting done or not necessary.
	 * Returns false if requested changing of state requires deletion of Data.
	 * Throws Exception if State-changing is invalid.
	 */
	public static boolean stateHelper(Kartoffelmarktspiel kms, String requestedState) throws IllegalStateException	{
		
		if(kms.getState() instanceof Preparation)	{
			if(requestedState.equals("prepare"))	{
				return true;
			}
			if(requestedState.equals("load"))	{
				kms.load();
				return true;
			}
			if(requestedState.equals("play"))	{
				// TODO internationalize
				throw new IllegalStateException("Statuswechsel von Vorbereitung -> Spiel läuft ist nicht möglich!");
			}
			if(requestedState.equals("evaluate"))	{
				// TODO internationalize
				throw new IllegalStateException("Statuswechsel von Vorbereitung -> Auswertung ist nicht möglich!");
			}
		}
		if(kms.getState() instanceof Load)	{
			if(requestedState.equals("prepare"))	{
				return false;
			}
			if(requestedState.equals("load"))	{
				return true;
			}
			if(requestedState.equals("play"))	{
				kms.play();
				return true;
			}
			if(requestedState.equals("evaluate"))	{
				// TODO internationalize
				throw new IllegalStateException("Statuswechsel von Spiel laden -> Auswertung ist nicht möglich!");
			}
		}
		if(kms.getState() instanceof Play)	{
			if(requestedState.equals("prepare"))	{
				return false;
			}
			if(requestedState.equals("load"))	{
				// TODO discuss validity of that
				return false;
			}
			if(requestedState.equals("play"))	{
				return true;
			}
			if(requestedState.equals("evaluate"))	{
				kms.evaluate();
				return true;
			}
		}
		if(kms.getState() instanceof Evaluation)	{
			if(requestedState.equals("prepare"))	{
				return false;
			}
			if(requestedState.equals("load"))	{
				kms.load();
				return true;
			}
			if(requestedState.equals("play"))	{
				// TODO internationalize
				throw new IllegalStateException("Statuswechsel von Auswertung -> Spiel ist nicht möglich!");
			}
			if(requestedState.equals("evaluate"))	{
				return true;
			}
		}
		
		throw new IllegalStateException("Kein Status.");
		
	}
	
	/*
	 * Returns IP of the User.
	 */
	
	public static String getIP()	{
		// TODO handle multiple IPs if User is connected to multiple Networks
		Enumeration<NetworkInterface> ifaces = null;
		// Pick up all Network Interfaces
		try {
			ifaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		// Go through the Network Interfaces
		while (ifaces.hasMoreElements()) {
			NetworkInterface ni = (NetworkInterface)ifaces.nextElement();
			// Pick up all Addresses of the Network Interface
		    Enumeration<InetAddress> addrs = ni.getInetAddresses();
		    // Go through the Addresses
		    while (addrs.hasMoreElements()) {
		    	InetAddress ia = (InetAddress)addrs.nextElement();
		    	// Only display an Address, which is not localhost and has no ":" in it [Filter for MAC Adresses]
		    	if(!ia.getHostAddress().toString().equals("127.0.0.1") && ia.getHostAddress().toString().indexOf(":") == -1)	{
		    		return ia.getHostAddress().toString();
		        }
		    }
		}
		return null;
	}
	
	/*
	 * Returns Port of the given request.
	 */
	public static int getPort(ServletRequest request)	{
		return request.getServerPort();
	}
	
	
	/*
	 * Gets the set of contracts and converts it to a string for the javascript flot library
	 */
	public static String setToString(Set<Contract> contracts){
		String str = "[";
		int i = 0;
		
		for(Contract c : contracts){
			str = str.concat("["+i+","+c.getPrice()+"],");
			i++;
			if(i == 1){
				str = str.concat("["+i+","+c.getPrice()+"],");
				i++;
			}
		}
		
		str = str.substring(0, str.length()-1).concat("]");
		
		return str;		
	}
	
	/*
	 * Gets a map of distribution and converts it to a string for the javascript flot library
	 */
	public static String mapToString(Map<Integer,Amount>  distribution){
		String str = "[";
		int absolute = 0;
		int lastKey = 0;
		
		for(Map.Entry<Integer, Amount> entry : distribution.entrySet()){
			
			str = str.concat("[" + absolute + "," + entry.getKey() + "],");
			absolute += entry.getValue().getAbsolute();
			
			lastKey = entry.getKey();
		}
		
		str = str.concat("[" + absolute + "," + lastKey + "]]");
		
		return str;
	}
	
}
