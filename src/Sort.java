
public class Sort {
	/*
	 * the purpose of the sort class is to accept 3 parameters: 
	 * table to sort, 
	 * field(fname) to sort by, 
	 * ASC or DESC order - 0 is ASC, 1 is DESC
	 * and return the table
	 */
	
	public Table orderBy(Table _table, String _field, boolean _order)
	{
		Table table = _table;
		String field = _field;		
		Record m;
		
		for(table.alRecord.alRecord.field.fName == field) // for values with XX field name
		{
		
		if(_order = 0) // if ascending order
		{
			for (int x=0; x < table.alRecord.size(); x++)
			{
				m = table.alRecord.get(x);
				
				for(int i = x + 1; (i <= table.alRecord.size()) && 
						(table.alRecord.alRecord.get(i).data < table.alRecord.alRecord.get(x).data); i++)
		          {
						 table.alRecord.set(x,table.alRecord.get(i));
						 table.alRecord.set(i,m);
						 m = table.alRecord.get(i);
		          }
			}
		}
		else if(_order=1) // if descending order
		{
			for (int x=0; x < table.alRecord.size(); x++)
			{
				m = table.alRecord.get(x);
				
				for(int i = x+ 1; (i <= table.alRecord.size()) && 
						(table.alRecord.alRecord.get(i).data > table.alRecord.alRecord.get(x).data); i++)
		          {
					 table.alRecord.set(x,table.alRecord.get(i));
					 table.alRecord.set(i,m);
					 m = table.alRecord.get(i);
		          }
			}
		}
		
		}
		
		return table;
		
	}

}
