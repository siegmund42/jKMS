package jKMS.exceptionHelper;

@SuppressWarnings("serial")
public class CreateFolderFailedException extends Exception {

	private String errorMessage;
	
	public CreateFolderFailedException(String message)	{
		this.errorMessage = message;
	}
	
	@Override
	public String toString()	{
		return "CreateFolderFailedException: " + errorMessage;
	}
	
	@Override
	public String getMessage()	{
		return errorMessage;
	}
}
