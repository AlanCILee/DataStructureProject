import java.util.ArrayList;



public class Search {
	enum ALPHA {INIT, ALPHA, OTHER};
	String searchStr;
	ArrayList<String> searchElement = new ArrayList<String>();; 
	
	//====================================================================================
	// Input Search String & divide it as string parts
	//====================================================================================
	public void getSearch(String _searchStr){
		searchStr = _searchStr.trim();
		String tempWord, tempStr;
		
		String words[] = searchStr.split("[\\s\\r\\t]+");
		
		
		for (int i=0; i<words.length;i++){
			tempWord = words[i];
			tempStr ="";
			ALPHA wordOrder = ALPHA.INIT;
			
//			System.out.println(tempWord);
			
			for (int j=0; j<tempWord.length();j++){				
				char tempChar = tempWord.charAt(j);
				if ((tempChar >= 'A' && tempChar <='Z') || (tempChar >='a' && tempChar <='z') || (tempChar >='0' && tempChar <='9')){
					if (wordOrder != ALPHA.ALPHA){	//Different Type
						if(wordOrder != ALPHA.INIT)
							searchElement.add(tempStr);
						wordOrder = ALPHA.ALPHA;
						tempStr ="";
					}
					tempStr += String.valueOf(tempChar);
				}else {
					if (wordOrder != ALPHA.OTHER){
						if(wordOrder != ALPHA.INIT)
							searchElement.add(tempStr);
						wordOrder = ALPHA.OTHER;
						tempStr ="";		
					}
					tempStr += String.valueOf(tempChar);					
				}				
			}
			searchElement.add(tempStr);
		}	
		System.out.println(searchElement);
	
	}
	
	public static void main(String args[]){
		Search sch = new Search();
		sch.getSearch(" (x>y) AND x>=z OR k != 1>0");	//Test
	}
	
}
