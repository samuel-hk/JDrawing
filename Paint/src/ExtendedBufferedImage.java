import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ExtendedBufferedImage extends BufferedImage
{
	
	public AffineTransform at;
	
	public ExtendedBufferedImage(BufferedImage image, AffineTransform tran)
	{
		super(image.getWidth(), image.getHeight(), image.getType());
		at = tran;
		
		
		// save image
		Graphics2D g = createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		
	} // end constructor
	
}
