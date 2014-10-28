import static org.junit.Assert.*;

import org.junit.Test;

import dit.fundamentals.technology.ProgramCounter;


public class ProgramCounterTest {
	
	private ProgramCounter pc;
	

	@Test
	public void shouldIncrementByOne()
	{
		pc = new ProgramCounter();
		pc.setAddress("00000011");
		pc.incrementAddress();
		String address = pc.getAddress();
		assertEquals("00000100",address);
	}

}
