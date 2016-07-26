import java.util.*;

import javax.swing.JOptionPane;

/*
 * The purpose of this class is to take in the raw user input text (the command)
 * and interpret it; then calling the corresponding method with the command.
 */

public class CommandFetch 
{
	Controller ctrl;
	public String fullCommand;
	
	List<String> reservedWords = Arrays.asList("SELECT", "FROM", "INNER", "JOIN", "CREATE", "TABLE", "NOJOIN", "UPDATE", "IN", "SET", "TO", "DELETE", "ALL", "INSERT", "ORDERBY", "STRING", "INT", "DOUBLE", "DATE");
	List<String> supportedDataTypes = Arrays.asList("String", "int", "double");
	
	//loader: takes the input from the GUI text area as input; removing the line
	//breaks and placing the 'words' into a string array (command). This array is
	//then passed to the interpret method.
	
	public CommandFetch(Controller _ctrl){
		ctrl = _ctrl;
	}
	
	public void loader(String text)
	{
		text = text.replace("\n", " ");
		fullCommand = text;
		String[] command = text.split(" ");
		
		//DEBUG MESSAGE
		//System.out.println(fullCommand);
		

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
		
		if (commArr[0].equals("CREATE") && commArr[1].equals("TABLE"))
		{
			control = 1;
		}
		
		if (commArr[0].equals("DELETE"))
		{
			control = 2;
		}
		
		if (commArr[0].equals("INSERT"))
		{
			control = 3;
		}
		
		if (commArr[0].equals("UPDATE"))
		{
			control = 4;
		}
		
		if (commArr[0].equals("SELECT"))
		{
			control = 5;
		}
		
		try
		{
			switch(control)
			{
				case 0:
					JOptionPane.showMessageDialog(null, "ERROR: INVALID COMMAND", "whoops", JOptionPane.ERROR_MESSAGE);
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
					
				case 5:
					List<String> commList = Arrays.asList(commArr);
					callSelect(commList);
					break;
					
				default:
					JOptionPane.showMessageDialog(null, "ERROR: INVALID COMMAND", "whoops", JOptionPane.ERROR_MESSAGE);
					break;
			}
		}
		catch(ReservedWordException | DataTypeException | GeneralSyntaxException | ArrayIndexOutOfBoundsException | NumberFormatException ex)
		{
			if (ex instanceof NumberFormatException)
			{
				JOptionPane.showMessageDialog(null, "ERROR: The input data does not match the expected data type!", "whoops", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				JOptionPane.showMessageDialog(null, ex.getMessage(), "whoops", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	//callCreateTable: takes the command string array as a parameter; gets the
	//table name, and puts the data types (in string format) in one arraylist, and 
	//the field (column) names in another. Finally, it will call the create table
	//command, passing the table name and the two arraylists as parameters.
	public void callCreateTable(String[] command) throws ReservedWordException, DataTypeException, GeneralSyntaxException
	{
		String tableName = command[2];
		
		String data = fullCommand.split("\\(")[1];
		data = data.split("\\)")[0];
		data = data.trim();
		
		if (tableName.contains("("))
		{
			String[] s = tableName.split("\\(");
			tableName = s[0];
		}
		
		if (reservedWords.contains(tableName.toUpperCase()))
		{
			throw new ReservedWordException("ERROR: [" + tableName + "] is a reserved key word!");
		}
		
		//DEBUG MESSAGE
		System.out.println("CREATE TABLE DATA: " + data);
		System.out.println("TABLENAME: " + tableName);
		
		ArrayList<String> colNames = new ArrayList<String>();
		ArrayList<String> dataTypes = new ArrayList<String>();
		
		String dataArr[] = data.split(",");
		
		if (dataArr.length < 2)
		{
			throw new GeneralSyntaxException("ERROR: Cannot have a table with only one column!");
		}
		
		//DEBUG MESSAGE
		System.out.println("ARRAY: " + dataArr.toString());
		System.out.println("A0: " + dataArr[0]);
		System.out.println("A1: " + dataArr[1]);
		
		for (int i = 0; i < dataArr.length; i++)
		{
			dataArr[i] = dataArr[i].trim();
			String tempArr[] = dataArr[i].split(" ");
			
			if (i > 0)
			{
				if (colNames.contains(tempArr[0]))
				{
					throw new GeneralSyntaxException("ERROR: Duplicate column name detected!");
				}
			}
			
			if (reservedWords.contains(tempArr[0].toUpperCase()))
			{
				throw new ReservedWordException("ERROR: [" + tempArr[0] + "] is a reserved key word!");
			}
			
			if (!supportedDataTypes.contains(tempArr[1]))
			{
				throw new DataTypeException("ERROR: [" + tempArr[1] + "] is an invalid data type!");
			}

			colNames.add(tempArr[0]);
			dataTypes.add(tempArr[1]);
		}
		
		//DEBUG MESSAGE
		//System.out.println(colNames);
		//System.out.println(dataTypes);
		
		if (colNames.get(0).charAt(0) != 'P' || colNames.get(0).charAt(1) != 'K' || dataTypes.get(0).compareTo("int") != 0)
		{
			throw new GeneralSyntaxException("ERROR: Primary Key is not properly declared!");
		}
		//AND THEN I JUST PASS THE TWO ARRAYLISTS TO THE CREATE TABLE METHOD
		ctrl.createTable(tableName, colNames, dataTypes);
	}

	public void callDelete(String[] command) throws GeneralSyntaxException
	{
		String tableName;
		
		//All Rows
		if (command[1].equalsIgnoreCase("ALL") && command[2].equalsIgnoreCase("ROWS"))
		{
			tableName = command[3];
			Controller.deleteAllRows(tableName);
		}
		else
		{
			//Table
			if (command[1].equalsIgnoreCase("TABLE"))
			{
				
				tableName = command[2];
				
				//DEBUG
				System.out.println("CF: " + tableName);
				
				ctrl.deleteTable(tableName);
			}
			else
			{
				//Single Row
				if (command[2].equalsIgnoreCase("FROM"))
				{
					tableName = command[3];
					
					
					
					int PK = Integer.parseInt(command[1]);
					
					Controller.deleteRow(tableName, PK);
				}
				else
				{
					throw new GeneralSyntaxException("ERROR: Re-check DELETE command syntax");
				}
			}
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
		data = data.trim();
		
		String dataArr[] = data.split(",");
		
		for (int i = 0; i < dataArr.length; i++)
		{
			dataArr[i] = dataArr[i].trim();
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
	
	
	public void callSelect(List<String> command) throws GeneralSyntaxException
	{
		ArrayList<String> colNames = new ArrayList<String>();
		String joinTableName = "";
		
		boolean whereControl = false;
		boolean orderControl = false;		
		
		//DEBUG MESSAGE
		System.out.println(command);
				
			
		int tNameIDX = command.indexOf("FROM") + 1;
		
		//DEBUG MESSAGE
		System.out.println("INDEX IS " + tNameIDX);
		
		String tableName = command.get(tNameIDX);
		
		if (command.get(1).equalsIgnoreCase("*"))
		{
			colNames.add(command.get(1));
			
			//DEBUG MESSAGE
			System.out.println("SELECT ALL DETECTED");
		}
		else
		{
			for (int i = 1; i < tNameIDX - 1; i++)
			{
				String temp = command.get(i);
				String temp2 = temp.split(",")[0];
				temp2.trim();
				colNames.add(temp2);
			}
		}
		
		if (command.contains("INNER"))
		{
			int idx = command.indexOf("INNER");
			
			if (!command.get(idx+1).equalsIgnoreCase("JOIN"))
			{
				throw new GeneralSyntaxException("ERROR: Check syntax on Inner Join!");
			}
			
			joinTableName = command.get(idx + 2);	
		}
			
		if (command.contains("WHERE"))
		{
			whereControl = true;
		}
			
		if (command.contains("ORDERBY"))
		{
			orderControl = true;
		}
		
		
					
		//Controller SELECT call, pass:
			//tableName
			//joinTableName
			//colNames
			//whereControl
			//orderControl
			//fullString

			//DEBUG MESSAGE
			System.out.println("FULL COMMAND: " + command);
			System.out.println("TABLE NAME: " + tableName);
			System.out.println("JOIN TABLE NAME: " + joinTableName);
			System.out.println("FIELDS: " + colNames);
			String testcon = fetchWhere(command);
			System.out.println("WHERE CONDITION: " + testcon);
			String testfield = fetchField(command);
			System.out.println("ORDERBY FIELD: " + testfield);
			boolean testDir = fetchDir(command);
			System.out.println("ORDERBY BOOL: " + testDir);
			
			CommandSet selectC = new CommandSet();
			selectC.fullCommand = command;
			selectC.tableName = tableName;
			selectC.joinTableName = joinTableName;
			selectC.colNames = colNames;
			if(whereControl)
				selectC.whereC = fetchWhere(command);
			else
				selectC.whereC = "";
			if(orderControl)	
				selectC.orderC = fetchField(command);
			else
				selectC.orderC = "";
				
			
			selectC.orderDir = fetchDir(command);
			ctrl.doSelect(selectC);
	}
		
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//FOLLOWING SECTION FOR SELECT FILTERING
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public String fetchWhere(List<String> command)
	{
		String condition = "";
		
		int start = command.indexOf("WHERE") + 1;
		int end;
		
		if (command.contains("ORDERBY"))
		{
			end = command.indexOf("ORDERBY");
			if (!command.contains("DESC") || !command.contains("ASC")) {
				JOptionPane.showMessageDialog(null,"Please add \"ASC\" for ascending order or \"DESC\" for descending order");
			}
		}
		else
		{
			end = command.size();
		}
		
		for (int i = start; i < end; i++)
		{
			condition += command.get(i) + " ";
		}
		
		return condition;
	}
	
	public String fetchField(List<String> command)
	{
		int idx = command.indexOf("ORDERBY") + 1;
		String fieldName = command.get(idx);
		
		return fieldName;
	}
	
	public boolean fetchDir(List<String> command)
	{
		boolean sortDir;
		
		int idx = command.indexOf("ORDERBY") + 2;

		String direction = command.get(idx);	
		
		if (direction.contains(";"))
		{
			String s[] = direction.split(";");
			direction = s[0];
		}
		
		if (direction.equalsIgnoreCase("DESC"))
		{
			sortDir = true;
		}
		else
		{
			sortDir = false;
		}
		
		return sortDir;
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//FOLLOWING SECTION FOR ERROR HANDLING
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//This exception for if the user inputs a table or field name that is a reserved word
	class ReservedWordException extends Exception
	{
		public ReservedWordException(String message)
		{
			super(message);
		}
	}
	
	//This exception for if the user inputs a non-supported datatype
	class DataTypeException extends Exception
	{
		public DataTypeException(String message)
		{
			super(message);
		}
	}
	
	//This exception for general errors
	class GeneralSyntaxException extends Exception
	{
		public GeneralSyntaxException(String message)
		{
			super(message);
		}
	}
	
}

class CommandSet{
	List<String> fullCommand;
	String tableName ="";
	String joinTableName ="";
	ArrayList<String> colNames;
	String whereC ="";
	String orderC ="";
	boolean orderDir = false;
}


