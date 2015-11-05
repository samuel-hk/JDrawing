import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
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

class a2Frame extends JFrame implements ActionListener, MouseMotionListener, MouseListener, ItemListener
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
	JPanel toolBarDetailPanel;
	JPanel toolBarPanel;
	JButton strokeColorButton;
	JComboBox<Integer> strokeWidthBox;

	// Paint Panel
	PaintPanel paintPanel;

	public a2Frame()
	{
		// init variables to avoid null pointer exception (no real purpose here)
		strokeColorButton = new JButton();
		strokeWidthBox = new JComboBox<>();
		
		// set current tool to pen
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
		toolBarPanel = new JPanel(new GridLayout(toolBarPanelRow, toolBarPanelCol));
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

		// init toolBarDetailPanel
		toolBarDetailPanel = new JPanel();
		toolBarPanel.add(toolBarDetailPanel);
		
		// test
		toolBarDetailPanel.setBackground(Color.red);
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
		} // end if, save file item pressed
		else if(e.getSource() == exitFileItem)
		{
			System.exit(0);
		}// end if, exit file Item pressed
		else if (e.getSource() == clearButton)
		{
			paintPanel.clearPaintPanel();
		} // end if, clear button pressed
		else if (e.getSource() == strokeButton)
		{
			currentTool = a2Frame.PEN;
			fillToolBarDetailPanelWithPen();
		} // end if, stroke button pressed
		else if (e.getSource() == earseButton)
		{
			System.out.println("Earse!");
			currentTool = a2Frame.ERASER;
			
			// test
			paintPanel.setScale();
		} // end if, eraser button pressed
		else if (e.getSource() == strokeColorButton)
		{
			setStrokeColor();
		} // end if, stroke color button

	} // end method actionPerformed
	
	private void setStrokeColor()
	{
		// fetch user selection
		Color tmp = JColorChooser.showDialog(this, "Choose Color", Color.black);
		
		// if user did not select a color, stop set color process
		if (tmp == null)	return;
		
		// set user sleection effective
		paintPanel.setStrokeColor(tmp);
	} // end method setStrokeColor
	
	private void fillToolBarDetailPanelWithPen()
	{
		// clear everything
		toolBarDetailPanel.removeAll();
		toolBarDetailPanel.repaint();
		
		// add color chooser
		strokeColorButton = new JButton("Color");
		strokeColorButton.addActionListener(this);
		toolBarDetailPanel.add(strokeColorButton);

		// add stroke width setting
		Integer[] size = {1,2,3,4,5,6,7,8,9} ;
		strokeWidthBox = new JComboBox<>(size);
		strokeWidthBox.addItemListener(this);
		toolBarDetailPanel.add(strokeWidthBox);
		
		toolBarDetailPanel.revalidate();
	} // end method fillToolBarDetailPanelWithPen

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

	@Override
	public void itemStateChanged(ItemEvent e) 
	{
		// TODO Auto-generated method stub
		if (e.getSource() == strokeWidthBox)
		{
			String selected = strokeWidthBox.getSelectedItem().toString();
			int selectedSizeInt = Integer.parseInt(selected);
			float selectedSizeFlt = (float) selectedSizeInt;
			paintPanel.setStrokeWidth(selectedSizeFlt);
//			JOptionPane.showMessageDialog(this, selected);
		} // end if, stroke state change
	}

} // end class a2Frame

class PaintPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; // keep compiler happy


	private ArrayList<ExtendedLine2DDouble> allStrokes;
	
	private Color strokeColor;
	private float strokeWidth;

	PaintPanel()
	{
		// initialize fields
		allStrokes = new ArrayList<>();
		
		// init default properties
		strokeColor = Color.black;
		strokeWidth = 1.0f;

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

		for (ExtendedLine2DDouble line : allStrokes)
		{
			g2D.setStroke(line.stroke);
			g2D.setColor(line.color);
			g2D.draw(line);
		} // end for loop, loop thru and draw back strokes

	} // end method drawAllStrokes

	public void drawInk(int x1, int x2, int y1, int y2)
	{
		// get graphics to draw
		Graphics2D g2 = (Graphics2D) this.getGraphics();

		// set stroke properties
		BasicStroke stroke = new BasicStroke(this.strokeWidth);
		g2.setStroke(stroke);
		g2.setColor(strokeColor);
		
		// get draw line
		ExtendedLine2DDouble inkSegment = new ExtendedLine2DDouble(x1, y1, x2, y2);
		inkSegment.color = strokeColor;
		inkSegment.stroke = stroke;
		
		// actual drawing
		g2.draw(inkSegment);

		// save drawing for windows resize
		allStrokes.add(inkSegment);
	} // end method drawInk
	
	public void setStrokeColor(Color color)
	{
		strokeColor = color;
	} // end method setStrokeColor
	
	public void setStrokeWidth(float width)
	{
		strokeWidth = width;
	} // end method  setStrokeWidth

	public void erase(int x1, int x2, int y1, int y2)
	{

		// eraser size
		int eraserSize = 80;
		
		// test
		ExtendedLine2DDouble line = null;
		for (int i = 0; i < allStrokes.size(); i++)
		{
			line = allStrokes.get(i);

			// fetch line properties
			Double lineX1 = line.x1;
			Double lineX2 = line.x2;
			Double lineY1 = line.y1;
			Double lineY2 = line.y2;

			// determine  if
			boolean sameLine = true;
			if ( !( x1 - lineX1 < eraserSize && x1 - lineX1 > eraserSize*-1) )
				sameLine = false;

			if ( !( x2 - lineX2 < eraserSize && x2 - lineX2 > eraserSize*-1) )
				sameLine = false;

			if ( !( y1 - lineY1 < eraserSize && y1 - lineY1  > eraserSize*-1) )
				sameLine = false;

			if ( !( y2 - lineY2 < eraserSize && y2 - lineY2 > eraserSize*-1) )
				sameLine = false;
			
			if (sameLine)	allStrokes.remove(line);
			

			//			if (lineX1.equals(x1) && lineX2 == x2 && lineY1 == y1 && lineY2 == y2)
			//				System.out.println("Erased!!!!!!!!!!!!!!!!!!!");
			//			System.out.println("lineX1 " + lineX1 + " ----- x1" + x2 );

		}
		repaint();
	} // end method erase
	
	// test
	public void setScale()
	{
		System.out.println("Scale!");
		AffineTransform tran = AffineTransform.getScaleInstance(3.0, 3.0);
		Graphics2D g = (Graphics2D) this.getGraphics();
//		g.setTransform(tran);
		g.scale(5.0, 5.0);
		
	} // end method setScale

	public void clearPaintPanel()
	{
		allStrokes.clear();
		repaint();
	} // end method clearPaintPanel

} // end class PaintPanel 
