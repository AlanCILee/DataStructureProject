public class Field implements Comparable<Object> {	
	public enum TYPE{INTEGER,VARCHAR,DOUBLE,DATE,NULL };
	public enum KEY {PRIMARY, FOREIGN, NORMAL};
	
	KEY fKey;			
	TYPE fType;
	String fName;
	String foreignTable = null;		// if this field is foreign key, it will has reference table's file name 
	
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
	
	public void setForeignKey(String _foreignTable){
		fKey = Field.KEY.FOREIGN;
		foreignTable = _foreignTable;
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
		return String.valueOf(fType) + " : "+fName +" : "+ foreignTable;
	}
}
/*
class ForeignKey {
	Table rTable;		//reference Table if this field is foreign key
	Field rField;		//reference Field if this field is foreign key
}*/