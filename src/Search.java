import java.util.*;

public class Search {
	enum ALPHA {INIT, ALPHA, OTHER};

	ArrayList<String> searchElement; 
	ArrayList<String> partSearchElement; 
	ArrayList result;
	Table searchTable;
	
	public void doSearch(Table _targetTable, String _searchString) throws SearchException{
		result = new ArrayList();			
		
		searchElement = splitSchString(_searchString);
		searchTable = _targetTable.clone();
		
		while(searchElement.size() != 0){								//Do part search : part means [A operator B]
			ArrayList<Record> partResult;		
			partSearchElement = getPartSearchElement(searchElement);
			partResult = partSearch(partSearchElement,searchTable);
			result.add(partResult);		
		}		
		
		while(result.size() > 1){										//Combine part search results : part result means [partA && partB || partC ...]   
			ArrayList<Record> left = (ArrayList<Record>)result.get(0);	
			ArrayList<Record> right = (ArrayList<Record>)result.get(2);
			ArrayList<Record> tempResult = new ArrayList<Record>();
			
			String operator = (String)result.get(1);
			
			if(operator.equalsIgnoreCase("AND") || operator.equalsIgnoreCase("&&")){				
				for(int i=0;i<right.size();i++){
					if(left.contains(right.get(i))){
						tempResult.add(right.get(i));
					}					
				}
			}else if(operator.equalsIgnoreCase("OR") || operator.equalsIgnoreCase("||")){
				tempResult = (ArrayList<Record>) left.clone();
				
				for(int i=0;i<right.size();i++){
					if(!left.contains(right.get(i))){
						tempResult.add(right.get(i));
					}					
				}
			}			
			result.subList(0, 3).clear();
			result.add(tempResult);	
		}
		
		System.out.println(result);
	}

	//====================================================================================
	// 1. Return 3 elements of ArrayList which has [X][operator][Y] from original search command 
	// 2. Remove these 3 elements from search command
	// 3. If it meets operators (AND, OR, &&, ||), store it to search result ArrayList
	//====================================================================================		
	public ArrayList<String> getPartSearchElement(ArrayList<String> _searchElement){
		System.out.println(_searchElement);
		int operationLength =3;
		boolean quotes = false;
		
		ArrayList<String> partSearchElement = new ArrayList<String>();
		
		for (int i=0; i<operationLength; i++){
			String element =_searchElement.get(i);
			
			switch (i){			
				// First Element : Operand or AND/OR Operator
				case 0:
					if (element.equalsIgnoreCase("AND") || element.equalsIgnoreCase("OR") 
						|| element.equalsIgnoreCase("&&") || element.equalsIgnoreCase("||")){
						result.add(element);
						_searchElement.remove(0);
						i--;				
					}else{
						partSearchElement.add(element);					
					}
					break;
					
				// Second Element : Operator	
/*				case 1:
					partSearchElement.add(element);					
					if (element.equalsIgnoreCase("BETWEEN"))
						operationLength = 5;
					break;*/
					
				default:
					partSearchElement.add(element);
					break;
				
			}
		}
		_searchElement.subList(0,operationLength).clear();
		System.out.println(partSearchElement);
		return partSearchElement;
		
	}

