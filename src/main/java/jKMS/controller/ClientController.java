package jKMS.controller;

import jKMS.LogicHelper;
import jKMS.states.Evaluation;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClientController extends AbstractController {
	
	/*
	 * Redirect to /contract Site
	 */
	@RequestMapping("/")
	public String redirect() {
		return "redirect:/contract";
	}
	
	/*
	 * Display the contract Form
	 */
	@RequestMapping(value = "/contract", method = RequestMethod.GET)
	public String contract(Model model)	{
		model.addAttribute("firstID", kms.getConfiguration().getFirstID());
		model.addAttribute("numberOfPlayers", kms.getConfiguration().getPlayerCount());
		return "contract";
	}
	
	/*
	 * Process the contract Request
	 */
	@RequestMapping(value = "/contract", method = RequestMethod.POST)
	public String contract(Model model,
		 	@RequestParam(value = "id1", required = false) String id1, 
	        @RequestParam(value = "id2", required = false) String id2,
	        @RequestParam(value = "price", required = false) String price,
		 	@RequestParam(value = "c", required = false) String c, 
		 	@RequestParam(value = "a", required = false) String a, 
	        @RequestParam(value = "b", required = false) String b,
	        @RequestParam(value = "p", required = false) String p,
	        HttpServletRequest request)	{
		
		try	{
			// Check if we need to correct [remove a contract]
			if(c != null)	{
				// Remove the Contract
				if(!kms.getState().removeContract(Integer.parseInt(a), Integer.parseInt(b), Integer.parseInt(p)))	{
					// It didnt work
			    	model.addAttribute("id1", id1);
			    	model.addAttribute("id2", id2);
			    	model.addAttribute("price", price);
			    	model.addAttribute("error", "Something went wrong during correction, please try again...");
					model.addAttribute("firstID", kms.getConfiguration().getFirstID());
					model.addAttribute("numberOfPlayers", kms.getConfiguration().getPlayerCount());
			    	return "contract";
				}
			}
			
			// Check if Fields empty
			if(id1 != "" && id2 != "" && price != "")	{
				// Convert Strings to Integer
				int idOne = Integer.parseInt(id1);
				int idTwo = Integer.parseInt(id2);
				int cost = Integer.parseInt(price);
				// Add the Contract
				int add = kms.getState().addContract(idOne, idTwo, cost, request.getRemoteAddr());
				
			    if(add == 0)	{
			    	// Succeeded
			    	return "redirect:/contract.html?success&a=" + idOne + "&b=" + idTwo + "&p=" + cost;
			    }	else	{
			    	// Failed - add Attributes to model
			    	model.addAttribute("id1", id1);
			    	model.addAttribute("id2", id2);
			    	model.addAttribute("price", price);
			    	model.addAttribute("error", add);
			    }
			}	else	{
				// Fields where empty - add Attributes to model
		    	model.addAttribute("id1", id1);
		    	model.addAttribute("id2", id2);
		    	model.addAttribute("price", price);
		    	model.addAttribute("error", "empty");
			}
			model.addAttribute("firstID", kms.getConfiguration().getFirstID());
			model.addAttribute("numberOfPlayers", kms.getConfiguration().getPlayerCount());
	    	return "contract";
			
		}	catch(NumberFormatException e)	{
			// Number seems to be fractional
			e.printStackTrace();
			model.addAttribute("error", "fraction");
			model.addAttribute("firstID", kms.getConfiguration().getFirstID());
			model.addAttribute("numberOfPlayers", kms.getConfiguration().getPlayerCount());
			return "contract";
		}	catch(IllegalStateException e)	{
			// Game not running
			e.printStackTrace();
			if(kms.getState() instanceof Evaluation)	{
				// Guess Game stopped
				model.addAttribute("error", "stopped");
			}	else	{
				// Guess Game not running
				model.addAttribute("error", "notRunning");
			}
			model.addAttribute("firstID", kms.getConfiguration().getFirstID());
			model.addAttribute("numberOfPlayers", kms.getConfiguration().getPlayerCount());
			return "contract";
		}
	}


}
