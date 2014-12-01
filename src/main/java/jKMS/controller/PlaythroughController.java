package jKMS.controller;

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
	public String load(Model model, ServletRequest request)	{

		boolean stateChangeSuccessful = true;
		
		try	{
			stateChangeSuccessful = ControllerHelper.stateHelper(kms, "play");
		}	catch(Exception e)	{
			e.printStackTrace();
			return "error?e=" + toString();
		}
		
		if(stateChangeSuccessful)	{
			
	//		char[] assistants = new char[26];
	//		for(int i = 0; i < kms.getConfiguration().getAssistantCount(); i++){
	//			assistants[i] = LogicHelper.IntToPackage(i);
	//		}
	//		model.addAttribute("assistants", assistants);
			
			model.addAttribute("firstID", kms.getConfiguration().getFirstID());
			model.addAttribute("playerCount", kms.getConfiguration().getPlayerCount());
			model.addAttribute("numberOfAssistants", kms.getAssistantCount());
			model.addAttribute("ip", ControllerHelper.getIP());
			model.addAttribute("port", ControllerHelper.getPort(request));
			
			return "load";
		}	else	{
			return "reset";
		}
		
	}
	
	@RequestMapping(value = "/load", method = RequestMethod.POST)
	public String start()	{
		return "redirect:/play";
	}
	
	@RequestMapping(value = "/play")
	public String play()	{

		boolean stateChangeSuccessful = true;
		
		try	{
			stateChangeSuccessful = ControllerHelper.stateHelper(kms, "play");
		}	catch(Exception e)	{
			e.printStackTrace();
			return "error?e=" + e.toString();
		}
		
		if(stateChangeSuccessful)	{
			return "play";
		}	else	{
			return "reset";
		}
	}

}