package jKMS.controller;

import jKMS.Amount;
import jKMS.LogicHelper;
import jKMS.states.Evaluation;
import jKMS.states.Load;
import jKMS.states.Play;
import jKMS.states.Preparation;

import java.io.IOException;
import java.util.TreeMap;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ServerController extends AbstractServerController	{

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Model model, ServletRequest request) {
		try {
			if(ControllerHelper.checkFolders())	{
				System.out.println("Adding Folders Succeeded!");
			}	else	{
				System.out.println("All Folders already here.");
			}
		} catch (IOException e) {
			e.printStackTrace();
			model.addAttribute("message", LogicHelper.getLocalizedMessage("error.buildFolderStructure.message"));
			model.addAttribute("error", LogicHelper.getLocalizedMessage("error.buildFolderStructure.error"));
			return "error";
		}
		return "index";
	}
	
	@RequestMapping("/settings")
	public String settigs()	{
		return "settings";
	}
	
	@RequestMapping(value = "/reset", method = RequestMethod.GET)
	public String reset()	{
		return "reset";
	}
	
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public String processReset()	{
		kms.getConfiguration().setPlayerCount(0);
		kms.getConfiguration().setGroupCount(0);
		kms.getConfiguration().setAssistantCount(0);
		kms.getConfiguration().setbDistribution(new TreeMap<Integer, Amount>());
		kms.getConfiguration().setsDistribution(new TreeMap<Integer, Amount>());
		kms.prepare();
		System.out.println("Reseted all data.");
		return "redirect:/index";
	}
	
	@RequestMapping(value = "/autoRedirect", method = RequestMethod.GET)
	public String autoRedirect(){
		if(kms.getState() instanceof Preparation) return "redirect:/index";
		else if(kms.getState() instanceof Load) return "redirect:/load1";
		else if(kms.getState() instanceof Play) return "redirect:/play";
		else if(kms.getState() instanceof Evaluation) return "redirect:/evaluate";
		else return "redirect:/index";
	}
}
