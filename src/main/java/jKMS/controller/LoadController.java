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
import org.springframework.web.multipart.MultipartFile;

@Controller
public class LoadController extends AbstractServerController {
	
	@RequestMapping(value = "/load1", method = RequestMethod.GET)
	public String load1(Model model, ServletRequest request)	{

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

			// Javascript - For number of Exclude Fields
			model.addAttribute("numberOfAssistants", kms.getAssistantCount());
			model.addAttribute("numberOfPlayers", kms.getPlayerCount());
			
			// JavaScript - For Quick-checking the given IDs
			model.addAttribute("firstID", kms.getConfiguration().getFirstID());
			
			return "load1";
		}	else	{
			return "reset";
		}
		
	}
	
	@RequestMapping(value = "/load1", method = RequestMethod.POST)
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
			return "redirect:/load1";
			
		}	else	{
			
			return "reset";
		}
		
	}
	
	@RequestMapping(value = "/load2", method = RequestMethod.GET)
	public String load(Model model, ServletRequest request)	{

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

			// Javascript - For number of Exclude Fields
			model.addAttribute("numberOfAssistants", kms.getAssistantCount());
			model.addAttribute("numberOfPlayers", kms.getPlayerCount());
			
			// JavaScript - For Quick-checking the given IDs
			model.addAttribute("firstID", kms.getConfiguration().getFirstID());
			
			return "load2";
		}	else	{
			return "reset";
		}
		
	}
	
	@RequestMapping(value = "/load2", method = RequestMethod.POST)
	public String start(Model model, @RequestParam("exclude[]") String exclude[], 
										@RequestParam("check[]") List<String> check)	{
		
		int playerCount = kms.getPlayerCount();
		for(int i = 0; i < exclude.length; i++)	{
			// Check if not all cards where given out
			if(check.indexOf(Integer.toString(i)) == -1)	{
				System.out.println("Excluding Cards from Package " + LogicHelper.IntToPackage(i));
				try	{
					if(exclude[i] != "")	{
						int number = Integer.parseInt(exclude[i]);
						if(number % 1 == 0 && number >= kms.getConfiguration().getFirstID() && 
								number <= (kms.getConfiguration().getFirstID() + playerCount))	{
								
							kms.getState().removeCard(LogicHelper.IntToPackage(i), number);
						}	else	{
							model.addAttribute("error", "exclude.oob");
							return "forward:/load2";
						}
					}	else	{
						model.addAttribute("error", "exclude.empty");
						return "forward:/load2";
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
					return "forward:/load2";
				}
			}	else	{
				System.out.println("Nothing to exclude in Package " + LogicHelper.IntToPackage(i));
			}
		}
		return "redirect:/play";
	}
	
}
