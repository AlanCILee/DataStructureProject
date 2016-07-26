
public class Sort {
	/*	public Table select(String s, boolean b1, boolean b2)
	{
		//create new table by cloning first table and if there's a second table, add all records onto the clone
		
	}*/
	
	/*
	 * orderBy method accepts 3 parameters: 
	 * table to sort, 
	 * field name to sort by, 
	 * ASC or DESC order - false is ASC, true is DESC
	 * and return the table
	 */
	public Table orderBy(Table _table, String _field, boolean _order)
	{
		Table table = _table.clone();
		int fieldIndex = 0;
		int size = table.alRecord.size();
		
		// find the field index
		for (int i=0; i < table.alField.size(); i++)
		{
			if(table.alField.get(i).fName.equalsIgnoreCase(_field))
			{
				fieldIndex = i;
				break;
			}
		}
		
		for(int x=0; x < size-1; x++)
		{
			// CURRENT record at int x, c is compared to all records after it
			Record c = table.alRecord.get(x);
			// the POSITION of the record being compared to c
			// the MIN/MAX record found by comparing c with records after it
			Record m = table.alRecord.get(x); 
			int pos = x; 
			for(int j=x+1; j<size; j++)
			{
				// if ascending order
				if(_order == false && 
	table.alRecord.get(j).getAlValue().get(fieldIndex).data.compareTo(m.getAlValue().get(fieldIndex).data) < 0)
				{
					pos = j;
					m = table.alRecord.get(j);
				}
				// else if descending order
				else if(_order == true && 
	table.alRecord.get(j).getAlValue().get(fieldIndex).data.compareTo(m.getAlValue().get(fieldIndex).data) > 0)
				{
					pos = j;
					m = table.alRecord.get(j);
				}
			}
			table.alRecord.set(x, m);
			table.alRecord.set(pos, c);
		}
		return table;
	}
	
}
