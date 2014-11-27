package jKMS.controller;

import org.springframework.stereotype.Controller;
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
	public String contract(
		 	@RequestParam(value="id1", required=false) int id1, 
	        @RequestParam(value="id2", required=false) int id2,
	        @RequestParam(value="price", required=false) int price)	{
		
			Boolean success = kms.getState().addContract(id1, id2, price);
		    if(success == false){
		    	return "failpage";
		    }else
		    return "contract";
	}


}
