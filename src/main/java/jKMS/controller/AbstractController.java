package jKMS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import jKMS.Kartoffelmarktspiel;

public abstract class AbstractController {

	@Autowired
	protected Kartoffelmarktspiel kms;
	
}
