package jKMS.controller;

import jKMS.Contract;

import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PlayController extends AbstractServerController {
	
	@RequestMapping("/getData")
	@ResponseBody
	/*
	 * Catches Ajax-Request, converts the set of contracts into a String with the help of the ControllerHelper
	 */
	public String insertData(){
		Set<Contract> contracts = kms.getContracts();
		String str = ControllerHelper.setToString(contracts);
		
		return str;
	}
	
	@RequestMapping(value = "/play")
	public String play(Model model, @RequestParam(value= "s", required = false) String s)	{

		boolean stateChangeSuccessful = true;
		
		try	{
			if(s != null && s.equals("stop"))
				stateChangeSuccessful = ControllerHelper.stateHelper(kms, "evaluate");
			else
				stateChangeSuccessful = ControllerHelper.stateHelper(kms, "play");
		}	catch(Exception e)	{
			e.printStackTrace();
			model.addAttribute("message", e.getMessage());
			model.addAttribute("error", e.getClass().toString());
			return "error";
		}
		
		if(stateChangeSuccessful)	{
			return "play";
		}	else	{
			return "reset";
		}
	}
}
