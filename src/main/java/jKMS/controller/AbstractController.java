package jKMS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

import jKMS.Kartoffelmarktspiel;

@Controller
@ComponentScan
public abstract class AbstractController {

	@Autowired
	protected Kartoffelmarktspiel kms;
}
