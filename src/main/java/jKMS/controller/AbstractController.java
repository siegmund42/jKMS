package jKMS.controller;

import jKMS.Kartoffelmarktspiel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

@Controller
@ComponentScan
public abstract class AbstractController {

	@Autowired
	protected Kartoffelmarktspiel kms;

}
