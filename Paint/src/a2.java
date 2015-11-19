import java.awt.*;	
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class a2 
{

	public static void main(String[] args)
	{
		a2Frame a2 = new a2Frame();
		a2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		System.out.println("Up and running!!");
	} // end main

} // end class a2

class a2Frame extends JFrame implements ActionListener, MouseMotionListener, MouseListener, ItemListener, WindowListener
{
	// fields for status
	final static int PEN = 0;
	final static int ERASER = 1;
	final static int TEXT = 2;
	String filePath;
	int currentTool;

	// frame main panel field
	JPanel mainPanel;

	// Menu Bar and related fields
	JMenuBar menuBar;
	JMenu fileMenu;
	JMenuItem saveFileItem, saveAsFileItem;
	JMenuItem exitFileItem;
	JMenuItem newFileItem;

	// Tool Bar and related fields
	JToolBar toolBar;
	JButton clearButton, strokeButton, earseButton, objectButton, changeBackgroundButton;
	JButton textButton;
	JPanel toolBarDetailPanel;
	JPanel toolBarPanel;
	JButton strokeColorButton;
	JPanel strokeColorPanel;
	JPanel strokeWeightPanel;

	// Text Objects Detail Panel related fields
	JPanel textDetailPanel;
	JPanel textStylePanel;
	JPanel textSizePanel;
	JPanel textContentPanel;
	JTextField textInputField;
	JLabel drawTextLabel;
	JComboBox<Integer> textSizeBox;
	JCheckBox textBoldCheckBox, textItalicsCheckBox, textUnderLineCheckBox;
	JComboBox<String> fontFamily;
	JButton textColorButton;

	// Draw Objects Detail Panel
	JPanel shapeChooserPanel;
	JRadioButton rectangleShapeButton;
	JRadioButton ovalShapeButton;
	JRadioButton circleShapeButton;
	JRadioButton lineShapeButton;
	ButtonGroup shapeButtonGroup;
	JButton objectBorderColorButton;
	JPanel shapeBorderColorPanel;
	JPanel shapeBorderThicknessPanel;
	JLabel shapeFillColorLabel;
	JPanel shapeFillColorPanel;
	JButton shapeFillColorButton;
	JButton shapeNoFillingButton;
	JLabel shapeNoFillingLabel;
	

	// import image realted fields
	JPanel importImagePanel;
	JButton importImageButton;

	JComboBox<Integer> strokeWidthBox;
	JComboBox<Integer> objectBorderThicknessBox;

	//coordinates for mouseEvents
	public double pressX;
	double pressY;
	double releaseX;
	double releaseY;

	// Paint Panel
	PaintPanel paintPanel;

	public a2Frame()
	{
		// init variables to avoid null pointer exception (no real purpose here)
		strokeColorButton = new JButton();
		strokeWidthBox = new JComboBox<>();
		objectBorderColorButton = new JButton();
		objectBorderThicknessBox = new JComboBox<>();
		shapeFillColorButton = new JButton();
		shapeNoFillingButton = new JButton();
		filePath = "";

		mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);

		// setup menu bar
		setupMenuBar();

		//setup tool bar
		setupToolBarPanel();

		// setup Paint Panel
		setupPaintPanel();

		// set current tool to pen and display pen properties
		setCurrentTool(a2Frame.PEN);

		// set application window properties
		addWindowListener(this);

