package jKMS;

//import java.io.IOException;
//import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
//import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class LogicHelper {// have static function to help implementation logic

	private static ReloadableResourceBundleMessageSource messageSource;
	
	public static void init(ReloadableResourceBundleMessageSource ms)	{
		messageSource = ms;
	}
	/**
	  * get an Char into int depending on the package it represent
	  *
	  * @param pack	the package
	  * @return 0 for "A",1 for "B" ....
	  *
	*/
	public static int PackageToInt(char pack){ // package to int A = 0  Z = 25 
		 int iPack;
		 iPack = (int)pack-65;
		  
		 if(iPack >= 0 && iPack <= 25) return iPack;
		 else return 42;
	}
	/**
	 *get an int into char depending on the package it represent
	 * 
	 * @param pack 	the package you want to chance
	 * @return 	"A" for 0,"B" for 0 ....
	 * 		
	 */	
	public static char IntToPackage(int pack){ // package to int A = 0  Z = 25 
		 char iPack;
		 pack = pack +65;
		 iPack = (char)pack;
		 
		 if(pack >= 65 && pack <= 90) return iPack;
		 else return '#';
	}
	
	/**
	  *calculate field with all Packages and there sizes index 0 is for the first package...
	  *
	  * @param 	assistantCount for number of package = fieldsize
	  * @param	playerCount to distribute the players over the packages
	  * @return a field with all Packages and there sizes
	  *
	*/
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
	

	/**
	 *sum up all absolute players in one distribution
	 * 
	 * @param distribution	 need a distribution to count
	 * @return 	sum of all  absolute players
	 */

	public static int getAbsoluteSum(Map<Integer, Amount> distribution){
		int sum=0;
			Set<Integer> d = distribution.keySet();
			for(int i : d){
				sum = sum + distribution.get(i).getAbsolute();
				}
		return sum;
		}
	

	/**
	 *sum up all relative players in one distribution
	 * 

	 * @param distribution need a distribution to count
	 * @return sum of all relative players
	 */

	public static int getRelativeSum(Map<Integer, Amount> distribution){
		int sum=0;
			Set<Integer> d = distribution.keySet();
			for(int i : d){
				sum = sum + distribution.get(i).getRelative();
				}
		return sum;
		}
    
	/**
	 * Gets the message from the .properties to the key using Spring methods.
	 * If Spring is not set up properly yet -> messageSource = null [would be set by init()] fallback to old function
	 * 
	 * @param key the key from the .properties file
	 * @return 	localized message if a .properties file is existing for the actual locale
	 */
    public static String getLocalizedMessage(String key)	{
    	if(messageSource != null)
    		return messageSource.getMessage(key, new Object[]{}, LocaleContextHolder.getLocale());
    	else	{
    		key = "begin." + key;
        	try	{
        		// get the right .properties File depending on current language [may throw MissingResourceException]
        		ResourceBundle messages = ResourceBundle.getBundle("lang/messages", LocaleContextHolder.getLocale());
        		// get the String from the .properties [may throw MissingResourceException]
        		String message = messages.getString(key);
            	return message;
        	}	catch(MissingResourceException e)	{
        		try {
    				// get the right .properties File depending on current language [may throw MissingResourceException]
    				ResourceBundle messages = ResourceBundle.getBundle("messages", Locale.ENGLISH);
    				// get the String from the .properties [may throw MissingResourceException]
    				String message = messages.getString(key);
    				return message;
    			} catch (MissingResourceException e1) {
    	    		// Resource/String not found
    	    		return "Language resource not found.";
    			}
        	}
    		
    	}
    }
    
    /**
     * Prints a well-formatted output to console [log] with message message and type
     * 
     * @param message message to be printed
     * @param type 
     *		  0 = INFO<br>
     * 		  1 = WARNING<br>
     * 		  2 = ERROR
     */
    public static void print(String message, int type)	{
    	String[] types = {"INFO   ", "WARNING", "ERROR  "};
		Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss.SSS");
    	System.out.println("jKMS: " + ft.format(dNow) + "  " + types[type] + " -- " + message);
    }
    
    /**
     * Simple print function for printing an INFO
     * 
     * @param message message to be printed
     */
    public static void print(String message)	{
    	print(message, 0);
    }
    
}
