import java.util.*;

public class Table {	
	String tableName;
	ArrayList<Record> alRecord;
	ArrayList<Field> alField;
	
//=====================================================================	
// Constructor for Table Class	
//=====================================================================	
	public Table(ArrayList<Field> _alField, String _tableName){
		
		tableName = _tableName;
		alField = _alField;
		alRecord = new ArrayList<Record>();
	}


// Can create table before creating field information =================
	public Table(String _tableName){
		
		tableName = _tableName;
		alRecord = new ArrayList<Record>();
	}

//=====================================================================
//	Clone table 
//=====================================================================
	public Table clone(){
		Table retTable = new Table(this.tableName);
		retTable.alRecord = (ArrayList<Record>) alRecord.clone();
		retTable.alField = (ArrayList<Field>) alField.clone();
		
		return retTable;
	}
//=====================================================================
//	Get Field Index to get Field Object in alField
//=====================================================================		
	public int getFieldIdx(Field _field){
		int idx = -1;
		for (int i=0;i<alField.size();i++){
			if(alField.get(i).compareTo(_field)==0){
				idx = i;
				break;
			}				
		}
		return idx;
	}
	
	public int getFieldIdx(String _fname){
		int idx = -1;
		for (int i=0;i<alField.size();i++){
			if(alField.get(i).compareTo(_fname)==0){
				idx = i;
				break;
			}				
		}
		return idx;
	}
	
//=====================================================================
//	Get Field with Field Name
//=====================================================================		
	public Field getField(String _fname) throws TableException{
		int index = getFieldIdx(_fname);
		if(index >=0)
			return alField.get(index);
		else
			throw new TableException("There is no Field : "+_fname);
	}
	
	public Field getField(int index) throws TableException{
		if(index >=0 && index <alField.size())
			return alField.get(index);
		else
			throw new TableException("There is no Field index: "+index);
	}
	
//=====================================================================	
// Add Record to Table
// 	Return 	true : success to add
//			false : Fail to add (Non valid Field)	
//=====================================================================		
	public boolean addRow(Record _record){	
		ArrayList<Value> alValueTemp = _record.getAlValue();		
		
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

		alRecord.add(_record);
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
		
		if(alRecord !=null){
			for (int i=0;i<alRecord.size();i++){
				result += alRecord.get(i).toString() + "\n";
			}		
		}
		return result;
	}
}

class TableException extends Exception{
	public TableException(String msg){
		super(msg);
	}
}
