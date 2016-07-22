import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*; 


public class UserGui extends CFrame implements ActionListener, ListSelectionListener{
	
	JTextArea taCommand;
	JTextArea taResult;
	JScrollPane scroll1;
	JList tableList;
	ArrayList tableArr = new ArrayList(); 
	Vector vtTables;
	JButton bRun;
	JButton bClear;
	
	Table currentTable;
	Controller ctrl;
	
	UserGui(int x, int y, int w, int h) {
		super(x, y, w, h);

		Font titleFont = new Font("Arial", Font.BOLD, 15);
		//Border border = new Border();
		
		ctrl = new Controller(this);
		tableArr = ctrl.fileHandlerObj.getFileList();
		vtTables = new Vector(tableArr);
		//----------Top Panel----------------------------
		JPanel topGui = new JPanel();
		topGui.setLayout(null);
		createGui(topGui,0,0,1280,80,this); //add topGui
		topGui.setBackground(Color.DARK_GRAY);		
		JLabel dbtitle = new JLabel("CSIS 3475 Database Structure - Alan, Caleb, Matt, Ronnie, Joy");	
		dbtitle.setFont(titleFont);
		dbtitle.setForeground(Color.white);
		createGui(dbtitle,10,15,500,50,topGui); //add title label
		
		//---------Side Panel----------------------------
		JPanel sideGui = new JPanel();
		sideGui.setLayout(null);
		createGui(sideGui,0,81,300,688,this);
		sideGui.setBackground(Color.ORANGE);	//For test
		JLabel tableTitle = new JLabel("Tables");	
		tableTitle.setFont(titleFont);
		createGui(tableTitle,120,-10,300,50,sideGui); //add title label
			//-----------------------------------
			// call method and show tables here
			//-----------------------------------
		tableArr = ctrl.fileHandlerObj.getFileList();
		System.out.println(tableArr);
		tableList = new JList(vtTables);
		createGui((JComponent) tableList,10,30,280,500,sideGui); //jlist
		tableList.addListSelectionListener(this);
		tableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		//---------Center Panel------------------------
		JPanel centerGui = new JPanel();
		centerGui.setLayout(null);
		createGui(centerGui,301,81,979,688,this);
		centerGui.setBackground(Color.white);	//For test
		
		JLabel sqlTitle = new JLabel("SQL Command");	
		sqlTitle.setFont(titleFont);
		createGui(sqlTitle,50,10,300,50,centerGui); //add title label
		taCommand = new JTextArea();
		createGui(taCommand,50,50,500,150,centerGui);
		taCommand.setBorder(BorderFactory.createLineBorder(Color.black));

		JLabel resTitle = new JLabel("Result");	
		resTitle.setFont(titleFont);
		createGui(resTitle,50,260,300,50,centerGui); //add title label
		taResult = new JTextArea();
		taResult.setEditable(false);
		
		scroll1 = new JScrollPane(taResult);
		createGui(scroll1,50,300,800,300,centerGui);
		
			// ------------- run button ----------------------
		bRun = new JButton("Run"); 
		createGui(bRun,570,100,100,50,centerGui);
		bRun.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		bRun.setBackground(Color.white);
		bRun.addActionListener(this);
		
			// ---------- clear button ----------------------
		bClear = new JButton("Clear");
		createGui(bClear,700,100,100,50,centerGui);
		bClear.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		bClear.setBackground(Color.white);
		bClear.addActionListener(this);
	}

	public static void main(String args[]){
		new UserGui(50,50,1280,768);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == bRun){
			currentTable = ctrl.getCommand(taCommand.getText());
/* Alan			if (currentTable == null) {
				taResult.setText("The table selected is null");
			} else {
				taResult.setText(updateContents(currentTable));
			}*/
		} else if (e.getSource() == bClear) {
			taCommand.setText("");
		}
	}

	public void updateTableList(Table _table){
		System.out.println("updateTableList");
		tableArr.add(_table.tableName);
		System.out.println(tableArr);
	}
	
//Alan	public String updateContents(Table _table) {
	public void updateContents(Table _table) {
		String result;
		
		result = _table.tableName + "\n";
		
		for (int i = 0; i < _table.alField.size(); i++) {
			result += _table.alField.get(i).fName+ "\t";
		}
		
		result += "\n------------------------------------------------------------------------------------------------\n";
		
		for (int i = 0; i < _table.alRecord.size(); i++) {
			for (int x = 0; x < _table.alRecord.get(i).getAlValue().size(); x++) {
				result += _table.alRecord.get(i).getAlValue().get(x).toString() + "\t"; 
			}
			result += "\n";
		}
		
		System.out.println(result);
		taResult.setText(result);
//Alan		return result;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
/*		currentTable = ctrl.fileHandlerObj.getFile(String.valueOf(tableList.getSelectedValue()));
		updateContents(currentTable); Alan*/
		updateContents(ctrl.fileHandlerObj.getFile(String.valueOf(tableList.getSelectedValue())));
//Alan		taResult.setText(updateContents(currentTable));
	}

	
	
}
