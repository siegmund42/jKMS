package jKMS.exceptionHelper;

@SuppressWarnings("serial")
public class NoContractsException extends Exception{
 
    public NoContractsException()
    {
         super("There are no contracts inserted yet!");
    }
 
}