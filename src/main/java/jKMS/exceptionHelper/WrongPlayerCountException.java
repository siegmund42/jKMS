package jKMS.exceptionHelper;

@SuppressWarnings("serial")
public class WrongPlayerCountException extends Exception {
	
	public WrongPlayerCountException(){
		super("Playercount is not equeal to distributionplayercount");
	}

}
