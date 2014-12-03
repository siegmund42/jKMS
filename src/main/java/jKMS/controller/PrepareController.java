package jKMS.controller;

import jKMS.Amount;

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
		 	@RequestParam(value="players", required = true) String numberOfPlayers, 
	        @RequestParam(value="assistants", required = true) String numberOfAssistants,
	        @RequestParam(value="c", required = false) String configuration)	{
		
		if(numberOfPlayers != "" && numberOfAssistants != "")	{
			int players = Integer.parseInt(numberOfPlayers);
			int assistants = Integer.parseInt(numberOfAssistants);
			
			if(players > 0 && players <= 8999 && players % 2 == 0 && 
					assistants > 0 && assistants <= 26 && assistants % 1 == 0)	{
				kms.getState().setBasicConfig(players, assistants);
				return "redirect:/prepare2?c=" + configuration;
			}
			
		}
		model.addAttribute("numberOfPlayers", numberOfPlayers);
		model.addAttribute("numberOfAssistants", numberOfAssistants);
		model.addAttribute("error", "config");
		return "prepare1";
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

		model.addAttribute("customerConfiguration", kms.getbDistribution());
		model.addAttribute("salesmanConfiguration", kms.getsDistribution());
		model.addAttribute("groupQuantity", kms.getGroupCount());
		
		return "redirect:/prepare2";
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
		boolean error = false;
		
		// Delete current Maps so they can be completely rewritten.
		kms.getbDistribution().clear();
		kms.getsDistribution().clear();
		
		// Iterate through lines and register 2 Groups (one for Buyer, one for Seller) per Line.
		for(i = 0; i < cRelativeQuantity.length; i++)	{
			
			if(cPrice[i] != "" && cRelativeQuantity[i] != "" && cAbsoluteQuantity[i] != "" &&
					sPrice[i] != "" && sRelativeQuantity[i] != "" && sAbsoluteQuantity[i] != "")	{
			
				int cP = Integer.parseInt(cPrice[i]);
				int cR = Integer.parseInt(cRelativeQuantity[i]);
				int cA = Integer.parseInt(cAbsoluteQuantity[i]);
				int sP = Integer.parseInt(sPrice[i]);
				int sR = Integer.parseInt(sRelativeQuantity[i]);
				int sA = Integer.parseInt(sAbsoluteQuantity[i]);
				
				if(	cP > 0 && cP % 1 == 0 && 
						cR > 0 && cR <= 100 && cR % 1 == 0 &&
						cA > 0 && cA <= kms.getPlayerCount() && cA % 1 == 0 &&
						sP > 0 && sP % 1 == 0 && 
						sR > 0 && sR <= 100 && sR % 1 == 0 &&
						sA > 0 && sA <= kms.getPlayerCount() && sA % 1 == 0)	{
					
					kms.getState().newGroup(true, cP, cR, cA);
					kms.getState().newGroup(false, sP, sR, sA);
					
				}	else	{
					error = true;
					break;
				}
				
			}	else	{
				error = true;
				break;
			}
		}
		
		if(!error)	{
			// Set Group Count [hidden field].
			kms.getConfiguration().setGroupCount(i);
			
			//model.addAttribute("configSavePath", kms.getState().save());
			model.addAttribute("configSavePath", "/test/test2/config.txt");
			
			return "generate";
			
		}	else	{
			// Build a new Map for giving it to the model to display the mistakes stupid non-javascript using User made.
			Map<Integer, Amount> cConf = new HashMap<>();
			Map<Integer, Amount> sConf = new HashMap<>();
			for(int a = 0; a <= cRelativeQuantity.length; a++)	{
				cConf.put(Integer.parseInt(cPrice[a]), new Amount(Integer.parseInt(cRelativeQuantity[a]), Integer.parseInt(cAbsoluteQuantity[a])));
				sConf.put(Integer.parseInt(sPrice[a]), new Amount(Integer.parseInt(sRelativeQuantity[a]), Integer.parseInt(sAbsoluteQuantity[a])));
			}
			model.addAttribute("customerConfiguration", cConf);
			model.addAttribute("customerConfiguration", sConf);
			model.addAttribute("error", "distribution");
			return "prepare2";
			
		}
	}

	
}
