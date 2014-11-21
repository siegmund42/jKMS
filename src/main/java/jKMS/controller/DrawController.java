package jKMS.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class DrawController {
	//data-List für Prototypzwecke, später mal an dieser Stelle das Vertrags-Objekt
	private List<String> data = new ArrayList<String>();
	//hard gecodet: JSONArray der Standardverteilung
	private String evaluation = "[[72,38],[144,43],[216,48],[288,53],[360,58],[400,63]];[[80,70],[144,65],[208,60],[272,55],[336,50],[400,45]]";
	
	
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
	//catches AjaxRequest, concatenates data list with standardverteilung
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
	
	
	

}