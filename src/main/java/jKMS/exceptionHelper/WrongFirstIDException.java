package jKMS.exceptionHelper;

@SuppressWarnings("serial")
public class WrongFirstIDException extends Exception{
	
	public WrongFirstIDException(){
		super("FirstID have to be >= 0");
	}

}
