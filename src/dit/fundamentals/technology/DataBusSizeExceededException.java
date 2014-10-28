package dit.fundamentals.technology;

public class DataBusSizeExceededException extends RuntimeException
{

	public DataBusSizeExceededException(){}
	
	public DataBusSizeExceededException(String excepMessage)
	{
		super(excepMessage);
	}
	
}

