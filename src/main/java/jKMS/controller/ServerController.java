package jKMS.controller;

import jKMS.Amount;

import java.util.TreeMap;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ServerController extends AbstractServerController	{

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ServletRequest request) {
		return "index";
	}
	
	@RequestMapping(value = "/index", method = RequestMethod.POST)
	public String processIndex(Model model, @RequestParam("input-file") MultipartFile file)	{
		
		boolean stateChangeSuccessful = true;
		
		try	{
			stateChangeSuccessful = ControllerHelper.stateHelper(kms, "load");
		}	catch(Exception e)	{
			e.printStackTrace();
			model.addAttribute("message", e.getMessage());
			model.addAttribute("error", e.getClass().toString());
			return "error";
		}
		
		if(stateChangeSuccessful)	{
				
			try {
				kms.getState().load(file);
				System.out.println("Load successfull!");
				
			} 	catch(NumberFormatException e){
				e.printStackTrace();
				// TODO i18n
				model.addAttribute("message", "Bitte die load file nicht verändern,die Nummer kann nicht String sein");
				model.addAttribute("error", e.getClass().toString());
				return "error";
				
			}	catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("message", e.getMessage());
				model.addAttribute("error", e.getClass().toString());
				return "error";
			}
			return "redirect:/load?s=1";
			
		}	else	{
			
			return "reset";
		}
		
	}
	
	@RequestMapping("/settings")
	public String settigs()	{
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
}
