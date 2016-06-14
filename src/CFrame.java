import java.awt.Container;

import javax.swing.*;

public class CFrame extends JFrame{
	CFrame(int x, int y, int w, int h){
		this.setLayout(null);
		this.setBounds(x,y,w,h);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void createGui(JComponent comp, int x,int y,int w,int h,Container frame){
		frame.add(comp);
		comp.setBounds(x,y,w,h);		
	}
}
