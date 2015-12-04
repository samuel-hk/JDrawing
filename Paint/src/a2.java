import java.awt.*;	
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.font.TextAttribute;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class a2 
{

	public static void main(String[] args)
	{
		a2Frame a2 = new a2Frame();
		a2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		System.out.println("Up and running!!");
	} // end main

} // end class a2

class a2Frame extends JFrame implements ActionListener, MouseMotionListener, MouseListener, ItemListener, WindowListener, ChangeListener
{
	// fields for status
	final static int PEN = 0;
	final static int ERASER = 1;
	final static int TEXT = 2;
	final static int IMAGE = 3;
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
	JLabel penStrokeColorLabel;

	// Text Objects Detail Panel related fields
	JPanel textDetailPanel, textStylePanel, textSizePanel, textContentPanel;
	JTextField textInputField;
	JLabel drawTextLabel, textColorLabel;
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
	JLabel shapeBorderColorLabel;
	JLabel shapeFillingColorLabel;

	// import image related fields
	JPanel importImagePanel;
	JButton importImageButton;
	JSlider rotationSlider;
	JSlider zoomSlider;
	JButton importImageToolBarDetailButton;
	JLabel imageImportLabel;
	JButton resetRotationButton;
	JButton resetZoomButton;
	int zoomMax = 200;
	int zoomMin = 1;
	int zoomDefault = 100;
	
	// Undo related fields
	JButton undoButton;

	JComboBox<Integer> strokeWidthBox;
	JComboBox<Integer> objectBorderThicknessBox;

	// coordinates for mouseEvents
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
		int toolBarRow = 6;
		int toolBarCol = 2;
		toolBar.setLayout(new GridLayout(toolBarRow, toolBarCol));

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
		
		// undo button
		undoButton = new JButton("Undo");
		toolBar.add(undoButton);
//		undoButton.setBackground(Color.red);
//		undoButton.setOpaque(true);
//		undoButton.setBorder(null);
		undoButton.addActionListener(this);

		// test
		toolBarDetailPanel.setBackground(Color.red);
		toolBar.setBackground(Color.gray);
		//		toolBar.setPreferredSize(new Dimension(300, 300));
//				toolBar.setMaximumSize(new Dimension(1300, 1300));
		
		// set frame window size
		int windoHeight = 1024;
		int windowWidth = 1400;
		this.setPreferredSize(new Dimension(windowWidth, windoHeight));

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
		case a2Frame.IMAGE:
			setCurrentToolImport();
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
		//textContentPanel.setLayout(new BoxLayout(textContentPanel,BoxLayout.Y_AXIS));
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
		//textStylePanel.setLayout(new BoxLayout(textStylePanel,BoxLayout.Y_AXIS));
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
		//textSizePanel.setLayout(new BoxLayout(textSizePanel,BoxLayout.Y_AXIS));
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

