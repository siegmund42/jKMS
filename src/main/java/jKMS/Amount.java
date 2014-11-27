package jKMS;

public class Amount {

	private int relative;
	private int absolute;
	
	public Amount(int relative, int absolute)	{
		this.relative = relative;
		this.absolute = absolute;
	}

	public int getRelative() {
		return relative;
	}

	public void setRelative(int relative) {
		this.relative = relative;
	}

	public int getAbsolute() {
		return absolute;
	}

	public void setAbsolute(int absolute) {
		this.absolute = absolute;
	}
	
}
