package jKMS.controller;

import jKMS.Amount;
import jKMS.Contract;
import jKMS.Kartoffelmarktspiel;
import jKMS.LogicHelper;
import jKMS.exceptionHelper.InvalidStateChangeException;
import jKMS.states.Evaluation;
import jKMS.states.Load;
import jKMS.states.Play;
import jKMS.states.Preparation;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletRequest;

public class ControllerHelper extends AbstractController {
	
	private static String configFolder = "KMS_Konfigurationen";
	private static String exportFolder = "KMS_Exports";
	
	public static String getConfigFolderName()	{
		return configFolder;
	}
	
	public static String getExportFolderName()	{
		return exportFolder;
	}
	
	/*
	 * Responsible for state setting.
	 * Returns true if state setting done or not necessary.
	 * Returns false if requested changing of state requires deletion of Data.
	 * Throws Exception if State-changing is invalid or if State is undefined.
	 */
	public static boolean stateHelper(Kartoffelmarktspiel kms, String requestedState) throws InvalidStateChangeException	{
		
		if(kms.getState() instanceof Preparation)	{
			if(requestedState.equals("prepare"))	{
				return true;
			}
			if(requestedState.equals("load"))	{
				kms.load();
				return true;
			}
			if(requestedState.equals("play"))	{
				throw new InvalidStateChangeException("Statechange Preparation -> Play is invalid!");
			}
			if(requestedState.equals("evaluate"))	{
				throw new InvalidStateChangeException("Statechange Preparation -> Evaluation is invalid!");
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
				if(kms.getAssistantCount() > 0 && kms.getPlayerCount() > 0 && kms.getGroupCount() > 0 
						&& kms.getbDistribution().size() == kms.getGroupCount() 
						&& kms.getsDistribution().size() == kms.getGroupCount()
						&& kms.getCards().size() > 0)	{
					kms.play();
				}	else	{
					throw new RuntimeException(LogicHelper.getLocalizedMessage("error.uncompleteConfiguration"));
				}
				return true;
			}
			if(requestedState.equals("evaluate"))	{
				throw new InvalidStateChangeException("Statechange Load -> Evaluation is invalid!");
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
				throw new InvalidStateChangeException("Statechange of Evaluation -> Play is invalid!");
			}
			if(requestedState.equals("evaluate"))	{
				return true;
			}
		}
		
		throw new IllegalStateException("The game has no or an invalid State.");
		
	}
	
