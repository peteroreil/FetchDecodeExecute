package dit.fundamentals.technology;

public class AddFiveValuesInstructionSet implements InstructionSet {

	private String[] program;
	
	
	public int getNumOfProgramInstructions()
	{
		return program.length;		
	}
	
	public AddFiveValuesInstructionSet()
	{
		createNewProgram();
	}
	
	public String getInstruction(int index)
	{
		return program[index];
	}

	
	public void createNewProgram()
	{
		program = new String[16];
		program[0]="00000001"; //Move out of (00) memory location (0000) to data register one (01)
		program[1]="00000110"; //Move out of (00) memory location (0001) to data register two (10)
		program[2]="10000110"; //Add (1000) data register one (01) to data register two (10)
		program[3]="01011010"; //Move from data register (01), register two (10) to memory location 6 (0110)
		program[4]="00001001"; //Move out of (00) memory location (0010) to data register one (01)
		program[5]="00011010"; //Move out of (00) memory location (0110) to data register two (10)
		program[6]="10000110"; //Add (1000) data register one (01) to data register two (10)
		program[7]="01011010"; //Move from data register (01), register two (10) to memory location 6 (0110)
		program[8]="00001101"; //Move out of (00) memory location (0011) to data register one (01)
		program[9]="00011010"; //Move out of (00) memory location (0110) to data register two (10)
		program[10]="10000110"; //Add (1000) data register one (01) to data register two (10)
		program[11]="01011010"; //Move from data register (01), register two (10) to memory location 6 (0110)
		program[12]="00010001"; //Move out of (00) memory location (0100) to data register one (01)
		program[13]="00011010"; //Move out of (00) memory location (0110) to data register two (10)
		program[14]="10000110"; //Add (1000) data register one (01) to data register two (10)
		program[15]="01011010"; //Move from data register (01), register two (10) to memory location 6 (0110)		
	}
}
