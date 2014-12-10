package jKMS.controller;

import java.util.List;

import jKMS.LogicHelper;
import jKMS.exceptionHelper.WrongAssistantCountException;
import jKMS.exceptionHelper.WrongFirstIDException;
import jKMS.exceptionHelper.WrongPlayerCountException;
import jKMS.exceptionHelper.WrongRelativeDistributionException;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoadController extends AbstractServerController {

	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public String load(Model model, ServletRequest request, @RequestParam(value = "s", required = false) String s)	{

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
			
			if(s.equals("1"))	{
				// Add some Attributes to display the Configuration of the game again
				model.addAttribute("customerConfiguration", kms.getbDistribution());
				model.addAttribute("salesmanConfiguration", kms.getsDistribution());
				model.addAttribute("groupQuantity", kms.getGroupCount());
				
				// IP and Port for Client connection
				List<String> IPs = ControllerHelper.getIP();
				if(IPs.size() > 1)	{
					model.addAttribute("ipError", true);
				}
				model.addAttribute("IPs", IPs);
				model.addAttribute("port", ControllerHelper.getPort(request));
			}

			// Javascript - For number of Exclude Fields
			model.addAttribute("numberOfAssistants", kms.getAssistantCount());
			model.addAttribute("numberOfPlayers", kms.getPlayerCount());
			
			// JavaScript - For Quick-checking the given IDs
			model.addAttribute("firstID", kms.getConfiguration().getFirstID());

			System.out.println(kms.getConfiguration().getbDistribution().size() + "Playercount short before serving GET request" + kms.getPlayerCount());
			
			return "load";
		}	else	{
			return "reset";
		}
		
	}
	
	@RequestMapping(value = "/load", method = RequestMethod.POST)
	public String start(Model model, @RequestParam("exclude[]") String exclude[])	{
		
		System.out.println(kms.getConfiguration().getbDistribution().size() + "Playercount at POST request" + kms.getPlayerCount());
		
		int playerCount = kms.getPlayerCount();
		for(int i = 0; i < exclude.length; i++)	{
			try	{
				if(exclude[i] != "")	{
					int number = Integer.parseInt(exclude[i]);
					if(number % 1 == 0 && number >= kms.getConfiguration().getFirstID() && 
							number <= (kms.getConfiguration().getFirstID() + playerCount))	{
						
						System.out.println(kms.getConfiguration().getbDistribution().size() + "Playercount short before removeing" + kms.getPlayerCount());
						
						kms.getState().removeCard(LogicHelper.IntToPackage(i), number);
					}	else	{
						model.addAttribute("error", "exclude.oob");
						return "forward:/load?s=2";
					}	
				}	else	{
					model.addAttribute("error", "exclude.empty");
					return "forward:/load?s=2";
				}
			}	catch (WrongPlayerCountException | WrongAssistantCountException
					| WrongFirstIDException
					| WrongRelativeDistributionException e) {
				e.printStackTrace();
				model.addAttribute("message", e.getMessage());
				model.addAttribute("error", e.getClass().toString());
				return "error";
			}	catch(Exception e)	{
				e.printStackTrace();
				model.addAttribute("error", "exclude.fraction");
				return "forward:/load?s=2";
			}
		}
		return "redirect:/play";
	}
	
}
