package dit.fundamentals.technology;



public class CPU implements Runnable {

	private Bus 					bus;
	private ProgramCounter 			pc;
	private Register 				instructionRegister;
	private Register 				memoryAddressRegister;
	private Register 				dataRegisterOne;
	private Register 				dataRegisterTwo;
	private int                     totalNumOfInstructions;
	private static final boolean 	MEMORY_CHIP_DISABLED = false;
	private static final boolean	MEMORY_CHIP_ENABLED  = true;
	private static final boolean    WRITE_ENABLED		 = false;
	private static final boolean    READ_ENABLED  		 = true;
	private static final boolean    MEMORY_POWERED_ON    = true;
	
		
	
	public CPU(Bus bus, ProgramCounter pc)
	{
		this.totalNumOfInstructions = 0;
		this.bus              	   = bus;
		this.pc                    = pc;
		this.dataRegisterOne	   = new DataRegister();
		this.dataRegisterTwo       = new DataRegister();
		this.instructionRegister   = new InstructionRegister();
		this.memoryAddressRegister = new MemoryAddressRegister();
	}
	
	
	//The CPU's Thread execution - executes Fetch, Decode & Execute
	// and shuts down memory thread once finished.
	public void run()
	{
		
		while(pc.getIntegerAddress()<totalNumOfInstructions+16)
		{			
			fetch();		
			decodeAndExecute(instructionRegister.getRegisterData());			
		}
				
		shutDownMemory();
	}	
	
	
	// enables the read cycle on the control bus
	// gets the ProgramCounter's Value and stores it in MemoryAddressRegister.
	// Increments the ProgramCounter, enqueues the address from MemoryAddressRegister,
	// dequeues the data returned and places it in the instructionRegister.
	public void fetch()
	{
		enableRead();
		String address = pc.getAddress();
		memoryAddressRegister.setRegisterData(address);
		pc.incrementAddress();
		bus.enqueueAddressBus(memoryAddressRegister.getRegisterData());	
		instructionRegister.setRegisterData(dequeueDataBus());
	}
	
	
	// enqueues conditions to control bus to enable write conditions.
	public void enableWrite()
	{
		bus.enqueueControlBus(MEMORY_CHIP_ENABLED,WRITE_ENABLED,MEMORY_POWERED_ON);
	}
	
	
	// enqueues conditions to control bus to enable read conditions.
	public void enableRead()
	{
		bus.enqueueControlBus(MEMORY_CHIP_ENABLED, READ_ENABLED,MEMORY_POWERED_ON);
	}
	
	
	//enqueues conditions to control bus to return the memory chip to default settings.
	public void disableMemoryChip()
	{
		bus.enqueueControlBus(MEMORY_CHIP_DISABLED,WRITE_ENABLED,MEMORY_POWERED_ON);
	}
	
	
	//enqueues conditions to control bus to permanently "power off" memory.
	public void shutDownMemory()
	{
		bus.enqueueControlBus(MEMORY_CHIP_DISABLED,WRITE_ENABLED,!MEMORY_POWERED_ON);
	}
	

	// dequeues the dataBus
	private String dequeueDataBus() 
	{
		String data=null;
		data = bus.dequeueDataBus();
		return data;
	}
	
	
	// decodes a Instruction returned from Memory
	public void decodeAndExecute(String instruction)
	{
		String function			   = instruction.substring(0,2);
		String memAddress		   = instruction.substring(2,6);
		String dataRegID           = instruction.substring(6,instruction.length());
		DataRegister dataRegister  = new DataRegister();
				
				
		switch(function)
		{
			case "00" :	dataRegister = assignDataRegister(dataRegID);
						mov(memAddress,dataRegister);break;
					
				
			case "01" :	dataRegister = assignDataRegister(dataRegID);
						mov(dataRegister, memAddress);break;				
						
									
			default   : add(dataRegisterOne, dataRegisterTwo);break; 
		}
		
	}

	// returns the required DataRegister Object from the String parameter of 
	// which is a portion of the assembly code that refers to the DataRegister ID.
	public DataRegister assignDataRegister(String dataRegID)
	{
		DataRegister dataReg = new DataRegister();
		
		switch(dataRegID)
		{
			case "01" : dataReg = (DataRegister)dataRegisterOne; break;
			case "10" : dataReg = (DataRegister)dataRegisterTwo; break;
			default   : throw new IllegalArgumentException("No Such Data Register");
		}
		
		return dataReg;
	}

    //moves from dataRgister to specified memory address
	private void mov(DataRegister Dx2, String memAddress)
	{
		enableWrite();
		bus.enqueueAddressBus(memAddress);
		disableMemoryChip();
		bus.enqueueDataBus(dataRegisterTwo.getRegisterData());
		dataRegisterTwo.setRegisterData(null);		
	}


    // adds the data stored in each data register and 
	// either truncates or adds bits to ensure 8 bit result	
	public void add(Register Dx1, Register Dx2)
	{
		int dataOne = 0;
		int dataTwo = 0;
		try{
				dataOne = Integer.parseInt(Dx1.getRegisterData(),2);
			    dataTwo = Integer.parseInt(Dx2.getRegisterData(),2);
		
		}catch(NullPointerException npe){
			
			throw new IllegalArgumentException("Cannot Add Empty or Null Data Registers");
		}
		String result = Integer.toBinaryString(dataOne+dataTwo);
		
		while(result.length()>8) // truncating bits longer than 8 bits long
		{
			result = result.substring(1, result.length());
		}
		
		while(result.length()<8)// adding bits
		{
			result = "0"+result;
		}
				
		Dx2.setRegisterData(result);
		System.out.println("DataRegister Two Value "+Dx2.getRegisterData());
		Dx1.setRegisterData(null);		
	}


	// moves from specified memory location to dataRegister
	public void mov(String memAddress, DataRegister dataRegister)
	{
		enableRead();
		bus.enqueueAddressBus(memAddress);
		String data = dequeueDataBus();
		disableMemoryChip();
		dataRegister.setRegisterData(data);		
	}


    //getter for the Instruction Register Objects Data.
	public String getInstructionRegister()
	{
		return this.instructionRegister.getRegisterData();
	}
	
	// setter for the Instruction Register Objects Data.
	public void setInstructionRegister(String str)
	{
		this.instructionRegister.setRegisterData(str);
	}
	
	public void setTotalNumOfInstructions(int totalNumOfInstructions)
	{
		this.totalNumOfInstructions = totalNumOfInstructions;
	}
	
	//For Testing Purposes Only!!
	public void setDataRegisters(String dataOne, String dataTwo)
	{
		dataRegisterOne.setRegisterData(dataOne);
		dataRegisterTwo.setRegisterData(dataTwo);
	}
	
	//For Testing Purposes Only!!
	public Register getDataRegisterOne()
	{
		return this.dataRegisterOne;
	}
	
	//For Testing Purposes Only!!
	public Register getDataRegisterTwo()
	{
		return this.dataRegisterTwo;
	}
	
	//For Testing Purposes Only!!
	public Bus getBus()
	{
		return this.bus;
	}
		

}	