package jKMS.exceptionHelper;

	@SuppressWarnings("serial")
	public class EmptyFileException extends Exception{
		String errorMessage;
	 
	    public EmptyFileException(String errorMessage)
	    {
	         this.errorMessage = errorMessage;
	    }
	 
	    public String toString()
	    {
	         return errorMessage;
	    }
	 
	    public String getMessage()
	    {
	         return errorMessage;
	    }
}
