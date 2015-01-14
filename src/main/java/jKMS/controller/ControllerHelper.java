package jKMS.controller;

import jKMS.Amount;
import jKMS.Contract;
import jKMS.Kartoffelmarktspiel;
import jKMS.LogicHelper;
import jKMS.exceptionHelper.CreateFolderFailedException;
import jKMS.exceptionHelper.InvalidStateChangeException;
import jKMS.states.Evaluation;
import jKMS.states.Load;
import jKMS.states.Play;
import jKMS.states.Preparation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletRequest;

/*
 * Some static help functions
 */
public class ControllerHelper extends AbstractController {
	
	private static Map<String, String> folders = new HashMap<>();
	
	public static void init() throws CreateFolderFailedException	{
		folders.put("config", "config");
		folders.put("export", "data");
		folders.put("settings", "settings");
		checkFolders();
	}
	
	/*
	 * gets the name of the Folder folder.
	 */
	public static String getFolderName(String folder)	{
		return folders.get(folder);
	}
	
	/*
	 * gets the absolute Path of the Folder folder.
	 * Example: /media/user/42/KMS/settings/
	 */
	public static String getFolderPath(String folder)	{
		String appFolder = getApplicationFolder();
		String folderName = folders.get(folder);
		return appFolder.concat(folderName).concat(File.separator);
	}
	
	/*
	 * Gets the name of a File (without Extension!) by concatenating the localized message to a timestamp
	 */
	public static String getFilename(String message)	{
		return LogicHelper.getLocalizedMessage(message) + "_" + getNiceDate();
	}
	
