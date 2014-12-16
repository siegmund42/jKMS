package jKMS.controller;

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
	public String contract(){
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
	        HttpServletRequest request)	{
		
		try	{
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
			    	model.addAttribute("success");
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
	    	return "contract";
			
		}	catch(NumberFormatException e)	{
			// Number seems to be fractional
			e.printStackTrace();
			model.addAttribute("error", "fraction");
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
			return "contract";
		}
	}


}
