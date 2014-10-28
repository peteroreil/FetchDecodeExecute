import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import dit.fundamentals.technology.AddFiveValuesInstructionSet;
import dit.fundamentals.technology.InstructionSet;

public class TestAddFiveValuesInstructionSet {
	
	private InstructionSet program;
	
	@Before
	public void setUpTests()
	{
		program = new AddFiveValuesInstructionSet();
	}
	
	@Test
	public void shouldReturnAStringBinaryInstruction()
	{
		String instruction = program.getInstruction(5);
		assertEquals("00011010",instruction);
	}
	
	@Test
	public void shouldReturnLengthOfInstructionSet()
	{
		AddFiveValuesInstructionSet test = (AddFiveValuesInstructionSet)program;
		assertEquals(16,test.getNumOfProgramInstructions());
	}
}
