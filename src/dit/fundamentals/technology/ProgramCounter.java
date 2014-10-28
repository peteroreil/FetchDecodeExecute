package dit.fundamentals.technology;

public class ProgramCounter {

	private String address;
	
	public ProgramCounter(){}
	
	
	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public void incrementAddress()
	{
		int intValue = Integer.parseInt(this.address,2);
		intValue++;	
		String incrementedAddress = Integer.toBinaryString(intValue);
		address = ensureIsEightBits(incrementedAddress);
	}
	
	public String getAddress()
	{
		return this.address;
	}
	
	private String ensureIsEightBits(String value)
	{
		while(value.length()<8)
		{
			value = "0"+value;
		}
		
		return value;
	}
	
	public int getIntegerAddress()
	{
		return Integer.parseInt(address,2);	
	}
}
