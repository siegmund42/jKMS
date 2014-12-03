package jKMS.controller;

import jKMS.Contract;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EvaluationController extends AbstractServerController {
	
	//get all attributes of "winner contract"
	@RequestMapping(value = "/lottery")
	public String lottery(Model model){
		
		boolean stateChangeSuccessful = true;
		
		try	{
			stateChangeSuccessful = ControllerHelper.stateHelper(kms, "evaluate");
		}	catch(Exception e)	{
			e.printStackTrace();
			return "error?e=" + e.toString();
		}
		
		if(stateChangeSuccessful)	{
		
			Contract winner = kms.getState().pickWinner();
			int bProfit = kms.getState().buyerProfit(winner);
			int sProfit = kms.getState().sellerProfit(winner);
			
			model.addAttribute("buyerID", winner.getBuyer().getId());
			model.addAttribute("wtp", winner.getBuyer().getValue());
			model.addAttribute("sellerID", winner.getSeller().getId());
			model.addAttribute("cost", winner.getSeller().getValue());
			model.addAttribute("price", winner.getPrice());
			model.addAttribute("bprofit", bProfit);
			model.addAttribute("sprofit", sProfit);
			
			return "lottery";
		}	else	{
			return "reset";
		}
		
	}
	
	@RequestMapping(value = "/evaluate")
	public String evaluate()	{
		boolean stateChangeSuccessful = true;
		
		try	{
			stateChangeSuccessful = ControllerHelper.stateHelper(kms, "evaluate");
		}	catch(Exception e)	{
			e.printStackTrace();
			return "error?e=" + e.toString();
		}
		
		if(stateChangeSuccessful)	{
			return "evaluate";
		}	else	{
			return "reset";
		}
	}
}
