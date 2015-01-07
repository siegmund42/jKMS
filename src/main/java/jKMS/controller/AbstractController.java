package jKMS.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jKMS.Kartoffelmarktspiel;

@Controller
@ComponentScan
public abstract class AbstractController {

	@Autowired
	protected Kartoffelmarktspiel kms;

}
