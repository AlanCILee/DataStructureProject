public class Field implements Comparable<Field> {	
	public enum TYPE{INTEGER,VARCHAR,DOUBLE,DATE,NULL };
	public enum KEY {PRIMARY, FOREIGN, NORMAL};
	
	KEY fKey;			
	TYPE fType;
	String fName;
	ForeignKey fForeign;		// Additional information is required if it is foreign key
	
	public Field(TYPE _type,String _fName){			
		fKey = Field.KEY.NORMAL;
		fType = _type;
		fName = _fName;
	}

	public Field(KEY _key, TYPE _type,String _fName){			
		fKey = _key;
		fType = _type;
		fName = _fName;
	}
	
	public void setForeignKey(Table _rTable,Field _rField){
		fKey = Field.KEY.FOREIGN;
		fForeign = new ForeignKey();
		fForeign.rTable = _rTable;
		fForeign.rField = _rField;
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

class ForeignKey {
	Table rTable;		//reference Table if this field is foreign key
	Field rField;		//reference Field if this field is foreign key
}