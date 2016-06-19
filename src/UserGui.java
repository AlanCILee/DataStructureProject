import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

public class UserGui extends CFrame implements ActionListener{
	JTextArea taCommand;
	JTextArea taResult;
	JButton bRun;
	
	Controller ctrl;
	
	UserGui(int x, int y, int w, int h) {
		super(x, y, w, h);
		
		ctrl = new Controller(this);

		JPanel topGui = new JPanel();
		topGui.setLayout(null);
		createGui(topGui,0,0,1280,80,this);
		topGui.setBackground(Color.cyan);		//For test
		//-----------------------------------
		// add title and team members
		//-----------------------------------		
		
		JPanel sideGui = new JPanel();
		sideGui.setLayout(null);
		createGui(sideGui,0,81,300,688,this);
		sideGui.setBackground(Color.yellow);	//For test
		//-----------------------------------
		// call method and show tables here
		//-----------------------------------
		
		JPanel centerGui = new JPanel();
		centerGui.setLayout(null);
		createGui(centerGui,301,81,979,688,this);
		centerGui.setBackground(Color.green);	//For test
		
		taCommand = new JTextArea();
		createGui(taCommand,50,50,500,150,centerGui);

		taResult = new JTextArea();
		createGui(taResult,50,300,800,300,centerGui);
		
		bRun = new JButton("Run");
		createGui(bRun,600,50,100,50,centerGui);
		bRun.addActionListener(this);
	}

	public static void main(String args[]){
		new UserGui(50,50,1280,768);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource() == bRun){
			ctrl.getCommand(taCommand.getText());
		}		
	}

	public void updateTableList(){
		System.out.println("updateTableList");
	}
	
	public void updateContents(){
		System.out.println("updateContents");
	}
	
}
