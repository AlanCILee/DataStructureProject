
public class Sort {
	/*
	 * select method accepts , , and parameters
	 * 
	 */
/*	public Table select(String s, boolean b1, boolean b2)
	{
		//create new table by cloning first table and if there's a second table, add all records onto the clone
		
	}*/
	
	/*
	 * orderBy method accepts 3 parameters: 
	 * table to sort, 
	 * field(fname) to sort by, 
	 * ASC or DESC order - false is ASC, true is DESC
	 * and return the table
	 */
	public Table orderBy(Table _table, String _field, boolean _order)
	{
		Table table = _table.clone();
		String field = _field;
		//Table temp = new Table("temporaryTable");	
		int fieldIndex = 0;
		
		// find out the field index
		for (int i=0; i < table.alField.size(); i++)
		{
			if(table.alField.get(i).fName.equalsIgnoreCase(field))
			{
				fieldIndex = i;
				break;
			}
			
		}
		
		for (int x=0; x < table.alRecord.size(); x++)
		{
			// set the first record as the max/min
			Record c = table.alRecord.get(x); //current record at x, use c to compare to all the other records
			int temp = 0; //record being compared to
			Record m = table.alRecord.get(x); //the min/max record found by comparing c with temp(s)
			for(int j=x+1; j<table.alRecord.size(); j++)
			{
				// if ascending order
				if(_order == false && 
				table.alRecord.get(j).getAlValue().get(fieldIndex).data.compareTo(c.getAlValue().get(fieldIndex).data) < 0)
				{
					m = table.alRecord.get(j);
					temp = j;
				}
				// else if descending order
				else if(_order == true && 
				table.alRecord.get(j).getAlValue().get(fieldIndex).data.compareTo(c.getAlValue().get(fieldIndex).data) > 0)
				{
					m = table.alRecord.get(j);
					temp = j;
				}
			}
			table.alRecord.set(x, m);
			table.alRecord.set(temp, c);

			//DEBUG MESSAGE
			System.out.println(m);
		}
		
		return table;
	}

}
