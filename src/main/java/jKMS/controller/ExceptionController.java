package jKMS.controller;

import jKMS.LogicHelper;
import jKMS.exceptionHelper.InvalidStateChangeException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionController {
	
	@ExceptionHandler
	public ModelAndView handleIllegalStateException(IllegalStateException e) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("error");
		mav.addObject("error", LogicHelper.getLocalizedMessage("error.state.error"));
		mav.addObject("message", LogicHelper.getLocalizedMessage("error.state.message"));
		return mav;
	}
	
	@ExceptionHandler
	public ModelAndView handleInavlidStateChangeException(InvalidStateChangeException e) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("error");
		mav.addObject("error", LogicHelper.getLocalizedMessage("error.load.error"));
		mav.addObject("message", LogicHelper.getLocalizedMessage("error.load.message"));
		return mav;
	}
	
	@ExceptionHandler
	public ModelAndView handleRuntimeException(RuntimeException e) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("error");
		mav.addObject("error", LogicHelper.getLocalizedMessage("error"));
		mav.addObject("message", e.getMessage());
		return mav;
	}
	
	@ExceptionHandler
	public ModelAndView handleSomeException(Exception e) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("error");
		mav.addObject("error", e.getClass());
		mav.addObject("message", e.getMessage());
		return mav;
	}
	
}