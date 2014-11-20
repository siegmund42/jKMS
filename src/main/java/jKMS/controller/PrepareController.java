package jKMS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PrepareController extends AbstractServerController {
	
	@RequestMapping(value = "/prepare1", method = RequestMethod.GET)
	public String prepare1()	{
		return "prepare1";
	}
	
	@RequestMapping(value = "/prepare2", method = RequestMethod.POST)
	public String processPrepare1(
					Model model, 
				 	@RequestParam(value="players", required=false) String numberOfPlayers, 
			        @RequestParam(value="assistants", required=false) String numberOfAssistants,
			        @RequestParam(value="c", required=false) String configuration
			        			)	{
		if(configuration.equals("standard") || configuration.equals("load"))	{
			model.addAttribute("standardCustomerConfiguration", helper.getStandardCustomerConfiguration());
			model.addAttribute("standardSalesmanConfiguration", helper.getStandardSalesmanConfiguration());
			model.addAttribute("groupQuantity", helper.getStandardSalesmanConfiguration().length);
		}	else	{
			// create own configuration - Fields empty
			// TODO: Ordentlich machen - keine Leerzeichen sondern im html hart min eine gruppe
			char[][] standardCustomerConfiguration = new char[1][2];
			standardCustomerConfiguration[0][0]	= ' ';
			standardCustomerConfiguration[0][1]	= ' ';
			
			char[][] standardSalesmanConfiguration = new char[1][2];
			standardSalesmanConfiguration[0][0]	= ' ';
			standardSalesmanConfiguration[0][1]	= ' ';
			
			model.addAttribute("standardCustomerConfiguration", standardCustomerConfiguration);
			model.addAttribute("standardSalesmanConfiguration", standardSalesmanConfiguration);
			model.addAttribute("groupQuantity", standardSalesmanConfiguration.length);
		}
		model.addAttribute("numberOfPlayers", numberOfPlayers);
		return "prepare2";
	}

	@RequestMapping(value = "generate", method = RequestMethod.POST)
	public String generate()	{
		return "generate";
	}
	
}
