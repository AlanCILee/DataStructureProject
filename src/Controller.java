import java.util.*;

import javax.swing.JOptionPane;

public class Controller {
	UserGui guiObj;
	Sort	sortObj;
	Search	searchObj;
	static FileHandler fileHandlerObj;
	CommandFetch fetchObj = new CommandFetch(this);
	static Table testTable;
	public static String aTable = "";
	
	boolean flag = true;
	Table selectTable = new Table("sTbl");
	
//	String userCommand;
	
	public Controller(UserGui _gui){
		guiObj = _gui;
		
		sortObj = new Sort();
		searchObj = new Search();
		fileHandlerObj = new FileHandler();		
	}
	
	public Table getCommand(String _input){
		//Deliver this input string to command fetch
		System.out.println("Before TEST------"+_input); 	//test
		
		String strUpperCmd = _input;
		strUpperCmd = strUpperCmd.replace("select", "SELECT");
		strUpperCmd = strUpperCmd.replace("from", "FROM");
		strUpperCmd = strUpperCmd.replace("where", "WHERE");
		strUpperCmd = strUpperCmd.replace("create", "CREATE");
		strUpperCmd = strUpperCmd.replace("table", "TABLE");
		strUpperCmd = strUpperCmd.replace("delete", "DELETE");
		strUpperCmd = strUpperCmd.replace("insert", "INSERT");
		strUpperCmd = strUpperCmd.replace("update", "UPDATE");
		strUpperCmd = strUpperCmd.replace("inner", "INNER");
		strUpperCmd = strUpperCmd.replace("orderby", "ORDERBY");
		strUpperCmd = strUpperCmd.replace("order by", "ORDERBY");
		strUpperCmd = strUpperCmd.replace("ORDER BY", "ORDERBY");		
		strUpperCmd = strUpperCmd.replace("asc", "ASC");
		strUpperCmd = strUpperCmd.replace("desc", "DESC");
		
		//check order by and put the default order
		if(strUpperCmd.indexOf("ORDERBY") != -1)
		{
			if(strUpperCmd.indexOf("ASC") == -1 &&
			   strUpperCmd.indexOf("DESC") == -1)								
			{
				strUpperCmd = strUpperCmd + " ASC";
			}			
		}
		
		System.out.println("After TEST------"+ strUpperCmd); 	//test
				
		fetchObj.loader(strUpperCmd); //Matt: something like this?
		
		if (!flag)
		{
			flag = true;
			return selectTable;
		}
		else
		{
			return fileHandlerObj.getFile(aTable + ".txt");
		}

		
		//return fileHandlerObj.getFile(aTable + ".txt");
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
					
					if (fileHandlerObj.getFile(refTable + ".txt") == null)
					{
						throw new CriticalExistanceFailure("ERROR: Referenced Table [" + refTable + "] doesn't exist");
					}
					
					aField.setForeignKey(refTable);
					theFields.add(aField);
				}
				else
				{
					theFields.add(new Field(theKey, theType, colNames.get(i)));
				}
				
