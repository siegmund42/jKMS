package jKMS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClientController extends AbstractController {
	
	@RequestMapping("/")
	public String redirect() {
		return "redirect:/contract";
	}
	
	@RequestMapping(value = "/contract", method = RequestMethod.GET)
	public String contract(){
		return "contract";
	}
	
	@RequestMapping(value = "/contract", method = RequestMethod.POST)
	public String contract(Model model,
		 	@RequestParam(value = "id1", required = false) String id1, 
	        @RequestParam(value = "id2", required = false) String id2,
	        @RequestParam(value = "price", required = false) String price)	{
		
		try	{
			
			int idOne = Integer.parseInt(id1);
			int idTwo = Integer.parseInt(id2);
			int cost = Integer.parseInt(price);
			
			int add = kms.getState().addContract(idOne, idTwo, cost);
			
		    if(add == 0)	{
		    	return "redirect:/contract";
		    }	else	{
		    	model.addAttribute("id1", id1);
		    	model.addAttribute("id2", id2);
		    	model.addAttribute("price", price);
		    	model.addAttribute("error", add);
		    	return "contract";
		    }
			
		}	catch(Exception e)	{
			e.printStackTrace();
			model.addAttribute("error", "contract.fraction");
			return "contract";
		}
	}


}
