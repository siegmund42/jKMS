package jKMS.controller;

import jKMS.Amount;
import jKMS.Contract;
import jKMS.exceptionHelper.InvalidStateChangeException;

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
	/**
	 * Catches Ajax-Request, converts the set of contracts into a String with the help of the ControllerHelper
	 */
	public String insertData(){
		//String with current data of contracts
		Set<Contract> contracts = kms.getContracts();
		String str = ControllerHelper.setToString(contracts);
		
		//get min and max values of the distributions to limit the chart
		TreeMap<Integer, Amount> sDistribution = (TreeMap<Integer, Amount>) kms.getsDistribution();
		TreeMap<Integer, Amount> bDistribution = (TreeMap<Integer, Amount>) kms.getbDistribution();
		int[] minMax = ControllerHelper.getMinMax(contracts, sDistribution, bDistribution);
		
		str = str.concat(";" + minMax[0] + ";" + minMax[1]);
		
		return str;
	}
	
	@RequestMapping(value = "/play")
	public String play(Model model, @RequestParam(value= "s", required = false) String s) throws InvalidStateChangeException	{

		boolean stateChangeSuccessful = true;
		
		if(s != null && s.equals("stop"))
			stateChangeSuccessful = ControllerHelper.stateHelper(kms, "evaluate");
		else
			stateChangeSuccessful = ControllerHelper.stateHelper(kms, "play");
		
		if(stateChangeSuccessful)	{
			return "play";
		}	else	{
			return "reset";
		}
	}
}
