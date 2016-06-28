import java.util.*;

public class Record implements Comparable<Record>{
	Table table;
	private ArrayList<Value> alRecord;
	
	public Record(Table _table){
		table = _table;
		alRecord = new ArrayList<Value>();
		
		for (int i=0;i<table.alField.size();i++){
			Field valField = table.alField.get(i);
			switch(valField.fType){						//Initialize Row with Null Data
				case INTEGER:
					alRecord.add(new Value(valField,0));	
					break;
					
				case DOUBLE:
					alRecord.add(new Value(valField,0.0));	
					break;
					
				default:	
					alRecord.add(new Value(valField,"null"));	
					break;
			}			
		}
	}	

	public void addValue(Value _value){
		Field valueField = _value.field;
		int fieldIndex = table.getFieldIdx(valueField);
		
		if(fieldIndex >=0)
			alRecord.set(fieldIndex,_value);
		else
			System.out.println("This value's type is exist on this table");
	}
	
	public String toString(){
		String result = "";
		for (int i=0;i<alRecord.size();i++){
			result += alRecord.get(i).toString() + "\t";
		}		
		return result;
	}

	public ArrayList<Value> getAlRecord() {
		return alRecord;
	}

	public void setAlRecord(ArrayList<Value> alRecord) {
		this.alRecord = alRecord;
	}

	@Override
	public int compareTo(Record arg0) {
		// TODO Auto-generated method stub
		for (int i=0;i<this.alRecord.size();i++){
			boolean found = false;
			Value vThis = this.alRecord.get(i);
			
			for (int j=0;j<arg0.alRecord.size();j++){
				if(vThis.compareTo(arg0.alRecord.get(j)) == 0){
					found = true;
					break;
				}
			}			
			if(!found)
				return -1;			
		}
		return 0;
	}
			
}
