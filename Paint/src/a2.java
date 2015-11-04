import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

public class a2 
{
	public static void main(String[] args)
	{
		a2Frame a2 = new a2Frame();
		System.out.println("Up and running!!");
	} // end main
	
} // end class a2

class a2Frame implements ActionListener, MouseMotionListener, MouseListener
{
	JFrame frame;
	
	JPanel mainPanel;
	
	// Menu Bar and related fields
	JMenuBar menuBar;
	JMenu fileMenu;
	JMenuItem saveFileItem;
	JMenuItem exitFileItem;
	
	// Tool Bar and related fields
	JToolBar toolBar;
	JButton clearButton;
	
	// Paint Panel
	PaintPanel paintPanel;
	
	public a2Frame()
	{
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainPanel = new JPanel(new BorderLayout());
		frame.setContentPane(mainPanel);
		
		// setup menu bar
		setupMenuBar();
		
		//setup tool bar
		setupToolBar();
		
		// setup Paint Panel
		setupPaintPanel();
		
		// set application visible
		frame.pack();
		frame.setVisible(true);
		
	} // end constructor
	
	private void setupPaintPanel()
	{
		paintPanel = new PaintPanel();
		mainPanel.add(paintPanel, BorderLayout.CENTER);
		
		paintPanel.addMouseListener(this);
		paintPanel.addMouseMotionListener(this);
	} // end method setupPaintPanel
	
	private void setupToolBar()
	{
		// init tool bar
		toolBar = new JToolBar();
		mainPanel.add(toolBar, BorderLayout.WEST);
		
		// clear button
		clearButton = new JButton("Clear");
		toolBar.add(clearButton);
		clearButton.addActionListener(this);
		
		// test
		toolBar.setBackground(Color.BLACK);
//		toolBar.setPreferredSize(new Dimension(300, 300));
//		toolBar.setMaximumSize(new Dimension(1300, 1300));
		
	} // end method setupToolBar
	
	private void setupMenuBar()
	{
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		// setup save
		saveFileItem = new JMenuItem("Save");
		fileMenu.add(saveFileItem);
		saveFileItem.addActionListener(this);
		saveFileItem.setMnemonic(KeyEvent.VK_S);
		saveFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		
		// setup exit
		fileMenu.addSeparator();
		exitFileItem = new JMenuItem("Exit");
		fileMenu.add(exitFileItem);
		exitFileItem.addActionListener(this);
		
		exitFileItem.setMnemonic(KeyEvent.VK_E); // test Samuel: not sure if we need this for exit 
		exitFileItem.setToolTipText("Exit Application");
	} // end method setupMenuBar

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub
		if (e.getSource() == saveFileItem)
		{
			System.out.println("Save Pressed");
		} // end if, save file item pressed
		else if(e.getSource() == exitFileItem)
		{
			System.exit(0);
		}// end if, exit file Item pressed
		else if (e.getSource() == clearButton)
		{
			System.out.println("Clear!");
			paintPanel.clearPaintPanel();
		} // end if, clear button pressed
		
	} // end method actionPerformed

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	// test
	private int oldX;
	private int oldY;
	private boolean firstPoint = true;
	
	@Override
	public void mouseDragged(MouseEvent e) 
	{
		
		// fetch pointer current location
		int x2 = e.getX();
		int y2 = e.getY();
		
		// for the first point draw
		if (firstPoint)
		{
			oldX = x2;
			oldY = y2;
			firstPoint = false;
		} // end if, first point initial setup
		
		paintPanel.drawInk(oldX, x2, oldY, y2);
		
		// save current pointer locaation as old for next point draw
		oldX = x2;
		oldY = y2;
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
} // end class a2Frame

class PaintPanel extends JPanel
{
	PaintPanel()
	{
		// test
		setBackground(Color.ORANGE); // because I love orange!!!!!!
		setPreferredSize(new Dimension(1200, 600));
	}
	
	public void drawInk(int x1, int x2, int y1, int y2)
	{
		// get graphics to draw
		Graphics2D g2 = (Graphics2D) this.getGraphics();
		
		// get draw line
		Line2D.Double inkSegment = new Line2D.Double(x1, y1, x2, y2);
		
		// actual drawing
		g2.setColor(Color.black);
		g2.draw(inkSegment);
		
	}
	
	public void clearPaintPanel()
	{
		repaint();
	}
}
