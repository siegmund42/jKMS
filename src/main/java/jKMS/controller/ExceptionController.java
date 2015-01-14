package jKMS.controller;

import jKMS.LogicHelper;
import jKMS.exceptionHelper.CreateFolderFailedException;
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
		mav.addObject("error", LogicHelper.getLocalizedMessage("error.state.error"));
		mav.addObject("message", LogicHelper.getLocalizedMessage("error.state.message"));
		return mav;
	}
	
	/**
	 * If user jumped through sites not by clicking our links
	 */
	@ExceptionHandler
	public ModelAndView handleInavlidStateChangeException(InvalidStateChangeException e) {
		e.printStackTrace();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("standardException");
		mav.addObject("error", LogicHelper.getLocalizedMessage("error.state.error"));
		mav.addObject("message", LogicHelper.getLocalizedMessage("error.state.message"));
		return mav;
	}
	
	/**
	 * Handles error when creating folders
	 */
	@ExceptionHandler
	public ModelAndView handleCreateFolderFailedException(CreateFolderFailedException e) {
		e.printStackTrace();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("standardException");
		mav.addObject("message", LogicHelper.getLocalizedMessage("error.buildFolderStructure.message"));
		mav.addObject("error", LogicHelper.getLocalizedMessage("error.buildFolderStructure.error"));
		return mav;
	}

}