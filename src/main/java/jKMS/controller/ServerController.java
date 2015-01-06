package jKMS.controller;

import jKMS.Amount;
import jKMS.LogicHelper;
import jKMS.states.Evaluation;
import jKMS.states.Load;
import jKMS.states.Play;
import jKMS.states.Preparation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.TreeMap;

import javax.servlet.ServletRequest;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import scala.collection.Set;

@Controller
public class ServerController extends AbstractServerController	{

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Model model, ServletRequest request) {
		try {
			ControllerHelper.checkFolders();
		} catch (IOException e) {
			e.printStackTrace();
			model.addAttribute("message", LogicHelper.getLocalizedMessage("error.buildFolderStructure.message"));
			model.addAttribute("error", LogicHelper.getLocalizedMessage("error.buildFolderStructure.error"));
			return "error";
		}
		return "index";
	}
	
	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public String settigs(Model model) throws IOException	{
		// Path to password-config-file
    	String path = ControllerHelper.getApplicationFolder() + "bin/config.txt";
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
		model.addAttribute("users", string);
		return "settings";
	}
	
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/settings", method = RequestMethod.POST)
	public String processSettings(Model model, 
			@RequestParam(value = "users") String username,
			@RequestParam(value = "oldPass") String oldPass,
			@RequestParam(value = "pass1") String pass1,
			@RequestParam(value = "pass2") String pass2) throws IOException	{
		// TODO Error Handling
		// Path to password-config-file
    	String path = ControllerHelper.getApplicationFolder() + "bin/config.txt";
    	FileReader fr = null;
		try {
			fr = new FileReader(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		
    	PasswordEncoder pe = new Md5PasswordEncoder();
		LogicHelper.print("Reading Login data from file.");
		StringBuffer str = new StringBuffer();
     	String ln = System.getProperty("line.separator");
        String line = br.readLine();
        // Every line is a User
		while(line != null)	{
			// Found the right User?
			if(line.substring(0, line.indexOf(":")).equals(username))	{
				if(pe.encodePassword(oldPass, null).equals(line.substring(line.indexOf(":") + 1, line.lastIndexOf(":"))))	{
					if(pass1 .equals(pass2))	{
						str.append(username).append(":")
							.append(pe.encodePassword(pass1, null)).append(":")
							.append(line.substring(line.lastIndexOf(":") + 1));
		    			LogicHelper.print("Username: " + username);
		    			LogicHelper.print("Password: " + pe.encodePassword(pass1, null));
		    			LogicHelper.print("Role: " + line.substring(line.lastIndexOf(":") + 1));
					}
				}
			}	else	{
				str.append(line).append(ln); 
			}
			// next line
			line = br.readLine();
		}
		
		FileOutputStream fos = new FileOutputStream(path);
		// Write to OutputStream
		fos.write(str.toString().getBytes());
		fos.close();
		LogicHelper.print("Wrote auth to config.txt");
		
		
		
		return "settings";
	}
	
	@RequestMapping(value = "/reset", method = RequestMethod.GET)
	public String reset()	{
		return "reset";
	}
	
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public String processReset()	{
		kms.getConfiguration().setPlayerCount(0);
		kms.getConfiguration().setGroupCount(0);
		kms.getConfiguration().setAssistantCount(0);
		kms.getConfiguration().setbDistribution(new TreeMap<Integer, Amount>());
		kms.getConfiguration().setsDistribution(new TreeMap<Integer, Amount>());
		kms.prepare();
		System.out.println("Reseted all data.");
		return "redirect:/index";
	}
	
	@RequestMapping(value = "/autoRedirect", method = RequestMethod.GET)
	public String autoRedirect(){
		if(kms.getState() instanceof Preparation) return "redirect:/prepare1";
		else if(kms.getState() instanceof Load) return "redirect:/load1";
		else if(kms.getState() instanceof Play) return "redirect:/play";
		else if(kms.getState() instanceof Evaluation) return "redirect:/evaluate";
		else return "redirect:/index";
	}
}
