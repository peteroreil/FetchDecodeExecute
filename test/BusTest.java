import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import dit.fundamentals.technology.AddressBusSizeExceededException;
import dit.fundamentals.technology.Bus;
import dit.fundamentals.technology.DataBusSizeExceededException;


public class BusTest {

	private Bus bus;
	
	@Before
	public void setUp() throws Exception	
	{
		bus = new Bus();
	}

	
	@Test
	public void shouldDequeueDataBus() throws IllegalAccessException
	{
		bus.dataBus[0]="SomeData";
		assertEquals("SomeData", bus.dequeueDataBus());
	}
	
	
	@Test(expected = DataBusSizeExceededException.class)
	public void shouldNotAllowGreaterThanEightBitsOnDataBus()
	{
		bus.enqueueDataBus("someDataGreaterThanEightBits");
	}
	
	@Test(expected = AddressBusSizeExceededException.class)
	public void shouldNotAllGreaterThanEightBitsOnAddressBus()
	{
		bus.enqueueAddressBus("SomeAddressGreaterThanEightBits");
	}

}
