package Raster2d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public abstract class Raster2d extends JFrame {

	//VARIABLES
	public Color clearColor = Color.MAGENTA;
	public Color pixelColor = Color.CYAN;
	private BufferedImage image;
	public ImageHandler imghand = new ImageHandler();
	//Coord of the origin pixel
	private int centrei, centrej;
	
	//Methods
	
	/**
	 * Raster2d
	 * @param width
	 * @param height
	 */
	public Raster2d(int width, int height) {
		super.setTitle("Raster2d v1.0, EL PRIMERO");	//SETS THE WINDOW'S TITLE
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);	//SETS TO EXIT ON CLOSE (CLOSE BTTN OR ALT+F4)
		init (width, height);
		this.centrei = this.image.getWidth() /2;
		this.centrej = this.image.getHeight() /2;
		super.setLocationRelativeTo(null); 				//CENTERS THE WINDOW
		super.setResizable(true);						//ALLOWS THE WINDOW TO BE RESIZABLE
		init();											//EXECUTES INIT FROM A SUBCLASS (DYNAMIC LINK)
														//EXECUTE THE ANIMATION!	**PENDING
			//WHEN THE WINDOW GETS RESIZED CLEAR IT, GET NEW WIDTH AND HEIGHT, THEN RE-PAINT IT
		super.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				init(getWidth(), getHeight()); 			//RESIZES THE WINDOW
				clear();								//CLEARS THE WINDOW
				display();								//DISPLAYS THE WINDOW
				repaint();								//RE-PAINTS THE WINDOW
			}
		});
		super.setSize(width, height);
		super.setVisible(true);	//SHOWS THE WINDOW
	}
	
	/**
	 * void init
	 * Creates the image on memory, in which the pixels will be drawn
	 * @param width
	 * @param height
	 */
	public void init(int width, int height) {
		if(width < 120)	width = 120;
		if(height < 10) height = 10;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}
	
	/**
	 * Clears the window with the background color
	 * void clear()
	 */
	public void clear() {
		Graphics g = this.image.getGraphics();
		g.setColor(this.clearColor);
		g.fillRect(0, 0, this.image.getWidth(), this.image.getHeight());
		g.setColor(Color.RED);
		g.drawLine(0, centrej, this.image.getWidth(), centrej);
		g.setColor(Color.RED);
		g.drawLine(centrei, 0, centrei, this.image.getHeight());
	}
	
	/**
	 * Sets the pixel's color to use
	 * void setPixelColor
	 * @param c
	 */
	public void setPixelColor(Color c) {
		this.pixelColor = c;
	}
	
	/**
	 * Returns the pixel's color that is being used
	 * Color getPixelColor
	 * @return Color
	 */
	public Color getPixelColor() {
		return this.pixelColor;
	}
	
	/**
	 * Sets the background clear color
	 * void setClearColor
	 * @param c
	 */
	public void setClearColor(Color c) {
		this.clearColor = c;
	}
	
	/**
	 * Sets a pixel in position i,j with the pixelColor
	 * void setPixel
	 * @param i
	 * @param j
	 */
	public void setPixel(int i, int j) {
		if (i < 0 || i >= this.image.getWidth() || j < 0 || j >= this.image.getHeight())
			return;
		this.image.setRGB(i, j, this.pixelColor.getRGB());
	}
	
	/**
	 * Sets a pixel in position i,j with the specified color
	 * void setPixel
	 * @param i
	 * @param j
	 * @param c
	 */
	public void setPixel(int i, int j, Color c) {
		if (i < 0 || i >= this.image.getWidth() || j < 0 || j >= this.image.getHeight())
			return;
		this.image.setRGB(i, j, c.getRGB());
	}
	
	/**
	 * Returns the Color of the pixel in position i,j
	 * Color getPixel
	 * @param i
	 * @param j
	 * @return Color
	 */
	public Color getPixel(int i, int j) {
		if (i < 0 || i >= this.image.getWidth() || j < 0 || j >= this.image.getHeight())
			return null;
		return new Color(this.image.getRGB(i, j));
	}
	
	/**
	 * Returns a random Color
	 * @return
	 */
	public static Color randomColor() {
		int red = (int) (255 * Math.random());
		int green = (int) (255 * Math.random());
		int blue = (int) (255 * Math.random());
		return new Color(red, green, blue);
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(this.image, 0, 0, this);
	}
	
	/**
	 * Method to be implemented by the programmer using Raster2d library
	 */
	public abstract void init();
	

	/**
	 * Method to be implemented by the programmer using Raster2d library
	 */
	public abstract void display();


	public void saveFile(Color[] table) {
		imghand = new ImageHandler(this.image);
		imghand.saveImage(table);
	}
	
	public void openFile() {
		//String filename = "";
		imghand = new ImageHandler(this.image);
		this.image = imghand.open();
		//super.setSize(this.image.getWidth(), this.image.getHeight());
		repaint();
	}
	
	
	public void open(String filename) {
		
	}

	/**
	 * Sets the centre of the figure to the passed parameters
	 * @param i
	 * @param j
	 */
	public void setCentre(int i, int j) {
		if (i < 0 || i >= this.image.getWidth() || j < 0 || j >= this.image.getHeight())
			return;
		this.centrei = i;
		this.centrej = j;
	}
	
	/**
	 * gets the i pixel in respect to the centre pixel
	 * @param x
	 * @return
	 */
	public int getI(double x) {
		return this.centrei + ((int) ((x>=0)? (x+0.5): (x-0.5)));
	}
	
	/**
	 * gets the j pixel in respect to the centre pixel
	 * @param y
	 * @return
	 */
	public int getJ(double y) {
		return this.centrej - ((int) ((y>=0)? (y+0.5): (y-0.5)));
	}
	
	public void setPoint(double x, double y) {
		this.setPixel(this.getI(x), this.getJ(y));
	}
	
	public void setPoint(double x, double y, Color c) {
		this.setPixel(this.getI(x), this.getJ(y), c);
	}


	/**
	 * Returns m
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public double getmybyx(double x1, double y1, double x2, double y2) {
		return (y2 - y1)/(x2 - x1);
	}
	
	/**
	 * Returns m
	 * @param dx
	 * @param dy
	 * @return
	 */
	public double getmybyx(double dx, double dy) {
		return dy/dx;
	}
	
	/**
	 * MŽtodo de l’nea LE
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void lineLE(double x1, double y1, double x2, double y2) {

		if (!generalRectMethods(x1, y1, x2, y2)) {	
			double m = this.getmybyx(x1, y1, x2, y2);
			double b = y1 - (m*x1);
			if (Math.abs(m) < 1) {
				if (x1 > x2) {
					double tx;	tx = x1;	x1 = x2;	x2 = tx;
					//double ty;	ty = y1;	y1 = y2;	y2 = ty;
				}
				for (double p = x1; p < x2; p++) {
					this.setPoint(p, (m*p) + b);
				}
			}
			else {
				if (y1 > y2) {
					//double tx;	tx = x1;	x1 = x2;	x2 = tx;
					double ty;	ty = y1;	y1 = y2;	y2 = ty;
				}
				for (double p = y1; p < y2; p++) {
					this.setPoint((p-b)/m, p);
				}
			}
		}
	}
	
	/**
	 * MŽtodo de linea DDA
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void lineDDA (double x1, double y1, double x2, double y2) {
		if (!generalRectMethods(x1, y1, x2, y2)) {	
			double m = this.getmybyx(x1, y1, x2, y2);
			if (Math.abs(m) < 1) {
				if (x1 > x2) {
					double tx;	tx = x1;	x1 = x2;	x2 = tx;
					double ty;	ty = y1;	y1 = y2;	y2 = ty;
				}
				double py = y1; 
				for (double p = x1; p < x2; p++) {
					this.setPoint(p, py);
					py = py + m;
				}
			}
			else {
				if (y1 > y2) {
					double tx;	tx = x1;	x1 = x2;	x2 = tx;
					double ty;	ty = y1;	y1 = y2;	y2 = ty;
				}
				double px = x1;
				double mi = 1/m;
				for (double p = y1; p < y2; p++) {
					this.setPoint(px, p);
					px = px + mi;
				}
			}
		}
	}
	
	/**
	 * General draw methods required by everyone!
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public boolean generalRectMethods (double x1, double y1, double x2, double y2) {
		int i1 = this.getI(x1);
		int i2 = this.getI(x2);
		int j1 = this.getI(y1);
		int j2 = this.getI(y2);
		
		if (i1 == i2 && j1 == j2) {
			this.setPixel(i1, j1);	
		}
		else if (i1 == i2) {
			this.drawVerticalRect(j1, j2, i1);
		}
		else if (j1 == j2) {
			this.drawHorizontalRect(i1, i2, j1);
		}
		else if (Math.abs(j2 - j1) == Math.abs(i2 - i1)) {
			this.drawDiagonalRect(i1, j1, i2, j2);
		}
		else
			return false;
		return true;
	}
	
	/**
	 * Draws an horizontal rect
	 * @param i1
	 * @param i2
	 * @param j
	 */
	public void drawHorizontalRect(int i1, int i2, int j) {
		if (i1 > i2) {
			int ti;
			ti = i1;
			i1 = i2;
			i2 = ti;
		}
		
		for (int p = i1; p < i2; p++) {
			this.setPixel(p, j);
		}
	}
	
	/**
	 * draws a vertical rect
	 * @param j1
	 * @param j2
	 * @param i
	 */
	public void drawVerticalRect(int j1, int j2, int i) {
		if (j1 > j2) {
			int tj;
			tj = j1;
			j1 = j2;
			j2 = tj;
		}
		
		for (int p = j1; p < j2; p++) {
			this.setPixel(i, p);
		}
	}
	
	/**
	 * draws a diagonal rect
	 * @param i1
	 * @param j1
	 * @param i2
	 * @param j2
	 */
	public void drawDiagonalRect(int i1, int j1, int i2, int j2) {
		int dj;
		if (i1 > i2) {
			int ti;	ti = i1;	i1 = i2;	i2 = ti;
			int tj;	tj = j1;	j1 = j2;	j2 = tj;
		}
		if (j1 < j2) {
			dj = 1;
		}
		else
			dj = -1;
		
		for (int pi = i1, pj = j1; pi < i2; pi++, pj+=dj) {
			this.setPixel(pi, pj);
		}
	}

}
