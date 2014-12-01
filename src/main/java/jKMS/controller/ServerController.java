package jKMS.controller;

import jKMS.Amount;

import java.util.TreeMap;

import javax.servlet.ServletRequest;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ServerController extends AbstractServerController implements ErrorController	{
	
	private static final String PATH = "/error";
	
	@Override
	public String getErrorPath()	{
		return PATH;
	}

	@RequestMapping(value = "/index")
	public String index(ServletRequest request) {
		return "index";
	}
	
	@RequestMapping("/settings")
	public String settigs()	{
		return "settings";
	}
	
	@RequestMapping(value = PATH)
	public String error(Model model, @RequestParam("e") String error)	{
		model.addAttribute("error", error);
		return "error";
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
		
		return "redirect:/index";
	}
}
