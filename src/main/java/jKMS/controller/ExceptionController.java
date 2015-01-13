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
		e.printStackTrace();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("standardException");
		mav.addObject("error", LogicHelper.getLocalizedMessage("error.load.error"));
		mav.addObject("message", LogicHelper.getLocalizedMessage("error.load.message"));
		return mav;
	}
	
	@ExceptionHandler
	public ModelAndView handleInavlidStateChangeException(InvalidStateChangeException e) {
		e.printStackTrace();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("standardException");
		mav.addObject("error", LogicHelper.getLocalizedMessage("error.state.error"));
		mav.addObject("message", LogicHelper.getLocalizedMessage("error.state.message"));
		return mav;
	}
	
}