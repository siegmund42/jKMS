package jKMS.exceptionHelper;

public class InvalidStateChangeException extends Exception {

	private String errorMessage;
	
	public InvalidStateChangeException(String message)	{
		this.errorMessage = message;
	}
	
	@Override
	public String toString()	{
		return "InvalidStateChangeException: " + errorMessage;
	}
	
	@Override
	public String getMessage()	{
		return errorMessage;
	}
}
