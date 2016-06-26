import java.util.*;

/*
 * The purpose of this class is to take in the raw user input text (the command)
 * and interpret it; then calling the corresponding method with the command.
 */

public class CommandFetch 
{
	public String fullCommand;
	
	//loader: takes the input from the GUI text area as input; removing the line
	//breaks and placing the 'words' into a string array (command). This array is
	//then passed to the interpret method.
	public void loader(String text)
	{
		text = text.replace("\n", "");
		fullCommand = text;
		String[] command = text.split(" ");
		
		//DEBUG MESSAGE
		System.out.println(fullCommand);
		
		interpret(command);
	}
	
	//interpret: takes the string array and determines which command is being
	//called via the first letter of the command; changing the control integer
	//and then using said integer in a switch statement to call the corresponding
	//calling method.
	public void interpret(String[] commArr)
	{
		int control = 0;
		/*
		 * LIST OF CONTROL VALUES:
		 * 0 = default, return syntax error
		 * 1 = create table
		 * 2 = create intersection table?
		 * 3 = insert
		 * 4 = update
		 * 5 = select
		 */
		
		if (commArr[0].equalsIgnoreCase("CREATE") && commArr[1].equalsIgnoreCase("TABLE"))
		{
			control = 1;
		}
		
		switch(control)
		{
			case 0:
				//error message goes here?
				break;
				
			case 1:
				callCreateTable(commArr);
				break;
				
			default:
				//Syntax error should go here?
				break;
		}
		
	}
	
	//callCreateTable: takes the command string array as a parameter; gets the
	//table name, and puts the data types (in string format) in one arraylist, and 
	//the field (column) names in another. Finally, it will call the create table
	//command, passing the table name and the two arraylists as parameters.
	public void callCreateTable(String[] command)
	{
		String tableName = command[2];
		
		String data = fullCommand.split("\\(")[1];
		data = data.split("\\)")[0];
		
		if (tableName.contains("("))
		{
			String[] s = tableName.split("\\(");
			tableName = s[0];
		}
		
		//DEBUG MESSAGE
		System.out.println(data);
		System.out.println(tableName);
		
		ArrayList<String> colNames = new ArrayList<String>();
		ArrayList<String> dataTypes = new ArrayList<String>();
		
		String dataArr[] = data.split(",");
		
		for (int i = 0; i < dataArr.length; i++)
		{
			String tempArr[] = dataArr[i].split(" ");
			colNames.add(tempArr[0]);
			dataTypes.add(tempArr[1]);
		}
		
		//AND THEN I JUST PASS THE TWO ARRAYLISTS TO THE CREATE TABLE METHOD
		
		//DEBUG MESSAGE
		System.out.println(colNames);
		System.out.println(dataTypes);
	}
}
