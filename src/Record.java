import java.util.*;

public class Record {
	private ArrayList<Value> alRecord;
	
	public Record(){
		alRecord = new ArrayList<Value>();
	}	
	
	public Record(ArrayList<Value> _alValue){
		alRecord = _alValue;
	}
	
	public void add(Value _in){
		alRecord.add(_in);
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
			
}
