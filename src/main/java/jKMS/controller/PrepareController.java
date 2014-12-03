package jKMS.controller;

import jKMS.Amount;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PrepareController extends AbstractServerController {
	
	@Autowired
	ServletContext servletContext;
	
	// Get Requests to first Site of Preparation - Metadata-Input
	@RequestMapping(value = "/prepare1", method = RequestMethod.GET)
	public String prepare1(Model model)	{
		
		// STATE-CHANGE
		boolean stateChangeSuccessful = true;
		
		try	{
			stateChangeSuccessful = ControllerHelper.stateHelper(kms, "prepare");
		}	catch(Exception e)	{
			e.printStackTrace();
			model.addAttribute("message", e.getMessage());
			model.addAttribute("error", e.getClass().toString());
			return "error";
		}
		
		if(stateChangeSuccessful)	{
			// Add Metadata if already set
			model.addAttribute("numberOfPlayers", kms.getPlayerCount());
			model.addAttribute("numberOfAssistants", kms.getAssistantCount());
			return "prepare1";
		}	else	{
			return "reset";
		}

	}
	
	// Handling of Metadata - POST Requests on first Site of Preparation
	@RequestMapping(value = "/prepare1", method = RequestMethod.POST)
	public String processPrepare1(Model model,
		 	@RequestParam(value="players", required = true) String numberOfPlayers, 
	        @RequestParam(value="assistants", required = true) String numberOfAssistants,
	        @RequestParam(value="c", required = false) String configuration)	{
		
		// Check some things to ensure Data are valid
		if(numberOfPlayers != "" && numberOfAssistants != "")	{
			int players, assistants;
			try	{
				players = Integer.parseInt(numberOfPlayers);
				assistants = Integer.parseInt(numberOfAssistants);
			}	catch(Exception e)	{
				e.printStackTrace();
				model.addAttribute("error", "fraction");
				return "prepare1";
			}
			
			if(players > 0 && players <= 8999 && players % 2 == 0 && 
					assistants > 0 && assistants <= 26 && assistants % 1 == 0)	{
				// Store Metadata in Logic
				kms.getState().setBasicConfig(players, assistants);
				return "redirect:/prepare2?c=" + configuration;
			}
			
		}
		
		// Add given Values to model if something was wrong
		model.addAttribute("numberOfPlayers", numberOfPlayers);
		model.addAttribute("numberOfAssistants", numberOfAssistants);
		model.addAttribute("error", "config");
		return "prepare1";
	}

	// GET Requests on Site for Distribution
	@RequestMapping(value = "/prepare2", method = RequestMethod.GET)
	public String prepare2(Model model, @RequestParam(value="c", required = false) String configuration)	{
		
		// STATE-CHANGE
		boolean stateChangeSuccessful = true;
		
		try	{
			stateChangeSuccessful = ControllerHelper.stateHelper(kms, "prepare");
		}	catch(Exception e)	{
			e.printStackTrace();
			return "error?e=" + e.toString();
		}
		
		if(stateChangeSuccessful)	{
		
			// Load from File
			if(configuration != null && configuration.equals("load"))	{
				//TODO: load from file
				
			}
			
			// Only show the already stored values
			if(configuration == null)	{
				 System.out.println("configuration == null");
				configuration = "load";
			}
			
			// Load values
			if(configuration.equals("load") || configuration.equals("standard"))	{
				if(configuration.equals("standard"))	{
					// Load Standard Distribution
					kms.getState().loadStandardDistribution();
					model.addAttribute("isStandard", true);
				}	else	{
					model.addAttribute("isStandard", false);
				}
				// Add stored values to model
				model.addAttribute("customerConfiguration", kms.getbDistribution());
				model.addAttribute("salesmanConfiguration", kms.getsDistribution());
				model.addAttribute("groupQuantity", kms.getGroupCount());
			}	else	{
				// Create own configuration - Fields are empty
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
	// POST Request on Distribution-Site -> Loading values from File, Display them by redirecting to "prepare2"
	@RequestMapping(value = "/prepare2", method = RequestMethod.POST)
	public String loadConfig(Model model, @RequestParam("input-file") MultipartFile file)	{
		//String fileurl = "/Users/yangxinyu/Desktop/"+filename;
		try {
			kms.getState().load(file);
			System.out.println("load successfull");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			model.addAttribute("message", e.getMessage());
			model.addAttribute("error", e.getClass().toString());
			return "error";
		}
		return "redirect:/prepare2";
	}

	// Processes Posted Values from Distribution-Site
	@RequestMapping(value = "generate", method = RequestMethod.POST)
	public String generate(	Model model,
							@RequestParam(value = "cRelativeQuantity[]") String[] cRelativeQuantity,
							@RequestParam(value = "cPrice[]") String[] cPrice,
							@RequestParam(value = "cAbsoluteQuantity[]") String[] cAbsoluteQuantity,
							@RequestParam(value = "sRelativeQuantity[]") String[] sRelativeQuantity,
							@RequestParam(value = "sPrice[]") String[] sPrice,
							@RequestParam(value = "sAbsoluteQuantity[]") String[] sAbsoluteQuantity)	{
		
		int i;
		String error = "";
		
		// Delete current Maps so they can be completely rewritten.
		kms.getbDistribution().clear();
		kms.getsDistribution().clear();
		
		// Iterate through lines and register 2 Groups (one for Buyer, one for Seller) per Line.
		for(i = 0; i < cRelativeQuantity.length; i++)	{
			
			// Check some things to ensure that data are valid
			if(cPrice[i] != "" && cRelativeQuantity[i] != "" && cAbsoluteQuantity[i] != "" &&
					sPrice[i] != "" && sRelativeQuantity[i] != "" && sAbsoluteQuantity[i] != "")	{
			
				try	{
					int cP = Integer.parseInt(cPrice[i]);
					int cR = Integer.parseInt(cRelativeQuantity[i]);
					int cA = Integer.parseInt(cAbsoluteQuantity[i]);
					int sP = Integer.parseInt(sPrice[i]);
					int sR = Integer.parseInt(sRelativeQuantity[i]);
					int sA = Integer.parseInt(sAbsoluteQuantity[i]);
					
					// Checking again....
					if(	cP > 0 && cP % 1 == 0 && 
							cR > 0 && cR <= 100 && cR % 1 == 0 &&
							cA > 0 && cA <= kms.getPlayerCount() && cA % 1 == 0 &&
							sP > 0 && sP % 1 == 0 && 
							sR > 0 && sR <= 100 && sR % 1 == 0 &&
							sA > 0 && sA <= kms.getPlayerCount() && sA % 1 == 0)	{
						
						// Data valid -> store in Logic
						kms.getState().newGroup(true, cP, cR, cA);
						kms.getState().newGroup(false, sP, sR, sA);
					
					}	else	{
						error = "generate";
						break;
					}
				}	catch(Exception e)	{
					e.printStackTrace();
					error = "generate.fraction";
					break;
				}
				
			}	else	{
				error = "generate.empty";
				break;
			}
		}
		
		if(error == "")	{
			// Set Group Count [hidden field].
			kms.getConfiguration().setGroupCount(i);
			
			// Generate Cards-Set
			kms.getState().generateCards();
			
			// TODO discuss Folder Structure and change to correct folder
			String path = servletContext.getRealPath(".").concat("config.txt");
			
			// Save Config File automatically
			kms.getState().save(path);
			
			// Add path to model
			model.addAttribute("configSavePath", path);
			
			return "generate";
			
		}	else	{
			// Build a new Map for giving it to the model to display the mistakes stupid deactivated-javascript-User made.
			Map<Integer, Amount> cConf = new HashMap<>();
			Map<Integer, Amount> sConf = new HashMap<>();
			for(int a = 0; a <= cRelativeQuantity.length; a++)	{
				cConf.put(Integer.parseInt(cPrice[a]), new Amount(Integer.parseInt(cRelativeQuantity[a]), Integer.parseInt(cAbsoluteQuantity[a])));
				sConf.put(Integer.parseInt(sPrice[a]), new Amount(Integer.parseInt(sRelativeQuantity[a]), Integer.parseInt(sAbsoluteQuantity[a])));
			}
			model.addAttribute("customerConfiguration", cConf);
			model.addAttribute("customerConfiguration", sConf);
			model.addAttribute("error", error);
			model.addAttribute("groupQuantity", kms.getGroupCount());
			model.addAttribute("isStandard", false);
			return "prepare2";
			
		}
	}

	
}
