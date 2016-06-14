import java.util.*;

public class Table {	
	String tableName;
	ArrayList<Record> alTable;
	ArrayList<Field> alField;
	
//=====================================================================	
// Constructor for Table Class	
//=====================================================================	
	public Table(ArrayList<Field> _alField, String _tableName){
		
		tableName = _tableName;
		alField = _alField;
		alTable = new ArrayList<Record>();
	}

//=====================================================================	
// Add Record to Table
// 	Return 	true : success to add
//			false : Fail to add (Non valid Field)	
//=====================================================================		
	public boolean addRow(Record _record){	
		ArrayList<Value> alValueTemp = _record.getAlRecord();		
		
		if(alValueTemp.size() > alField.size()){
			System.out.println("Input record size is bigger than the Table's field size");
			return false;
		}
		
		for (int i=0;i<alValueTemp.size();i++){
			for (int j=i+1;j<alValueTemp.size();j++){
				if(alValueTemp.get(i).field.compareTo(alValueTemp.get(j).field) ==0 ){
					System.out.println("There is a duplicate Field in the Record");
					return false;		
				}
			}
		}
		
		for (int i=0; i< alValueTemp.size();i++){
			boolean found = false;
			
			for (int j=0; j< alField.size();j++){				
				if(alValueTemp.get(i).field.compareTo(alField.get(j)) == 0){
					found = true;
					break;	
				}
			}
			
			if(!found){
				System.out.println("Target record has non-valid Field in Table");
				return false;
			}
		}

		alTable.add(_record);
		return true;
	}
	
	public String toString(){
		String result ="";
		
		if(alField !=null){
			for (int i=0;i<alField.size();i++){
				result += alField.get(i).fName + "\t";
			}
			result += "\n";
		}
		
		if(alTable !=null){
			for (int i=0;i<alTable.size();i++){
				result += alTable.get(i).toString() + "\n";
			}		
		}
		return result;
	}
}
