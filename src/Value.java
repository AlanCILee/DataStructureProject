
public class Value <T extends Comparable<T>> {
	Field field;
	T data;
	
	public Value(Field _field, T _data) {
		field =_field;
		data =_data;
	}
	
	public String toString(){
		return data.toString();
	}
	
	
}
