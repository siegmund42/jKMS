package jKMS.controller;

import jKMS.LogicHelper;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
	
	@ExceptionHandler
	public String handleIllegalStateException(Model model, IllegalStateException e) {
		model.addAttribute("error", LogicHelper.getLocalizedMessage("error.load.error"));
		model.addAttribute("message", LogicHelper.getLocalizedMessage("error.load.message"));
		return "error";
	}
	
	@ExceptionHandler
	public String handleRuntimeException(Model model, RuntimeException e) {
		model.addAttribute("error", LogicHelper.getLocalizedMessage("error"));
		model.addAttribute("message", e.getMessage());
		return "error";
	}
	
	@ExceptionHandler
	public String handleSomeException(Model model, Exception e) {
		model.addAttribute("error", e.getClass());
		model.addAttribute("message", e.getMessage());
		return "error";
	}
	
}