	/*
	 * Returns IPs of the User in a List of Strings.
	 */
	public static List<String> getIP()	{
		
		List<String> IPs = new LinkedList<>();
		
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
		    try {
				if(ni.isUp() && !ni.isVirtual())	{
					// Pick up all Addresses of the Network Interface
				    Enumeration<InetAddress> addrs = ni.getInetAddresses();
					// Go through the Addresses
					while (addrs.hasMoreElements()) {
						InetAddress ia = (InetAddress)addrs.nextElement();
						// Only display an Address, which is not localhost and has no ":" in it [Filter for MAC Adresses]
						if(!ia.getHostAddress().toString().equals("127.0.0.1") && ia.getHostAddress().toString().indexOf(":") == -1)	{
							IPs.add(ia.getHostAddress().toString());
					    }
					}
				}
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return IPs;
	}
	
	/*
	 * Returns Port of the given request.
	 */
	public static int getPort(ServletRequest request)	{
		return request.getServerPort();
	}
	
	/*
	 * Returns the path to the Application Folder, which holds the jar
	 * Example: /media/user/jKMS/
	 */
	public static String getApplicationFolder()	{
		URL url = AbstractController.class.getProtectionDomain().getCodeSource().getLocation();
		String path = url.getPath();
		// remove "file:" part of the URL if existing
		if(path.substring(0, 4).equals("file:"))
			path = path.substring(path.indexOf(File.separator));
		// 
		String folderPath = path.substring(0, path.lastIndexOf(File.separator, path.length() - 2) + 1);
		System.out.println(path);
		System.out.println(folderPath);
		return folderPath;
	}
	
	/*
	 * Checks and, if not yet existing, creates the Folder Structure
	 * Returns true if something was created, false if not.
	 */
	public static boolean checkFolders() throws IOException	{

		String path = getApplicationFolder();
		System.out.println(path);
		File games = new File(path + configFolder);
		File exports = new File(path + exportFolder);
		Boolean created = false;
		
		if(!games.exists())	{
			if(games.mkdir())	{
				System.out.println("Created Folder: " + games.getAbsolutePath());
				created = true;
			}	else	{
				throw new IOException("Failed to generate folder \"" + games.getAbsolutePath() + "\".");
			}
		}
		
		if(!exports.exists())	{
			if(exports.mkdir())	{
				System.out.println("Created Folder: " + exports.getAbsolutePath());
				created = true;
			}	else	{
				throw new IOException("Failed to generate folder \"" + exports.getAbsolutePath() + "\".");
			}
		}
		return created;
	}
	
	public static String getNiceDate()	{
		Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMddHHmmss");
	    return ft.format(dNow);
	}
	
	/*
	 * Gets the set of contracts and converts it to a string for the javascript flot library
	 */
	public static String setToString(Set<Contract> contracts){
		if(contracts.isEmpty()) return "[]";
		
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
	
	/*
	 * gets the minimum and maximum values of the distributions and compares it to the min and max of the contracts set. 
	 * With these values we can limit the chart on 20% difference to the highest and lowest possible value, if a contract price is much to high or much to low .
	 */
	public static int[] getMinMax(Set<Contract> contracts, TreeMap<Integer,Amount> sDistribution, TreeMap<Integer,Amount> bDistribution) {
		int smin = sDistribution.firstKey();
		int smax = sDistribution.lastKey();
		int bmin = bDistribution.firstKey();
		int bmax = bDistribution.lastKey();
		int min, max, tempPrice;
		
		//get the min and max values (+20%) of the distributions
		if(smin < bmin){
			min = smin - smin/5;
		}else {
			min = bmin - bmin/5;
		}
		
		if(smax > bmax) {
			max = smax + smax/5;
		}else{
			max = bmax + bmax/5;
		}
		
		//if there are no contracts yet, we will limit the y-axis scale. otherwise there is a weird scale with negatives...
		if(contracts.isEmpty()){
			int[] result = {min,max};
			return result;
		}
		
		//get the min and max prices of the contracts
		int maxPrice = 0;
		int minPrice = max;
		Iterator<Contract> i = contracts.iterator();
		while(i.hasNext()){
			tempPrice = i.next().getPrice();
			
			if(tempPrice < minPrice){
				minPrice = tempPrice;
			}
			if(tempPrice > maxPrice){
				maxPrice = tempPrice;
			}
		}
		
		//cases in which we don't have to limit the chart with the distribution values
		if(maxPrice < max){
			max = 0;
		}
		if(minPrice > min){
			min = 0;
		}
		
		int[] result = {min,max};
		
		return result;
	}
	
//	public static boolean openBrowser() throws MalformedURLException, IOException, URISyntaxException	{
//		if(kms.getState() instanceof Preparation){
////			BareBonesBrowserLaunch.openURL("http://localhost:8080/index");
//			Desktop.getDesktop().browse(new URL("http://localhost:8080/index").toURI());
//		}
//		else if(kms.getState() instanceof Load){
////			BareBonesBrowserLaunch.openURL("http://localhost:8080/load?s=1");
//			Desktop.getDesktop().browse(new URL("http://localhost:8080/load1").toURI());
//		}
//		else if(kms.getState() instanceof Play){
////			BareBonesBrowserLaunch.openURL("http://localhost:8080/play");
//			Desktop.getDesktop().browse(new URL("http://localhost:8080/play").toURI());
//		}
//		else if(kms.getState() instanceof Evaluation){
////			BareBonesBrowserLaunch.openURL("http://localhost:8080/evaluate");
//			Desktop.getDesktop().browse(new URL("http://localhost:8080/evaluate").toURI());
//		}
//		return true;
//	}
	
}
