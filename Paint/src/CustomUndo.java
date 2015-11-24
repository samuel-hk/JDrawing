import java.awt.Color;
import java.util.ArrayList;

public class CustomUndo 
{
	public static final int STROKE_ACTION = 0;
	public static final int TEXT_ACTION = 1;
	public static final int ERASER_ACTION = 2;
	public static final int SHAPE_ACTION = 3;
	public static final int BACKGROUND_ACTION = 4;
	public static final int IMAGE_ACTION = 5;
	
	// holding recent action objects
	public ArrayList<ExtendedLine2DDouble> lastStroke;
	public ArrayList<TextOnPanel> lastText;
	public ArrayList<ExtendedLine2DDouble> lastErase;
	public Object lastShape;
	public Color lastBackgroundColor;
	public ExtendedBufferedImage lastImage;
	
	public int lastAction;
	
	public CustomUndo()
	{
		lastAction = -1;
		lastStroke = new ArrayList<>();
		lastText = new ArrayList<>();
		lastErase = new ArrayList<>();
		lastShape = new Object();
	} // end constructor
	
	
} // end class CustomUndo