	//====================================================================================
	// Return Search Result of [ X operator Y ] to _partResult
	// 
	//====================================================================================	
	public ArrayList<Record> partSearch(ArrayList<String> alCommand, Table _targetTable) throws SearchException{
		ArrayList<Record> partResult = new ArrayList<Record>();		//Result Record
		ArrayList<Field> searchField = searchTable.alField;
		ArrayList<Record> searchRecord = searchTable.alRecord;

		String targetField = alCommand.get(0);	
		String operator = alCommand.get(1);
		operator = operator.toLowerCase();
		int targetFieldIdx = _targetTable.getFieldIdx(targetField);		//Find index of variable		

		if(targetFieldIdx >= 0){
			
			for(int i=0;i<searchRecord.size();i++){		//search each rows
				Record rec = searchRecord.get(i);
				Value val = rec.getAlRecord().get(targetFieldIdx);
			
				switch (operator){				
					case ">" :
					case "<" :
					case "=" :
					case ">=" :
					case "<=" :																	
						if((operator.equals(">") && val.compareTo(alCommand.get(2)) > 0)
								|| (operator.equals("<") && val.compareTo(alCommand.get(2)) < 0) 
								|| (operator.equals(">=") && val.compareTo(alCommand.get(2)) >= 0)
								|| (operator.equals("<=") && val.compareTo(alCommand.get(2)) <= 0)
								|| (operator.equals("=") && val.compareTo(alCommand.get(2)) == 0))
						{
							partResult.add(rec);
						}		
						break;
	
					case "like" :
						if(((String)(val.data)).contains(alCommand.get(2).toLowerCase()))
							partResult.add(rec);
						break;
						
					default:
						System.out.println("Not supported Command");
						throw new SearchException("Not supported Command");
						
				}	//end of switch
			}	//end of for
			System.out.println(partResult);
		}else{
			System.out.println("No such Field in the table");
			throw new SearchException("No such Field in the table");
		}
		
		return partResult;
	}
	

	//====================================================================================
	// Return Field Type by getting Field name
	// If there is no field name, it returns 'Null' Field Type
	//====================================================================================
	public Field.TYPE getFieldType(String _fName){
		ArrayList<Field> searchField = searchTable.alField;
		Field.TYPE found = Field.TYPE.NULL;
		
		for (int i=0; i < searchField.size(); i++){
			if(searchField.get(i).fName.equalsIgnoreCase(_fName))
				found = searchField.get(i).fType;
		}		
		return found;				
	}
	
	//====================================================================================
	// Check whether the search variable is one of field
	//====================================================================================
/*	public boolean isExistField(String fieldName){
		ArrayList<Field> searchField = searchTable.alField;
		boolean found = false;
		
		for (int i=0; i < searchField.size(); i++){
			if(searchField.get(i).fName == fieldName)
				found = true;
		}		
		return found;
	}*/
	
	//====================================================================================
	// Input Search String & divide it as elements of ArrayList<String>
	//	1. Divide by space, line change, tab
	//	2. Divide by Non alphabet characters
	//====================================================================================
	public ArrayList<String> splitSchString(String _searchStr){
		ArrayList<String> arrStr = new ArrayList<String>();; 
		String searchStr = _searchStr.trim();
		String tempWord, tempStr;
		
		String words[] = searchStr.split("[\\s\\r\\t\'()]+");		
		
		for (int i=0; i<words.length;i++){
			tempWord = words[i];
			tempStr ="";
			ALPHA wordOrder = ALPHA.INIT;
			
			for (int j=0; j<tempWord.length();j++){				
				char tempChar = tempWord.charAt(j);
				if ((tempChar >= 'A' && tempChar <='Z') || (tempChar >='a' && tempChar <='z') || (tempChar >='0' && tempChar <='9')){
					if (wordOrder != ALPHA.ALPHA){			//Character type changed from Non alphabet to Alphabet Type
						if(wordOrder != ALPHA.INIT)
							arrStr.add(tempStr);
						wordOrder = ALPHA.ALPHA;
						tempStr ="";
					}
					tempStr += String.valueOf(tempChar);	//add character to String
				}else {
					if (wordOrder != ALPHA.OTHER){			//Character type changed from Alphabet to Non alphabet Type
						if(wordOrder != ALPHA.INIT)
							arrStr.add(tempStr);
						wordOrder = ALPHA.OTHER;
						tempStr ="";		
					}
					tempStr += String.valueOf(tempChar);	//add character to String				
				}				
			}
			arrStr.add(tempStr);							//add String to ArrayList<String>
		}	
		System.out.println(arrStr);
		return arrStr;
	}
	
}

class SearchException extends Exception{
	public SearchException(String msg){
		super(msg);
	}
}
