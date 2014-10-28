import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import dit.fundamentals.technology.AddFiveValuesInstructionSet;
import dit.fundamentals.technology.InstructionSet;
import dit.fundamentals.technology.MainMemory;


public class TestMemoryClass {

private	MainMemory memory;
private InstructionSet program;
	
	@Before
	public void setUpTest()
	{
		memory = new MainMemory(null);
		program = new AddFiveValuesInstructionSet();
		memory.loadProgramIntoMainMemory(program);	
	}
	
	@Test
	public void shouldAddAndReturnDataToArray()
	{
		memory.write("0000101", "11111100001");
		String data = memory.read("0000101");
		assertEquals("11111100001",data);
	}
	
	@Test
	public void shouldHaveProgramInMemory()
	{
		String programCodeStart = memory.read("10000");
		String programCodeEnd = memory.read("11111");		
		assertEquals("00000001",programCodeStart);
		assertEquals("01011010",programCodeEnd);		
	}
	
	@Test(expected=IllegalAccessError.class)
	public void writeOutOfBounds()
	{
		memory.write("01000000","");
	}
	
	@Test(expected=IllegalAccessError.class)
	public void readOutOfBounds()
	{
		memory.read("01000000");
	}
	
}
