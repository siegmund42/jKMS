package jKMS.controller;

import jKMS.Application;
import jKMS.LogicHelper;
import jKMS.exceptionHelper.CreateFolderFailedException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Only one page to handle...
 * @author siegmund42
 *
 */
@Controller
public class LoginController extends AbstractController	{

	/**
	 * Show login page, init Helpers
	 * @return 	Name of template
	 * @throws	CreateFolderFailedException
	 */
	@RequestMapping("/login")
	public String login() throws CreateFolderFailedException	{
		ControllerHelper.init(messageSource);
		LogicHelper.init(messageSource);
		Application.gui.setReady();
		Application.gui.changeLanguage();
		return "login";
	}
}
