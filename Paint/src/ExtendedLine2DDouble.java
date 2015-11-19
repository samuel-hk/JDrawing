import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Line2D;

public class ExtendedLine2DDouble extends Line2D.Double 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Color color;
	public BasicStroke stroke;

	public ExtendedLine2DDouble(int x1, int y1, int x2, int y2) 
	{
		super (x1, y1, x2, y2);
	} // end constructor
	
} // end class ExtendedLine2DDouble
