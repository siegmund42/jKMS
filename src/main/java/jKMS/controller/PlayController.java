package jKMS.controller;

import jKMS.Amount;
import jKMS.Contract;
import jKMS.LogicHelper;

import java.util.Set;
import java.util.TreeMap;

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
		//String with current data of contracts
		Set<Contract> contracts = kms.getContracts();
		String str = ControllerHelper.setToString(contracts);
		
		//get min and max values of the distributions to limit the chart
		TreeMap<Integer, Amount> sDistribution = (TreeMap<Integer, Amount>) kms.getsDistribution();
		TreeMap<Integer, Amount> bDistribution = (TreeMap<Integer, Amount>) kms.getbDistribution();
		int[] minMax = ControllerHelper.getMinMax(sDistribution, bDistribution);
		
		str = str.concat(";" + minMax[0] + ";" + minMax[1]);
		
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
		}	catch(IllegalStateException e)	{
			e.printStackTrace();
			model.addAttribute("message", LogicHelper.getLocalizedMessage("error.state.message"));
			model.addAttribute("error", LogicHelper.getLocalizedMessage("error.state.error"));
			return "error";
		}
		
		if(stateChangeSuccessful)	{
			return "play";
		}	else	{
			return "reset";
		}
	}
}
