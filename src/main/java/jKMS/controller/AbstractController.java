package jKMS.controller;

import jKMS.Kartoffelmarktspiel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;

/*
 * Abstract for all controllers so that they all get a kms attribute.
 */
@Controller
@ComponentScan
public abstract class AbstractController {

	@Autowired
	protected Kartoffelmarktspiel kms;
	@Autowired
	protected ReloadableResourceBundleMessageSource messageSource;

}
