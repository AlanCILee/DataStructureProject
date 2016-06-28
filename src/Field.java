public class Field implements Comparable<Object> {	
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
	public int compareTo(Object o) {
		int result = -1;
		if(o instanceof Field){
			if(fType == ((Field)o).fType && fName.equalsIgnoreCase(((Field)o).fName))
				result = 0;
			else
				result = -1;
		}else if(o instanceof String){
			if(fName.equalsIgnoreCase((String)o))
				result = 0;
			else
				result = -1;			
		}
		return result;
	}
	
	public String toString(){		
		return String.valueOf(fType) + " : "+fName;
	}	

}

class ForeignKey {
	Table rTable;		//reference Table if this field is foreign key
	Field rField;		//reference Field if this field is foreign key
}