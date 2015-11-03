import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class a2 
{
	public static void main(String[] args)
	{
		a2Frame a2 = new a2Frame();
		System.out.println("Hello World!");
	} // end main
	
} // end class a2

class a2Frame implements ActionListener
{
	JFrame frame;
	
	// Menu Bar Fields
	JMenuBar menuBar;
	JMenu fileMenu;
	JMenuItem saveFileItem;
	JMenuItem exitFileItem;
	
	public a2Frame()
	{
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// setup menu bar
		setupMenuBar();
		
		// set application visible
		frame.pack();
		frame.setVisible(true);
		
	} // end constructor
	
	private void setupMenuBar()
	{
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		// setup save and save accleator/mnemonic
		saveFileItem = new JMenuItem("Save");
		fileMenu.add(saveFileItem);
		saveFileItem.addActionListener(this);
		saveFileItem.setMnemonic(KeyEvent.VK_S);
		saveFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		
		// setup exit
		exitFileItem = new JMenuItem("Exit");
		fileMenu.add(exitFileItem);
		exitFileItem.addActionListener(this);
		exitFileItem.setMnemonic(KeyEvent.VK_E);
		exitFileItem.setToolTipText("Exit Application");
	} // end method setupMenuBar

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub
		if (e.getSource() == saveFileItem)
		{
			System.out.println("Save Pressed");
		}
		else if(e.getSource() == exitFileItem)
		{
			System.exit(0);
		}
		
	} // end method actionPerformed
	
} // end class a2Frame
