package jKMS.exceptionHelper;

@SuppressWarnings("serial")
public class WrongPackageException extends Exception{

	public WrongPackageException(){
		super("The entered lastId is not in the given package");
	}
}
