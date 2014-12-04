package jKMS.exceptionHelper;

public class WrongPlayerCountException extends Exception {
	
	public WrongPlayerCountException(){
		super("Playercount is not equeal to distributionplayercount");
	}

}
