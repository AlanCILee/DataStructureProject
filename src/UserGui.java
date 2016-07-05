import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*; 


public class UserGui extends CFrame implements ActionListener, KeyListener, ListSelectionListener{
	
	JTextArea taCommand;
	JTextArea taResult;
	JScrollPane scroll1;
	JList tableList;
	//ListModel<ArrayList> tableList;
	JButton bRun;
	
	Controller ctrl;
	
	UserGui(int x, int y, int w, int h) {
		super(x, y, w, h);

		Font titleFont = new Font("Arial", Font.BOLD, 15);
		//Border border = new Border();
		
		ctrl = new Controller(this);

		//----------Top Panel----------------------------
		JPanel topGui = new JPanel();
		topGui.setLayout(null);
		createGui(topGui,0,0,1280,80,this); //add topGui
		topGui.setBackground(Color.DARK_GRAY);		
		JLabel dbtitle = new JLabel("CSIS 3475 Database Structure - Alan, Caleb, Matt, Ronnie, Joy");	
		dbtitle.setFont(titleFont);
		dbtitle.setForeground(Color.white);
		createGui(dbtitle,10,10,500,50,topGui); //add title label
		
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
		ArrayList tableArr = Controller.fileHandlerObj.getFileList();
	//	tableList = new JList<ArrayList>(tableArr);
		Vector vtTables = new Vector(tableArr);
	//	tableList = tableArr;
		tableList = new JList(vtTables);
		createGui((JComponent) tableList,10,30,280,500,sideGui); //jlist
		tableList.addListSelectionListener(this);
	//	tableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	//	tableList.revalidate();
		
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
		taCommand.addKeyListener(this);

		JLabel resTitle = new JLabel("Result");	
		resTitle.setFont(titleFont);
		createGui(resTitle,50,260,300,50,centerGui); //add title label
		taResult = new JTextArea();
		
		scroll1 = new JScrollPane(taResult);
		createGui(scroll1,50,300,800,300,centerGui);
		
		bRun = new JButton("Run");
		createGui(bRun,570,100,100,50,centerGui);
		bRun.setOpaque(true);
		bRun.setBackground(Color.DARK_GRAY);
		bRun.addActionListener(this);
	}

	public static void main(String args[]){
		new UserGui(50,50,1280,768);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == bRun){
			ctrl.getCommand(taCommand.getText());
			System.out.println("click"); //test
			taResult.setText(String.valueOf(taCommand.getText())); //test
			
		}		
	}

	public void updateTableList(){
		System.out.println("updateTableList");
	}
	
	public void updateContents(){
		System.out.println("updateContents");
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
