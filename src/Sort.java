
public class Sort {
	/*
	 * the purpose of the sort class is to accept 3 parameters: 
	 * table to sort, 
	 * field to sort by, 
	 * ASC or DESC order - 0 is ASC, 1 is DESC
	 * and return the 
	 */
	
	public void orderBy(Table _table, Field _field, boolean order)
	{
		Table table;
		Field field;
		table = _table;
		field = _field;
		String m;
		
		if(order)
		{
			for (int x=0; x < table.alField.size(); x++)
			{
				m = table.alField.get(x);
				
				for(int i = x+ 1; (i <= table.alField.size()) && (table.alField.get(i) < (table.alField.get(x)); i++)
		          {
					     record.setAlRecord(x,record.getAlRecord(i));
					     record.setAlRecord(i,m);
		          }
			}
		}
		else 
		{
			for (int x=0; x < table.alField.size(); x++)
			{
				m = table.alField.get(x);
				for(int i = x+ 1; (i <= table.alField.size()) && (table.alField.get(i) > (table.alField.get(x)); i++)
		          {
						 table.alField.set(x,table.alField.get(i));
		 				 table.alField.set(i,m)
		          }
			}
		}
		
		// sort based on field name (string)
		
	}

}
