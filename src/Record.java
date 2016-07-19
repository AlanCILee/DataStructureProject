import java.util.*;

public class Record implements Comparable<Record>{
	Table table;
	private ArrayList<Value> alValue;
	
	public Record(Table _table){
		table = _table;
		alValue = new ArrayList<Value>();
		
		for (int i=0;i<table.alField.size();i++){
			Field valField = table.alField.get(i);
			switch(valField.fType){						//Initialize Row with Null Data
				case INTEGER:
					alValue.add(new Value(valField,0));	
					break;
					
				case DOUBLE:
					alValue.add(new Value(valField,0.0));	
					break;
					
				default:	
					alValue.add(new Value(valField,"null"));	
					break;
			}			
		}
	}	

	public void addValue(Value _value){
		Field valueField = _value.field;

		switch(valueField.fType){						//Converting Data to Matched DataType
			case INTEGER:
				_value.data =_value.data;	
				break;
				
			case DOUBLE:
				_value.data = (Double)(_value.data);		
				break;
				
			default:	
				_value.data = (String)(_value.data);			
				break;
		}		
		
		int fieldIndex = table.getFieldIdx(valueField);	//Find location to be stored in ArrayList depends on table's filed information
		
		if(fieldIndex >=0)
			alValue.set(fieldIndex,_value);
		else
			System.out.println("This value's type is exist on this table");
	}
	
	public String toString(){
		String result = "";
		for (int i=0;i<alValue.size();i++){
			result += "/" + alValue.get(i).toString();
		}		
		return result;
	}

	public ArrayList<Value> getAlValue() {
		return alValue;
	}

	public void setAlRecord(ArrayList<Value> _alValue) {
		this.alValue = _alValue;
	}

	public Value getValue(int index){
		return alValue.get(index);
	}
	
	@Override
	public int compareTo(Record arg0) {
		// TODO Auto-generated method stub
		for (int i=0;i<this.alValue.size();i++){
			boolean found = false;
			Value vThis = this.alValue.get(i);
			
			for (int j=0;j<arg0.alValue.size();j++){
				if(vThis.compareTo(arg0.alValue.get(j)) == 0){
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
