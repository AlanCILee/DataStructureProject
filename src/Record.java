import java.util.*;

public class Record implements Comparable<Record>{
	private ArrayList<Value> alRecord;
	
	public Record(){
		alRecord = new ArrayList<Value>();
	}	
	
	public Record(ArrayList<Value> _alValue){
		alRecord = _alValue;
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
