package jKMS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PrepareController extends AbstractServerController {
	@RequestMapping(value = "/prepare1", method = RequestMethod.GET)
	public String prepare1(Model model)	{
		
		boolean stateChangeSuccessful = true;
		
		try	{
			stateChangeSuccessful = ControllerHelper.stateHelper(kms, "prepare");
		}	catch(Exception e)	{
			e.printStackTrace();
			return "error?e=" + e.toString();
		}
		
		if(stateChangeSuccessful)	{
			model.addAttribute("numberOfPlayers", kms.getPlayerCount());
			model.addAttribute("numberOfAssistants", kms.getAssistantCount());
			return "prepare1";
		}	else	{
			return "reset";
		}

	}
	
	@RequestMapping(value = "/prepare1", method = RequestMethod.POST)
	public String processPrepare1(Model model,
		 	@RequestParam(value="players", required = false) int numberOfPlayers, 
	        @RequestParam(value="assistants", required = false) int numberOfAssistants,
	        @RequestParam(value="c", required = false) String configuration)	{
		
		kms.getState().setBasicConfig(numberOfPlayers, numberOfAssistants);
		return "redirect:/prepare2?c=" + configuration;
	}

	@RequestMapping(value = "/prepare2", method = RequestMethod.GET)
	public String prepare2(Model model, @RequestParam(value="c", required = false) String configuration)	{
		
		boolean stateChangeSuccessful = true;
		
		try	{
			stateChangeSuccessful = ControllerHelper.stateHelper(kms, "prepare");
		}	catch(Exception e)	{
			e.printStackTrace();
			return "error?e=" + e.toString();
		}
		
		if(stateChangeSuccessful)	{
		
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
		}	else	{
			return "reset";
		}
	}
	
	//defalt load path:Users/yangxinyu/git/jKMS
	@RequestMapping(value = "/prepare2", method = RequestMethod.POST)
	public String loadConfig(Model model, @RequestParam(value="input-file") String filename,@RequestParam(value="c") String configuration)	{
		//String fileurl = "/Users/yangxinyu/Desktop/"+filename;
		try {
			kms.getState().load(filename);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/prepare2?c=" + configuration;
	}

	@RequestMapping(value = "generate", method = RequestMethod.POST)
	public String generate(	Model model,
							@RequestParam(value = "cRelativeQuantity[]") String[] cRelativeQuantity,
							@RequestParam(value = "cPrice[]") String[] cPrice,
							@RequestParam(value = "cAbsoluteQuantity[]") String[] cAbsoluteQuantity,
							@RequestParam(value = "sRelativeQuantity[]") String[] sRelativeQuantity,
							@RequestParam(value = "sPrice[]") String[] sPrice,
							@RequestParam(value = "sAbsoluteQuantity[]") String[] sAbsoluteQuantity)	{
		
		int i;
		
		kms.getbDistribution().clear();
		kms.getsDistribution().clear();
		
		for(i = 0; i < cRelativeQuantity.length; i++)	{
			kms.getState().newGroup(true, Integer.parseInt(cPrice[i]), Integer.parseInt(cRelativeQuantity[i]), Integer.parseInt(cAbsoluteQuantity[i]));
			kms.getState().newGroup(false, Integer.parseInt(sPrice[i]), Integer.parseInt(sRelativeQuantity[i]), Integer.parseInt(sAbsoluteQuantity[i]));
		}
		
		kms.getConfiguration().setGroupCount(i);
		
		//model.addAttribute("configSavePath", kms.getState().save());
		model.addAttribute("configSavePath", "/test/test2/config.txt");
		
		return "generate";
	}

	
}
