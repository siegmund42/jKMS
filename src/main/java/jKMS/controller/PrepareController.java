package jKMS.controller;

import jKMS.Amount;
import jKMS.LogicHelper;
import jKMS.exceptionHelper.EmptyFileException;
import jKMS.exceptionHelper.FalseLoadFileException;
import jKMS.exceptionHelper.InvalidStateChangeException;
import jKMS.exceptionHelper.WrongAssistantCountException;
import jKMS.exceptionHelper.WrongFirstIDException;
import jKMS.exceptionHelper.WrongPlayerCountException;
import jKMS.exceptionHelper.WrongRelativeDistributionException;

import java.io.FileOutputStream;
import java.io.IOException;
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
	public String prepare1(Model model) throws InvalidStateChangeException	{
		
		// STATE-CHANGE
		if(ControllerHelper.stateHelper(kms, "prepare"))	{
			// Add Metadata if already set
			model.addAttribute("firstID", kms.getConfiguration().getFirstID());
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
			}	catch(NumberFormatException e)	{
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
	public String prepare2(Model model, @RequestParam(value="c", required = false) String configuration) throws IllegalStateException, InvalidStateChangeException	{
		
		// STATE-CHANGE
		if(ControllerHelper.stateHelper(kms, "prepare"))	{
			
			// Only show the already stored values
			if(configuration == null)	{
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
			model.addAttribute("firstID", kms.getConfiguration().getFirstID());
	
			return "prepare2";
		}	else	{
			return "reset";
		}
	}
	
	// POST Request on Distribution-Site -> Loading values from File, Display them by redirecting to "prepare2"
	@RequestMapping(value = "/prepare2", method = RequestMethod.POST)
	public String loadConfig(Model model, @RequestParam("input-file") MultipartFile file)	{
		//String fileurl = "/Users/yangxinyu/Desktop/"+filename;
		try {
			kms.getState().load(file);
		} 	catch(NumberFormatException | IOException | EmptyFileException|FalseLoadFileException e)	{
			// File empty/broken/something went wrong
			e.printStackTrace();
			model.addAttribute("message", LogicHelper.getLocalizedMessage("error.load.message"));
			model.addAttribute("error", LogicHelper.getLocalizedMessage("error.load.error"));
			return "error";
		}
		return "redirect:/prepare2";
	}

	// Processes Posted Values from Distribution-Site
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(	Model model,
							@RequestParam(value = "cRelativeQuantity[]") String[] cRelativeQuantity,
							@RequestParam(value = "cPrice[]") String[] cPrice,
							@RequestParam(value = "cAbsoluteQuantity[]") String[] cAbsoluteQuantity,
							@RequestParam(value = "sRelativeQuantity[]") String[] sRelativeQuantity,
							@RequestParam(value = "sPrice[]") String[] sPrice,
							@RequestParam(value = "sAbsoluteQuantity[]") String[] sAbsoluteQuantity)
						throws IOException	{
		
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
						error = "save";
						break;
					}
					
				}	catch(NumberFormatException e)	{
					e.printStackTrace();
					error = "save.fraction";
					break;
				}
				
			}	else	{
				error = "save.empty";
				break;
			}
		}
		
		// Check if actual amount of players is exceeding 8999
		if((LogicHelper.getAbsoluteSum(kms.getbDistribution()) + LogicHelper.getAbsoluteSum(kms.getsDistribution())) > 8999)	{
			error = "config.playerCountOV";
		}
		
		// Check if number of buyerGroups != number of sellerGroups
		if(kms.getbDistribution().size() != kms.getsDistribution().size())	{
			error = "config.groupcount";
		}
		
		if(error == "")	{
			// Set Group Count [From data!! Not from hidden field!!1einself!1].
			kms.getConfiguration().setGroupCount((kms.getbDistribution().size() + kms.getsDistribution().size())/2);
			
			// Set new PlayerCount
			kms.getConfiguration().setPlayerCount(LogicHelper.getAbsoluteSum(kms.getbDistribution()) + LogicHelper.getAbsoluteSum(kms.getsDistribution()));
			
			// Generate Cards-Set
			try	{
				kms.getState().generateCards();
			}	catch (WrongFirstIDException | WrongAssistantCountException | WrongPlayerCountException | WrongRelativeDistributionException e) {
				e.printStackTrace();
				model.addAttribute("error", LogicHelper.getLocalizedMessage("error.PDF.error"));
				model.addAttribute("message", LogicHelper.getLocalizedMessage("error.PDF.message"));
				return "error";
			}
			
			if(ControllerHelper.checkFolders())	{
				// Set File Path
				String path = ControllerHelper.getFolderPath("config") + ControllerHelper.getFilename("filename.config") + ".txt";
				
				// Save Config File automatically
				try	{
					FileOutputStream fos = new FileOutputStream(path);
					kms.getState().save(fos);
					System.out.println("Saved Config File in: " + path);
				} catch(IOException e){
					e.printStackTrace();
					model.addAttribute("error", LogicHelper.getLocalizedMessage("error.config.save.error"));
					model.addAttribute("message", LogicHelper.getLocalizedMessage("error.config.save.message"));
					return "error";	
				}
				
				// Add path to model
				model.addAttribute("configSavePath", path);
				
			}
				
			return "save";
			
		}	else	{
			// Build a new Map for giving it to the model to display the mistakes stupid deactivated-javascript-User made.
			if(error != "save.fraction")	{
				Map<Integer, Amount> cConf = new HashMap<>();
				Map<Integer, Amount> sConf = new HashMap<>();
				for(int a = 0; a < cRelativeQuantity.length; a++)	{
					cConf.put(Integer.parseInt(cPrice[a]), new Amount(Integer.parseInt(cRelativeQuantity[a]), Integer.parseInt(cAbsoluteQuantity[a])));
					sConf.put(Integer.parseInt(sPrice[a]), new Amount(Integer.parseInt(sRelativeQuantity[a]), Integer.parseInt(sAbsoluteQuantity[a])));
				}
				int diff = cConf.size() - sConf.size();
				if(diff > 0)	{
					for(int a = 0; a < diff; a++)	{
						sConf.put(0, new Amount(0, 0));
					}
				}
				if(diff < 0)	{
					for(int a = 0; a > diff; a--)	{
						cConf.put(0, new Amount(0, 0));
					}
				}
				model.addAttribute("customerConfiguration", cConf);
				model.addAttribute("salesmanConfiguration", sConf);
				model.addAttribute("groupQuantity", cConf.size());
			}	else	{
				model.addAttribute("groupQuantity", 0);
			}
			model.addAttribute("numberOfPlayers", kms.getPlayerCount());
			model.addAttribute("error", error);
			model.addAttribute("isStandard", false);
			return "prepare2";
		}
	}
	
	// Processes Posted Values from Distribution-Site
	@RequestMapping(value = "generate", method = RequestMethod.GET)
	public String generate() throws InvalidStateChangeException	{
		// STATE-CHANGE
		if(ControllerHelper.stateHelper(kms, "prepare"))	{
			return "generate";
		}	else	{
			return "reset";
		}
	}
	
}
