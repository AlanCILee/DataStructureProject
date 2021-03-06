import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class FileHandler 
{	
	private static final String FOLDER_NAME = "dbfile";
	private static final String DB_FIELD    = "[F]";
	private static final String DB_ROW      = "[R]";
	private static final String DB_FK       = "[FK]";
		
	
	private void _createFolder()
	{		
		File folder = new File(FOLDER_NAME);
		
		if(!folder.exists())
		{
			boolean re = folder.mkdirs();
			
			if(re)
				System.out.println("[FH] Success to create foler");
			else
				System.out.println("[FH] Fail to create foler");
		}					
	}
	
	
	// create file
	private boolean _createFile(String fname)
	{		
		// check dbfile folder
		_createFolder();
		
		boolean re = false;		
		File file = new File(fname);
		
		if(!file.exists())
		{
			try
			{	
				re = file.createNewFile();
				if(re)
					System.out.println("[FH] Success to create file");
			}
			catch(IOException e)
			{
				System.out.println("[FH] fail to create file");
			}
		}
		else
		{
			System.out.println("[FH] " + fname + " file exists! ");
		}		
		return re; 
	}
	
	
	// delete file
	public boolean deleteFile(String fname)
	{
		String strDbfile = FOLDER_NAME + "//" + fname;
		
		boolean re = false;		
		File file = new File(strDbfile);
		
		if(file.exists())
		{
			re = file.delete();
			if(re)
				System.out.println("[FH] Success to delete file");
		}
		else
		{
			System.out.println("[FH] " + strDbfile + " file does not exist! ");
		}		
		return re; 
	}
	
	
	// set file
	public boolean setFile(String fname, Table tbl)
	{
		boolean re = true;
		
		// check folder and file
		String strDbfile = FOLDER_NAME + "//" + fname;
		_createFile(strDbfile);
		
		BufferedWriter writer;
		try{
			   
			writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(strDbfile),"UTF-8"));		
			
			// set Field
			if(tbl.alField !=null)
			{
				String strField = "[F]";
				for (int i=0;i<tbl.alField.size();i++){
					strField += "/" + tbl.alField.get(i).fName + "-" + tbl.alField.get(i).fType
							 + "-" + tbl.alField.get(i).fKey;
					
					// check foreign key
					if(tbl.alField.get(i).fKey.toString() == "FOREIGN")
					{
						strField += "-" + tbl.alField.get(i).foreignTable; 
					}						
				}				 
				writer.write(strField, 0, strField.length());				
				writer.newLine();
			}
				
			// set Row
			if(tbl.alField !=null)
			{
				for(int i=0; i<tbl.alRecord.size(); i++)
				{
					String strRow = "[R]" + tbl.alRecord.get(i).toString();
					writer.write(strRow, 0, strRow.length());				
					writer.newLine();				
				}
			}
			
			System.out.println("[FH] table: " + tbl.toString());
			   
			writer.close();
		}
		catch(Exception ex){		   
			System.out.println("[FH][set] " + ex );		
			re = false;
		}				
		return re;	
	}

	
	// get file
	public Table getFile(String fname)
	{	
		String strDbfile = FOLDER_NAME + "//" + fname;
		String[] tblName = fname.split("[.]");
		
		// set table
		ArrayList<Field> newField = new ArrayList<Field>();			
		Table tbl = null; 	
		Record newRecord = null; 
				
		ArrayList<String> arrField = new ArrayList<String>();
		ArrayList<String> arrType = new ArrayList<String>();
		
		BufferedReader reader;
		try{			
			reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(strDbfile),"UTF-8"));
			
			String strLine;
			while((strLine = reader.readLine()) != null)
		    {
				System.out.println(strLine);
				
				// IF.[F] -> Field, [R] -> Row, ...
				String[] val = strLine.split("/");
				
							
				if(val[0].equalsIgnoreCase(DB_FIELD))
				{
					for(int i=1; i<val.length; i++)
					{
						//arrFieldAttr : [Field Name]-[Type]-[PK,FK,Normal]
						String[] arrFieldAttr = val[i].split("-");		
						String fdName = arrFieldAttr[0];
						String fdType = arrFieldAttr[1];
						String fdKey  = arrFieldAttr[2];
						String fdFkTable  = null;
						if(arrFieldAttr.length == 4)
							fdFkTable  = arrFieldAttr[3];
						
						
						if(fdKey.equalsIgnoreCase("PRIMARY"))
						{
							if(fdType.equalsIgnoreCase("INTEGER"))
								newField.add(new Field(Field.KEY.PRIMARY, Field.TYPE.INTEGER,fdName));
							else if(fdType.equalsIgnoreCase("VARCHAR"))
								newField.add(new Field(Field.KEY.PRIMARY, Field.TYPE.VARCHAR,fdName));
							else if(fdType.equalsIgnoreCase("DOUBLE"))
								newField.add(new Field(Field.KEY.PRIMARY, Field.TYPE.DOUBLE,fdName));
							else if(fdType.equalsIgnoreCase("DATE"))
								newField.add(new Field(Field.KEY.PRIMARY, Field.TYPE.DATE,fdName));
							else if(fdType.equalsIgnoreCase("NULL"))
								newField.add(new Field(Field.KEY.PRIMARY, Field.TYPE.NULL,fdName));
						}
						else if(fdKey.equalsIgnoreCase("FOREIGN"))
						{
							if(fdType.equalsIgnoreCase("INTEGER"))
							{	Field fd = new Field(Field.KEY.FOREIGN, Field.TYPE.INTEGER,fdName);
								fd.setForeignKey(fdFkTable);
								newField.add(fd);
							}
							else if(fdType.equalsIgnoreCase("VARCHAR"))
							{	
								Field fd = new Field(Field.KEY.FOREIGN, Field.TYPE.VARCHAR,fdName);
								fd.setForeignKey(fdFkTable);
								newField.add(fd);
							}								
							else if(fdType.equalsIgnoreCase("DOUBLE"))
							{
								Field fd = new Field(Field.KEY.FOREIGN, Field.TYPE.DOUBLE,fdName);
								fd.setForeignKey(fdFkTable);
								newField.add(fd);								
							}
							else if(fdType.equalsIgnoreCase("DATE"))
							{
								Field fd = new Field(Field.KEY.FOREIGN, Field.TYPE.DATE,fdName);
								fd.setForeignKey(fdFkTable);
								newField.add(fd);								
							}
							else if(fdType.equalsIgnoreCase("NULL"))
							{
								Field fd = new Field(Field.KEY.FOREIGN, Field.TYPE.NULL,fdName);
								fd.setForeignKey(fdFkTable);
								newField.add(fd);								
							}
						}
						else
						{
							if(fdType.equalsIgnoreCase("INTEGER"))
								newField.add(new Field(Field.KEY.NORMAL, Field.TYPE.INTEGER,fdName));
							else if(fdType.equalsIgnoreCase("VARCHAR"))
								newField.add(new Field(Field.KEY.NORMAL, Field.TYPE.VARCHAR,fdName));
							else if(fdType.equalsIgnoreCase("DOUBLE"))
								newField.add(new Field(Field.KEY.NORMAL, Field.TYPE.DOUBLE,fdName));
							else if(fdType.equalsIgnoreCase("DATE"))
								newField.add(new Field(Field.KEY.NORMAL, Field.TYPE.DATE,fdName));
							else if(fdType.equalsIgnoreCase("NULL"))
								newField.add(new Field(Field.KEY.NORMAL, Field.TYPE.NULL,fdName));
						}
																		
							
						// save field name and type
						arrField.add(fdName);	
						arrType.add(fdType);
					}
									
					// create table with field
					tbl = new Table(newField, tblName[0]);					
				}
				else if(val[0].equalsIgnoreCase(DB_ROW))				
				{
					// create record : specify table to check added data type
					newRecord = new Record(tbl);			
					
					for(int i=1; i<val.length; i++)
					{	
						if(arrType.get(i-1).toString().equalsIgnoreCase("INTEGER")){
							try{
								newRecord.addValue(new Value(tbl.getField(arrField.get(i-1).toString()),Integer.parseInt(val[i])));
							}catch(TableException e){
								System.out.println(e.getMessage());				
							}
						}
						else if(arrType.get(i-1).toString().equalsIgnoreCase("DOUBLE")){
							try{
								newRecord.addValue(new Value(tbl.getField(arrField.get(i-1).toString()),Double.parseDouble(val[i])));
							}catch(TableException e){
								System.out.println(e.getMessage());				
							}
						}
						else{ 
							try{
								newRecord.addValue(new Value(tbl.getField(arrField.get(i-1).toString()),val[i]));
							}catch(TableException e){
								System.out.println(e.getMessage());				
							}
						}
					}
					if(!tbl.addRow(newRecord)){
						System.out.println("Failed to add row");
					}																																				
				}
				else if(val[0] == DB_FK)
				{
					// coming soon ...
				}
				
		    }
		   
		    reader.close();						
		}
		catch(FileNotFoundException e)
		{
			System.out.println("[FH][get] " + fname + " file does not exist! ");	
		}
		catch(IOException e)
		{
			System.out.println("[FH][get] " + e );
		}
		
		return tbl;	
	}
		
	
	// get file
	public ArrayList<String> getFileList()
	{
		ArrayList<String> arrList = new ArrayList<String>();
		
		File folder = new File(FOLDER_NAME);
		
		if(!folder.exists())
		{			
			System.out.println("[FH] Dbfile does not exist.");			
			return arrList;
		}		
		
		File files[] = folder.listFiles();
		
		for(int i=0; i<files.length; i++)
		{
			arrList.add(files[i].getName());
		}
		
		return arrList;	
	}
	
	
} //class FileHandler 
