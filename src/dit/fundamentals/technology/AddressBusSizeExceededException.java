package dit.fundamentals.technology;

public class AddressBusSizeExceededException extends RuntimeException
{
	public AddressBusSizeExceededException(){}
	
	public AddressBusSizeExceededException(String exceptionMessage)
	{
		super(exceptionMessage);
	}
	
}
