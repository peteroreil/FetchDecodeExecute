import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


import dit.fundamentals.technology.Bus;
import dit.fundamentals.technology.CPU;
import dit.fundamentals.technology.MainMemory;
import dit.fundamentals.technology.ProgramCounter;


public class CPUTest
{
	private Bus 					bus;
	private CPU 					cpu;
	private MainMemory 				memory;
	private ProgramCounter  		pc;
	private Thread 					cpuThread;
	private Thread 					memoryThread;
	private static final boolean 	MEMORY_CHIP_DISABLED = false;
	private static final boolean	MEMORY_CHIP_ENABLED  = true;
	private static final boolean    WRITE_ENABLED		 = false;
	private static final boolean    READ_ENABLED  		 = true;
	private static final boolean    MEMORY_POWERED_ON    = true;
	
	private void joinThreads(Thread threadOne, Thread threadTwo)
	{
		try{
			threadOne.join();
			threadTwo.join();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Before
	public void setUp() throws Exception
	{
		bus = new Bus();
		pc = new ProgramCounter();
		cpu = new CPU(bus,pc);	
		memory = new MainMemory(bus);
	}

	
	@Test
	public void shouldExecuteMovMovAddMovInstructions()
	{
		memory.write("00010000", "00000001");//Move out of (00) memory location (0000) to data register one (01)
		memory.write("00010001", "00000110"); //Move out of (00) memory location (0001) to data register two (10)
		memory.write("00010010", "10000110"); //Add (1000) data register one (01) to data register two (10)
		memory.write("00010011", "01011010");// move from data register two to memory location 6
		memory.write("00000000", "11000100");// data stored in location 3
		memory.write("00000001", "11001100");// data stored in location 16
		
		pc.setAddress("00010000");
		cpu.setTotalNumOfInstructions(4);
		cpuThread = new Thread(cpu);
		memoryThread = new Thread(memory);
		cpuThread.start();
		memoryThread.start();
		joinThreads(cpuThread, memoryThread);
		
		assertEquals("10010000",memory.read("00000110"));		
	}
	
	
	@Test
	public void shouldAddRegisters()
	{
		cpu.setDataRegisters("00000000","00000000");
		cpu.add(cpu.getDataRegisterOne(), cpu.getDataRegisterTwo());
		assertEquals("00000000",cpu.getDataRegisterTwo().getRegisterData());
	}
	
	@Test
	public void shouldAddAndTruncateRegistersWhenResultExceedsEightBits()
	{
		cpu.setDataRegisters("11111111","11111111");
		cpu.add(cpu.getDataRegisterOne(), cpu.getDataRegisterTwo());
		assertEquals("11111110",cpu.getDataRegisterTwo().getRegisterData());
	}
	
	@Test
	(expected = IllegalArgumentException.class)
	public void shouldNotAddWhenRegisterIsNull()
	{
		cpu.setDataRegisters("11111111",null);
		cpu.add(cpu.getDataRegisterOne(), cpu.getDataRegisterTwo());
	}
	
	@Test
	public void shouldAddBitsIfLessThanEightBitResult()
	{
		cpu.setDataRegisters("1","10");
		cpu.add(cpu.getDataRegisterOne(), cpu.getDataRegisterTwo());
		assertEquals("00000011",cpu.getDataRegisterTwo().getRegisterData());
	}
	
	@Test
	public void defaultSettingsOfSettingControlBus()
	{
		assertEquals(MEMORY_CHIP_DISABLED, cpu.getBus().controlBus[0]); // disabled
		assertEquals(WRITE_ENABLED,cpu.getBus().controlBus[1]);         // Write enabled
		assertEquals(MEMORY_POWERED_ON ,cpu.getBus().controlBus[2]);    // turned on
	}
	
	@Test
	public void shouldEnableWrite()
	{
		cpu.enableWrite();
		assertEquals(MEMORY_CHIP_ENABLED, cpu.getBus().controlBus[0]); // disabled
		assertEquals(WRITE_ENABLED,cpu.getBus().controlBus[1]);         // Write enabled
		assertEquals(MEMORY_POWERED_ON ,cpu.getBus().controlBus[2]);    // turned on		
	}
	
	@Test
	public void shouldEnableRead()
	{
		cpu.enableRead();
		assertEquals(MEMORY_CHIP_ENABLED, cpu.getBus().controlBus[0]); // disabled
		assertEquals(READ_ENABLED,cpu.getBus().controlBus[1]);         // Read enabled
		assertEquals(MEMORY_POWERED_ON ,cpu.getBus().controlBus[2]);    // turned on		
	}
	
	@Test
	public void shouldDisableMemory()
	{
		cpu.disableMemoryChip();
		assertEquals(MEMORY_CHIP_DISABLED, cpu.getBus().controlBus[0]); // disabled
		assertEquals(WRITE_ENABLED,cpu.getBus().controlBus[1]);         // Write enabled
		assertEquals(MEMORY_POWERED_ON ,cpu.getBus().controlBus[2]);    // turned on
	}
	
	@Test
	public void shouldShutDownMemory()
	{
		cpu.shutDownMemory();
		assertEquals(MEMORY_CHIP_DISABLED, cpu.getBus().controlBus[0]); // disabled
		assertEquals(WRITE_ENABLED,cpu.getBus().controlBus[1]);         // Write enabled
		assertEquals(!MEMORY_POWERED_ON ,cpu.getBus().controlBus[2]);    // turned OFF
	}
	
	@Test
	public void shouldReturnDataRegisterObject()
	{
		String dataRegID = "10";		
		assertNotNull(cpu.assignDataRegister(dataRegID));
	}
	
	@Test
	public void shouldAssignDataRegisterOne()
	{
		String dataRegID = "01";		
		assertEquals(cpu.getDataRegisterOne(),cpu.assignDataRegister(dataRegID));
	}
	
	@Test
	public void shouldAssignDataREgisterTwo()
	{
		String dataRegID = "10";		
		assertEquals(cpu.getDataRegisterTwo(),cpu.assignDataRegister(dataRegID));
	}
	
	@Test
	(expected = IllegalArgumentException.class)
	public void shouldNotAssignDataRegisterIfNotExist()
	{
		String dataRegID = "00";		
		cpu.assignDataRegister(dataRegID);
	}

	
}
