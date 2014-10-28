package dit.fundamentals.technology;

public class MainMemory implements Runnable
{
	private static final int 			memSize = 32;
	private String[] 					memoryArray;
	private Bus 						bus;
		

	public MainMemory(Bus bus)
	{
		this.bus = bus;
		memoryArray = new String[memSize];
	}
	
	// Memory Thread
	public void run()
	{
		while(isMemoryChipEnabled())
		{
			if(isReadOperation())
			{
				String address = dequeueAddressBus();
				String data = read(address);
				bus.enqueueDataBus(data);
			}
			
			else if(isWriteOperation())
			{
				String address = dequeueAddressBus();
				System.out.println("Memory");
				String data = dequeueDataBus();
				write(address,data);
			}				
		}
		System.out.println("Finished");
	}
	

	// returns true if is a write operation
	private boolean isWriteOperation()
	{
		return (bus.controlBus[1]==false);
	}

	// returns true if is read operation
	private boolean isReadOperation() 
	{
		return bus.controlBus[1]==true&&bus.controlBus[2]==true;
	}

	// returns true if memory chip is enabled
	private boolean isMemoryChipEnabled() 
	{
		boolean[] controlArray = bus.dequeueControlBus();
		
		return controlArray[0];
	}

	// dequeues the address from the addressBus
	private String  dequeueAddressBus() 
	{
		String address = null;		
		
		try {
			
			address = bus.dequeueAddressBus();
			
		}catch (IllegalAccessException e) {
			
			throw new RuntimeException("dequeueAddressBus() - MainMemory.class");
		}
		
		return address;
	}
	
	// dequeues the dataBus
	private String dequeueDataBus()
	{
		String data = null;		
		data = bus.dequeueDataBus();
		return data;
	}
	
	
    // reads from a specified location in memory and returns the data from that location
	// as a String.
	public String read(String location) throws IllegalAccessError
	{
		try{			
			int index = convertBinaryStringToInt(location);	
			System.out.println("Memory: Reading Data from Memory Location "+location);
			return shouldEnsureEightBits(memoryArray[index]);
			
		}catch(ArrayIndexOutOfBoundsException aioobe){
			
			throw new IllegalAccessError("Cannot Read from this Memory Location, does not exist");
		}
			
	}
	
	
	//writes the data to the specified location in memory	
	public void write(String location, String data) throws IllegalAccessError
	{
		try{			
			int index = convertBinaryStringToInt(location);
			memoryArray[index] = data;
			location = shouldEnsureEightBits(location);
			
			System.out.println("Memory: Writing data "+data+" to memory location "+ location);
			
		}catch(ArrayIndexOutOfBoundsException aioobe){
			
			throw new IllegalAccessError("Cannot Write to this Memory Location, does not exist");
		}
	}
	
	
	
	
	//converts the String binary representation of data to an integer	
	private int convertBinaryStringToInt(String binary)
	{
		return Integer.parseInt(binary, 2);		
	}
	
	
	
	
	// used for loading a program into memory
	public void loadProgramIntoMainMemory(InstructionSet program)
	{
		for(int i = memSize/2; i < memSize; i++)
		{
			memoryArray[i]= program.getInstruction(i-memSize/2);
		}
	}
	
	public String shouldEnsureEightBits(String data)
	{
		while(data.length()<8)
		{
			data = "0"+data;
		}		
				
		return data;
	}
	
	
}
