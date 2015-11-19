import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class ExtendedRectangle2DDouble extends Rectangle2D.Double
{
	 Color fillColor;
	 Color bordercolor;
	 BasicStroke strokeThickness;
	 
	 
	 public ExtendedRectangle2DDouble(double startX,double startY, double width,double height)
	 {
		 super(startX,startY,width,height);
	 }
	 
	 public void setFillColor(Color color)
	 {
		 this.fillColor = color;
	 }
	 
	 public void setBorderColor(Color color)
	 {
		 this.bordercolor = color;
	 }
	 
	 public void setBorderThickness(BasicStroke stroke)
	 {
		 this.strokeThickness = stroke;
	 }
	

}
