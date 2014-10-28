package dit.fundamentals.technology;

public class Bus {
	
	public String[] 				dataBus;
	public String[] 				addressBus;
	public boolean[]   				controlBus;
	private static final int 		BUS_SIZE 			 = 1;
	private final boolean 			MEMORY_CHIP_DISABLED = false;
	private final boolean   		WRITE_ENABLED		 = false;
	private final boolean           MEMORY_POWERED_ON    = true;
	
		
	
	public Bus()
	{
		addressBus = new String[BUS_SIZE];
		dataBus    = new String[BUS_SIZE];
		controlBus = new boolean[]{MEMORY_CHIP_DISABLED, WRITE_ENABLED, MEMORY_POWERED_ON};
	}		
	
	
	public synchronized void enqueueControlBus(boolean chipEnabled, boolean readWriteEnabled, boolean memoryPoweredOn)
	{
		controlBus[0] = chipEnabled;
		controlBus[1] = readWriteEnabled;
		controlBus[2] = memoryPoweredOn;
		
		if(controlBus[2]==false)
		{
			notify();
		}
	}
	
	
	public synchronized boolean[] dequeueControlBus()
	{
		if(controlBus[0]==false&&controlBus[2]==true)
		{
			notify();
			waitForNotify();
		}
		
		if(controlBus[0]==false&&controlBus[2]==false)
		{
			notify();
		}
		
		return controlBus;
	}
	

	/*adds address to the address bus*/
	public synchronized void enqueueAddressBus(String memoryAddress)
	{
		if(isGreaterThanByte(memoryAddress))
		{
			throw new AddressBusSizeExceededException("Cannot Enqueue Data Greater than Eight Bits On Address Bus");
		}
		
		addressBus[0] = memoryAddress;
		System.out.println("Address "+memoryAddress+" being pushed to Address Bus"+"  Thread "+ Thread.currentThread().getName());
		sleep(1000);
		notify();
		waitForNotify();
	}
	
	/*dequeues the address bus*/
	public synchronized String dequeueAddressBus() throws IllegalAccessException
	{
		if(busIsEmpty(addressBus))
		{
			waitForNotify();
		}
		
		String address = addressBus[0];
		System.out.println("Address "+address+" being removed from Address Bus"+"  Thread "+ Thread.currentThread().getName());
		sleep(1000);
		addressBus[0] = null;
		return address;
	}
	


	/*enqueues the data bus*/
	public synchronized void enqueueDataBus(String data)
	{
		if(isGreaterThanByte(data))
		{
			throw new DataBusSizeExceededException("Cannot Enqueue Data Greater than Eight Bits On Data Bus");
		}
		dataBus[0] = data;
		System.out.println("Data "+data+" being pushed onto Data Bus"+"  Thread "+ Thread.currentThread().getName());
		sleep(1000);
		notify();
		waitForNotify();
	}
	
	/*dequeues the data bus*/
	public synchronized String dequeueDataBus()
	{
		if(busIsEmpty(dataBus))
		{
			notify();
			waitForNotify();			
		}
		
		String data = dataBus[0];
		System.out.println("Data "+data+" being removed from Data Bus"+"  Thread "+ Thread.currentThread().getName());
		sleep(1000);
		dataBus[0] = null;
		
		return data;
	}
	
	/*Tests if the data bus contains data*/
	public boolean busIsEmpty(String[] bus)
	{
		return bus[0]==null;
	}
	
	/*Tests the size of Data enqueued onto the bus is of correct size*/
	private boolean isGreaterThanByte(String data)
	{
		return data.length()>8;
	}
	
	
	private void waitForNotify() 
	{
		try {
				wait();		
				
			} catch (InterruptedException e) 
			
			{		
				throw new RuntimeException("Problem with wait for notify() - Bus.class");
			}		
	}
	
		
	private void sleep(int milliSeconds)
	{
		try {
				Thread.sleep(milliSeconds);
				
			} catch (InterruptedException e) {
		
				throw new RuntimeException("Problem with sleep() - Bus.class");
			}
	}

}
