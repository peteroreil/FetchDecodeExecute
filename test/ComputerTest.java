
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.InputMismatchException;
import org.junit.Before;
import org.junit.Test;
import dit.fundamentals.technology.Computer;


@SuppressWarnings("deprecation")
public class ComputerTest {

	private Computer    computer;
	private InputStream input;
	
	
	@Before
	public void setUpTest() 
	{
		computer = new Computer();
	}
	
	
	@Test
	(expected = InputMismatchException.class)
	public void shouldThrowExceptionIfNotValidInput()
	{
		input = new StringBufferInputStream("blah");
		computer.scanInput(input);		
	}
	
	
	@Test
	public void shouldNotThrowExceptionIfInSideRangeUpperBound()
	{
		input    = new StringBufferInputStream("255");
		int data = computer.scanInput(input);
		computer.validateInteger(data);
	}
	
	
	@Test
	public void shouldNotThrowExceptionIfInSideRangeLowerBound()
	{
		input = new StringBufferInputStream("0");
		int data = computer.scanInput(input);
		computer.validateInteger(data);
	}
	
	
	@Test
	(expected = InputMismatchException.class)
	public void shouldThrowExceptionIfOutSideRangeLowerBound()
	{
		input = new StringBufferInputStream("-1");
		int data = computer.scanInput(input);
		computer.validateInteger(data);
	}
	
	
	@Test
	(expected = InputMismatchException.class)
	public void shouldThrowExceptionIfOutSideRangeUpperBound()
	{
		input = new StringBufferInputStream("256");
		int data = computer.scanInput(input);
		computer.validateInteger(data);
	}
	
}
