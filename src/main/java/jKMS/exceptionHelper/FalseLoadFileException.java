package jKMS.exceptionHelper;

	public class FalseLoadFileException extends Exception{
	    String errorMessage;
	 
	    public FalseLoadFileException(String errorMessage)
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
