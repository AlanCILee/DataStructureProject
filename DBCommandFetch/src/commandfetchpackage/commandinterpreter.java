package commandfetchpackage;
import java.util.*;

/*
 * This class will contain methods which will first:
 * 	1. load the typed user input into a string
 * 	2. a first stage method that determines what command is being called
 * 	3. call the correct second stage method that will further break down the string 
 */

public class commandinterpreter 
{	
	//BELOW METHOD IS WHAT'S CALLED BY THE EXECUTE BUTTON ON THE GUI
	//this method accepts a string (the text from the GUI's input field) and loads it into the string array
	//it then passes the command string to the interpret method
	public void loadCommand(String text)
	{
		text = text.replace("\n", "");
		String[] command = text.split(" ");
		
		//console display for debug only
		for (int i = 0; i < command.length; i++)
		{
			System.out.print(command[i] + " ");
		}
		System.out.print("\n");
		
		interpret(command);
	}
	
	//this method will determine what command is being executed and call the correct second stage
	public void interpret(String[] commandArr)
	{
		if (commandArr[0].equalsIgnoreCase("CREATE") && commandArr[1].equalsIgnoreCase("TABLE"))
		{		
			String tableName = commandArr[2];
			
			if (tableName.contains("("))
			{
				String[] s = tableName.split("\\(");
				tableName = s[0];
			}
			
			System.out.println(tableName);
		}
	}
	
	
}
