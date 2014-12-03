package jKMS.controller;

import jKMS.LogicHelper;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PlaythroughController extends AbstractServerController {

	//data-List für Prototypzwecke, später mal an dieser Stelle das Vertrags-Objekt
	private List<String> data = new ArrayList<String>();
	//hard gecodet: JSONArray der Standardverteilung
	private String evaluation = "[[0,38],[72,43],[144,48],[216,53],[288,58],[360,63],[400,63]];[[0,70],[80,65],[144,60],[208,55],[272,50],[336,45],[400,45]]";
	
	
	@RequestMapping("/getData")
	@ResponseBody
	//Catches AjaxRequest, writes new value into data list
	public String insertData(@RequestParam(value="lastValue", defaultValue="-1") String lastValue){
		if(!lastValue.equals("-1")){
			data.add(lastValue);
		}
		String str = "";
		str = str.concat(listToJSON(data));
		return str;
	}
	
	
	@RequestMapping("/getEvaluation")
	@ResponseBody
	//catches AjaxRequest, concatenates data list with standardDistribution
	public String evaluationChart(){
		String str = listToJSON(data);	
		str = str.concat(";" + evaluation);
			return str;
		
	}
	
	//transforms list object to JSONArray similar string
	public String listToJSON(List<String> data){
		String str;
		
		str = "[";
		for(int i = 0; i < data.size(); i++){			
			str = str.concat("["+i+","+data.get(i)+"],"); 
		}
		
		if(data.size() == 1){
			str = str.concat("[1,"+data.get(0)+"],");
		}
		
		str = str.substring(0, str.length()-1).concat("]");
		return str;
	}

	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public String load(Model model, ServletRequest request, @RequestParam("s") String s)	{

		boolean stateChangeSuccessful = true;
		
		try	{
			stateChangeSuccessful = ControllerHelper.stateHelper(kms, "play");
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
				model.addAttribute("ip", ControllerHelper.getIP());
				model.addAttribute("port", ControllerHelper.getPort(request));
			}

			// Javascript - For number of Exclude Fields
			model.addAttribute("numberOfAssistants", kms.getAssistantCount());
			model.addAttribute("numberOfPlayers", kms.getPlayerCount());
			
			// JavaScript - For Quick-checking the given IDs
			model.addAttribute("firstID", kms.getConfiguration().getFirstID());
			
			return "load";
		}	else	{
			return "reset";
		}
		
	}
	
	@RequestMapping(value = "/load", method = RequestMethod.POST)
	public String start(Model model, @RequestParam("exclude[]") String exclude[])	{
		for(int i = 0; i < exclude.length; i++)	{
			try	{
				if(exclude[i] != "")	{
					int number = Integer.parseInt(exclude[i]);
					if(number % 1 == 0 && number >= kms.getConfiguration().getFirstID() && 
							number <= (kms.getConfiguration().getFirstID() + kms.getPlayerCount()))	{
						kms.getState().removeCard(LogicHelper.IntToPackage(i), number);
					}	else	{
						model.addAttribute("error", "exclude.oob");
						return "load";
					}	
				}	else	{
					model.addAttribute("error", "exclude.empty");
					return "load";
				}
			}	catch(Exception e)	{
				e.printStackTrace();
				model.addAttribute("error", "exclude.fraction");
				return "load";
			}
		}
		// TODO exclude Cards
		return "redirect:/play";
	}
	
	@RequestMapping(value = "/play")
	public String play(Model model)	{

		boolean stateChangeSuccessful = true;
		
		try	{
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