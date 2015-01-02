package jKMS.exceptionHelper;

public class NoIntersectionException extends Exception{
	
	public NoIntersectionException()
    {
         super("Obviously there is no intersection of the supply and demand functions. Please correct your configuration of the game");
    }
}