		textColorLabel = new JLabel();
		textColorLabel.setPreferredSize(new Dimension(30,30));
		textColorLabel.setOpaque(true);
		textColorLabel.setBackground(PaintPanel.DEFAULT_TEXT_COLOR);
		textColorPanel.add(textColorLabel);
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
		toolBarDetailPanel.removeAll();
		toolBarDetailPanel.repaint();
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
		else if (e.getSource() == undoButton)
		{
//			System.out.println("Undo");
			paintPanel.undoLastAction();
		}
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
			Color tmp = JColorChooser.showDialog(this, "Choose Color", Color.black);
			paintPanel.setTextColor(tmp);
			textColorLabel.setBackground(tmp);
		}
		else if (e.getSource() == importImageButton)
		{
			setCurrentTool(a2Frame.IMAGE);
		}
		else if (e.getSource() == importImageToolBarDetailButton)
		{
			importImageIntoMemory();
		}
		else if (e.getSource() == resetRotationButton)
		{
			int reset = -1;
			paintPanel.updateImageOnPanel(reset);
		}

	} // end method actionPerformed

	private void setCurrentToolImport()
	{
		currentTool = a2Frame.IMAGE;
		fillToolBarDetailPanelWithImportImage();
		
		// test
		importImageCache = null;
		importImageIntoMemory();
	} // end method setCurrentToolImport

	private void askExitWithoutSaving()
	{
		int n = JOptionPane.showConfirmDialog(mainPanel, "Exit Without Saving?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
		if (n == JOptionPane.YES_OPTION)	System.exit(0);
		else if (n == JOptionPane.NO_OPTION)	saveToFile();
	}  // end method askExitWithoutExiting

	private void fillToolBarDetailPanelWithImportImage()
	{
		// clear toolbardetail panel
		toolBarDetailPanel.removeAll();
		toolBarDetailPanel.repaint();

		// init detail panel
		importImagePanel = new JPanel(new BorderLayout());

		// add import button
		importImageToolBarDetailButton = new JButton("Change Import File");
		importImagePanel.add(importImageToolBarDetailButton, BorderLayout.NORTH);
		importImageToolBarDetailButton.addActionListener(this);

		// add label to show imported image
		imageImportLabel = new JLabel();
		imageImportLabel.setVerticalAlignment(JLabel.CENTER);
		imageImportLabel.setHorizontalAlignment(JLabel.CENTER);
		importImagePanel.add(imageImportLabel, BorderLayout.CENTER);
		
		//panel to hold all image properties
		JPanel imagePropertyPanel = new JPanel();
		imagePropertyPanel.setLayout(new BoxLayout(imagePropertyPanel,BoxLayout.Y_AXIS));
		
		// panel to hold rotation
		JPanel rotationPanel = new JPanel();
		rotationPanel.setBorder(BorderFactory.createTitledBorder("Rotation"));
		
		
		// add roatation slider
		int minROtation = 0;
		int defaultRotation = 7;
		int MaxRotation = 13;
		rotationSlider = new JSlider(JSlider.HORIZONTAL, minROtation, MaxRotation, defaultRotation);
		rotationSlider.addChangeListener(this);
		rotationSlider.setEnabled(false);
		rotationPanel.add(rotationSlider);
		
		// add reset rotation
		resetRotationButton = new JButton("Reset");
		resetRotationButton.addActionListener(this);
		rotationPanel.add(resetRotationButton);
		
		// test
		rotationSlider.addMouseListener(this);
		rotationSlider.addMouseMotionListener(this);
		
		
		// panel to hold zoom in/out
		JPanel zoomPanel = new JPanel();
		zoomPanel.setBorder(BorderFactory.createTitledBorder("Zoom"));
		
		//add zoom slider
		zoomSlider = new JSlider(JSlider.HORIZONTAL,zoomMin, zoomMax, zoomDefault);
		zoomSlider.addChangeListener(this);
		zoomSlider.setEnabled(false);
		
		// test
		zoomSlider.addMouseListener(this);
		
		//add reset zoom
		resetZoomButton = new JButton("Reset");
		resetZoomButton.addActionListener(this);
		
		zoomPanel.add(zoomSlider);
		zoomPanel.add(resetZoomButton);
		
		
		imagePropertyPanel.add(rotationPanel);
		imagePropertyPanel.add(zoomPanel);
		importImagePanel.add(imagePropertyPanel, BorderLayout.SOUTH);
		
		
		

		// put import panel to detail panel
		toolBarDetailPanel.add(importImagePanel);
		toolBarDetailPanel.revalidate();
	} // end method fillToolBarDetailPanelWithImportImage

	
	private BufferedImage importImageCache;
	private void importImageIntoMemory()
	{

		// present open file dialog and receive input
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter ff = new FileNameExtensionFilter("Image FIle", "png", "jpg", "jpeg", "gif");
		fc.setFileFilter(ff);
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

		// read image from path
		Image img = null;
		try {
//			img = ImageIO.read(new File("/eecs/home/cse13185/zzz.png"));
			img = ImageIO.read(new File(path));
		} catch (Exception e) {
			System.out.println("The selected file is not an image file!");
			return;
		}
		BufferedImage image = (BufferedImage) img;
		importImageCache = image;
		
		// scale image
		int width = 200;
		int height = 200;
		try
		{
			img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		} catch (Exception e)
		{
			System.out.println("The selected file is not an image file!");
			return;
		}

		// display image on the toolBarDetailPanel
		ImageIcon icon = new ImageIcon(img);
		imageImportLabel.setIcon(icon);
		
	} // end method importImageIntoMemory

	private void drawImportedImageOntoPanel(int x, int y)
	{
		// if no image imported exit directly
		if (importImageCache == null)	return;
		
		// import image to panel
		paintPanel.drawGivenImageAtLocation(importImageCache, x, y);
		rotationSlider.setEnabled(true);
		zoomSlider.setEnabled(true);
	} // end method importImage

	private void setStrokeColor()
	{
		// fetch user selection
		Color tmp = JColorChooser.showDialog(this, "Choose Color", Color.black);

		// if user did not select a color, stop set color process
		if (tmp == null)	return;

		// set user selection effective
		paintPanel.setStrokeColor(tmp);
		penStrokeColorLabel.setBackground(PaintPanel.strokeColor);

	} // end method setStrokeColor

	private void setBackgroundColor()
	{
		//fetch user selection
		Color tmp = JColorChooser.showDialog(this, "Choose Color", Color.white);

		// if user did not select a color, stop set color process
		if (tmp == null) return;

		paintPanel.saveBackgroundColor(tmp);
		
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
		shapeBorderColorLabel.setBackground(PaintPanel.objectBorderColor);
	}

	private void setObjectFillColor()
	{
		//fetch user selection
		Color tmp = JColorChooser.showDialog(this, "Choose Color", Color.orange);

		//if user did not select a color, stop set color process
		if(tmp == null)	return;

		//set user selection effective
		paintPanel.setObjectFillColor(tmp);
		shapeFillingColorLabel.setBackground(PaintPanel.objectFillColor);

	}

	private void fillToolBarDetailPanelWithPen()
	{
		// clear everything
		toolBarDetailPanel.removeAll();
		toolBarDetailPanel.repaint();


		toolBarDetailPanel.setLayout(new BoxLayout(toolBarDetailPanel,BoxLayout.Y_AXIS));
		toolBarDetailPanel.setBackground(Color.white);
		// add color chooser
		strokeColorPanel = new JPanel();
		strokeColorPanel.setBackground(Color.white);
		//strokeColorPanel.setLayout(new BoxLayout(strokeColorPanel,BoxLayout.Y_AXIS));
		TitledBorder strokeColorTitle;
		strokeColorTitle = BorderFactory.createTitledBorder("Stroke Color");
		strokeColorPanel.setBorder(strokeColorTitle);
		strokeColorButton = new JButton("Choose Color");
		strokeColorButton.addActionListener(this);
		strokeColorPanel.add(strokeColorButton);


		// add color label
		penStrokeColorLabel = new JLabel();
		penStrokeColorLabel.setPreferredSize(new Dimension(50,50));
		penStrokeColorLabel.setOpaque(true);
		penStrokeColorLabel.setBackground(PaintPanel.strokeColor);
		//penStrokeColorLabel.setForeground(Color.red);
		strokeColorPanel.add(penStrokeColorLabel);

		toolBarDetailPanel.add(strokeColorPanel);

		// add stroke width setting
		strokeWeightPanel = new JPanel();
		//strokeWeightPanel.setLayout(new BoxLayout(strokeWeightPanel,BoxLayout.Y_AXIS));
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

		//toolBarDetailPanel.setOpaque(true);
		// panel to hold shape related fields
		JPanel shapeDetailPanel = new JPanel();
		shapeDetailPanel.setLayout(new BoxLayout(shapeDetailPanel,BoxLayout.Y_AXIS));

		shapeChooserPanel = new JPanel();
		//shapeChooserPanel.setLayout(new BoxLayout(shapeChooserPanel,BoxLayout.X_AXIS));

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
		//shapeBorderColorPanel.setOpaque(true);
		//shapeBorderColorPanel.setLayout(new BoxLayout(shapeBorderColorPanel,BoxLayout.Y_AXIS));
		//		shapeBorderColorPanel.setBackground(Color.red);
		objectBorderColorButton = new JButton("Choose Color");
		objectBorderColorButton.addActionListener(this);

		shapeBorderColorLabel = new JLabel();
		shapeBorderColorLabel.setPreferredSize(new Dimension(50,50));
		shapeBorderColorLabel.setOpaque(true);
		shapeBorderColorLabel.setBackground(PaintPanel.objectBorderColor);

		shapeBorderColorPanel.add(objectBorderColorButton);
		shapeBorderColorPanel.add(shapeBorderColorLabel);
		shapeBorderColorPanel.setBorder(BorderFactory.createTitledBorder("Border Color"));
		shapeDetailPanel.add(shapeBorderColorPanel);

		//Border Thickness Panel
		Integer[] size = {1,2,3,4,5,6,7,8,9};
		objectBorderThicknessBox = new JComboBox<>(size);
		objectBorderThicknessBox.addItemListener(this);

		shapeBorderThicknessPanel = new JPanel();
		//shapeBorderThicknessPanel.setLayout(new BoxLayout(shapeBorderThicknessPanel, BoxLayout.Y_AXIS ));

		shapeBorderThicknessPanel.add(objectBorderThicknessBox);
		shapeBorderThicknessPanel.setBorder(BorderFactory.createTitledBorder("Thickness"));
		shapeDetailPanel.add(shapeBorderThicknessPanel);

		//object fill color panel
		shapeFillColorPanel = new JPanel();
		//shapeFillColorPanel.setLayout(new BoxLayout(shapeFillColorPanel,BoxLayout.Y_AXIS));
		shapeFillColorLabel = new JLabel("Filled Object");
		shapeFillColorButton = new JButton("Choose Color");
		shapeFillColorButton.addActionListener(this);
		//shapeNoFillingLabel = new JLabel("Draw Without Filling");
		shapeFillColorPanel.setBorder(BorderFactory.createTitledBorder("Filling Color"));

		shapeFillingColorLabel = new JLabel();
		shapeFillingColorLabel.setPreferredSize(new Dimension(50,50));
		shapeFillingColorLabel.setOpaque(true);
		shapeFillingColorLabel.setBackground(PaintPanel.objectFillColor);
		shapeNoFillingButton = new JButton("Clear Filling");
		shapeNoFillingButton.addActionListener(this);

		shapeFillColorPanel.add(shapeFillColorButton);
		shapeFillColorPanel.add(shapeFillingColorLabel);
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
		// test
		if (e.getSource() == rotationSlider)
		{
			paintPanel.pushRotatedImageToUndoStact();
			return;
		}
		else if (e.getSource() == zoomSlider)
		{
			return;
		}
		
		// TODO Auto-generated method stub
		oldX = e.getX();
		oldY = e.getY();

		//get coordinates when mouse is pressed
		pressX = e.getX();
		pressY = e.getY();

		// writing text on panel
		if (currentTool == a2Frame.TEXT)
		{
			paintPanel.saveText();
			paintPanel.drawText(textInputField.getText(), (String) fontFamily.getSelectedItem(), (int) pressX, (int) pressY);
		}
		
		else if (currentTool == a2Frame.IMAGE)	drawImportedImageOntoPanel(e.getX(), e.getY());
		
		// drawing stroke on panel
		else if (currentTool == a2Frame.PEN)
		{
			paintPanel.startSavingStroke();
		}
		
		// erasing stroke
		else if (currentTool == a2Frame.ERASER)
		{
			paintPanel.startSavingErasedStroke();
		}

	} // end method mousePressed

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		// test
		if (e.getSource() == rotationSlider)
		{
			System.out.println("exiting rotation");
//			paintPanel.pushRotatedImageToUndoStact();
			return;
		}

		//get coordinates when mouse is released
		releaseX = e.getX();
		releaseY = e.getY();

		if (currentTool == Cursor.HAND_CURSOR)
		{
			Object shape = null;
			
			//draw rectangle
			if(rectangleShapeButton.isSelected())
				shape = paintPanel.drawRectangle(pressX, releaseX, pressY, releaseY);
			//draw oval
			else if(ovalShapeButton.isSelected())
				shape = paintPanel.drawOval(pressX, releaseX, pressY, releaseY);
			//draw circle
			else if(circleShapeButton.isSelected())
				shape = paintPanel.drawCircle(pressX, releaseX, pressY, releaseY);
			//draw line
			else if(lineShapeButton.isSelected())
				shape = paintPanel.drawLine(pressX, releaseX, pressY, releaseY);	
			
			paintPanel.saveShape(shape);
			
		}

		 // test
		else if (currentTool == a2Frame.PEN)
		{
			paintPanel.endSavingStroke();
		} 
		
		// end saving onto undo stacks
		else if (currentTool == a2Frame.ERASER)
		{
			paintPanel.endSavingStroke();
		}
		
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
		
//		System.out.println("dragging!");
		
		// do not draw, if mouse left button not clicked
		if (!SwingUtilities.isLeftMouseButton(e))	return;

		// fetch pointer current location
		int x2 = e.getX();
		int y2 = e.getY();


		// perform draw/erase on the current spot
		if (currentTool == a2Frame.PEN)
		{
			
			paintPanel.drawStroke(oldX, x2, oldY, y2);
		}
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

	@Override
	public void stateChanged(ChangeEvent e)
	{
		JSlider source = (JSlider) e.getSource();
		

		if (source == rotationSlider)
		{
//			paintPanel.repaint();
			int value = (int) source.getValue();
			paintPanel.updateImageOnPanel(value);
			//			paintPanel.rotateImage(value);
		}
		else if (source == zoomSlider)
		{
			int value = (int) source.getValue();
			// test
//			double zoomRatio = ( (double) value ) / zoomDefault;
			
			paintPanel.zoomImageOnPanel(value, zoomDefault);
		}

	} // end method stateChanged

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
	private ArrayList<ExtendedEllipse2DDouble> allEllipse;

	// text properties
	public static Color textColor;

	// stroke properties
	public static Color strokeColor;
	private float strokeWidth;

	// fields for objects
	public static Color objectBorderColor;
	private float objectBorderThickness;
	public static Color objectFillColor;
	int fillOrDraw = 0;

	// fields for text
	int fontStyle;
	int fontSize = 14;
	boolean textShouldBeUnderlined;
	int DEFAULT_STYLE = Font.PLAIN;
	
	// undo fields
	Stack<CustomUndo> undoStack;

	PaintPanel()
	{
		// initialize fields
		allStrokes = new ArrayList<>();
		allRectangles = new ArrayList<>();
		allEllipse = new ArrayList<>();
		allImage = new ArrayList<>();
		allTextList = new ArrayList<>();
		fontStyle = DEFAULT_STYLE;
		textShouldBeUnderlined = false;
		undo = new CustomUndo();
		undoStack = new Stack<>();

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
		drawAllRectangles(g);
		drawAllEllipses(g);
		drawAllImages(g);
		drawAllText(g);
		drawAllStrokes(g);
	} // end method paintComponent

	public void drawAllText(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		
		for (TextOnPanel text : allTextList)
		{
			// set Font
			Font f = text.font;
			g2D.setFont(f);

			// set text color
			g2D.setColor(text.textColor);

			// actual draw string
			g2D.drawString(text.text, text.x, text.y);
		}
		
	} // end method drawAllText
	
	public void drawAllImages(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		
		for (ExtendedBufferedImage image : allImage)
		{
			g2D.drawImage(image, image.at, null);
		}
		
	}
	
	public void drawAllEllipses(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;

		for (ExtendedEllipse2DDouble circle : allEllipse)
		{
			if (circle.fillColor != null)
			{
				g2D.setColor(circle.fillColor);
				g2D.fill(circle);
			}

			g2D.setStroke(circle.stroke);
			g2D.setColor(circle.borderColor);
			g2D.draw(circle);
		}

	} // end method drawAllEllipses

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
			g2D.setStroke(rectangle.strokeThickness);
			g2D.setColor(rectangle.bordercolor);
			g2D.draw(rectangle);

			if(rectangle.fillColor != null)
			{
				g2D.setColor(rectangle.fillColor);
				g2D.fill(rectangle);
			}
			g2D.setColor(rectangle.bordercolor);
			g2D.draw(rectangle);
		}
	}



	public ExtendedRectangle2DDouble drawRectangle(double x1,double x2,double y1,double y2)
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
		}

		else
		{
			g2.setColor(objectFillColor);
			g2.fill(r);
			g2.setColor(objectBorderColor);

			r.setBorderColor(objectBorderColor);
			r.setFillColor(objectFillColor);

		}
		allRectangles.add(r);
		r.setBorderThickness(stroke);

		g2.draw(r);
		
		return r;

	}

	public ExtendedEllipse2DDouble drawOval(double x1,double x2,double y1, double y2)
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
		ExtendedEllipse2DDouble r;

		if(x2>x1 && y2<y1)
		{
			startX = x1;
			startY = y1-height;
			r = new ExtendedEllipse2DDouble(startX,startY,width,height);

		}
		else if(x1>x2 && y1<y2)
		{
			startX = x1-width;
			startY = y1;
			r = new ExtendedEllipse2DDouble(startX, startY, width, height);
		}
		else if(x1>x2 && y1>y2)
		{
			startX = x1-width;
			startY = y1-height;
			r = new ExtendedEllipse2DDouble(startX, startY, width, height);
		}
		else
		{
			r = new ExtendedEllipse2DDouble(x1, y1, width, height);
		}


		if( fillOrDraw== 0)
		{
			g2.setColor(objectBorderColor);
		}

		else
		{
			g2.setColor(objectFillColor);
			g2.fill(r);
			g2.setColor(objectBorderColor);

			r.setBorderColor(objectBorderColor);
			r.setFillColor(objectFillColor);
		}
		allEllipse.add(r);
		r.setStroke(stroke);
		g2.draw(r);
		
		return r;
	}

	public ExtendedEllipse2DDouble drawCircle(double x1, double x2, double y1, double y2)
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

		ExtendedEllipse2DDouble r;

		if(x2>x1 && y2<y1)
		{
			startX = x1;
			startY = y1-height;
			r = new ExtendedEllipse2DDouble (startX,startY,width,width);

		}
		else if(x1>x2 && y1<y2)
		{
			startX = x1-width;
			startY = y1;
			r = new ExtendedEllipse2DDouble (startX, startY, width, width);
		}
		else if(x1>x2 && y1>y2)
		{
			startX = x1-width;
			startY = y1-height;
			r = new ExtendedEllipse2DDouble (startX, startY, width, width);
		}
		else
		{
			r = new ExtendedEllipse2DDouble (x1, y1, width, width);
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
			r.setFillColor(objectFillColor);
		}
		r.setStroke(stroke);
		r.setBorderColor(objectBorderColor);
		allEllipse.add(r);
		
		return r;

	}

	public ExtendedLine2DDouble drawLine(double x1, double x2, double y1, double y2)
	{
		Graphics2D g2 = (Graphics2D)this.getGraphics();

		BasicStroke stroke = new BasicStroke(this.objectBorderThickness);
		g2.setStroke(stroke);

		//default color black
		g2.setColor(objectBorderColor);

		ExtendedLine2DDouble r = new ExtendedLine2DDouble((int)x1, (int)y1, (int)x2, (int)y2);
		r.color = objectBorderColor;
		r.stroke = stroke;
		g2.draw(r);

		allStrokes.add(r);
		
		return r;
	}

	CustomUndo undo;
	public void startSavingStroke()
	{
		undo = new CustomUndo();
		undo.lastAction = CustomUndo.STROKE_ACTION;
		undo.lastStroke = new ArrayList<>();
	} // end method saveStroke
	
	public void endSavingStroke()
	{
		undoStack.push(undo);
		undo = new CustomUndo();
	} // end method 
	
	public void saveText()
	{
		undo = new CustomUndo();
		undo.lastAction = CustomUndo.TEXT_ACTION;
		undo.lastText = new ArrayList<>();
	} // end method saveText
	
	public void saveBackgroundColor(Color color)
	{
		undo = new CustomUndo();
		undo.lastAction = CustomUndo.BACKGROUND_ACTION;
		undo.lastBackgroundColor = this.getBackground();
		undoStack.push(undo);
		undo = new CustomUndo();
	} // end method saveBackgroundColor
	
	public void startSavingErasedStroke()
	{
		undo = new CustomUndo();
		undo.lastAction = CustomUndo.ERASER_ACTION;
		undo.lastErase = new ArrayList<>();
	} // end method saveErasedStroke
	
	public void saveShape(Object shape)
	{
		undo = new CustomUndo();
		undo.lastAction = CustomUndo.SHAPE_ACTION;
		undo.lastShape = shape;
		undoStack.push(undo);
		undo = new CustomUndo();
	}
	
	public void undoLastAction()
	{
		// nothing to undo, exit undo
		if (undoStack.isEmpty())	
		{
			JOptionPane.showMessageDialog(null, "Nothing to Undo");
			return;
		}
		
		CustomUndo lastUndo = undoStack.pop();
		
		// last action stroke
		if (lastUndo.lastAction == CustomUndo.STROKE_ACTION)
		{
			System.out.println("Undo1");
			for (ExtendedLine2DDouble stroke : lastUndo.lastStroke)
			{
				allStrokes.remove(stroke);
			}
		} // end if, last action is stroke
		
		// last action eraser
		else if (lastUndo.lastAction == CustomUndo.ERASER_ACTION)
		{
			for (ExtendedLine2DDouble stroke : lastUndo.lastErase)
			{
				allStrokes.add(stroke);
			}
		} // end if, last action eraser
		
		// last action text
		else if (lastUndo.lastAction == CustomUndo.TEXT_ACTION)
		{
			for (TextOnPanel text : lastUndo.lastText)
			{
				allTextList.remove(text);
			}
			
		} // end if, last action text
		
		// last action shape
		else if (lastUndo.lastAction == CustomUndo.SHAPE_ACTION)
		{
			Object lastShape = lastUndo.lastShape;
			allEllipse.remove(lastShape);
			allRectangles.remove(lastShape);
			allStrokes.remove(lastShape);
		}
		
		// last action background color
		else if (lastUndo.lastAction == CustomUndo.BACKGROUND_ACTION)
		{
			setBackground(lastUndo.lastBackgroundColor);
		}
		
		else if (lastUndo.lastAction == CustomUndo.IMAGE_ACTION)
		{
			allImage.remove(lastUndo.lastImage);
		}
		
		else if (lastUndo.lastAction == CustomUndo.IMAGE_ROTATION_ACTION)
		{
			allImage.remove(allImage.size() - 1);
			System.out.println("adding before roatae image back");
			allImage.remove(lastImage);
			allImage.add(lastUndo.lastImage);
			lastImage = lastUndo.lastImage;
		}
		
		repaint();
	} // end method undoLastAction
	
	public void drawStroke(int x1, int x2, int y1, int y2)
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
		
		// save stroke for undo
		undo.lastStroke.add(inkSegment);
	} // end method drawStroke
	
	private ArrayList<TextOnPanel> allTextList;
	public void drawText(String text, String font, int x, int y)
	{
		// get grpahics to draw
		Graphics g = this.getGraphics();

		// set Font
		Font f = new Font(font, fontStyle, fontSize);
		Font finalFont = null;
		if (textShouldBeUnderlined)
		{
			Map attritubes = f.getAttributes();
			attritubes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
			finalFont = f.deriveFont(attritubes);
			g.setFont(finalFont);
		}	// end if, font should be underlined
		else	finalFont = f;
		g.setFont(finalFont);

		// set text color, use default if no color selected
		if (textColor == null)	textColor = PaintPanel.DEFAULT_TEXT_COLOR;
		g.setColor(textColor);

		// actual draw string
		g.drawString(text, x, y);
		
		// save attritubes for repaint and undo
		TextOnPanel saveText = new TextOnPanel(text, finalFont, textColor, x, y);
		allTextList.add(saveText);
		undo.lastText.add(saveText);
		
		undoStack.push(undo);
		undo = new CustomUndo();
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
			// fetch the nth element
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
			
			// save line for undo
			undo.lastErase.add(line);

		}

		// update display to reflect what is erased
		repaint();

	} // end method erase

	// test
	public void setScale()
	{
//		System.out.println("Scale!");
		//		AffineTransform tran = AffineTransform.getScaleInstance(3.0, 3.0);
		Graphics2D g = (Graphics2D) this.getGraphics();
		//		g.setTransform(tran);
		g.scale(5.0, 5.0);

	} // end method setScale

	public void clearPaintPanel()
	{
		allStrokes.clear();
		allEllipse.clear();
		allImage.clear();
		allRectangles.clear();
		allTextList.clear();
		repaint();

		setBackground(DEFAULT_BACKGROUND_COLOR);
	} // end method clearPaintPanel

	/*
	 * Convert an Image object into a BufferedImage object and return the BuffferedImage object
	 */
	private BufferedImage imageToBufImage(Image img)
	{
		// declare the var to return
		BufferedImage bufImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
	    // Draw the image on to the buffered image
	    Graphics2D bGr = bufImage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // return BufferedImage
		return bufImage;
	} // end method imageToBufImage
	
	public void zoomImageOnPanel(int zoom, int zoomDefault)
	{
		// clear old images
		repaint();
		
		// retrieve properties
		ExtendedBufferedImage image = lastImage;
		int oldImageHeight = image.originalImageHeight;
		int oldImageWidth = image.originalImageWIdth;
		
		// compute new size
		double zoomRatio = ( (double) zoom ) / zoomDefault;
		int newImgHeight = (int) (oldImageHeight * zoomRatio);
		int newImgWidth = (int) (oldImageWidth * zoomRatio);
		
		// scale the image using getScaledInstance
		Image tmpImg = image.getScaledInstance(newImgWidth, newImgHeight, Image.SCALE_SMOOTH);
		BufferedImage newImage = imageToBufImage(tmpImg);
		
		// create new ExtendedBufferedImage object to represent the scaled image
		AffineTransform atNew = new AffineTransform(lastImage.at);
		ExtendedBufferedImage newImg = new ExtendedBufferedImage(newImage, atNew);
		newImg.originalImageHeight = oldImageHeight;
		newImg.originalImageWIdth = oldImageWidth;
		lastImage = newImg;
		
		// save images for repaint
		allImage.remove(image);
		allImage.add(newImg);
		
		// save old image for undo
		pushImageToUndoStack(newImg);
	} // end method zoomImageOnPanelb
	
	public void updateImageOnPanel(int rotation)
	{
		// clear old images
		repaint();
		
		// retrieve properties
		BufferedImage image = lastImage;
		int x = lastImageX;
		int y = lastImageY;
		
		// rotate transform
		if (rotation == -1)	rotateImage(image, false, rotation, x, y);
		else	rotateImage(image, true, rotation, x, y);

		// draw image to panel
		Graphics g = this.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(image, at, null);
		
		// test
//		ExtendedBufferedImage oldImage = undoStack.peek().lastImage;
//		allImage.remove(oldImage);
//		ExtendedBufferedImage newImage = new ExtendedBufferedImage(image, at);		
//		allImage.add(newImage);
		
		allImage.remove(lastImage);
		lastImage = new ExtendedBufferedImage(image, at);
		lastImage.originalImageHeight = lastImage.getHeight();
		lastImage.originalImageWIdth = lastImage.getWidth(); 
		allImage.add(lastImage);
		
	} // end method updateImageOnPanel
	
	private int lastImageX, lastImageY;
	private ExtendedBufferedImage lastImage;
	private AffineTransform at;
	private ArrayList<ExtendedBufferedImage> allImage;
	public void drawGivenImageAtLocation(BufferedImage image, int x, int y)
	{

		// try to transform
		rotateImage(image, false, 100, x, y);

		// draw image to panel
		Graphics g = this.getGraphics();
		//		g.drawImage(img, 0, 0, null);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(image, at, null);

		// save properties for transform
		lastImageX = x;
		lastImageY = y;
		lastImage = new ExtendedBufferedImage(image, at);
		lastImage.originalImageHeight = lastImage.getHeight();
		lastImage.originalImageWIdth = lastImage.getWidth();
		allImage.add(lastImage);
		
		// push to undo stack for "undo button"
		pushImageToUndoStack(lastImage);
	} // end method importImage

	public void pushImageToUndoStack(ExtendedBufferedImage image)
	{
		undo = new CustomUndo();
		undo.lastAction = CustomUndo.IMAGE_ACTION;
		undo.lastImage = image;
		undoStack.push(undo);
		undo = new CustomUndo();
	} // end method saveImageToUndoStack
	
	public void pushRotatedImageToUndoStact()
	{
		ExtendedBufferedImage image = allImage.get(allImage.size() - 1);
		undo = new CustomUndo();
		undo.lastAction = CustomUndo.IMAGE_ROTATION_ACTION;
		undo.lastImage = image;
		undoStack.push(undo);
		undo = new CustomUndo();
	} // end method pushRotatedImageToUndoStact
	
//	public void prepareImageRotationUndo()
//	{
//		
//	} // end method
	
	public void rotateImage(BufferedImage image, boolean rotate, double degree, int x, int y)
	{
		at = new AffineTransform();
		
		// do not rate if rotate false
		at.translate(x, y);
		if (!rotate)	return;
		
		at.rotate(degree);
		
	} // end method rotateImage
	
} // end class PaintPanel 
