package jKMS.exceptionHelper;

@SuppressWarnings("serial")
public class WrongRelativeDistributionException extends Exception{
 
	public WrongRelativeDistributionException(){
		super("relative Amounts of Distributions did not sum up to 100 % !");
	}
}
