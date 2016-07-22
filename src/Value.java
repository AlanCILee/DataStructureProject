
public class Value <T extends Comparable<T>> implements Comparable <Object>{
	Field field;	//	public enum TYPE{INTEGER,VARCHAR,FLOAT,DATE,NULL };
	T data;
	
	public Value(Field _field, T _data) {
		field =_field;
		data =_data;
	}
	
	public String toString(){
		return data.toString();
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		int result;
		String oString ="";
		
		if(o instanceof String){
			oString = (String)o;
		}else if(o instanceof Value){
			if(((Value)o).field.fType == this.field.fType){
				oString = String.valueOf(((Value)o).data);
			}else{
				return -9999;
			}
		}	
		
		try{
			switch (field.fType){
				case INTEGER:
					result = ((Integer)data).compareTo(Integer.valueOf(oString));
					break;
				case VARCHAR:
				case DATE:	
					result = ((String)data).compareTo(oString);
					break;
				case DOUBLE:
					result = ((Double)data).compareTo(Double.valueOf(oString));
					break;
				default:
					result = -9999;
			}			
		}catch(Exception e){
			System.out.println("Not matched Data Type");
			result = -9999;
		}
		
		return result;
	}
	
}

