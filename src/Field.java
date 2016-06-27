public class Field implements Comparable<Field> {	
	public enum TYPE{INTEGER,VARCHAR,FLOAT,DATE,NULL };

	Key fKey;			//Optional member
	TYPE fType;
	String fName;

	public Field(TYPE _type,String _fName){
		fType = _type;
		fName = _fName;
	}

	@Override
	public int compareTo(Field o) {
		if(fType == o.fType && fName.equalsIgnoreCase(o.fName))
			return 0;
		else
			return -1;
	}
	
	public String toString(){		
		return String.valueOf(fType) + " : "+fName;
	}
}

class Key {
	public enum KEY {PRIMARY, FOREIGN, NULL};
	
	KEY kKey;			//type of key
	Table rTable;		//reference Table
	Field rField;		//reference Field
	// KKK
}