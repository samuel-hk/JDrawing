import java.awt.Color;
import java.awt.Font;

public class TextOnPanel 
{
	public String text;
	public Font font;
	public Color textColor;
	public int x;
	public int y;
	
	public TextOnPanel(String text, Font font, Color textColor, int x, int y)
	{
		this.text = text;
		this.textColor = textColor;
		this.font = font;
		this.x = x;
		this.y = y;
	} // end constructor TextOnPanel
	
} // end class TextOnPanel