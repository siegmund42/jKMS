package jKMS.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PrepareController extends AbstractServerController {
	@RequestMapping(value = "/prepare1", method = RequestMethod.GET)
	public String prepare1(Model model)	{

		model.addAttribute("numberOfPlayers", kms.getPlayerCount());
		model.addAttribute("numberOfAssistants", kms.getAssistantCount());

		return "prepare1";
	}
	
	@RequestMapping(value = "/prepare1", method = RequestMethod.POST)
	public String processPrepare1(Model model,
		 	@RequestParam(value="players", required=false) int numberOfPlayers, 
	        @RequestParam(value="assistants", required=false) int numberOfAssistants,
	        @RequestParam(value="c", required=false) String configuration)	{
		
		kms.getState().setBasicConfig(numberOfPlayers, numberOfAssistants);
		return "redirect:/prepare2?c=" + configuration;
	}

	@RequestMapping(value = "/prepare2", method = RequestMethod.GET)
	public String prepare2(Model model, @RequestParam(value="c", required=false) String configuration) throws NumberFormatException, IOException	{
		//load Implementieren
		if(configuration != null && configuration.equals("load"))	{
			//TODO: load from file
			
			model.addAttribute("isStandard", true);
			model.addAttribute("customerConfiguration", kms.getbDistribution());
			model.addAttribute("salesmanConfiguration", kms.getsDistribution());
			model.addAttribute("groupQuantity", kms.getGroupCount());
		}
		
		
		if(configuration == null)	{
			 System.out.println("configuration == null");
			configuration = "load";
		}
		
		if(configuration.equals("load") || configuration.equals("standard"))	{
			if(configuration.equals("standard"))	{
				kms.getState().loadStandardDistribution();
				model.addAttribute("isStandard", true);
			}
			model.addAttribute("customerConfiguration", kms.getbDistribution());
			model.addAttribute("salesmanConfiguration", kms.getsDistribution());
			model.addAttribute("groupQuantity", kms.getGroupCount());
		}	else	{
			
			// create own configuration - Fields empty
			kms.getbDistribution().clear();
			kms.getsDistribution().clear();
			kms.getConfiguration().setGroupCount(0);
			model.addAttribute("addEmptyRows", 1);
			model.addAttribute("groupQuantity", 0);
		}
		
		model.addAttribute("numberOfPlayers", kms.getPlayerCount());

		return "prepare2";
	}
	
	//defalt load path:Users/yangxinyu/git/jKMS
	@RequestMapping(value = "/prepare2", method = RequestMethod.POST)
	public String processPrepare1(Model model, @RequestParam(value="input-file") String filename,@RequestParam(value="c") String configuration) throws NumberFormatException, FileNotFoundException, IOException	{
		//String fileurl = "/Users/yangxinyu/Desktop/"+filename;
		kms.getState().load(filename);
		return "redirect:/prepare2?c=" + configuration;
	}

	@RequestMapping(value = "generate", method = RequestMethod.POST)
	public String generate()	{
		return "generate";
	}

	
}
