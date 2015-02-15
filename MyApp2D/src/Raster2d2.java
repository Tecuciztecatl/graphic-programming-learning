import java.awt.Color;

import javax.swing.JFrame;


public abstract class Raster2d2 extends JFrame {

	public Color clearColor;
	public Color pixelColor;
	public abstract void Raster2d(int width, int height);
	public abstract void init(int width, int height);
	public abstract void clear();
	public abstract void setPixelColor(Color c);
	public abstract Color getPixelColor();
	public abstract void setClearColor(Color c);
	public abstract void setPixel(int i, int j);
	public abstract void setPixel(int i, int j, Color c);
	public abstract Color getPixel(int i, int j);
	public abstract void init();
	public abstract void display();
	
}
