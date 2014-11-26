package jKMS.controller;

import javax.servlet.ServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoadController extends AbstractServerController {

	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public String load(Model model, ServletRequest request)	{
		char[] assistants = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N'};
		kms.play();
		model.addAttribute("assistants", assistants);
		model.addAttribute("numberOfAssistants", kms.getAssistantCount());
		model.addAttribute("ip", ControllerHelper.getIP());
		model.addAttribute("port", ControllerHelper.getPort(request));
		return "load";
	}
	
	@RequestMapping(value = "/load", method = RequestMethod.POST)
	public String start()	{
		return "redirect:/play";
	}
	
	@RequestMapping(value = "/play")
	public String play()	{
		return "play";
	}
	
	@RequestMapping(value = "/evaluate")
	public String evaluate()	{
		return "evaluate";
	}

	@RequestMapping(value = "/lottery")
	public String lottery()	{
		return "lottery";
	}
	
}
