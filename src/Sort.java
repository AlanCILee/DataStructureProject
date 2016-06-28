
public class Sort {
	/*
	 * the purpose of the sort class is to accept 3 parameters: 
	 * table to sort, 
	 * field to sort by, 
	 * ASC or DESC order - 0 is ASC, 1 is DESC
	 * and return the 
	 */
	
	public void orderBy(Table _table, Field _field, boolean _order)
	{
		Table table;
		Field field;
		table = _table;
		field = _field;
		String m;
		
		if(_order = 0)
		{
			for (int x=0; x < table.alfield.size(); x++)
			{
				m = table.alfield.get(x);
				
				for(int i = x+ 1; (i <= table.alfield.size()) && (table.alfield.get(i) < (table.alfield.get(x)); i++)
		          {
					     record.setAlRecord(x,record.getAlRecord(i));
					     record.setAlRecord(i,m);
		          }
			}
		}
		else if(_order=1)
		{
			for (int x=0; x < table.alfield.size(); x++)
			{
				m = table.alfield.get(x);
				for(int i = x+ 1; (i <= table.alfield.size()) && (table.alfield.get(i) > (table.alfield.get(x)); i++)
		          {
						 table.alfield.set(x,table.alfield.get(i));
		 				 table.alfield.set(i,m)
		          }
			}
		}
		
		// sort based on field name (string)
		
	}

}
