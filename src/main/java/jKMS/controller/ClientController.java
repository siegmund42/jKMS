package jKMS.controller;

import jKMS.states.Evaluation;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ClientController extends AbstractController {
	
	/**
	 * Redirect to /contract Site
	 */
	@RequestMapping("/")
	public String redirect() {
		return "redirect:/contract";
	}
	
	/**
	 * Display the contract Form
	 */
	@RequestMapping(value = "/contract", method = RequestMethod.GET)
	public String contract(Model model)	{
		model.addAttribute("firstID", kms.getConfiguration().getFirstID());
		model.addAttribute("numberOfPlayers", kms.getConfiguration().getPlayerCount());
		return "contract";
	}
	
	/**
	 * Process the contract Request
	 */
	@RequestMapping(value = "/contract", method = RequestMethod.POST)
	public String contract(Model model, RedirectAttributes ra,
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
			    	ra.addFlashAttribute("id1", id1);
			    	ra.addFlashAttribute("id2", id2);
			    	ra.addFlashAttribute("price", price);
			    	ra.addFlashAttribute("error", "correct");
					ra.addFlashAttribute("firstID", kms.getConfiguration().getFirstID());
					ra.addFlashAttribute("numberOfPlayers", kms.getConfiguration().getPlayerCount());
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
			    	ra.addFlashAttribute("success", 42);
			    	ra.addFlashAttribute("a", idOne);
			    	ra.addFlashAttribute("b", idTwo);
			    	ra.addFlashAttribute("p", cost);
			    	return "redirect:/contract.html";
			    	//return "redirect:/contract.html?success&a=" + idOne + "&b=" + idTwo + "&p=" + cost;
			    }	else	{
			    	// Failed - add Attributes to model
			    	ra.addFlashAttribute("id1", id1);
			    	ra.addFlashAttribute("id2", id2);
			    	ra.addFlashAttribute("price", price);
			    	ra.addFlashAttribute("error", add);
			    }
			}	else	{
				// Fields where empty - add Attributes to model
		    	ra.addFlashAttribute("id1", id1);
		    	ra.addFlashAttribute("id2", id2);
		    	ra.addFlashAttribute("price", price);
		    	ra.addFlashAttribute("error", "empty");
			}
			ra.addFlashAttribute("firstID", kms.getConfiguration().getFirstID());
			ra.addFlashAttribute("numberOfPlayers", kms.getConfiguration().getPlayerCount());
	    	return "redirect:/contract";
			
		}	catch(NumberFormatException e)	{
			// Number seems to be fractional
			e.printStackTrace();
			ra.addFlashAttribute("error", "fraction");
			ra.addFlashAttribute("firstID", kms.getConfiguration().getFirstID());
			ra.addFlashAttribute("numberOfPlayers", kms.getConfiguration().getPlayerCount());
			return "redirect:/contract";
		}	catch(IllegalStateException e)	{
			// Game not running
			e.printStackTrace();
			if(kms.getState() instanceof Evaluation)	{
				// Guess Game stopped
				ra.addFlashAttribute("error", "stopped");
			}	else	{
				// Guess Game not running
				ra.addFlashAttribute("error", "notRunning");
			}
			ra.addFlashAttribute("firstID", kms.getConfiguration().getFirstID());
			ra.addFlashAttribute("numberOfPlayers", kms.getConfiguration().getPlayerCount());
			return "redirect:/contract";
		}
	}

}
