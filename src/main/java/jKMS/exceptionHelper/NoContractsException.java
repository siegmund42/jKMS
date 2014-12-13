package jKMS.exceptionHelper;

public class NoContractsException extends Exception{
 
    public NoContractsException()
    {
         super("There are no contracts inserted yet!");
    }
 
}