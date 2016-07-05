
public class Sort {
	/*
	 * select method accepts , , and parameters
	 * 
	 */
	public Table select(String s, boolean b1, boolean b2)
	{
		//create new table by cloning first table and if there's a second table, add all records onto the clone
		
		
	}
	
	/*
	 * orderBy method accepts 3 parameters: 
	 * table to sort, 
	 * field(fname) to sort by, 
	 * ASC or DESC order - false is ASC, true is DESC
	 * and return the table
	 */
	public Table orderBy(Table _table, String _field, boolean _order)
	{
		Table table = _table;
		String field = _field;		
		Record m;
		
		if(_order == false) // if ascending order
		{
			for (int x=0; x < table.alRecord.size(); x++)
			{
				m = table.alRecord.get(x);
				
				for(int i = x + 1; (i <= table.alRecord.size()) && 
(table.alRecord.get(i).getAlRecord().get(table.getFieldIdx(field)).data.compareTo(table.alRecord.get(x).getAlRecord().get(table.getFieldIdx(field)).data) == -1); i++)
		          {
						 table.alRecord.set(x,table.alRecord.get(i));
						 table.alRecord.set(i,m);
						 m = table.alRecord.get(i);
		          }
			}
		}
		else if(_order == true) // if descending order
		{
			for (int x=0; x < table.alRecord.size(); x++)
			{
				m = table.alRecord.get(x);
				
				for(int i = x+ 1; (i <= table.alRecord.size()) && 
(table.alRecord.get(i).getAlRecord().get(table.getFieldIdx(field)).data.compareTo(table.alRecord.get(x).getAlRecord().get(table.getFieldIdx(field)).data) == 1); i++)
		          {
					 table.alRecord.set(x,table.alRecord.get(i));
					 table.alRecord.set(i,m);
					 m = table.alRecord.get(i);
		          }
			}
		}
		
		return table;
	}

}
