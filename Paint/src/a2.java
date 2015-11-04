import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class a2 
{
	public static void main(String[] args)
	{
		a2Frame a2 = new a2Frame();
		System.out.println("Up and running!!");
	} // end main
	
} // end class a2

class a2Frame extends JFrame implements ActionListener, MouseMotionListener, MouseListener
{
	// fields for status
	final static int PEN = 0;
	final static int ERASER = 1;
	int currentTool;
	
	// frame main panel
	JPanel mainPanel;
	
	// Menu Bar and related fields
	JMenuBar menuBar;
	JMenu fileMenu;
	JMenuItem saveFileItem;
	JMenuItem exitFileItem;
	
	// Tool Bar and related fields
	JToolBar toolBar;
	JButton clearButton;
	JButton strokeButton;
	JButton earseButton;
	
	// Paint Panel
	PaintPanel paintPanel;
	
	public a2Frame()
	{
		
		currentTool = a2Frame.PEN;
		
		mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);
		
		// setup menu bar
		setupMenuBar();
		
		//setup tool bar
		setupToolBarPanel();
		
		// setup Paint Panel
		setupPaintPanel();
		
		// set application visible
		pack();
		setVisible(true);
		
	} // end constructor
	
	private void setupPaintPanel()
	{
		paintPanel = new PaintPanel();
		mainPanel.add(paintPanel, BorderLayout.CENTER);
		
		paintPanel.addMouseListener(this);
		paintPanel.addMouseMotionListener(this);
	} // end method setupPaintPanel
	
	private void setupToolBarPanel()
	{
		// set panel properties
		int toolBarPanelRow = 2; // one for tool bar, one for tool bar selection detail
		int toolBarPanelCol = 1;
		
		// init panel
		JPanel toolBarPanel = new JPanel(new GridLayout(toolBarPanelRow, toolBarPanelCol));
		mainPanel.add(toolBarPanel, BorderLayout.WEST);
		
		// init tool bar
		toolBar = new JToolBar();
		toolBarPanel.add(toolBar);
		
		// clear button
		clearButton = new JButton("Clear");
		clearButton.addActionListener(this);
		toolBar.add(clearButton);
		
		// stroke button
		strokeButton = new JButton("Stroke");
		strokeButton.addActionListener(this);
		toolBar.add(strokeButton);
		
		// earser button
		earseButton = new JButton("Earser");
		earseButton.addActionListener(this);
		toolBar.add(earseButton);
		
		// test
		toolBar.setBackground(Color.BLACK);
//		toolBar.setPreferredSize(new Dimension(300, 300));
//		toolBar.setMaximumSize(new Dimension(1300, 1300));
		
	} // end method setupToolBarPanel
	
	private void setupMenuBar()
	{
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
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
			saveToFile();
//			System.out.println("Save Pressed");
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
		else if (e.getSource() == strokeButton)
		{
			System.out.println("Stroke!");
			currentTool = a2Frame.PEN;
		} // end if, stroke button pressed
		else if (e.getSource() == earseButton)
		{
			System.out.println("Earse!");
			currentTool = a2Frame.ERASER;
		} // end if, eraser button pressed
		
	} // end method actionPerformed
	
	// save current graphics to a file
	private void saveToFile()
	{
		// fetch properties of the drawing
		int paintPanelWidth = paintPanel.getWidth();
		int paintPanelHeight = paintPanel.getHeight();
		
		// create image to hold the drawing
		BufferedImage image = new BufferedImage(paintPanelWidth, paintPanelHeight, BufferedImage.TYPE_INT_BGR);
		Graphics2D g2D = image.createGraphics();
		paintPanel.paint(g2D);
		g2D.dispose();
		
		// save the image to a file
		try
		{
			// ask for path to save file
			JFileChooser fc = new JFileChooser();
			int userInput = fc.showSaveDialog(null);
			
			// end save action if user pressed cancel when chosing the file
			if (userInput == JFileChooser.CANCEL_OPTION)	return;
			
			// write to file and notify user of the action
			String path = fc.getSelectedFile().getCanonicalPath() + ".jpg";
			ImageIO.write( image, "jpg", new File(path) );
			JOptionPane.showMessageDialog(this, "Image saved to " + path);
		}
		catch (Exception e)
		{
			System.out.println("Save File error! Try to restart the application with more privilages");
		} // end catch, save image to a file
		
	} // end method saveToFile

	@Override
	public void mouseClicked(MouseEvent e) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		// TODO Auto-generated method stub
		oldX = e.getX();
		oldY = e.getY();
	} // end method mousePressed

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
	
	@Override
	public void mouseDragged(MouseEvent e) 
	{
		// do not draw, if mouse left button not clicked
		if (!SwingUtilities.isLeftMouseButton(e))	return;
		
		// fetch pointer current location
		int x2 = e.getX();
		int y2 = e.getY();
		
		
		// perform draw/erase on the current spot
		if (currentTool == a2Frame.PEN)	paintPanel.drawInk(oldX, x2, oldY, y2);
		else if (currentTool == a2Frame.ERASER)	paintPanel.erase(oldX, x2, oldY, y2);
		
		// save current pointer location as old for next point draw
		oldX = x2;
		oldY = y2;
		
	} // end method mouseDragged

	@Override
	public void mouseMoved(MouseEvent e) 
	{
		// TODO Auto-generated method stub
		
	}
	
} // end class a2Frame

class PaintPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; // keep compiler happy
	
	
	private ArrayList<Line2D.Double> allStrokes;
	
	PaintPanel()
	{
		// initialize fields
		allStrokes = new ArrayList<>();
		
		// test
		setBackground(Color.ORANGE); // because I love orange!!!!!!
		setPreferredSize(new Dimension(1200, 600));
	} // end constructor class PaintPanel
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		drawAllStrokes(g);
	} // end method paintComponent
	
	public void drawAllStrokes(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		
		for (Line2D.Double line : allStrokes)
		{
			g2D.draw(line);
		} // end for loop, loop thru and draw back strokes
		
	} // end method drawAllStrokes
	
	public void drawInk(int x1, int x2, int y1, int y2)
	{
		// get graphics to draw
		Graphics2D g2 = (Graphics2D) this.getGraphics();
		
		// get draw line
		Line2D.Double inkSegment = new Line2D.Double(x1, y1, x2, y2);
		
		// actual drawing
		g2.setColor(Color.black);
		g2.draw(inkSegment);
		
		// save drawing for windows resize
		allStrokes.add(inkSegment);
	} // end method drawInk
	
	public void erase(int x1, int x2, int y1, int y2)
	{
		// test
		for (Line2D.Double line : allStrokes)
		{
			int lineX1 = (int) line.getX1();
			int lineX2 = (int) line.getX2();
			int lineY1 = (int) line.getY1();
			int lineY2 = (int) line.getY2();
			
			if (lineX1 == x1 && lineX2 == x2 && lineY1 == y1 && lineY2 == y2)
				System.out.println("Erased!!!!!!!!!!!!!!!!!!!");
//			System.out.println("lineX1 " + lineX1 + " ----- x1" + x2 );
			
//			else
//				System.out.println("Oh no!");
		}
		
	}
	
	public void clearPaintPanel()
	{
		allStrokes.clear();
		repaint();
	} // end method clearPaintPanel
	
} // end class PaintPanel 
