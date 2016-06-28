import java.util.*;

public class Controller {
	UserGui guiObj;
	Sort	sortObj;
	Search	searchObj;
	static FileHandler fileHandlerObj;
	CommandFetch fetchObj = new CommandFetch();

//	String userCommand;
	
	public Controller(UserGui _gui){
		guiObj = _gui;
		
		sortObj = new Sort();
		searchObj = new Search();
		fileHandlerObj = new FileHandler();		

		//Example to create table ==============================================
		ArrayList<Field> newField = new ArrayList<Field>();
		newField.add(new Field(Field.TYPE.INTEGER,"ID"));
		newField.add(new Field(Field.TYPE.VARCHAR,"Name"));		
		Table testTable = new Table(newField,"testTable");

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
		System.out.println(testTable.toString());
		try{
			searchObj.doSearch(testTable, "Name like a");
		}catch(SearchException ex){
			System.out.println(ex.getMessage());
		}
		//Example to create table ==============================================
		
		//Call back function to update Table List : Parameters need to be defined
		guiObj.updateTableList();
		
		//Call back function to update Show Result : Parameters need to be defined		
		guiObj.updateContents();
	}
	
	public void getCommand(String _input){
		//Deliver this input string to command fetch
		System.out.println(_input); 	//test
		
		fetchObj.loader(_input); //Matt: something like this?
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// THIS SECTION FOR CREATING TABLES
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void createTable(String tName, ArrayList<String> colNames, ArrayList<String> dataTypes)
	{
		ArrayList<Field> theFields = new ArrayList<Field>();
		
		for (int i = 0; i < colNames.size(); i++)
		{
			Field.TYPE theType = getDataType(dataTypes.get(i));
			Field.KEY theKey = getKey(colNames.get(i));
			theFields.add(new Field(theKey, theType, colNames.get(i)));
		}
		
		Table newTable = new Table(theFields, tName);
		//I need to add this new table some sort of DB object?
		
		//DEBUG MESSAGE
		System.out.println(newTable.toString());
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
// THIS SECTION FOR INSERTING INTO TABLES
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void insertTable(String tName, ArrayList<String> fields, ArrayList<String> values)
	{
		Table activeTable = fileHandlerObj.getFile(tName);
		
		Record newRecord = new Record(activeTable);
		
		for (int i = 0; i < fields.size(); i++)
		{
			newRecord.addValue(new Value(activeTable.getField(fields.get(i)), values.get(i)));
		}
		
		//This method will then have to save the additions to the table
	}
	
	
}