				aTable = tName;
			}
			
			Table newTable = new Table(theFields, tName);
			//I need to add this new table some sort of DB object?
			fileHandlerObj.setFile(tName + ".txt", newTable);
			
			//DEBUG MESSAGE
			System.out.println(newTable.toString());
		}
		catch(DataFormatException | CriticalExistanceFailure z)
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
		try
		{
			Table activeTable = fileHandlerObj.getFile(tName + ".txt");
			
			if (activeTable == null)
			{
				throw new CriticalExistanceFailure("ERROR: Referenced Table [" + tName + "] doesn't exist");
			}
			
			if (fields.get(0).compareTo(activeTable.alField.get(0).fName) != 0)
			{
				throw new CriticalExistanceFailure("ERROR: A primary key is requred to insert!");
			}
			
			Record newRecord = new Record(activeTable);
			
			for (int i = 0; i < fields.size(); i++)
			{			
				for (int f = 0; f < activeTable.alRecord.size(); f++)
				{
					int h = 0;
					
					if ((int)(activeTable.alRecord.get(f).getAlValue().get(h).data) == Integer.parseInt(values.get(0)))
					{
						throw new DataFormatException("ERROR: An entry with the primary key [" + values.get(0) + "] already exists in the table [" + tName + "]!");
					}
				}
				
				if (activeTable.alField.get(i).fName.equalsIgnoreCase(fields.get(i)) == false)
				{
					throw new CriticalExistanceFailure("ERROR: The field [" + fields.get(i) + "] does not exist in table [" + tName + "]!");
				}
				try{
					newRecord.addValue(new Value(activeTable.getField(fields.get(i)), values.get(i)));
				}catch(TableException e){
					JOptionPane.showMessageDialog(null, e.getMessage(), "whoops", JOptionPane.ERROR_MESSAGE);
				}
			}
			
			activeTable.addRow(newRecord);
			
			//DEBUG MESSAGE
			System.out.println(activeTable.toString());
			
			//This method will then have to save the additions to the table
			fileHandlerObj.setFile(tName + ".txt", activeTable);
			
			aTable = tName;
		}
		catch(CriticalExistanceFailure | DataFormatException z)
		{
			JOptionPane.showMessageDialog(null, z.getMessage(), "whoops", JOptionPane.ERROR_MESSAGE);
		}
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//THESE METHODS FOR DELETING THINGS
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void deleteAllRows(String tName)
	{
		try
		{
			Table activeTable = fileHandlerObj.getFile(tName + ".txt");
			
			if (activeTable == null)
			{
				throw new CriticalExistanceFailure("ERROR: Referenced Table [" + tName + "] doesn't exist");
			}
			
			activeTable.alRecord.clear();
			
			//TABLE SAVE GOES HERE
			fileHandlerObj.setFile(tName + ".txt", activeTable);
			
			aTable = tName;
		}
		catch(CriticalExistanceFailure z)
		{
			JOptionPane.showMessageDialog(null, z.getMessage(), "whoops", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	public static void deleteTable(String tName)
	{
		try
		{
			if (fileHandlerObj.getFile(tName + ".txt") == null)
			{
				throw new CriticalExistanceFailure("ERROR: Referenced Table [" + tName + "] doesn't exist");
			}
			
			//PLEASE DOUBLE CHECK WITH PARK TO SEE IF THIS WILL WORK
			fileHandlerObj.deleteFile(tName +".txt");
			
			aTable = tName;
		}
		catch(CriticalExistanceFailure z)
		{
			JOptionPane.showMessageDialog(null, z.getMessage(), "whoops", JOptionPane.ERROR_MESSAGE);
		}
		
		
	}
	
	public static void deleteRow(String tName, int pKey)
	{
		try
		{
			Table activeTable = fileHandlerObj.getFile(tName + ".txt");
			
			if (activeTable == null)
			{
				throw new CriticalExistanceFailure("ERROR: Referenced Table [" + tName + "] doesn't exist");
			}

			boolean found = false;
			
			for (int i = 0; i < activeTable.alRecord.size(); i++)
			{

				for (int z = 0; z < activeTable.alField.size(); z++)
				{
					if (activeTable.alRecord.get(i).getAlValue().get(z).compareTo(String.valueOf(pKey)) == 0 && activeTable.alField.get(z).fKey == Field.KEY.PRIMARY)
					{
						activeTable.alRecord.remove(i);
						found = true;
						break;
					}
				}
				
				if (found)
					break;
			}

			if (!found)
			{
				throw new CriticalExistanceFailure("ERROR: Entry No." + pKey + " in [" + tName + "] doesn't exist");
			}
			
			//and then a table save/display update goes here
			fileHandlerObj.setFile(tName + ".txt", activeTable);
			
			aTable = tName;
		}
		catch(CriticalExistanceFailure z)
		{
			JOptionPane.showMessageDialog(null, z.getMessage(), "whoops", JOptionPane.ERROR_MESSAGE);
		}
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//THIS METHOD FOR UPDATING FIELDS IN TABLES
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void updateField(String tName, int pKey, String field, String value)
	{
		try
		{
			Table activeTable = fileHandlerObj.getFile(tName + ".txt");
			//Table activeTable = testTable;
			
			if (activeTable == null)
			{
				throw new CriticalExistanceFailure("ERROR: Referenced Table [" + tName + "] doesn't exist");
			}
			
			//DEBUG MESSAGE
			//System.out.println("BEFORE: " + testTable.toString());
			
			//find the correct index using the PK
			int row = 0;
			
			boolean found = false;
			boolean found2 = false;
			
			for (int i = 0; i < activeTable.alRecord.size(); i++)
			{
				for (int z = 0; z < activeTable.alField.size(); z++)
				{
					if (String.valueOf(activeTable.alRecord.get(i).getAlValue().get(z).data).compareTo(String.valueOf(pKey)) == 0 && activeTable.alField.get(z).fKey == Field.KEY.PRIMARY)
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
			
			if (!found)
			{
				throw new CriticalExistanceFailure("ERROR: Entry No." + pKey + " in [" + tName + "] does not exist");
			}
			
			for (int i = 0; i < activeTable.alField.size(); i++)
			{
				if (activeTable.alRecord.get(row).getAlValue().get(i).field.compareTo(field) == 0)
				{
					//DEBUG MESSAGE
					System.out.println("TEST");
					
					found2 = true;
					
					activeTable.alRecord.get(row).getAlValue().get(i).data = value;
					//aTable = tName;
				}
			}
			
			if (!found2)
			{
				throw new CriticalExistanceFailure("ERROR: Field [" + field + "] does not exist");
			}
			
			//DEBUG MESSAGE
			System.out.println("AFTER: " + activeTable.toString());
			
			//TABLE SAVE GOES HERE
			fileHandlerObj.setFile(tName + ".txt", activeTable);
			aTable = tName;
		}
		catch(CriticalExistanceFailure z)
		{
			JOptionPane.showMessageDialog(null, z.getMessage(), "whoops", JOptionPane.ERROR_MESSAGE);

		}
	}
	
	public void doSelect(CommandSet command){
		System.out.println("SE:"+command.tableName);
		Table tTable = fileHandlerObj.getFile(command.tableName+".txt");
		Table resultTbl;
		
		String whereC = command.whereC;
		String orderC = command.orderC;
		boolean orderDir = command.orderDir;
		int primaryIdx;
		
		try{
			resultTbl = tTable.clone();
		}catch(Exception ex){
			JOptionPane.showMessageDialog(null, "Table is not Exist", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		System.out.println("tTable:"+tTable);
		
		if(!command.joinTableName.equalsIgnoreCase("")){
			int foreignIdx = tTable.getForeignFieldIdx();	//Foreign key index from main table

			try{
				Field foreignField = tTable.getField(foreignIdx);
				
				if (!command.joinTableName.equalsIgnoreCase(foreignField.foreignTable)){
					System.out.println(command.joinTableName);
					System.out.println(foreignField.foreignTable);
					
					System.out.println("Reference Table is not matched");
					return;
				}
				System.out.println("Reference Table is checked");
			}catch (TableException e){
				System.out.println(e.getMessage());
			}
			
			Table jTable = fileHandlerObj.getFile(command.joinTableName+".txt");
			primaryIdx = jTable.getPrimaryFieldIdx();
			
			//Add Field
			for (int i=0; i<jTable.alField.size();i++){
				if(i!=primaryIdx){
					resultTbl.alField.add(jTable.alField.get(i));
				}
			}
			
			//Add Value
			for (int i=0; i<resultTbl.alRecord.size();i++){
				Value foreignV = resultTbl.alRecord.get(i).getValue(foreignIdx);	//Get each foreign key value (Primary key of other table)
				
				try{
					Record foreignRec = jTable.getRecord(foreignV);					//Get Record which has a certain primary key value)				
					
					for (int j=0; j<jTable.alField.size();j++){
						if(j!=primaryIdx){						
							resultTbl.alRecord.get(i).getAlValue().add(foreignRec.getValue(j));
						}
					}
				}catch(TableException e){
					System.out.println(e.getMessage());				
				}
			}
			
			System.out.println("After Inner Join=========================");
			System.out.println(resultTbl);
		}
		
		
		System.out.println("WHERE:"+whereC);
		if(!whereC.equalsIgnoreCase("")){
			System.out.println("Start Search==================");
			try{
				resultTbl = searchObj.doSearch(resultTbl, whereC);
				System.out.println("1st"+resultTbl.toString());
				//resultTbl = selectField(command.colNames,resultTbl);
			}catch(SearchException ex){
				System.out.println(ex.getMessage());
			}
		}		
		
		System.out.println("ORDERBY:"+orderC);
		if(!orderC.equalsIgnoreCase("")){
			try{
				// resultTbl is sorted after this line
				resultTbl = sortObj.orderBy(resultTbl, orderC, orderDir);
				System.out.println("After orderBy ================");
				System.out.println(resultTbl.toString());
				
			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
		}
			
		resultTbl = selectField(command.colNames,resultTbl);
		System.out.println("Final Table ===========================" );
		System.out.println(resultTbl);
		
		flag = false;
		selectTable = resultTbl.clone();
		aTable = resultTbl.tableName;
		
		guiObj.updateContents(resultTbl);
	}
	
	public Table selectField(ArrayList<String> _colNames, Table _targetTable){
  	    Table resultTbl = _targetTable.clone();
		resultTbl.alRecord = new ArrayList<Record>();
		
		ArrayList<Field> alNewField = new ArrayList<Field>();
		ArrayList<Integer> alIndex = new ArrayList<Integer>();
		
		if(_colNames.get(0).equals("*")){
			return _targetTable;
		}
		
		for (int i=0;i<_colNames.size();i++){
			String colName = _colNames.get(i);
			int idx = _targetTable.getFieldIdx(colName);
			
			if(idx >= 0){
				try{
					alNewField.add(_targetTable.getField(idx));
					alIndex.add(idx);		//accumulate valid index numbers					
					
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
			}			
//			System.out.println("Index AL:"+alIndex);
		}
		resultTbl.alField = alNewField;
		
		ArrayList<Record> alRecordR = new ArrayList<Record>();
		for (int i=0;i<_targetTable.alRecord.size();i++){	
			Record sampleR = _targetTable.alRecord.get(i);		//get n-th row record object
			Record returnR = new Record(resultTbl);				//create New Record to be generated as a result record

			for (int j=0;j<alIndex.size();j++){
				int index = alIndex.get(j);
				returnR.addValue(sampleR.getValue(index));
			}
			resultTbl.addRow(returnR);			
		}		
		return resultTbl;
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