	/*
	 * Responsible for state setting.
	 * Returns true if state setting done or not necessary.
	 * Returns false if requested changing of state requires deletion of Data.
	 * Throws Exception if State-changing is invalid or if State is undefined.
	 */
	public static boolean stateHelper(Kartoffelmarktspiel kms, String requestedState) throws InvalidStateChangeException	{
		// We are actual in Preparation
		if(kms.getState() instanceof Preparation)	{
			// We want to switch to prepare
			if(requestedState.equals("prepare"))	{
				return true;
			}
			// We want to switch to load
			if(requestedState.equals("load"))	{
				kms.load();
				return true;
			}
			// We want to switch to play
			if(requestedState.equals("play"))	{
				throw new InvalidStateChangeException("Statechange Preparation -> Play is invalid!");
			}
			// We want to switch to evaluate
			if(requestedState.equals("evaluate"))	{
				throw new InvalidStateChangeException("Statechange Preparation -> Evaluation is invalid!");
			}
		}
		// We are actual in Load
		if(kms.getState() instanceof Load)	{
			// We want to switch to prepare
			if(requestedState.equals("prepare"))	{
				return false;
			}
			// We want to switch to load
			if(requestedState.equals("load"))	{
				return true;
			}
			// We want to switch to play
			if(requestedState.equals("play"))	{
				// Check if configuration is ok at this moment
				if(kms.getAssistantCount() > 0 && kms.getPlayerCount() > 0 
						&& kms.getGroupCount("s") > 0 && kms.getGroupCount("b") > 0
						&& kms.getCards().size() > 0)	{
					kms.play();
				}	else	{
					throw new RuntimeException(LogicHelper.getLocalizedMessage("error.uncompleteConfiguration"));
				}
				return true;
			}
			// We want to switch to evaluate
			if(requestedState.equals("evaluate"))	{
				throw new InvalidStateChangeException("Statechange Load -> Evaluation is invalid!");
			}
		}
		// We are actual in Play
		if(kms.getState() instanceof Play)	{
			// We want to switch to prepare
			if(requestedState.equals("prepare"))	{
				return false;
			}
			// We want to switch to load
			if(requestedState.equals("load"))	{
				return false;
			}
			// We want to switch to play
			if(requestedState.equals("play"))	{
				return true;
			}
			// We want to switch to evaluate
			if(requestedState.equals("evaluate"))	{
				kms.evaluate();
				return true;
			}
		}
		// We are actual in Evaluation
		if(kms.getState() instanceof Evaluation)	{
			// We want to switch to prepare
			if(requestedState.equals("prepare"))	{
				return false;
			}
			// We want to switch to load
			if(requestedState.equals("load"))	{
				kms.load();
				return true;
			}
			// We want to switch to play
			if(requestedState.equals("play"))	{
				throw new InvalidStateChangeException("Statechange of Evaluation -> Play is invalid!");
			}
			// We want to switch to evaluate
			if(requestedState.equals("evaluate"))	{
				return true;
			}
		}
		// Nothing done?
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
				LogicHelper.print("Was unable to access Socket for getting the IP.");
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
		boolean isWin;
		// Check if we have Windows OS
		if(File.separator.equals("\\"))	{
			// We need to replace every "/" with a "\"
			path = path.replace("/", File.separator);
			isWin = true;
		}	else	{
			isWin = false;
		}
		
		// remove "file:" part of the URL if existing
		if(path.substring(0, 5).equals("file:"))	{
			path = path.substring(path.indexOf(File.separator));
		}
		
		if(isWin && path.substring(0, 1) == File.separator)
			path = path.substring(1);
		
		// Go to parent Folder
		String folderPath = null;
		try {
			folderPath = URLDecoder.decode(path.substring(0, path.lastIndexOf(File.separator, path.length() - 2) + 1), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return folderPath;
	}
	
	/*
	 * Creates folders from folders Map if they aren't existing.
	 */
	public static boolean createFolders() throws CreateFolderFailedException	{

		String path = getApplicationFolder();
		boolean fine = true;
		
		// Get all folders to check
		for(String folder : folders.keySet())	{
			File tempFolder = new File(path + getFolderName(folder));
			// Check if existing
			if(!tempFolder.exists())	{
				// Build directory
				if(tempFolder.mkdir())	{
					LogicHelper.print("Created Folder: " + tempFolder.getAbsolutePath());
				}	else	{
					throw new CreateFolderFailedException("Failed to generate folder \"" + tempFolder.getAbsolutePath() + "\".");
				}
			}
			// Set variable if erverything worked
			fine = fine && tempFolder.exists();
		}
		return fine;
	}
	
	/*
	 * Checks and, if not yet existing, creates the Folder Structure
	 * Returns true if something was created, false if not.
	 */
	public static boolean checkFolders() throws CreateFolderFailedException	{
		
		String path = getApplicationFolder();
		boolean fine = true;
		
		for(String folder : folders.keySet())	{
			File tempFolder = new File(path + getFolderName(folder));
			if(!(fine = fine && tempFolder.exists()))
				break;
		}
		
		if(!fine)	{
			// Some directories are missing
			if(createFolders())	{
				// Add succeed
				LogicHelper.print("Adding folders succeeded.");
				return true;
			}	else	{
				// Add fail
				LogicHelper.print("Adding folders failed. Please try again by re-installing KMS", 2);
				return false;
			}
		}	else	{
			// Everything was already fine
			LogicHelper.print("Folder structure ok.");
			return true;
		}
		
	}
	
	/*
	 * Returns all the users written in settings file.
	 */
	public static Set<String> getUsers() throws IOException	{
		// Path to password-config-file
		String path = ControllerHelper.getFolderPath("settings") + LogicHelper.getLocalizedMessage("filename.settings") + ".txt";
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(path));
				
		String line = br.readLine();
		// Every line is a User
		HashSet<String> string = new HashSet<String>();
		while(line != null)	{
			string.add(line.substring(0, line.indexOf(":")));
			line = br.readLine();
		}
		br.close();
		return string;
	}
	
	/*
	 * Returns a well-formatted timestamp for adding it to filenames.
	 */
	public static String getNiceDate()	{
		Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd_HHmmss");
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
}
