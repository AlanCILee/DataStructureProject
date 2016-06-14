import java.util.*;

public class Controller {
	UserGui guiObj;
	Sort	sortObj;
	Search	searchObj;
	FileHandler fileHandlerObj;
	
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
			
		Record newRecord = new Record();
		newRecord.add(new Value(newField.get(0),Integer.valueOf(1)));
		newRecord.add(new Value(newField.get(1),"Alan Lee"));
		
		if(!testTable.addRow(newRecord)){
			System.out.println("Failed to add row");
		}
		
		System.out.println(testTable.toString());
		//Example to create table ==============================================
		
		//Call back function to update Table List : Parameters need to be defined
		guiObj.updateTableList();
		
		//Call back function to update Show Result : Parameters need to be defined		
		guiObj.updateContents();
	}
	

}
