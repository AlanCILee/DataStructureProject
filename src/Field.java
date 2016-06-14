public class Field implements Comparable<Field> {
	public enum TYPE{INTEGER,VARCHAR,FLOAT,DATE };

	TYPE fType;
	String fName;

	public Field(TYPE _type,String _fName){
		fType = _type;
		fName = _fName;
	}

	@Override
	public int compareTo(Field o) {
		if(fType == o.fType && fName == o.fName)
			return 0;
		else
			return -1;
	}
	
	public String toString(){		
		return String.valueOf(fType) + " : "+fName;
	}
}
