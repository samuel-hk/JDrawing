import java.awt.Color;
import java.awt.geom.Ellipse2D;

public class ExtendedEllipse2DDouble extends Ellipse2D.Double
{
	Color fillColor;
	Color borderColor;
	
	public ExtendedEllipse2DDouble (double startX, double startY, double width, double height)
	{
		super(startX, startY, width, height);
	}
	
	public void setFillColor(Color color)
	{
		this.fillColor = color;
	}  // end method setFillColor
	
	public void setBorderColor(Color color)
	{
		this.borderColor= color;
	} // end method setFillColor(Color color)
	
} // end class ExtendedEllipse2DDouble 
