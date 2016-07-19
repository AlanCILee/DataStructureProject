import java.util.*;

import javax.swing.JOptionPane;

public class Controller {
	UserGui guiObj;
	Sort	sortObj;
	Search	searchObj;
	static FileHandler fileHandlerObj;
	CommandFetch fetchObj = new CommandFetch(this);
	static Table testTable;
//	String userCommand;
	
	public Controller(UserGui _gui){
		guiObj = _gui;
		
		sortObj = new Sort();
		searchObj = new Search();
		fileHandlerObj = new FileHandler();		

		//Example to create table ==============================================
		ArrayList<Field> newField = new ArrayList<Field>();
		newField.add(new Field(Field.KEY.PRIMARY, Field.TYPE.INTEGER,"ID"));
		newField.add(new Field(Field.TYPE.VARCHAR,"Name"));		
		testTable = new Table(newField,"testTable");

		//Example to add rows on table =========================================		
		String names[] = {"Alan Lee","Matt","Ronnie","Joy","Park"};
		for (int i=0;i<names.length;i++){
			
			Record newRecord = new Record(testTable);			// create record : specify table to check added data type	
			newRecord.addValue(new Value(testTable.getField("Name"),names[i]));
			newRecord.addValue(new Value(testTable.getField("ID"),i));
			
			if(!testTable.addRow(newRecord)){
				System.out.println("Failed to add row");
			}
		}		
		
//TEST		sortObj.orderBy(testTable, "ID", true);
		
		
		System.out.println(testTable.toString());
		try{
			searchObj.doSearch(testTable, "ID >= 1 && name like a");
		}catch(SearchException ex){
			System.out.println(ex.getMessage());
		}
		//Example to create table ==============================================
		
		//Call back function to update Table List : Parameters need to be defined
	//	guiObj.updateTableList();
		
		//Call back function to update Show Result : Parameters need to be defined		
		guiObj.updateContents(testTable);
	}
	