		// set application visible
		pack();
		setVisible(true);

	} // end constructor a2JFrame

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

		// text button
		textButton = new JButton("Text");
		textButton.addActionListener(this);
		toolBar.add(textButton);

		// earser button
		earseButton = new JButton("Earser");
		earseButton.addActionListener(this);
		toolBar.add(earseButton);

		//draw object button
		objectButton = new JButton("Draw Object");
		objectButton.addActionListener(this);
		toolBar.add(objectButton);

		//change background color button
		changeBackgroundButton = new JButton("Change Background Color");
		changeBackgroundButton.addActionListener(this);
		toolBar.add(changeBackgroundButton);

		// import image button
		importImageButton = new JButton("Import Image");
		importImageButton.addActionListener(this);
		toolBar.add(importImageButton);

		// init toolBarDetailPanel
		toolBarDetailPanel = new JPanel();
		toolBarPanel.add(toolBarDetailPanel);
		toolBarDetailPanel.setLayout(new BoxLayout(toolBarDetailPanel,BoxLayout.Y_AXIS));

		// test
		toolBarDetailPanel.setBackground(Color.red);
		toolBar.setBackground(Color.gray);
		//		toolBar.setPreferredSize(new Dimension(300, 300));
		//		toolBar.setMaximumSize(new Dimension(1300, 1300));

	} // end method setupToolBarPanel

	private void setupMenuBar()
	{
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		// setup new
		newFileItem = new JMenuItem("New");
		fileMenu.add(newFileItem);
		newFileItem.addActionListener(this);
		newFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));

		// setup save
		saveFileItem = new JMenuItem("Save");
		fileMenu.add(saveFileItem);
		saveFileItem.addActionListener(this);
		saveFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));

		// setup save as
		saveAsFileItem = new JMenuItem("Save As...");
		fileMenu.add(saveAsFileItem);
		saveAsFileItem.addActionListener(this);
		saveAsFileItem.setMnemonic(KeyEvent.VK_S);
		saveAsFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));

		// setup exit
		fileMenu.addSeparator();
		exitFileItem = new JMenuItem("Exit");
		fileMenu.add(exitFileItem);
		exitFileItem.addActionListener(this);

		exitFileItem.setMnemonic(KeyEvent.VK_E); // test Samuel: not sure if we need this for exit 
		exitFileItem.setToolTipText("Exit Application");
	} // end method setupMenuBar

	private void setCurrentTool(int tool)
	{
		switch (tool)
		{
		case a2Frame.PEN:
			setCurrentToolPen();
			break;
		case a2Frame.ERASER:
			setCurrentToolEraser();
			break;
		case a2Frame.TEXT:
			setCurrentToolText();
			break;
		} // end switch, set tool according to param
	} // end method setCurrentTool

	private void setCurrentToolText()
	{
		currentTool = a2Frame.TEXT;
		fillToolBarDetailPanelWithText();
	} // end method setCurrentToolText

	private void fillToolBarDetailPanelWithText()
	{
		// clear detail pane for text buttons arrival
		toolBarDetailPanel.removeAll();
		//toolBarDetailPanel.revalidate();
		toolBarDetailPanel.repaint();

		// create panel to hold text detail buttons
		textDetailPanel = new JPanel();
		textDetailPanel.setLayout(new BoxLayout(textDetailPanel,BoxLayout.Y_AXIS));

		// add "Drawing Text" text label
		//drawTextLabel = new JLabel("Insert Text");
		//textDetailPanel.add(drawTextLabel);
		textContentPanel = new JPanel();
		textContentPanel.setLayout(new BoxLayout(textContentPanel,BoxLayout.Y_AXIS));
		TitledBorder contentTitle;
		contentTitle = BorderFactory.createTitledBorder("Content");
		textContentPanel.setBorder(contentTitle);

		// add text input field
		int textInputFieldWidth = 10;
		textInputField = new JTextField(textInputFieldWidth);
		textContentPanel.add(textInputField);
		textDetailPanel.add(textContentPanel);

		// add text style panel
		textStylePanel = new JPanel();
		textStylePanel.setLayout(new BoxLayout(textStylePanel,BoxLayout.Y_AXIS));
		TitledBorder styleTitle;
		styleTitle = BorderFactory.createTitledBorder("Styles");
		textStylePanel.setBorder(styleTitle);
		textBoldCheckBox = new JCheckBox("Bold");
		textBoldCheckBox.addItemListener(this);
		textBoldCheckBox.setSelected(false);
		textItalicsCheckBox = new JCheckBox("Italic");
		textItalicsCheckBox.addItemListener(this);
		textItalicsCheckBox.setSelected(false);
		textUnderLineCheckBox = new JCheckBox("UnderLine");
		textUnderLineCheckBox.addItemListener(this);
		textUnderLineCheckBox.setSelected(false);
		textStylePanel.add(textBoldCheckBox);
		textStylePanel.add(textItalicsCheckBox);
		textStylePanel.add(textUnderLineCheckBox);
		textDetailPanel.add(textStylePanel);

		// add text size panel
		textSizePanel = new JPanel();
		textSizePanel.setLayout(new BoxLayout(textSizePanel,BoxLayout.Y_AXIS));
		TitledBorder sizeTitle;
		sizeTitle = BorderFactory.createTitledBorder("Size");
		textSizePanel.setBorder(sizeTitle);
		Integer[] size = {10,12,14,16,18,20,22,24,26,28,30,32,34,36} ;
		textSizeBox = new JComboBox<>(size);
		textSizeBox.addItemListener(this);
		textSizePanel.add(textSizeBox);
		textDetailPanel.add(textSizePanel);

		// add text color panel
		JPanel textColorPanel = new JPanel();
		textColorPanel.setBorder(BorderFactory.createTitledBorder("Color"));
		textColorButton = new JButton("Color");
		textColorButton.addActionListener(this);
		textColorPanel.add(textColorButton);
		textDetailPanel.add(textColorPanel);

		// add font panel
		JPanel textFontFamilyPanel = new JPanel();
		textFontFamilyPanel.setBorder(BorderFactory.createTitledBorder("Font"));
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontList = ge.getAvailableFontFamilyNames();
		fontFamily = new JComboBox<>(fontList);
		textFontFamilyPanel.add(fontFamily);


		textDetailPanel.add(textFontFamilyPanel);
//		textDetailPanel.setPreferredSize(toolBarDetailPanel.getSize());



		// show textDetailPanel on toolBarDetailPanel
		toolBarDetailPanel.add(textDetailPanel);
		toolBarDetailPanel.revalidate();




	} // end method fillToolBarDetailPanelWithText

	private void setCurrentToolPen()
	{
		currentTool = a2Frame.PEN;
		fillToolBarDetailPanelWithPen();
	} // end method setCurrentToolPen

	private void setCurrentToolEraser()
	{
		currentTool = a2Frame.ERASER;
	} // end method setCurrentToolEraser

	private void createNewPaint()
	{
		// ask for confirmation to discard current paintings
		String message = "Create new painting? Current Painting will be lost.";
		int answer = JOptionPane.showConfirmDialog(this, message, "Create", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE);

		// create new painting if user selected yes
		if (answer == JOptionPane.YES_OPTION)	paintPanel.clearPaintPanel();
	} // end method createNewPaintPanel

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == newFileItem)
		{
			System.out.println("New File Item pressed!");
			createNewPaint();
		} // end if, new file item pressed
		else if (e.getSource() == saveFileItem)
		{
			System.out.println("Save Only pressed!");
			saveToFile();
		} // end if, save file item pressed
		else if (e.getSource() == saveAsFileItem)
		{
			System.out.println("Save As pressed!");
			saveAsToFile();
		} // end if, save as file item pressed
		else if(e.getSource() == exitFileItem)
		{
			askExitWithoutSaving();
		}// end if, exit file Item pressed
		else if (e.getSource() == clearButton)
		{
			paintPanel.clearPaintPanel();
		} // end if, clear button pressed
		else if (e.getSource() == strokeButton)
		{
			setCurrentTool(a2Frame.PEN);
		} // end if, stroke button pressed
		else if (e.getSource() == textButton)
		{
			setCurrentTool(a2Frame.TEXT);
		} // end if, text button pressed
		else if(e.getSource() == objectButton)
		{
			currentTool = Cursor.HAND_CURSOR;
			fillToolBarDetailPanelWithShape();
		}
		else if (e.getSource() == earseButton)
		{
			setCurrentTool(a2Frame.ERASER);
		} // end if, eraser button pressed
		else if (e.getSource() == changeBackgroundButton)
		{
			setBackgroundColor();
		}// end if, changeBackgroundButton
		else if (e.getSource() == strokeColorButton)
		{
			setStrokeColor();
		} // end if, stroke color button
		else if(e.getSource() == objectBorderColorButton)
		{
			setObjectBorderColor();
		}// end if, stroke color button
		else if(e.getSource() == shapeFillColorButton)
		{
			paintPanel.fillOrDraw = 1;
			setObjectFillColor();
		}
		else if(e.getSource() == shapeNoFillingButton)
		{
			paintPanel.fillOrDraw = 0;
		}
		else if (e.getSource() == textColorButton)
		{
			System.out.println("Color!");
			Color tmp = JColorChooser.showDialog(this, "Choose Color", Color.black);
			paintPanel.setTextColor(tmp);
		}
		else if (e.getSource() == importImageButton)
		{
			importImage();
		}

	} // end method actionPerformed
	
	private void askExitWithoutSaving()
	{
		int n = JOptionPane.showConfirmDialog(mainPanel, "Exit Without Saving?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
		if (n == JOptionPane.YES_OPTION)	System.exit(0);
		else if (n == JOptionPane.NO_OPTION)	saveToFile();
	}  // end method askExitWithoutExiting

	private void importImage()
	{

		// present open file dialog and receive input
		JFileChooser fc = new JFileChooser();
		int userInput = fc.showOpenDialog(null);

		// cancel open action if user press cancel
		if (userInput == JFileChooser.CANCEL_OPTION)	return;

		// fetch properties of the file 
		String path = "";
		try {
			path = fc.getSelectedFile().getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Fail to open file");
			return;
		}

		// import image to panel
		paintPanel.importImage(path);
	} // end method importImage

	private void setStrokeColor()
	{
		// fetch user selection
		Color tmp = JColorChooser.showDialog(this, "Choose Color", Color.black);

		// if user did not select a color, stop set color process
		if (tmp == null)	return;

		// set user selection effective
		paintPanel.setStrokeColor(tmp);
	} // end method setStrokeColor

	private void setBackgroundColor()
	{
		//fetch user selection
		Color tmp = JColorChooser.showDialog(this, "Choose Color", Color.white);

		// if user did not select a color, stop set color process
		if (tmp == null) return;

		//set user selection effective
		paintPanel.setBackground(tmp);
	}

	private void setObjectBorderColor()
	{
		//fetch user selection
		Color tmp = JColorChooser.showDialog(this, "Choose Color", Color.black);

		//if user did not select a color, stop set color process
		if(tmp == null)	return;

		//set user selection effective
		paintPanel.setObjectBorderColor(tmp);
	}

	private void setObjectFillColor()
	{
		//fetch user selection
		Color tmp = JColorChooser.showDialog(this, "Choose Color", Color.orange);

		//if user did not select a color, stop set color process
		if(tmp == null)	return;

		//set user selection effective
		paintPanel.setObjectFillColor(tmp);
	}

	private void fillToolBarDetailPanelWithPen()
	{
		// clear everything
		toolBarDetailPanel.removeAll();
		toolBarDetailPanel.repaint();
		

		// add color chooser
		strokeColorPanel = new JPanel();
		strokeColorPanel.setLayout(new BoxLayout(strokeColorPanel,BoxLayout.Y_AXIS));
		TitledBorder strokeColorTitle;
		strokeColorTitle = BorderFactory.createTitledBorder("Stroke Color");
		strokeColorPanel.setBorder(strokeColorTitle);
		strokeColorButton = new JButton("Color");
		strokeColorButton.addActionListener(this);
		strokeColorPanel.add(strokeColorButton);
		toolBarDetailPanel.add(strokeColorPanel);

		// add stroke width setting
		strokeWeightPanel = new JPanel();
		strokeWeightPanel.setLayout(new BoxLayout(strokeWeightPanel,BoxLayout.Y_AXIS));
		TitledBorder strokeWeightTitle;
		strokeWeightTitle = BorderFactory.createTitledBorder("Weight");
		strokeWeightPanel.setBorder(strokeWeightTitle);
		Integer[] size = {1,2,3,4,5,6,7,8,9} ;
		strokeWidthBox = new JComboBox<>(size);
		strokeWidthBox.addItemListener(this);
		strokeWeightPanel.add(strokeWidthBox);
		toolBarDetailPanel.add(strokeWeightPanel);

		// repaint tool bar for new widget to show up
		toolBarDetailPanel.revalidate();
	} // end method fillToolBarDetailPanelWithPen

	private void fillToolBarDetailPanelWithShape()
	{
		// clear everything
		toolBarDetailPanel.removeAll();
		toolBarDetailPanel.repaint();

		// panel to hold shape related fields
		JPanel shapeDetailPanel = new JPanel();
		shapeDetailPanel.setLayout(new BoxLayout(shapeDetailPanel,BoxLayout.Y_AXIS));

		shapeChooserPanel = new JPanel();
		shapeChooserPanel.setLayout(new BoxLayout(shapeChooserPanel,BoxLayout.Y_AXIS));

		// add shape button
		rectangleShapeButton = new JRadioButton("Rectangle");
		ovalShapeButton = new JRadioButton("Oval");
		circleShapeButton = new JRadioButton("Circle");
		lineShapeButton = new JRadioButton("Line");
		shapeButtonGroup = new ButtonGroup();
		shapeButtonGroup.add(rectangleShapeButton);
		shapeButtonGroup.add(ovalShapeButton);
		shapeButtonGroup.add(circleShapeButton);
		shapeButtonGroup.add(lineShapeButton);

		//toolBarDetailPanel.add(rectangleShapeButton);
		//toolBarDetailPanel.add(ovalShapeButton);


		//add radioButtons to subPanel
		shapeChooserPanel.add(rectangleShapeButton);
		shapeChooserPanel.add(ovalShapeButton);
		shapeChooserPanel.add(circleShapeButton);
		shapeChooserPanel.add(lineShapeButton);
		shapeChooserPanel.setBorder(BorderFactory.createTitledBorder("Shape"));
//		shapeChooserPanel.setBackground(Color.red);

		//add subPanel to toolBarDetailPanel
		shapeDetailPanel.add(shapeChooserPanel);

		//Border Color Panel
		shapeBorderColorPanel = new JPanel();
		shapeBorderColorPanel.setLayout(new BoxLayout(shapeBorderColorPanel,BoxLayout.Y_AXIS));
//		shapeBorderColorPanel.setBackground(Color.red);
		objectBorderColorButton = new JButton("Choose Color");
		objectBorderColorButton.addActionListener(this);

		shapeBorderColorPanel.add(objectBorderColorButton);
		shapeBorderColorPanel.setBorder(BorderFactory.createTitledBorder("Border Color"));
		shapeDetailPanel.add(shapeBorderColorPanel);

		//Border Thickness Panel
		Integer[] size = {1,2,3,4,5,6,7,8,9};
		objectBorderThicknessBox = new JComboBox<>(size);
		objectBorderThicknessBox.addItemListener(this);

		shapeBorderThicknessPanel = new JPanel();
		shapeBorderThicknessPanel.setLayout(new BoxLayout(shapeBorderThicknessPanel, BoxLayout.Y_AXIS ));

		shapeBorderThicknessPanel.add(objectBorderThicknessBox);
		shapeBorderThicknessPanel.setBorder(BorderFactory.createTitledBorder("Thickness"));
		shapeDetailPanel.add(shapeBorderThicknessPanel);

		//object fill color panel
		shapeFillColorPanel = new JPanel();
		shapeFillColorPanel.setLayout(new BoxLayout(shapeFillColorPanel,BoxLayout.Y_AXIS));
		shapeFillColorLabel = new JLabel("Filled Object");
		shapeFillColorButton = new JButton("Choose Color");
		shapeFillColorButton.addActionListener(this);
		//shapeNoFillingLabel = new JLabel("Draw Without Filling");
		shapeFillColorPanel.setBorder(BorderFactory.createTitledBorder("Filling Color"));
		shapeNoFillingButton = new JButton("Clear Filling");
		shapeNoFillingButton.addActionListener(this);

		shapeFillColorPanel.add(shapeFillColorButton);
		shapeFillColorPanel.add(shapeNoFillingButton);
		shapeDetailPanel.add(shapeFillColorPanel);

		toolBarDetailPanel.add(shapeDetailPanel);
		
		rectangleShapeButton.setSelected(true);
		toolBarDetailPanel.revalidate();

	}
	

	private void saveToFile()
	{
		// fresh save, save like save as
		if (filePath.equals(""))	saveAsToFile();

		// save to the last saved file destination
		else	saveHelper("");

	} // end method saveToFile

	// save current graphics to a file
	private void saveAsToFile()
	{

		// save the image to a file
		try
		{
			// ask for path to save file
			JFileChooser fc = new JFileChooser();
			int userInput = fc.showSaveDialog(null);

			// end save action if user pressed cancel when chosing the file
			if (userInput == JFileChooser.CANCEL_OPTION)	return;

			// get real destination path 
			String path = fc.getSelectedFile().getCanonicalPath();
			if ( !path.endsWith(".jpg") )	path += ".jpg";
			
			// prompt user to overwrite file if file exist
			File f = new File(path);
			if (f.exists())
			{
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to overwrite " + fc.getSelectedFile().getName() + " ?", "Overwrite", JOptionPane.YES_NO_CANCEL_OPTION);
				if (choice != JOptionPane.YES_OPTION)	return;
			}
			
			// write to file and notify user of the action
			saveHelper(path);

		}
		catch (Exception e)
		{
			System.out.println("Save File error! Try to restart the application with more privilages");
		} // end catch, save image to a file

	} // end method saveAsToFile

	private void saveHelper(String path)
	{
		// use default file destination if no path is specified
		if (path.equals(""))	path = filePath;

		// fetch properties of the drawing
		int paintPanelWidth = paintPanel.getWidth();
		int paintPanelHeight = paintPanel.getHeight();

		// create image to hold the drawing
		BufferedImage image = new BufferedImage(paintPanelWidth, paintPanelHeight, BufferedImage.TYPE_INT_BGR);
		Graphics2D g2D = image.createGraphics();
		paintPanel.paint(g2D);
		g2D.dispose();


		// write to file and notify user of the action
		try 
		{
			ImageIO.write( image, "jpg", new File(path) );
			JOptionPane.showMessageDialog(this, "Image saved to " + path);

			// save file name into var
			filePath = path;
		}  // end try, save file try
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // end catch, IOException when saving to file

	} // end method saveHelper

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

		//get coordinates when mouse is pressed
		pressX = e.getX();
		pressY = e.getY();

		// test
		if (currentTool == a2Frame.TEXT)	paintPanel.drawText(textInputField.getText(), (String) fontFamily.getSelectedItem(), (int) pressX, (int) pressY);


	} // end method mousePressed

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

		//get coordinates when mouse is released
		releaseX = e.getX();
		releaseY = e.getY();

		//draw rectangle
		if(currentTool==Cursor.HAND_CURSOR && rectangleShapeButton.isSelected())
			paintPanel.drawRectangle(pressX, releaseX, pressY, releaseY);
		//draw oval
		else if(currentTool == Cursor.HAND_CURSOR && ovalShapeButton.isSelected())
			paintPanel.drawOval(pressX, releaseX, pressY, releaseY);
		//draw circle
		else if(currentTool == Cursor.HAND_CURSOR && circleShapeButton.isSelected())
			paintPanel.drawCircle(pressX, releaseX, pressY, releaseY);
		//draw line
		else if(currentTool == Cursor.HAND_CURSOR && lineShapeButton.isSelected())
			paintPanel.drawLine(pressX, releaseX, pressY, releaseY);	

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
	} // end method mouseMoved

	@Override
	public void itemStateChanged(ItemEvent e) 
	{
		if (e.getSource() == strokeWidthBox)
		{
			String selected = strokeWidthBox.getSelectedItem().toString();
			int selectedSizeInt = Integer.parseInt(selected);
			float selectedSizeFlt = (float) selectedSizeInt;
			paintPanel.setStrokeWidth(selectedSizeFlt);
			//			JOptionPane.showMessageDialog(this, selected);
		} // end if, stroke state change
		else if(e.getSource() == objectBorderThicknessBox)
		{
			String selected = objectBorderThicknessBox.getSelectedItem().toString();
			int selectedSizeInt = Integer.parseInt(selected);
			float selectedSizeFlt = (float) selectedSizeInt;
			paintPanel.setObjectBorderThickness(selectedSizeFlt);
		}
		else if(e.getSource() == textSizeBox)
		{
			String selected = textSizeBox.getSelectedItem().toString();
			paintPanel.fontSize = Integer.parseInt(selected);
		}
		else if(e.getSource() == textBoldCheckBox)
		{
			if(textBoldCheckBox.isSelected())
				paintPanel.fontStyle = paintPanel.fontStyle | Font.BOLD;
			else if(!textBoldCheckBox.isSelected())
			{
				System.out.println("Bold not selected" );
				paintPanel.fontStyle = paintPanel.fontStyle & ~Font.BOLD;
			}

		}
		else if(e.getSource() == textItalicsCheckBox)
		{
			if(textItalicsCheckBox.isSelected())
				paintPanel.fontStyle = paintPanel.fontStyle | Font.ITALIC;
			else
				paintPanel.fontStyle = paintPanel.fontStyle & ~Font.ITALIC;
		}

		else if (e.getSource() == textUnderLineCheckBox)
		{
			// if underline check box is selected, text created have underline 
			if (textUnderLineCheckBox.isSelected())	paintPanel.textShouldBeUnderlined = true;
			else	paintPanel.textShouldBeUnderlined = false;
		}

	} // end method itemStateChanged

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) 
	{
		askExitWithoutSaving();
	}

	@Override
	public void windowClosed(WindowEvent e) 
	{
		// TODO Auto-generated method stub
	} // end method windowClosed

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

} // end class a2Frame

class PaintPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; // keep compiler happy

	// default properties
	final public static Color DEFAULT_BACKGROUND_COLOR = Color.white;
	final public static Color DEFAULT_TEXT_COLOR = Color.RED;

	private ArrayList<ExtendedLine2DDouble> allStrokes;
	private ArrayList<ExtendedRectangle2DDouble> allRectangles;

	// text properties
	private Color textColor;

	// stroke properties
	private Color strokeColor;
	private float strokeWidth;

	// fields for objects
	private Color objectBorderColor;
	private float objectBorderThickness;
	private Color objectFillColor;
	int fillOrDraw = 0;

	// fields for text
	int fontStyle;
	int fontSize = 14;
	boolean textShouldBeUnderlined;
	int DEFAULT_STYLE = Font.PLAIN;

	PaintPanel()
	{
		// initialize fields
		allStrokes = new ArrayList<>();
		allRectangles = new ArrayList<>();
		fontStyle = DEFAULT_STYLE;
		textShouldBeUnderlined = false;

		// init default stokre properties
		strokeColor = Color.black;
		strokeWidth = 1.0f;

		// init default properties for objects
		objectBorderColor = Color.black;
		objectBorderThickness = 1.0f;

		// test
		setBackground(Color.ORANGE); // because I love orange!!!!!!
		setPreferredSize(new Dimension(1200, 600));


	} // end constructor class PaintPanel

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		drawAllStrokes(g);
		drawAllRectangles(g);
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
	
	public void drawAllRectangles(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		
		for(ExtendedRectangle2DDouble rectangle : allRectangles)
		{
			g2D.setColor(rectangle.bordercolor);
			g2D.draw(rectangle);
		}
	}



	public void drawRectangle(double x1,double x2,double y1,double y2)
	{

		Graphics2D g2 = (Graphics2D) this.getGraphics();

		BasicStroke stroke = new BasicStroke(this.objectBorderThickness);
		g2.setStroke(stroke);
		//default color black
		g2.setColor(objectBorderColor);
		g2.setPaint(objectFillColor);

		double width;
		double height;
		double startX;
		double startY;

		width = Math.abs(x2-x1);
		height = Math.abs(y2-y1);
		ExtendedRectangle2DDouble r;

		if(x2>x1 && y2<y1)
		{
			startX = x1;
			startY = y1-height;
			r = new ExtendedRectangle2DDouble(startX,startY,width,height);
		}
		else if(x1>x2 && y1<y2)
		{
			startX = x1-width;
			startY = y1;
			r = new ExtendedRectangle2DDouble(startX, startY, width, height);
		}
		else if(x1>x2 && y1>y2)
		{
			startX = x1-width;
			startY = y1-height;
			r = new ExtendedRectangle2DDouble(startX, startY, width, height);

		}
		else
		{
			r = new ExtendedRectangle2DDouble(x1, y1, width, height);

		}

		if( fillOrDraw== 0)
		{
			g2.setColor(objectBorderColor);
			g2.draw(r);
			allRectangles.add(r);
		}

		else
		{
			g2.setColor(objectFillColor);
			g2.fill(r);
			allRectangles.add(r);
			g2.setColor(objectBorderColor);
			g2.draw(r);
			allRectangles.add(r);

		}
		
		

	}

	public void drawOval(double x1,double x2,double y1, double y2)
	{
		Graphics2D g2 = (Graphics2D) this.getGraphics();

		BasicStroke stroke = new BasicStroke(this.objectBorderThickness);
		g2.setStroke(stroke);

		//default color black
		//g2.setColor(objectBorderColor);
		//g2.setPaint(objectFillColor);

		double width;
		double height;
		double startX;
		double startY;

		width = Math.abs(x2-x1);
		height = Math.abs(y2-y1);
		Ellipse2D.Double r;

		if(x2>x1 && y2<y1)
		{
			startX = x1;
			startY = y1-height;
			r = new Ellipse2D.Double(startX,startY,width,height);

		}
		else if(x1>x2 && y1<y2)
		{
			startX = x1-width;
			startY = y1;
			r = new Ellipse2D.Double(startX, startY, width, height);
		}
		else if(x1>x2 && y1>y2)
		{
			startX = x1-width;
			startY = y1-height;
			r = new Ellipse2D.Double(startX, startY, width, height);
		}
		else
		{
			r = new Ellipse2D.Double(x1, y1, width, height);
		}


		if( fillOrDraw== 0)
		{
			g2.setColor(objectBorderColor);
			g2.draw(r);
		}

		else
		{
			g2.setColor(objectFillColor);
			g2.fill(r);
			g2.setColor(objectBorderColor);
			g2.draw(r);
		}
	}

	public void drawCircle(double x1, double x2, double y1, double y2)
	{
		Graphics2D g2 = (Graphics2D)this.getGraphics();

		BasicStroke stroke = new BasicStroke(this.objectBorderThickness);
		g2.setStroke(stroke);

		//default color black
		g2.setColor(objectBorderColor);
		g2.setPaint(objectFillColor);

		double width;
		double height;
		double startX;
		double startY;

		width = Math.abs(x2-x1);
		height = Math.abs(y2-y1);

		Ellipse2D.Double r;

		if(x2>x1 && y2<y1)
		{
			startX = x1;
			startY = y1-height;
			r = new Ellipse2D.Double(startX,startY,width,width);

		}
		else if(x1>x2 && y1<y2)
		{
			startX = x1-width;
			startY = y1;
			r = new Ellipse2D.Double(startX, startY, width, width);
		}
		else if(x1>x2 && y1>y2)
		{
			startX = x1-width;
			startY = y1-height;
			r = new Ellipse2D.Double(startX, startY, width, width);
		}
		else
		{
			r = new Ellipse2D.Double(x1, y1, width, width);
		}

		if( fillOrDraw== 0)
		{
			g2.setColor(objectBorderColor);
			g2.draw(r);
		}

		else
		{
			g2.setColor(objectFillColor);
			g2.fill(r);
			g2.setColor(objectBorderColor);
			g2.draw(r);
		}

	}

	public void drawLine(double x1, double x2, double y1, double y2)
	{
		Graphics2D g2 = (Graphics2D)this.getGraphics();

		BasicStroke stroke = new BasicStroke(this.objectBorderThickness);
		g2.setStroke(stroke);

		//default color black
		g2.setColor(objectBorderColor);

		Line2D.Double r = new Line2D.Double(x1, y1, x2, y2);
		g2.draw(r);
	}

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

	public void drawText(String text, String font, int x, int y)
	{

		Graphics g = this.getGraphics();

		// test
		//		FontMetrics fm = g.getFontMetrics();

		// set Font
		System.out.println("fontStyle: "+fontStyle);
		Font f = new Font(font, fontStyle, fontSize);
		if (textShouldBeUnderlined)
		{
			Map attritubes = f.getAttributes();
			attritubes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
			g.setFont(f.deriveFont(attritubes));
		}	// end if, font should be underlined
		else	g.setFont(f);

		// set text color, use default if no color selected
		if (textColor == null)	g.setColor(PaintPanel.DEFAULT_TEXT_COLOR);
		else	g.setColor(textColor);

		g.drawString(text, x, y);
	} // end method drawText

	public void setStrokeColor(Color color)
	{
		strokeColor = color;
	} // end method setStrokeColor

	public void setObjectBorderColor(Color color)
	{
		objectBorderColor = color;
	}//end method setStrokeColor

	public void setObjectFillColor(Color color)
	{
		objectFillColor = color;

	}//end method setObjectFillColor

	public void setStrokeWidth(float width)
	{
		strokeWidth = width;
	} // end method  setStrokeWidth

	public void setObjectBorderThickness(float width)
	{
		objectBorderThickness = width;
	}

	public void setTextColor(Color color)
	{
		this.textColor = color;
	}  // end method, setTextColor

	public void erase(int x1, int x2, int y1, int y2)
	{

		// eraser size
		int eraserSize = 80;

		// test
		ExtendedLine2DDouble line = null;
		for (int i = 0; i < allStrokes.size(); i++)
		{
			// fetch the nth elemetn
			line = allStrokes.get(i);

			// fetch line properties
			Double lineX1 = line.x1;
			Double lineX2 = line.x2;
			Double lineY1 = line.y1;
			Double lineY2 = line.y2;

			// determine  if current point falls within eraser area
			boolean samePoint = true;
			if ( !( x1 - lineX1 < eraserSize && x1 - lineX1 > eraserSize*-1) )	samePoint = false;
			if ( !( x2 - lineX2 < eraserSize && x2 - lineX2 > eraserSize*-1) )	samePoint = false;
			if ( !( y1 - lineY1 < eraserSize && y1 - lineY1  > eraserSize*-1) )	samePoint = false;
			if ( !( y2 - lineY2 < eraserSize && y2 - lineY2 > eraserSize*-1) )	samePoint = false;

			// remove line if conditions are met
			if (samePoint)	allStrokes.remove(line);

		}

		// update display to reflect what is erased
		repaint();

	} // end method erase

	// test
	public void setScale()
	{
		System.out.println("Scale!");
		//		AffineTransform tran = AffineTransform.getScaleInstance(3.0, 3.0);
		Graphics2D g = (Graphics2D) this.getGraphics();
		//		g.setTransform(tran);
		g.scale(5.0, 5.0);

	} // end method setScale

	public void clearPaintPanel()
	{
		allStrokes.clear();
		repaint();

		setBackground(DEFAULT_BACKGROUND_COLOR);
	} // end method clearPaintPanel

	public void importImage(String path)
	{

		// read image from path
		Image img = null;
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			System.out.println("The selected file is not an image file!");
		}

		// draw image to panel
		Graphics g = this.getGraphics();
		g.drawImage(img, 0, 0, null);

	} // end method importImage

} // end class PaintPanel 
