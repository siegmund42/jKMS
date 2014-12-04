package jKMS.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PlayController extends AbstractServerController {
	
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
