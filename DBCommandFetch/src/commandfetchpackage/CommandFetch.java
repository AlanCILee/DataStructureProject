/*
This is a test program containing a GUI to test Matt's crap code

DO NOT INCLUDE THIS IN THE FINAL PROGRAM
*/
package commandfetchpackage;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class CommandFetch 
{
	public static void main(String args[])
	{
		testFrame tf = new testFrame("i love to code poorly");
		tf.setSize(700,700);
		tf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tf.setVisible(true);
	}
	
	public static class testFrame extends JFrame
	{
		Container con;
		JTextArea txtCommand;
		JButton btnExecute;
		JTextArea txtOutput;
		TitledBorder tbcommand = new TitledBorder("Enter Command");
		TitledBorder tbOutput = new TitledBorder("Output");
		
		JPanel pA, pB;
		
		Font fCon = new Font("Consolas",Font.PLAIN, 12);
		
		testFrame(String title)
		{
			super(title);
			
			con = getContentPane();
			con.setLayout(new GridLayout(2,1,10,10));
			
			pA = new JPanel();
			pA.setLayout(new FlowLayout(FlowLayout.LEFT));
			txtCommand = new JTextArea(7,96);
			txtCommand.setBorder(tbcommand);
			txtCommand.setFont(fCon);
			
			btnExecute = new JButton("Execute");
			pA.add(txtCommand);
			pA.add(btnExecute);
			
			pB = new JPanel();
			pB.setLayout(new BorderLayout());
			txtOutput = new JTextArea();
			txtOutput.setBorder(tbOutput);
			txtOutput.setFont(fCon);
			pB.add(txtOutput);
			
			con.add(pA);
			con.add(pB);
		}
	}
}
