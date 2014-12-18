package jKMS.controller;

import java.io.IOException;
import java.util.List;

import jKMS.LogicHelper;
import jKMS.exceptionHelper.EmptyFileException;
import jKMS.exceptionHelper.FalseLoadFileException;
import jKMS.exceptionHelper.InvalidStateChangeException;
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
	public String load1(Model model, ServletRequest request) throws InvalidStateChangeException	{
		
		if(ControllerHelper.stateHelper(kms, "load"))	{
			// Add some Attributes to display the Configuration of the game again
			model.addAttribute("customerConfiguration", kms.getbDistribution());
			model.addAttribute("salesmanConfiguration", kms.getsDistribution());
			model.addAttribute("groupQuantity", kms.getGroupCount());
			
			// IP and Port for Client connection
			List<String> IPs = ControllerHelper.getIP();
			// Got Multiple IPs
			if(IPs.size() > 1)	{
				model.addAttribute("ipError", "multipleIP");
			}
			// Got No IP
			if(IPs.size() == 0)	{
				model.addAttribute("ipError", "noIP");
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
	
	/*
	 * For loading an existing Config.txt File
	 */
	@RequestMapping(value = "/load1", method = RequestMethod.POST)
	public String processIndex(Model model, @RequestParam("input-file") MultipartFile file) throws IllegalStateException, InvalidStateChangeException	{
		// State Change
		
		if(ControllerHelper.stateHelper(kms, "load"))	{
			// Load from File
			try {
				kms.getState().load(file);
			} 	catch(NumberFormatException | IOException | EmptyFileException | FalseLoadFileException e)	{
				// File empty/broken/something went wrong
				e.printStackTrace();
				model.addAttribute("message", LogicHelper.getLocalizedMessage("error.load.message"));
				model.addAttribute("error", LogicHelper.getLocalizedMessage("error.load.error"));
				return "error";
			}
			// Everything loaded - Load Data into GUI
			return "redirect:/load1";
		}	else	{
			return "reset";
		}
		
	}
	
	/*
	 * Page for excluding Cards
	 */
	@RequestMapping(value = "/load2", method = RequestMethod.GET)
	public String load(Model model, ServletRequest request) throws InvalidStateChangeException	{

		// State Change
		if(ControllerHelper.stateHelper(kms, "load"))	{

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
	
	/*
	 * Exclude Cards POST Request
	 */
	@RequestMapping(value = "/load2", method = RequestMethod.POST)
	public String start(Model model, @RequestParam("exclude[]") String exclude[], 
										@RequestParam("check[]") List<String> check)	{
		
		int playerCount = kms.getPlayerCount();
		for(int i = 0; i < exclude.length; i++)	{
			// Check if not all cards where given out [Checkbox]
			if(check.indexOf(Integer.toString(i)) == -1)	{
				System.out.println("Excluding Cards from Package " + LogicHelper.IntToPackage(i));
				try	{
					if(exclude[i] != "")	{
						int number = Integer.parseInt(exclude[i]);
						// Check some things..
						if(number % 1 == 0 && number >= kms.getConfiguration().getFirstID() && 
								number <= (kms.getConfiguration().getFirstID() + playerCount))	{
							// Exclude Card from Package
							kms.getState().removeCard(LogicHelper.IntToPackage(i), number);
						}	else	{
							// Checking Failed - number invalid
							model.addAttribute("error", "exclude.oob");
							return "forward:/load2";
						}
					}	else	{
						// Field Empty
						model.addAttribute("error", "exclude.empty");
						return "forward:/load2";
					}
				// Catch Exceptions from removeCard() 
				}	catch (WrongPlayerCountException | WrongAssistantCountException
						| WrongFirstIDException
						| WrongRelativeDistributionException e) {
					e.printStackTrace();
					model.addAttribute("message", LogicHelper.getLocalizedMessage("error.exclude.exception.message"));
					model.addAttribute("error", LogicHelper.getLocalizedMessage("error.exclude.exception.error"));
					return "error";
				// Got a fractional Number?
				}	catch(NumberFormatException e)	{
					e.printStackTrace();
					model.addAttribute("error", "exclude.fraction");
					return "forward:/load2";
				}
			}	else	{
				// Nothing to exclude
				System.out.println("Nothing to exclude in Package " + LogicHelper.IntToPackage(i));
			}
		}
		// Finished all excluding -> Go to play
		return "redirect:/play";
	}
	
}
