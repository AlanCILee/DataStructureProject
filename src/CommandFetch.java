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
		 * 2 = delete
		 * 3 = insert
		 * 4 = update
		 * 5 = select
		 */
		
		if (commArr[0].equalsIgnoreCase("CREATE") && commArr[1].equalsIgnoreCase("TABLE"))
		{
			control = 1;
		}
		
		if (commArr[0].equalsIgnoreCase("DELETE"))
		{
			control = 2;
		}
		
		if (commArr[0].equalsIgnoreCase("INSERT"))
		{
			control = 3;
		}
		
		if (commArr[0].equalsIgnoreCase("UPDATE"))
		{
			control = 4;
		}
		
		switch(control)
		{
			case 0:
				//error message goes here?
				break;
				
			case 1:
				callCreateTable(commArr);
				break;
				
			case 2:
				callDelete(commArr);
				break;
				
			case 3:
				callInsert(commArr);
				break;
			
			case 4:
				callUpdate(commArr);
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
		//System.out.println(data);
		//System.out.println(tableName);
		
		ArrayList<String> colNames = new ArrayList<String>();
		ArrayList<String> dataTypes = new ArrayList<String>();
		
		String dataArr[] = data.split(",");
		
		for (int i = 0; i < dataArr.length; i++)
		{
			String tempArr[] = dataArr[i].split(" ");
			colNames.add(tempArr[0]);
			dataTypes.add(tempArr[1]);
		}
		
		//DEBUG MESSAGE
		//System.out.println(colNames);
		//System.out.println(dataTypes);
		
		//AND THEN I JUST PASS THE TWO ARRAYLISTS TO THE CREATE TABLE METHOD
		Controller.createTable(tableName, colNames, dataTypes);
		
		
	}

	public void callDelete(String[] command)
	{
		String tableName;
		
		if (command[1].equalsIgnoreCase("ALL") && command[2].equalsIgnoreCase("ROWS"))
		{
			tableName = command[3];
			Controller.deleteAllRows(tableName);
		}
		
		if (command[1].equalsIgnoreCase("TABLE"))
		{
			tableName = command[2];
			Controller.deleteTable(tableName);
		}
		
		if (command[2].equalsIgnoreCase("FROM"))
		{
			tableName = command[3];
			int PK = Integer.parseInt(command[1]);
			
			Controller.deleteRow(tableName, PK);
		}
	}
	
	//This method operates in very much they same way as callCreateTable.
	//It first takes the raw input string and grabs the table name from it, then moves one to
	//fill two string arraylists with the field names and the values in string format. It then
	//passes these 3 as parameters to the Controller's insertTable function.
	public void callInsert(String[] command)
	{
		String tableName = command[1];
		
		if (tableName.contains("("))
		{
			String[] s = tableName.split("\\(");
			tableName = s[0];
		}
		
		ArrayList<String> values = new ArrayList<String>();
		ArrayList<String> fNames = new ArrayList<String>();
		
		String data = fullCommand.split("\\(")[1];
		data = data.split("\\)")[0];
		
		String dataArr[] = data.split(",");
		
		for (int i = 0; i < dataArr.length; i++)
		{
			String tempArr[] = dataArr[i].split(" ");
			fNames.add(tempArr[0]);
			values.add(tempArr[1]);
		}
		
		Controller.insertTable(tableName, fNames, values);
	}
	
	public void callUpdate(String[] command)
	{
		String tableName = command[3];
		int PK = Integer.parseInt(command[1]);
		String fName = command[5];
		String value = command[7];
		
		Controller.updateField(tableName, PK, fName, value);
	}
}