	public void getCommand(String _input){
		//Deliver this input string to command fetch
		System.out.println(_input); 	//test
		
		fetchObj.loader(_input); //Matt: something like this?
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// THESE METHODS FOR CREATING TABLES
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void createTable(String tName, ArrayList<String> colNames, ArrayList<String> dataTypes)
	{
		ArrayList<Field> theFields = new ArrayList<Field>();
		
		try
		{
			for (int i = 0; i < colNames.size(); i++)
			{
				Field.TYPE theType = getDataType(dataTypes.get(i));
				Field.KEY theKey = getKey(colNames.get(i));
	
				if (theKey == Field.KEY.FOREIGN)
				{
					if (theType != Field.TYPE.INTEGER)
					{
						throw new DataFormatException("ERROR: Foreign Keys must be an integer");
					}
					
					String refTable = JOptionPane.showInputDialog(null, "Please enter the reference table for the field " + colNames.get(i), "Foreign Key", JOptionPane.INFORMATION_MESSAGE);
					Field aField = new Field(theKey, theType, colNames.get(i));
	
					aField.setForeignKey(refTable);
					theFields.add(aField);
				}
				else
				{
					theFields.add(new Field(theKey, theType, colNames.get(i)));
				}
			}
			
			Table newTable = new Table(theFields, tName);
			//I need to add this new table some sort of DB object?
			fileHandlerObj.setFile(tName + ".txt", newTable);
			
			//DEBUG MESSAGE
			System.out.println(newTable.toString());
		}
		catch(DataFormatException z)
		{
			JOptionPane.showMessageDialog(null, z.getMessage(), "whoops", JOptionPane.ERROR_MESSAGE);
		}	
	}
	
	//this interprets the data type
	public static Field.TYPE getDataType(String a)
	{
		Field.TYPE temp = Field.TYPE.NULL;
		
		if (a.equalsIgnoreCase("STRING"))
		{
			temp = Field.TYPE.VARCHAR;
		}
		else
		{
			if (a.equalsIgnoreCase("INT"))
			{
				temp = Field.TYPE.INTEGER;
			}
			else
			{
				if (a.equalsIgnoreCase("DOUBLE"))
				{
					temp = Field.TYPE.DOUBLE;
				}
				else
				{
					if (a.equalsIgnoreCase("DATE"))
					{
						temp = Field.TYPE.DATE;
					}
				}
			}
		}
		
		return temp;
	}
	
	//this interprets the key
	public static Field.KEY getKey(String a)
	{
		Field.KEY temp;
		
		if (a.charAt(0) == 'P' && a.charAt(1) == 'K')
		{
			temp = Field.KEY.PRIMARY;
		}
		else
		{
			if (a.charAt(0) == 'F' && a.charAt(1) == 'K')
			{
				temp = Field.KEY.FOREIGN;
			}
			else
			{
				temp = Field.KEY.NORMAL;
			}
		}
		return temp;
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// THIS METHOD FOR INSERTING INTO TABLES (adding records?)
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void insertTable(String tName, ArrayList<String> fields, ArrayList<String> values)
	{
		Table activeTable = fileHandlerObj.getFile(tName + ".txt");
		
		Record newRecord = new Record(activeTable);
		
		for (int i = 0; i < fields.size(); i++)
		{
			newRecord.addValue(new Value(activeTable.getField(fields.get(i)), values.get(i)));
		}
		
		activeTable.addRow(newRecord);
		
		//DEBUG MESSAGE
		System.out.println(activeTable.toString());
		
		//This method will then have to save the additions to the table
		fileHandlerObj.setFile(tName + ".txt", activeTable);
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//THESE METHODS FOR DELETING THINGS
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void deleteAllRows(String tName)
	{
		Table activeTable = fileHandlerObj.getFile(tName + ".txt");
		
		activeTable.alRecord.clear();
		
		//TABLE SAVE GOES HERE
		fileHandlerObj.setFile(tName + ".txt", activeTable);
	}
	
	public static void deleteTable(String tName)
	{
		//PLEASE DOUBLE CHECK WITH PARK TO SEE IF THIS WILL WORK
		fileHandlerObj.deleteFile(tName + ".txt");
	}
	
	public static void deleteRow(String tName, int pKey)
	{
		Table activeTable = fileHandlerObj.getFile(tName + ".txt");
		
		boolean found = false;
		
		for (int i = 0; i < activeTable.alRecord.size(); i++)
		{
			for (int z = 0; z < activeTable.alField.size(); z++)
			{
				if (activeTable.alRecord.get(i).getAlRecord().get(z).compareTo(String.valueOf(pKey)) == 0 && activeTable.alField.get(z).fKey == Field.KEY.PRIMARY)
				{
					activeTable.alRecord.remove(i);
					found = true;
					break;
				}
			}
			
			if (found)
				break;
		}
		
		//and then a table save/display update goes here
		fileHandlerObj.setFile(tName + ".txt", activeTable);
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//THIS METHOD FOR UPDATING FIELDS IN TABLES
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void updateField(String tName, int pKey, String field, String value)
	{
		Table activeTable = fileHandlerObj.getFile(tName + ".txt");
		//Table activeTable = testTable;
		
		//DEBUG MESSAGE
		System.out.println("BEFORE: " + testTable.toString());
		
		//find the correct index using the PK
		int row = 0;
		
		boolean found = false;
		for (int i = 0; i < activeTable.alRecord.size(); i++)
		{
			for (int z = 0; z < activeTable.alField.size(); z++)
			{
				if (String.valueOf(activeTable.alRecord.get(i).getAlRecord().get(z).data).compareTo(String.valueOf(pKey)) == 0 && activeTable.alField.get(z).fKey == Field.KEY.PRIMARY)
				{
					row = i;
					found = true;
					
					//DEBUG MESSAGE
					System.out.println("THE ROW IS " + i);
					
					break;
				}
			}
			
			if (found)
				break;
		}
		
		for (int i = 0; i < activeTable.alField.size(); i++)
		{
			if (activeTable.alRecord.get(row).getAlRecord().get(i).field.compareTo(field) == 0)
			{
				//DEBUG MESSAGE
				System.out.println("TEST");
				
				activeTable.alRecord.get(row).getAlRecord().get(i).data = value;
			}
		}
		
		//DEBUG MESSAGE
		System.out.println("AFTER: " + activeTable.toString());
		
		//TABLE SAVE GOES HERE
		fileHandlerObj.setFile(tName + ".txt", activeTable);
	}
	

}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//CONTROLLER-SIDE ERROR HANDLING GOES IN THIS SECTION
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//This exception for data not matching the datatype
	class DataFormatException extends Exception
	{
		public DataFormatException(String message)
		{
			super(message);
		}
	}
	
	//this exception if a table or field does not exist
	class CriticalExistanceFailure extends Exception
	{
		public CriticalExistanceFailure(String message)
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
