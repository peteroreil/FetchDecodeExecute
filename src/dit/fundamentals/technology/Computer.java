package dit.fundamentals.technology;

import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Computer 
{
	private CPU 			cpu;
	private MainMemory		memory;
	private InstructionSet 	program;
	private Bus				systemBus;
	private ProgramCounter  pc;
	private Scanner         scan;
	private Thread          cpuThread;
	private Thread          memoryThread;

	public Computer()
	{
		pc        = new ProgramCounter();
		systemBus = new Bus();
		cpu 	  = new CPU(systemBus,pc);
		memory 	  = new MainMemory(systemBus);
		program   = new AddFiveValuesInstructionSet();
	}
	
	
	private void loadProgram(InstructionSet is)
	{
		System.out.println("Loading Program");
		memory.loadProgramIntoMainMemory(is);
		cpu.setTotalNumOfInstructions(is.getNumOfProgramInstructions());
		sleepComputer(1000);
		pc.setAddress("00010000");	
	}
	
	
	private void sleepComputer(int i)
	{
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
	}


	public void getUserInput()
	{
		boolean isError = false;
		int count       = 0;
		
		while(count<5)
		{
			int input = 0;
						
			do{
				try{					
	
					System.out.println("Please enter in your unsigned byte value");			
					input = scanInput(System.in);													
					validateInteger(input);					
					isError = false;
					
				}catch(InputMismatchException ime)
				{
					isError = true;
					System.out.println("That is not a valid input");					
				}
			
			}while(isError);
						    
				String data 	 = Integer.toBinaryString(input);
				String location  = Integer.toBinaryString(count);
				memory.write(location, data);
				count++;				
		}
	}
	
	
	
	public boolean requestStartProgram()
	{
		Scanner newScan = new Scanner(System.in);
		System.out.println("                 Welcome to TechFundamentals CPU Model");
		System.out.println("*************************************************************************");
		System.out.println("           \n To load program press 'Y' or any key to exit.");
		String input = newScan.next();
		
		return input.toLowerCase().equals("y");
	}
	
	public MainMemory getMemory()
	{
		return this.memory;
	}
	
	
	public void validateInteger(int input) throws InputMismatchException
	{
		if(input>255||input<0)
		{
			throw new InputMismatchException("outside range of a byte");
		}
	}
	
	
	public int scanInput(InputStream input) throws InputMismatchException
	{
		scan  = new Scanner(input);
		return scan.nextInt();
	}
	
	private void startProgram()
	{
		cpuThread    = new Thread(cpu);
		memoryThread = new Thread(memory);
		cpuThread.start();
		memoryThread.start();
		join(cpuThread, memoryThread);
	}

	
	private void join(Thread threadOne, Thread threadTwo)
	{		
		try{
			threadOne.join();
			threadTwo.join();
		}catch(Exception e)
		{
			throw new  RuntimeException("Problem Joining Threads - Computer.class");
		}		
	}


	public void runAssignment()
	{
		if(requestStartProgram())
		{
			getUserInput();
			loadProgram(program);
			startProgram();
			displayResult();
		}
		else
		{
			System.out.println("Goodbye");
			System.exit(-1);			
		}
	}
	
	private void displayResult() 
	{
		String result = memory.read("00000110");
		System.out.println("************************************************************");
		System.out.println("Result of Program Stored at memory location: 00000110\n is "+result);
		System.out.println("Result is :"+Integer.parseInt(result,2));                                                                  
		System.out.println("************************************************************");
		
	}


	public static void main(String[] funwithcpus)
	{
		Computer comp = new Computer();
		comp.runAssignment();
	}
}


	
