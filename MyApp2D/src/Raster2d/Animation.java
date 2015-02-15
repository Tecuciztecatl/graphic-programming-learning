package Raster2d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JFrame;

public abstract class Animation extends JFrame{

	//VARIABLES
	public Color clearColor = Color.BLACK;
	public Color pixelColor = Color.CYAN;
	private BufferedImage image;
	private int delay = 10;
	private boolean animated = false;
	private Thread thread;
	private Object sem = new Object();
	public ImageHandler imghand = new ImageHandler();
	//Coord of the origin pixel
	private int centrei, centrej;
	
	private final double MAXDIST = 2.1213;
	
	//Matrix
	public Stack<Matrix> matrixStack;
	public Matrix currMatrix;
	
	//Methods
	
	/**
	 * Raster2d
	 * @param width
	 * @param height
	 */
	public Animation(int width, int height) {
		super.setTitle("Raster2d v1.0, EL PRIMERO");	//SETS THE WINDOW'S TITLE
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);	//SETS TO EXIT ON CLOSE (CLOSE BTTN OR ALT+F4)
		init (width, height);
		this.centrei = this.image.getWidth() /2;
		this.centrej = this.image.getHeight() /2;
		super.setLocationRelativeTo(null); 				//CENTERS THE WINDOW
		super.setResizable(true);						//ALLOWS THE WINDOW TO BE RESIZABLE
		init();											//EXECUTES INIT FROM A SUBCLASS (DYNAMIC LINK)
		createThread();												//EXECUTE THE ANIMATION!	**PENDING
			//WHEN THE WINDOW GETS RESIZED CLEAR IT, GET NEW WIDTH AND HEIGHT, THEN RE-PAINT IT
		//BUSCAR PARA PAUSAR EN LO QUE UNO ESTA CAMBIANDO EL TAMANIO A LA VENTANA!~~~~~~~~
		//Pausa en click!
		super.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			
			@Override
			public synchronized void mouseClicked(MouseEvent arg0) {
				if (animated) {
					System.out.println("STOP");
					stop();
				}
				else {
					System.out.println("START");
					start();
				}
			}
		});
		super.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				init(getWidth(), getHeight()); 			//RESIZES THE WINDOW
				//System.out.println("START");
				centrei = image.getWidth() /2;
				centrej = image.getHeight() /2;
				init();
			//	start();
				sincrepaint();								//CLEARS THE WINDOW
												//DISPLAYS THE WINDOW
												//RE-PAINTS THE WINDOW
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
		if (i < 0 || i > this.image.getWidth() -1 || j < 0 || j > this.image.getHeight() -1)
			return;//throw new IllegalArgumentException("'i', and 'j' must be positive and less than the window's size");
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
		if (i < 0 || i > this.image.getWidth() -1 || j < 0 || j > this.image.getHeight() -1)
			return;//throw new IllegalArgumentException("'i', and 'j' must be positive and less than the window's size");
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
		if (i < 0 || i > this.image.getWidth() || j < 0 || j > this.image.getHeight())
			return null;//throw new IllegalArgumentException("'i', and 'j' must be positive and less than the window's size");
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
	
	/**
	 * Sets the delay in milliseconds to wait for the next frame
	 * @param delay
	 */
	public void setDelay(int delay) {
		if (delay < 10)
			throw new IllegalArgumentException("The delay must be greater or equal to 10");
		this.delay = delay;
	}
	
	public void start() {
		this.animated = true;
		try {
			synchronized (sem) {
				sem.notify();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void stop () {
		this.animated = false;
	}
	
	private void createThread() {
		thread = new Thread() {
			public void run() {
				while(true) {
					if(!animated) {
						try {
							synchronized (sem) {
								sem.wait();
							}
						} catch (Exception e) {
							System.out.println(e);
						}
					}
					sincrepaint();
					try {
						thread.sleep(delay);
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			}
		};
		thread.start();
	}
	
	private synchronized void sincrepaint() {
		clear();
		display();
		repaint();
	}
	

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
		Point2D.Double p = this.getPoint(x, y);
		this.setPixel(this.getI(p.x), this.getJ(p.y));
	}
	
	public void setPoint(double x, double y, Color c) {
		Point2D.Double p = this.getPoint(x, y);
		this.setPixel(this.getI(p.x), this.getJ(p.y), c);
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
		Point2D.Double p1 = this.getPoint(x1, y1);
		Point2D.Double p2 = this.getPoint(x2, y2);
		int i1 = this.getI(p1.x);
		int i2 = this.getI(p2.x);
		int j1 = this.getJ(p1.y);
		int j2 = this.getJ(p2.y);
		if (!generalRectMethods(i1, j1, i2, j2)) {
		//if (!generalRectMethods(x1, y1, x2, y2)) {	
			double m = this.getmybyx(x1, y1, x2, y2);
			double b = y1 - (m*x1);
			if (Math.abs(m) < 1) {
				//With this I invert the biggest to the smallest
				//So I can always process from small to big
				if (x1 > x2) {
					double tx;	tx = x1;	x1 = x2;	x2 = tx;
					//double ty;	ty = y1;	y1 = y2;	y2 = ty;
				}
				for (double p = x1; p < x2; p++) {
					this.setPoint(p, (m*p) + b);
				}
			}
			else {
				//With this I invert the biggest to the smallest
				//So I can always process from small to big
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
	 * MŽtodo de l’nea LE
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void lineLE(double x1, double y1, double x2, double y2, Color c) {
		Point2D.Double p1 = this.getPoint(x1, y1);
		Point2D.Double p2 = this.getPoint(x2, y2);
		int i1 = this.getI(p1.x);
		int i2 = this.getI(p2.x);
		int j1 = this.getJ(p1.y);
		int j2 = this.getJ(p2.y);
		if (!generalRectMethods(i1, j1, i2, j2, c)) {
		//if (!generalRectMethods(x1, y1, x2, y2, c)) {	
			double m = this.getmybyx(x1, y1, x2, y2);
			double b = y1 - (m*x1);
			if (Math.abs(m) < 1) {
				//With this I invert the biggest to the smallest
				//So I can always process from small to big
				if (x1 > x2) {
					double tx;	tx = x1;	x1 = x2;	x2 = tx;
					//double ty;	ty = y1;	y1 = y2;	y2 = ty;
				}
				for (double p = x1; p < x2; p++) {
					this.setPoint(p, (m*p) + b, c);
				}
			}
			else {
				//With this I invert the biggest to the smallest
				//So I can always process from small to big
				if (y1 > y2) {
					//double tx;	tx = x1;	x1 = x2;	x2 = tx;
					double ty;	ty = y1;	y1 = y2;	y2 = ty;
				}
				for (double p = y1; p < y2; p++) {
					this.setPoint((p-b)/m, p, c);
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
		Point2D.Double p1 = this.getPoint(x1, y1);
		Point2D.Double p2 = this.getPoint(x2, y2);
		int i1 = this.getI(p1.x);
		int i2 = this.getI(p2.x);
		int j1 = this.getJ(p1.y);
		int j2 = this.getJ(p2.y);
		if (!generalRectMethods(i1, j1, i2, j2)) {
		
		//if (!generalRectMethods(x1, y1, x2, y2)) {	
			double m = this.getmybyx(i1, j1, i2, j2);
			if (Math.abs(m) < 1) {
				//With this I invert the biggest to the smallest
				//So I can always process from small to big
				if (i1 > i2) {
					int tx;	tx = i1;	i1 = i2;	i2 = tx;
					int ty;	ty = j1;	j1 = j2;	j2 = ty;
				}
				double py = j1; 
				for (int p = i1; p <= i2; p++) {
					//this.setPoint(p, py, c);
					this.setPixel(p, (int)py);
					py = py + m;
				}
			}
			else {
				//With this I invert the biggest to the smallest
				//So I can always process from small to big
				if (j1 > j2) {
					int tx;	tx = i1;	i1 = i2;	i2 = tx;
					int ty;	ty = j1;	j1 = j2;	j2 = ty;
				}
				double px = i1;
				double mi = 1/m;
				for (int p = j1; p <= j2; p++) {
					//this.setPoint(px, p, c);
					this.setPixel((int)px, p);
					px = px + mi;
				}
			}
		}
	}
	
	/**
	 * 
	 * @param x current X
	 * @param y current Y
	 * @param xo original X
	 * @param yo original Y
	 * @param c
	 */
	public void doAliasing(double x, double y, double xo, double yo, Color c) {
		//Get the distance between the points!
		double dist = Math.sqrt(Math.pow((x - xo), 2) + Math.pow((y-yo), 2));
		//If it is bigger than the max, just get out of here!
		if (dist >= MAXDIST) {
			System.out.println("No Aliasing curr X=" + x + ", curr Y=" + y+ ", OX=" + xo+ ", OY=" + yo);
			return;
		}
		else {
			//double perc = dist / MAXDIST;
			//Get the percentage according to MAXDIST
			int perc = (int) ((dist / MAXDIST) * 100);
			
			//Get the background color
			Color back = this.getPixel((int) x, (int) y);
			if (back == null)
				return;
			
			//Get the delta of 100 sections
			double dr = (back.getRed() - c.getRed()) / 100;
			double dg = (back.getGreen() - c.getGreen()) / 100;
			double db = (back.getBlue() - c.getBlue()) / 100;

			//Set the current rgb acording to the percentage and the delta obtained
			int r = (int) (c.getRed() + (dr * perc));
			int g = (int) (c.getGreen() + (dg * perc));
			int b = (int) (c.getBlue() + (db * perc));
			
			/*int r = (int) ((back.getRed() * perc) + (c.getRed() * (1-perc)));
			int g = (int) ((back.getGreen() * perc) + (c.getGreen() * (1-perc)));
			int b = (int) ((back.getBlue() * perc) + (c.getBlue() * (1-perc)));
			*/
			
			//Fail-safe fix
			if (r > 255) r = 255;
			if (g > 255) g = 255;
			if (b > 255) b = 255;
			if (r < 0) r = 0;
			if (g < 0) g = 0;
			if (b < 0) b = 0;
			//Set the pixel!
			this.setPixel((int)x, (int)y, new Color(r, g, b));
		}
	}
	
	/**
	 * MŽtodo de linea DDA
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void lineAA (double x1, double y1, double x2, double y2, Color c) {

		Point2D.Double p1 = this.getPoint(x1, y1);
		Point2D.Double p2 = this.getPoint(x2, y2);
		int i1 = this.getI(p1.x);
		int i2 = this.getI(p2.x);
		int j1 = this.getJ(p1.y);
		int j2 = this.getJ(p2.y);
		
		if (!generalRectMethodsA(i1, j1, i2, j2, c)) {	
			double m = this.getmybyx(i1, j1, i2, j2);
			if (Math.abs(m) < 1) {
				//With this I invert the biggest to the smallest
				//So I can always process from small to big
				if (i1 > i2) {
					int tx;	tx = i1;	i1 = i2;	i2 = tx;
					int ty;	ty = j1;	j1 = j2;	j2 = ty;
				}
				double py = j1; 
				for (int p = i1; p <= i2; p++) {
					int y = (int) py;
					int yt = (int)(py+1);
					int yb = (int)(py-1);
					this.doAliasing(p, y, p, py, c);
					//this.setPixel(p, y, c);
					this.doAliasing(p, yt, p, py, c);
					this.doAliasing(p, yb, p, py, c);
					py = py + m;
				}
			}
			else {
				//With this I invert the biggest to the smallest
				//So I can always process from small to big
				if (j1 > j2) {
					int tx;	tx = i1;	i1 = i2;	i2 = tx;
					int ty;	ty = j1;	j1 = j2;	j2 = ty;
				}
				double px = i1;
				double mi = 1/m;
				for (int p = j1; p <= j2; p++) {
					int x = (int) px;
					int xl = (int) (px-1);
					int xr = (int) (px+1);
					this.doAliasing(x, p, px, p, c);
					//this.setPixel(x, p, c);
					this.doAliasing(xl, p, px, p, c);
					this.doAliasing(xr, p, px, p, c);
					px = px + mi;
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
	public void lineDDA (double x1, double y1, double x2, double y2, Color c) {

		Point2D.Double p1 = this.getPoint(x1, y1);
		Point2D.Double p2 = this.getPoint(x2, y2);
		int i1 = this.getI(p1.x);
		int i2 = this.getI(p2.x);
		int j1 = this.getJ(p1.y);
		int j2 = this.getJ(p2.y);
		if (!generalRectMethods(i1, j1, i2, j2, c)) {	
			double m = this.getmybyx(i1, j1, i2, j2);
			if (Math.abs(m) < 1) {
				//With this I invert the biggest to the smallest
				//So I can always process from small to big
				if (i1 > i2) {
					int tx;	tx = i1;	i1 = i2;	i2 = tx;
					int ty;	ty = j1;	j1 = j2;	j2 = ty;
				}
				double py = j1; 
				for (int p = i1; p <= i2; p++) {
					//this.setPoint(p, py, c);
					this.setPixel(p, (int)py, c);
					py = py + m;
				}
			}
			else {
				//With this I invert the biggest to the smallest
				//So I can always process from small to big
				if (j1 > j2) {
					int tx;	tx = i1;	i1 = i2;	i2 = tx;
					int ty;	ty = j1;	j1 = j2;	j2 = ty;
				}
				double px = i1;
				double mi = 1/m;
				for (int p = j1; p <= j2; p++) {
					//this.setPoint(px, p, c);
					this.setPixel((int)px, p, c);
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
	private boolean generalRectMethods (double x1, double y1, double x2, double y2) {
		Point2D.Double p1 = this.getPoint(x1, y1);
		Point2D.Double p2 = this.getPoint(x2, y2);
		int i1 = this.getI(p1.x);
		int i2 = this.getI(p2.x);
		int j1 = this.getJ(p1.y);
		int j2 = this.getJ(p2.y);
		
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
	 * General draw Aliasing methods required by everyone!
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	private boolean generalRectMethodsA (int i1, int j1, int i2, int j2, Color c) {
		
		if (i1 == i2 && j1 == j2) {
			this.setPixel(i1, j1, c);	
		}
		else if (i1 == i2) {
			this.drawVerticalRectA(j1, j2, i1, c);
		}
		else if (j1 == j2) {
			this.drawHorizontalRectA(i1, i2, j1, c);
		}
		else if (Math.abs(j2 - j1) == Math.abs(i2 - i1)) {
			this.drawDiagonalRectA(i1, j1, i2, j2, c);
		}
		else
			return false;
		return true;
	}
	
	/**
	 * General draw methods required by everyone!
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	private boolean generalRectMethods (int i1, int j1, int i2, int j2, Color c) {
		/*Point2D.Double p1 = this.getPoint(x1, y1);
		Point2D.Double p2 = this.getPoint(x2, y2);
		int i1 = this.getI(p1.x);
		int i2 = this.getI(p2.x);
		int j1 = this.getJ(p1.y);
		int j2 = this.getJ(p2.y);*/
		
		if (i1 == i2 && j1 == j2) {
			this.setPixel(i1, j1, c);	
		}
		else if (i1 == i2) {
			this.drawVerticalRect(j1, j2, i1, c);
		}
		else if (j1 == j2) {
			this.drawHorizontalRect(i1, i2, j1, c);
		}
		else if (Math.abs(j2 - j1) == Math.abs(i2 - i1)) {
			this.drawDiagonalRect(i1, j1, i2, j2, c);
		}
		else
			return false;
		return true;
	}

	
	/**
	 * General draw methods required by everyone!
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	private boolean generalRectMethods2 (double x1, double y1, double x2, double y2) {
		Point2D.Double p1 = this.getPoint(x1, y1);
		Point2D.Double p2 = this.getPoint(x2, y2);
		int i1 = this.getI(p1.x);
		int i2 = this.getI(p2.x);
		int j1 = this.getJ(p1.y);
		int j2 = this.getJ(p2.y);
		
		if (i1 == i2 && j1 == j2) {
			this.setPixel(i1, j1);	
		}
		else if (i1 == i2) {
			this.drawVerticalRect(j1, j2, i1);
		}
		else if (j1 == j2) {
			this.drawHorizontalRect(i1, i2, j1);
		}
		else
			return false;
		return true;
	}
	
	/**
	 * General draw methods required by everyone!
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	private boolean generalRectMethods2 (double x1, double y1, double x2, double y2, Color c) {
		Point2D.Double p1 = this.getPoint(x1, y1);
		Point2D.Double p2 = this.getPoint(x2, y2);
		int i1 = this.getI(p1.x);
		int i2 = this.getI(p2.x);
		int j1 = this.getJ(p1.y);
		int j2 = this.getJ(p2.y);
		
		if (i1 == i2 && j1 == j2) {
			this.setPixel(i1, j1, c);	
		}
		else if (i1 == i2) {
			this.drawVerticalRect(j1, j2, i1, c);
		}
		else if (j1 == j2) {
			this.drawHorizontalRect(i1, i2, j1, c);
		}
		else
			return false;
		return true;
	}
	

	private boolean generalRectMethods3 (double x1, double y1, double x2, double y2, Color c) {
		Point2D.Double p1 = this.getPoint(x1, y1);
		Point2D.Double p2 = this.getPoint(x2, y2);
		int i1 = (int)x1;
		int i2 = (int)x2;
		int j1 = (int)y1;
		int j2 = (int)y2;
		
		if (i1 == i2 && j1 == j2) {
			this.setPixel(i1, j1, c);	
		}
		else if (i1 == i2) {
			this.drawVerticalRect(j1, j2, i1, c);
		}
		else if (j1 == j2) {
			this.drawHorizontalRect(i1, i2, j1, c);
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
	private void drawHorizontalRect(int i1, int i2, int j) {
		if (i1 > i2) {
			int ti;
			ti = i1;
			i1 = i2;
			i2 = ti;
		}
		
		for (int p = i1; p <= i2; p++) {
			this.setPixel(p, j);
		}
	}
	
	/**
	 * Draws an horizontal rect
	 * @param i1
	 * @param i2
	 * @param j
	 */
	private void drawHorizontalRect(double x1, double x2, double y, Color[][] pattern) {
		/*Point2D.Double p1 = this.getPoint(x1, y);
		Point2D.Double p2 = this.getPoint(x2, y);
		int i1 = this.getI(p1.x);
		int i2 = this.getI(p2.x);
		int j = this.getJ(p1.y);
		*///System.out.println("i: " + x1 + ", j: " + y);
		int i1 = (int) x1;
		int i2 = (int) x2;
		int j = (int) y;

		int height = pattern.length;
		//System.out.println(height);		
		int width = pattern[0].length;
		//System.out.println(width);
		
		if (i1 > i2) {
			int ti;
			ti = i1;
			i1 = i2;
			i2 = ti;
		}
		int py = j % height;
		for (int p = i1; p <= i2; p++) {
			int px = p % width;
			//System.out.println("x: " + p + ", y: " + j);
			this.setPixel(p, j, pattern[py][px]);
		}
	}
	
	/**
	 * Draws an horizontal Aliased rect
	 * @param i1
	 * @param i2
	 * @param j
	 */
	private void drawHorizontalRectA(int i1, int i2, int j, Color c) {
		if (i1 > i2) {
			int ti;
			ti = i1;
			i1 = i2;
			i2 = ti;
		}
		
		for (int p = i1; p <= i2; p++) {
			this.setPixel(p, j, c);
			this.doAliasing(p, j+1, p, j, c);
			this.doAliasing(p, j-1, p, j, c);
		}
	}
	
	/**
	 * Draws an horizontal rect
	 * @param i1
	 * @param i2
	 * @param j
	 */
	private void drawHorizontalRect(int i1, int i2, int j, Color c) {
		if (i1 > i2) {
			int ti;
			ti = i1;
			i1 = i2;
			i2 = ti;
		}
		
		for (int p = i1; p <= i2; p++) {
			this.setPixel(p, j, c);
		}
	}
	
	/**
	 * draws a vertical rect
	 * @param j1
	 * @param j2
	 * @param i
	 */
	private void drawVerticalRect(int j1, int j2, int i) {
		if (j1 > j2) {
			int tj;
			tj = j1;
			j1 = j2;
			j2 = tj;
		}
		
		for (int p = j1; p <= j2; p++) {
			this.setPixel(i, p);
		}
	}
	
	/**
	 * draws a vertical Aliased rect
	 * @param j1
	 * @param j2
	 * @param i
	 */
	private void drawVerticalRectA(int j1, int j2, int i, Color c) {
		if (j1 > j2) {
			int tj;
			tj = j1;
			j1 = j2;
			j2 = tj;
		}
		
		for (int p = j1; p <= j2; p++) {
			this.setPixel(i, p, c);
			this.doAliasing(i+1, p, i, p, c);
			this.doAliasing(i-1, p, i, p, c);
		}
	}
	
	/**
	 * draws a vertical rect
	 * @param j1
	 * @param j2
	 * @param i
	 */
	private void drawVerticalRect(int j1, int j2, int i, Color c) {
		if (j1 > j2) {
			int tj;
			tj = j1;
			j1 = j2;
			j2 = tj;
		}
		
		for (int p = j1; p <= j2; p++) {
			this.setPixel(i, p, c);
		}
	}
	
	/**
	 * draws a diagonal rect
	 * @param i1
	 * @param j1
	 * @param i2
	 * @param j2
	 */
	private void drawDiagonalRect(int i1, int j1, int i2, int j2) {
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
		
		for (int pi = i1, pj = j1; pi <= i2; pi++, pj+=dj) {
			this.setPixel(pi, pj);
		}
	}
	
	/**
	 * draws a diagonal Aliased rect
	 * @param i1
	 * @param j1
	 * @param i2
	 * @param j2
	 */
	private void drawDiagonalRectA(int i1, int j1, int i2, int j2, Color c) {
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
		
		for (int pi = i1, pj = j1; pi <= i2; pi++, pj+=dj) {
			this.setPixel(pi, pj, c);
			this.doAliasing(pi+1, pj, pi, pj, c);
			this.doAliasing(pi-1, pj, pi, pj, c);
		}
	}
	
	/**
	 * draws a diagonal rect
	 * @param i1
	 * @param j1
	 * @param i2
	 * @param j2
	 */
	private void drawDiagonalRect(int i1, int j1, int i2, int j2, Color c) {
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
		
		for (int pi = i1, pj = j1; pi <= i2; pi++, pj+=dj) {
			this.setPixel(pi, pj, c);
		}
	}
	
	
	/**
	 * Draws an horizontal rect
	 * @param i1
	 * @param i2
	 * @param j
	 */
	private void drawHorizontalRect(int i1, int i2, int j, Color c1, Color c2) {
		if (i1 > i2) {
			int ti;
			ti = i1;
			i1 = i2;
			i2 = ti;
			Color tc = new Color(c1.getRGB());
			c1 = new Color(c2.getRGB());
			c2 = new Color(tc.getRGB());
		}
		double dif = Math.abs(i2 - i1);
		double dr = (c2.getRed() - c1.getRed()) / dif;
		double dg = (c2.getGreen() - c1.getGreen()) / dif;
		double db = (c2.getBlue() - c1.getBlue()) / dif;
		Color c = new Color(c1.getRGB());
		double cr = c.getRed();
		double cg = c.getGreen();
		double cb = c.getBlue();
		
		for (int p = i1; p <= i2; p++) {
			this.setPixel(p, j, c);
			cr += dr;
			cg += dg;
			cb += db;
			if (cr > 255) cr = 255;
			else if (cr < 0) cr = 0;
			if (cg > 255) cg = 255;
			else if (cg < 0) cg = 0;
			if (cb > 255) cb = 255;
			else if (cb < 0) cb = 0;
			c = new Color((int)(cr), (int)(cg), (int)(cb));
		}
	}
	
	/**
	 * draws a vertical rect
	 * @param j1
	 * @param j2
	 * @param i
	 */
	public void drawVerticalRect(int j1, int j2, int i, Color c1, Color c2) {
		if (j1 > j2) {
			int tj;
			tj = j1;
			j1 = j2;
			j2 = tj;
			Color tc = new Color(c1.getRGB());
			c1 = new Color(c2.getRGB());
			c2 = new Color(tc.getRGB());
		}
		double dif = Math.abs(j2 - j1);
		double dr = (c2.getRed() - c1.getRed()) / dif;
		double dg = (c2.getGreen() - c1.getGreen()) / dif;
		double db = (c2.getBlue() - c1.getBlue()) / dif;
		Color c = new Color(c1.getRGB());
		
		for (int p = j1; p <= j2; p++) {
			this.setPixel(i, p, c);
			int r = (int)(c.getRed()+dr);
			int g = (int)(c.getGreen()+dg);
			int b = (int)(c.getBlue()+db);
			if (r > 255) r = 255;
			else if (r < 0) r = 0;
			if (g > 255) g = 255;
			else if (g < 0) g = 0;
			if (b > 255) b = 255;
			else if (b < 0) b = 0;
			c = new Color(r, g, b);
		}
	}
	
	/**
	 * draws a diagonal rect
	 * @param i1
	 * @param j1
	 * @param i2
	 * @param j2
	 */
	private void drawDiagonalRect(int i1, int j1, int i2, int j2, Color c1, Color c2) {
		int dj;
		if (i1 > i2) {
			int ti;	ti = i1;	i1 = i2;	i2 = ti;
			int tj;	tj = j1;	j1 = j2;	j2 = tj;
			Color tc = new Color(c1.getRGB());
			c1 = new Color(c2.getRGB());
			c2 = new Color(tc.getRGB());
		}
		if (j1 < j2) {
			dj = 1;
		}
		else
			dj = -1;

		double dif = Math.abs(i2 - i1);
		double dr = (c2.getRed() - c1.getRed()) / dif;
		double dg = (c2.getGreen() - c1.getGreen()) / dif;
		double db = (c2.getBlue() - c1.getBlue()) / dif;
		Color c = new Color(c1.getRGB());
		double cr = c.getRed();
		double cg = c.getGreen();
		double cb = c.getBlue();
		
		for (int pi = i1, pj = j1; pi <= i2; pi++, pj+=dj) {
			this.setPixel(pi, pj);
			cr += dr;
			cg += dg;
			cb += db;
			if (cr > 255) cr = 255;
			else if (cr < 0) cr = 0;
			if (cg > 255) cg = 255;
			else if (cg < 0) cg = 0;
			if (cb > 255) cb = 255;
			else if (cb < 0) cb = 0;
			c = new Color((int)(cr), (int)(cg), (int)(cb));
		}
	}
	
	public void colorRect (Point2D p1, Point2D p2, Color c1, Color c2) {
		if (!generalRectMethods(p1, p2, c1, c2)) {	
			double m = this.getmybyx(p1.getX(), p1.getY(), p2.getX(), p2.getY());
			if (Math.abs(m) < 1) {
				//With this I invert the biggest to the smallest
				//So I can always process from small to big
				if (p1.getX() > p2.getX()) {
					Point2D t = new Point2D.Double();	t.setLocation(p1.getX(), p1.getY());
					p1.setLocation(p2.getX(), p2.getY());
					p2.setLocation(t.getX(), t.getY());
					Color tc = new Color(c1.getRGB());
					c1 = new Color(c2.getRGB());
					c2 = new Color(tc.getRGB());
				}
				double py = p1.getY(); 
				double dif = Math.abs(p2.getX() - p1.getX());
				double dr = (c2.getRed() - c1.getRed()) / dif;
				double dg = (c2.getGreen() - c1.getGreen()) / dif;
				double db = (c2.getBlue() - c1.getBlue()) / dif;
				Color c = new Color(c1.getRGB());
				double cr = c.getRed();
				double cg = c.getGreen();
				double cb = c.getBlue();
				
				for (double p = p1.getX(); p < p2.getX(); p++) {
					this.setPoint(p, py, c);
					py = py + m;
					cr += dr;
					cg += dg;
					cb += db;
					if (cr > 255) cr = 255;
					else if (cr < 0) cr = 0;
					if (cg > 255) cg = 255;
					else if (cg < 0) cg = 0;
					if (cb > 255) cb = 255;
					else if (cb < 0) cb = 0;
					c = new Color((int)(cr), (int)(cg), (int)(cb));
				}
			}
			else {
				//With this I invert the biggest to the smallest
				//So I can always process from small to big
				if (p1.getY() > p2.getY()) {
					Point2D t = new Point2D.Double();	t.setLocation(p1.getX(), p1.getY());
					p1.setLocation(p2.getX(), p2.getY());
					p2.setLocation(t.getX(), t.getY());
					Color tc = new Color(c1.getRGB());
					c1 = new Color(c2.getRGB());
					c2 = new Color(tc.getRGB());
				}
				double px = p1.getX();
				double mi = 1/m;
				double dif = Math.abs(p2.getY() - p1.getY());
				double dr = (c2.getRed() - c1.getRed()) / dif;
				double dg = (c2.getGreen() - c1.getGreen()) / dif;
				double db = (c2.getBlue() - c1.getBlue()) / dif;
				Color c = new Color(c1.getRGB());
				double cr = c.getRed();
				double cg = c.getGreen();
				double cb = c.getBlue();
				
				for (double p = p1.getY(); p < p2.getY(); p++) {
					this.setPoint(px, p, c);
					px = px + mi;
					cr += dr;
					cg += dg;
					cb += db;
					if (cr > 255) cr = 255;
					else if (cr < 0) cr = 0;
					if (cg > 255) cg = 255;
					else if (cg < 0) cg = 0;
					if (cb > 255) cb = 255;
					else if (cb < 0) cb = 0;
					c = new Color((int)(cr), (int)(cg), (int)(cb));
				}
			}
		}
	}
	
	public void colorArea(Point2D cornerbl, Point2D cornertr, Color cbl, Color cbr, Color ctl, Color ctr) {
		Point2D.Double p1 = this.getPoint(cornerbl.getX(), cornerbl.getY());
		Point2D.Double p2 = this.getPoint(cornertr.getX(), cornertr.getY());
		int il = this.getI(p1.getX());
		int ir = this.getI(p2.getX());
		int jb = this.getJ(p1.getY());
		int jt = this.getJ(p2.getY());
		
		//double dx = ir - il;
		double dy = Math.abs(jt - jb);
		//Bottom color delta
		double dlr = (ctl.getRed() - cbl.getRed())/dy;
		double dlg = (ctl.getGreen() - cbl.getGreen())/dy;
		double dlb = (ctl.getBlue() - cbl.getBlue())/dy;
		//Top color delta
		double drr = (ctr.getRed() - cbr.getRed())/dy;
		double drg = (ctr.getGreen() - cbr.getGreen())/dy;
		double drb = (ctr.getBlue() - cbr.getBlue())/dy;
		//Current Colors, starting at the left
		Color currl = new Color(cbl.getRGB());
		Color currr = new Color(cbr.getRGB());

		double clr = currl.getRed();
		double clg = currl.getGreen();
		double clb = currl.getBlue();
		double crr = currr.getRed();
		double crg = currr.getGreen();
		double crb = currr.getBlue();
		
		//Draw an horizontal line, then add the delta to the current colors
		for (int j = jb; j > jt; j--) {
			this.drawHorizontalRect(il, ir, j, currl, currr);
			clr += dlr;
			clg += dlg;
			clb += dlb;
			clr = (clr>255)? (255) : (clr < 0)? (clr = 0):(clr);
			clg = (clg>255)? (255) : (clg < 0)? (clg = 0):(clg);
			clb = (clb>255)? (255) : (clb < 0)? (clb = 0):(clb);
			currl = new Color((int)(clr), (int)(clg), (int)(clb));
			crr += drr;
			crg += drg;
			crb += drb;
			crr = (crr>255)? (255) : (crr < 0)? (crr = 0):(crr);
			crg = (crg>255)? (255) : (crg < 0)? (crg = 0):(crg);
			crb = (crb>255)? (255) : (crb < 0)? (crb = 0):(crb);
			currr = new Color((int)(crr), (int)(crg), (int)(crb));
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
	private boolean generalRectMethods (Point2D p1, Point2D p2, Color c1, Color c2) {

		Point2D.Double p3 = this.getPoint(p1.getX(), p1.getY());
		Point2D.Double p4 = this.getPoint(p2.getX(), p2.getY());
		int i1 = this.getI(p3.x);
		int i2 = this.getI(p4.x);
		int j1 = this.getJ(p3.y);
		int j2 = this.getJ(p4.y);
		
		if (i1 == i2 && j1 == j2) {
			this.setPixel(i1, j1);	
		}
		else if (i1 == i2) {
			this.drawVerticalRect(j1, j2, i1, c1, c2);
		}
		else if (j1 == j2) {
			this.drawHorizontalRect(i1, i2, j1, c1, c2);
		}
		else if (Math.abs(j2 - j1) == Math.abs(i2 - i1)) {
			this.drawDiagonalRect(i1, j1, i2, j2, c1, c2);
		}
		else
			return false;
		return true;
	}
	
	/**
	 * Draws an unclosed polygon
	 * @param points
	 */
	public void polyLine(Point2D.Double[] points) {
		if (points.length < 2) 
			return;
		for (int p = 1; p < points.length; p++) {
			this.lineDDA(points[p-1].x, points[p-1].y, points[p].x,points[p].y);
		}
	}
	
	/**
	 * Draws an unclosed polygon
	 * @param points
	 * @param c
	 */
	public void polyLine(Point2D.Double[] points, Color c) {
		if (points.length < 2) 
			return;
		for (int p = 1; p < points.length; p++) {
			this.drawRectBre(points[p-1].x, points[p-1].y, points[p].x,points[p].y, c);
		}
	}
	
	/**
	 * Draws a closed polygon
	 * @param points
	 */
	public void lineLoop(Point2D.Double[] points) {
		if (points.length < 2) 
			return;
		for (int p = 1; p < points.length; p++) {
			this.lineDDA(points[p-1].x, points[p-1].y, points[p].x,points[p].y);
		}
		this.lineDDA(points[0].x,points[0].y, points[points.length-1].x, points[points.length-1].y);
	}
	
	/**
	 * Draws a closed polygon
	 * @param points
	 */
	public void lineLoop(Vector<Point2D.Double> points) {
		if (points.size() < 2) 
			return;
		for (int p = 1; p < points.size(); p++) {
			this.lineDDA(points.get(p-1).x, points.get(p-1).y, points.get(p).x,points.get(p).y);
		}
		this.lineDDA(points.get(0).x, points.get(0).y, 
				points.get(points.size()-1).x, points.get(points.size()-1).y);
	}
	
	/**
	 * Draws a closed poygon
	 * @param points
	 * @param c
	 */
	public void lineLoop(Point2D.Double[] points, Color c) {
		if (points.length < 2) 
			return;
		for (int p = 1; p < points.length; p++) {
			this.lineDDA(points[p-1].x, points[p-1].y, points[p].x,points[p].y, c);
		}
		this.lineDDA(points[0].x,points[0].y, points[points.length-1].x, points[points.length-1].y, c);
	}
	
	/**
	 * Draws a closed poygon
	 * @param points
	 * @param c
	 */
	public void lineLoop(Point2D.Double[] points, Color c, Color back) {
		if (points.length < 2) 
			return;
		
		this.scanLine(points, back);
		
		for (int p = 1; p < points.length; p++) {
			this.lineDDA(points[p-1].x, points[p-1].y, points[p].x,points[p].y, c);
		}
		this.lineDDA(points[0].x,points[0].y, points[points.length-1].x, points[points.length-1].y, c);
	}
	
	/**
	 * Draws a closed poygon
	 * @param points
	 * @param c
	 */
	public void lineLoop(Point2D.Double[] points, Color c, Color[][] back) {
		if (points.length < 2) 
			return;
		
		this.scanLine(points, back);
		
		for (int p = 1; p < points.length; p++) {
			this.lineDDA(points[p-1].x, points[p-1].y, points[p].x,points[p].y, c);
		}
		this.lineDDA(points[0].x,points[0].y, points[points.length-1].x, points[points.length-1].y, c);
	}
	
	/**
	 * Draws a closed polygon
	 * @param points, which is a vector
	 * @param c
	 */
	public void lineLoop(Vector<Point2D.Double> points, Color c) {
		if (points.size() < 2) 
			return;
		for (int p = 1; p < points.size(); p++) {
			this.lineDDA(points.get(p-1).x, points.get(p-1).y, points.get(p).x,points.get(p).y, c);
		}
		this.lineDDA(points.get(0).x, points.get(0).y, 
				points.get(points.size()-1).x, points.get(points.size()-1).y, c);
	}

	/**
	 * Draws a closed polygon
	 * @param points, which is a vector
	 * @param c
	 */
	public void lineLoop(Vector<Point2D.Double> points, Color c, Color back) {
		if (points.size() < 2) 
			return;

		this.scanLine(points, back);
		
		for (int p = 1; p < points.size(); p++) {
			this.lineDDA(points.get(p-1).x, points.get(p-1).y, points.get(p).x,points.get(p).y, c);
		}
		this.lineDDA(points.get(0).x, points.get(0).y, 
				points.get(points.size()-1).x, points.get(points.size()-1).y, c);
	}

	/**
	 * Draws a closed polygon
	 * @param points, which is a vector
	 * @param c
	 */
	public void lineLoop(Vector<Point2D.Double> points, Color c, Color[][] back) {
		if (points.size() < 2) 
			return;

		this.scanLine(points, back);
		
		for (int p = 1; p < points.size(); p++) {
			this.lineDDA(points.get(p-1).x, points.get(p-1).y, points.get(p).x,points.get(p).y, c);
		}
		this.lineDDA(points.get(0).x, points.get(0).y, 
				points.get(points.size()-1).x, points.get(points.size()-1).y, c);
	}
	
	/**
	 * Draws a rectangle
	 * @param width
	 * @param height
	 */
	public void drawRectangle(double width, double height) {
		Point2D.Double[] points = { new Point2D.Double(-(width/2), height/2), new Point2D.Double((width/2), height/2),
				new Point2D.Double( (width/2), (-height/2)), new Point2D.Double( -(width/2), -(height/2))
				};
		this.lineLoop(points);
	}
	
	/**
	 * Draws a rectangle
	 * @param width
	 * @param height
	 * @param c
	 */
	public void drawRectangle(double width, double height, Color c) {
		Point2D.Double[] points = { new Point2D.Double(-(width/2), height/2), new Point2D.Double((width/2), height/2),
				new Point2D.Double( (width/2), (-height/2)), new Point2D.Double( -(width/2), -(height/2))
				};
		this.lineLoop(points, c);
	}
	
	/**
	 * Draws a rectangle
	 * @param width
	 * @param height
	 * @param c
	 */
	public void drawRectangle(double width, double height, Color c, Color back) {
		Point2D.Double[] points = { new Point2D.Double(-(width/2), height/2), new Point2D.Double((width/2), height/2),
				new Point2D.Double( (width/2), (-height/2)), new Point2D.Double( -(width/2), -(height/2))
				};
		this.lineLoop(points, c, back);
	}
	
	/**
	 * Draws a rectangle
	 * @param width
	 * @param height
	 * @param c
	 */
	public void drawRectangle(double width, double height, Color c, Color[][] back) {
		Point2D.Double[] points = { new Point2D.Double(-(width/2), height/2), new Point2D.Double((width/2), height/2),
				new Point2D.Double( (width/2), (-height/2)), new Point2D.Double( -(width/2), -(height/2))
				};
		this.lineLoop(points, c, back);
	}
	
	/**
	 * Draws a polygon by sending its points to polyLine
	 * @param radius
	 * @param nsides
	 */
	public void drawPolygon(double radius, int nsides) {
		//If nsides is less than 3, there is no point in using this method... waste of processor
		if (nsides < 3)
			return;
		//Get the angle delta
		int dangle = 360 / nsides;
		//We create the points for the polygon
		//Each point must be separated by the angle delta
		Vector<Point2D.Double> points = new Vector<Point2D.Double>();
		for (int i = 0, cangle = 0; i < nsides; i ++, cangle+=dangle){ 
				points.add(new Point2D.Double(radius * Math.cos(Math.toRadians(cangle)),
						radius * Math.sin(Math.toRadians(cangle))));
		}
		this.lineLoop(points);
	}
	
	/**
	 * Draws a polygon by sending its points to polyLine
	 * @param radius
	 * @param nsides
	 * @param c
	 */
	public void drawPolygon(double radius, int nsides, Color c) {
		//If nsides is less than 3, there is no point in using this method... waste of processor
		if (nsides < 3)
			return;
		//Get the angle delta
		int dangle = 360 / nsides;
		//We create the points for the polygon
		//Each point must be separated by the angle delta
		Vector<Point2D.Double> points = new Vector<Point2D.Double>();
		for (int i = 0, cangle = 0; i < nsides; i ++, cangle+=dangle){ 
				points.add(new Point2D.Double(radius * Math.cos(Math.toRadians(cangle)),
						radius * Math.sin(Math.toRadians(cangle))));
		}
		this.lineLoop(points, c);
	}
	
	/**
	 * Draws a polygon by sending its points to polyLine
	 * @param radius
	 * @param nsides
	 * @param c
	 */
	public void drawPolygon(double radius, int nsides, Color c, Color back) {
		//If nsides is less than 3, there is no point in using this method... waste of processor
		if (nsides < 3)
			return;
		//Get the angle delta
		int dangle = 360 / nsides;
		//We create the points for the polygon
		//Each point must be separated by the angle delta
		Vector<Point2D.Double> points = new Vector<Point2D.Double>();
		for (int i = 0, cangle = 0; i < nsides; i ++, cangle+=dangle){ 
				points.add(new Point2D.Double(radius * Math.cos(Math.toRadians(cangle)),
						radius * Math.sin(Math.toRadians(cangle))));
		}
		this.lineLoop(points, c, back);
		  //this.borderFill(new Point2D.Double(this.getI(0), this.getJ(0)), c, back);
	}
	
	/**
	 * Draws a polygon by sending its points to polyLine
	 * @param radius
	 * @param nsides
	 * @param c
	 */
	public void drawPolygon(double radius, int nsides, Color c, Color[][] back) {
		//If nsides is less than 3, there is no point in using this method... waste of processor
		if (nsides < 3)
			return;
		//Get the angle delta
		int dangle = 360 / nsides;
		//We create the points for the polygon
		//Each point must be separated by the angle delta
		Vector<Point2D.Double> points = new Vector<Point2D.Double>();
		for (int i = 0, cangle = 0; i < nsides; i ++, cangle+=dangle){ 
				points.add(new Point2D.Double(radius * Math.cos(Math.toRadians(cangle)),
						radius * Math.sin(Math.toRadians(cangle))));
		}
		//this.scanLine(points, back);
		this.lineLoop(points, c, back);
		  //this.borderFill(new Point2D.Double(this.getI(0), this.getJ(0)), c, back);
	}
	
	/**
	 * Draws a circle according to the first trigonometric description
	 * @param r
	 */
	public void drawCircleTrig(double r) {
		double dx = 1/(r/4);
		int xc = 0;
		int yc = 0;
		for (double x = xc-r; x < r; x+=dx) {
			int y1 = (int) (yc + Math.sqrt(Math.pow(r, 2) - Math.pow((x - xc), 2)));
			int y2 = (int) (yc - Math.sqrt(Math.pow(r, 2) - Math.pow((x - xc), 2)));
			this.setPoint(x, y1);
			this.setPoint(x, y2);
		}
	}
	
	/**
	 * Draws a circle according to the first trigonometric description
	 * @param r
	 * @param c
	 */
	public void drawCircleTrig(double r, Color c) {
		double dx = 1/(r/4);
		int xc = 0;
		int yc = 0;
		for (double x = xc-r; x < r; x+=dx) {
			int y1 = (int) (yc + Math.sqrt(Math.pow(r, 2) - Math.pow((x - xc), 2)));
			int y2 = (int) (yc - Math.sqrt(Math.pow(r, 2) - Math.pow((x - xc), 2)));
			this.setPoint(x, y1, c);
			this.setPoint(x, y2, c);
		}
	}
	
	/**
	 * Draws a circle according to the second trigonometric description
	 * @param r
	 */
	public void drawCircleTrig2(double r) {
		double dx = 1/(r/4);
		int xc = 0;
		//int yc = 0;
		double to =  r / Math.sqrt(2);
		for (double x = xc; x < to; x+=dx) {
			double y = (int) Math.sqrt(Math.pow(r, 2) - Math.pow(x, 2));
			this.setPoint(x, y);
			this.setPoint(x, -y);
			this.setPoint(-x, y);
			this.setPoint(-x, -y);
			this.setPoint(y, x);
			this.setPoint(-y, x);
			this.setPoint(y, -x);
			this.setPoint(-y, -x);
		}
	}
	
	/**
	 * Draws a circle according to the second trigonometric description
	 * @param r
	 * @param c
	 */
	public void drawCircleTrig2(double r, Color c) {
		double dx = 1/(r/4);
		int xc = 0;
		//int yc = 0;
		double to =  r / Math.sqrt(2);
		for (double x = xc; x < to; x+=dx) {
			double y = (int) Math.sqrt(Math.pow(r, 2) - Math.pow(x, 2));
			this.setPoint(x, y, c);
			this.setPoint(x, -y, c);
			this.setPoint(-x, y, c);
			this.setPoint(-x, -y, c);
			this.setPoint(y, x, c);
			this.setPoint(-y, x, c);
			this.setPoint(y, -x, c);
			this.setPoint(-y, -x, c);
		}
	}
	
	/**
	 * Draws the circle using sin, and cos to get the coordinates
	 * @param r
	 */
	public void drawCirclePoli(double r) {
		double dangle = Math.PI/(180);
		//double to =  Math.PI*4;
		for (double angle = 0; angle < 45; angle+=dangle) {
			double y = r * Math.sin(Math.toRadians(angle));
			double x = r * Math.cos(Math.toRadians(angle));
			this.setPoint(x, y);
			this.setPoint(x, -y);
			this.setPoint(-x, y);
			this.setPoint(-x, -y);
			this.setPoint(y, x);
			this.setPoint(-y, x);
			this.setPoint(y, -x);
			this.setPoint(-y, -x);
		}
	}
	
	/**
	 * Draws the circle using sin, and cos to get the coordinates
	 * @param r
	 * @param c
	 */
	public void drawCirclePoli(double r, Color c) {
		double dangle;
		if ( r >= Math.PI)
			dangle = Math.PI/(r);
		else
			dangle = 1;
		//double to =  Math.PI*4;
		for (double angle = 0; angle < 45; angle+=dangle) {
			double y = r * Math.sin(Math.toRadians(angle));
			double x = r * Math.cos(Math.toRadians(angle));
			this.setPoint(x, y, c);
			this.setPoint(x, -y, c);
			this.setPoint(-x, y, c);
			this.setPoint(-x, -y, c);
			this.setPoint(y, x, c);
			this.setPoint(-y, x, c);
			this.setPoint(y, -x, c);
			this.setPoint(-y, -x, c);
		}
	}
	
	/**
	 * Draws the Aliased circle using sin, and cos to get the coordinates
	 * @param r
	 * @param c
	 */
	public void circleAA(double r, Color c) {
		double dangle;
		if ( r >= Math.PI)
			dangle = Math.PI/(r);
		else
			dangle = 1;
		//double to =  Math.PI*4;
		for (double angle = 0; angle < 90; angle+=dangle) {
			double dy = r * Math.sin(Math.toRadians(angle));
			double dx = r * Math.cos(Math.toRadians(angle));
			int y = (int) dy;
			int x = (int) dx;
			/*
			this.setPoint(x, y, c);
			this.setPoint(x, -y, c);
			this.setPoint(-x, y, c);
			this.setPoint(-x, -y, c);
			this.setPoint(y, x, c);
			this.setPoint(-y, x, c);
			this.setPoint(y, -x, c);
			this.setPoint(-y, -x, c);
			*/
			//Do the sets for each point!
			this.aSetX(dx, dy, c);
			this.aSetX(dx, -dy, c);
			this.aSetX(-dx, dy, c);
			this.aSetX(-dx, -dy, c);
			
			this.aSetY(dx, dy, c);
			this.aSetY(dx, -dy, c);
			this.aSetY(-dx, dy, c);
			this.aSetY(-dx, -dy, c);
			/*
			this.aSetY(dy, dx, c);
			this.aSetY(dy, -dx, c);
			this.aSetY(-dy, dx, c);
			this.aSetY(-dy, -dx, c);
			*/
		}
	}
	
	/**
	 * 
	 * @param x current X
	 * @param y current Y
	 * @param xo original X
	 * @param yo original Y
	 * @param c
	 */
	public void doAliasingC(double x, double y, double xo, double yo, Color c) {
		double dist = Math.sqrt(Math.pow((x - xo), 2) + Math.pow((y-yo), 2));
		if (dist >= MAXDIST) {
			System.out.println("No Aliasing curr X=" + x + ", curr Y=" + y+ ", OX=" + xo+ ", OY=" + yo);
			return;
		}
		else {
			//double perc = dist / MAXDIST;
			
			Point2D.Double p = this.getPoint(x, y);
			int i = this.getI(p.x);
			int j = this.getJ(p.y);
			//Color back = this.getPointColor(x, y);

			int perc = (int) ((dist / MAXDIST) * 100);
			
			System.out.println("dist = " +dist+", perc = " +perc);
			
			Color back = this.getPixel(i, j);
			if (back == null)
				return;
			

			double dr = (back.getRed() - c.getRed()) / 100;
			double dg = (back.getGreen() - c.getGreen()) / 100;
			double db = (back.getBlue() - c.getBlue()) / 100;

			int r = (int) (c.getRed() + (dr * perc));
			int g = (int) (c.getGreen() + (dg * perc));
			int b = (int) (c.getBlue() + (db * perc));
			
			/*
			int r = (int) ((back.getRed() * perc) + (c.getRed() * (1-perc)));
			int g = (int) ((back.getGreen() * perc) + (c.getGreen() * (1-perc)));
			int b = (int) ((back.getBlue() * perc) + (c.getBlue() * (1-perc)));
			*/
			
			if (r > 255) r = 255;
			if (g > 255) g = 255;
			if (b > 255) b = 255;
			if (r < 0) r = 0;
			if (g < 0) g = 0;
			if (b < 0) b = 0;
			this.setPixel(i, j, new Color(r, g, b));
		}
	}
	
	public void aSetX (double x, double y, Color c) {
		Point2D.Double p = this.getPoint(x, y);
		//Point2D.Double p1 = this.getPoint(x+1, y);
		//Point2D.Double p2 = this.getPoint(x-1, y);
		//int i = (int)Math.round(p.x + this.centrei);
		//int i1 = this.getI(p1.x);
		//int i2 = this.getI(p2.x);
		//int j = (int)Math.round(p.y + this.centrej);

		//Get the I and J according to the point!
		int i = this.getI(p.x);
		int j = this.getJ(p.y);
		//To maintain the double without rounding I just added the centres
		x = p.x + this.centrei;
		y =  this.centrej - p.y;
		//this.setPixel(i, j);
		
		/*
		this.doAliasingC(x, y, (int)x, (int)y, c);
		this.doAliasingC((x+1), y, (int)x, (int)y, c);
		this.doAliasingC((x-1), y, (int)x, (int)y, c);
		*/
		/*
		this.doAliasing(i, j, x, y, c);
		this.doAliasing((i+1), j, x, y, c);
		this.doAliasing((i-1), j, x, y, c);
		*/
		this.doAliasing(x, y, i, j, c);
		this.doAliasing((x+1), y, i, j, c);
		this.doAliasing((x-1), y, i, j, c);
	}
	
	public void aSetY (double x, double y, Color c) {
		Point2D.Double p = this.getPoint(x, y);
		//Point2D.Double p1 = this.getPoint(x, y+1);
		//Point2D.Double p2 = this.getPoint(x, y-1);
		//int i = (int)Math.round(p.x + this.centrei);
		//int j = (int)Math.round(p.y + this.centrej);
		//Get the I and J according to the point!
		int i = this.getI(p.x);
		int j = this.getJ(p.y);
		//To maintain the double without rounding I just added the centres
		x = p.x + this.centrei;
		y = this.centrej - p.y;
		//int j1 = this.getJ(p1.y);
		//int j2 = this.getJ(p2.y);
		//this.setPixel(i, j);
		/*
		this.doAliasingC(x, y, (int)x, (int)y, c);
		this.doAliasingC(x, (y+1), (int)x, (int)y, c);
		this.doAliasingC(x, (y-1), (int)x, (int)y, c);
		*/
		/*
		this.doAliasing(i, j, x, y, c);
		this.doAliasing(i, j+1, x, y, c);
		this.doAliasing(i, j-1, x, y, c);
		*/
		this.doAliasing(x, y, i, j, c);
		this.doAliasing(x, y+1, i, j, c);
		this.doAliasing(x, y-1, i, j, c);
	}
	
	public void drawElipse(double rada, double radb) {
		double dx;
		if (rada > radb)
			dx = 1/(rada/4);
		else
			dx = 1/(radb/4);
			
		int xc = 0;
		//int yc = 0;
		//double to =  r / Math.sqrt(2);
		for (double x = xc; x <= rada; x+=dx) {
			//y = sqrt( 1 - (x/a)^2 ) *b
			double y = radb * Math.sqrt(1-Math.pow((x/rada), 2));
			this.setPoint(x, y);
			this.setPoint(x, -y);
			this.setPoint(-x, y);
			this.setPoint(-x, -y);
		}
	}
	
	public void drawElipsePoli(double rada, double radb) {
		double dx;
		//Calculate the angle delta
		if (rada > radb)
			if ( rada >= Math.PI)
				dx = Math.PI/(rada);
			else
				dx = 1;
		else
			if ( radb >= Math.PI)
				dx = Math.PI/(radb);
			else
				dx = 1;
		
		int xc = 0;
		//int yc = 0;
		//double to =  r / Math.sqrt(2);
		//Now we will draw each point mirrored 4 times, from angle 0 to 90
		for (double ang = 0; ang <= 90; ang+=dx) {
			//y = sqrt( 1 - (x/a)^2 ) *b
			double x = rada * Math.cos(Math.toRadians(ang));
			double y = radb * Math.sin(Math.toRadians(ang));
			this.setPoint(x, y);
			this.setPoint(x, -y);
			this.setPoint(-x, y);
			this.setPoint(-x, -y);
		}
	}
	
	/**
	 * Colorea todos lo pixeles que son distintos al color del borde
	 * @param i
	 * @param j
	 * @param border
	 * @param cnew
	 */
	public void borderFill(int i, int j, Color border, Color cnew) {
		Point2D.Double p = this.getPoint(i, j);
		i = this.getI(p.x);
		j = this.getJ(p.y);
		if (i < 0 || i >= this.image.getWidth() || j < 0 || j >= this.image.getHeight())
			return;
		Stack<Point2D.Double> points = new Stack<Point2D.Double>();
		//Vector<Point2D.Double> points = new Vector<Point2D.Double>();
		points.add(new Point2D.Double(i, j));
		while (points.size() > 0) {
			if (this.getPixel((int)(points.lastElement().getX()), 
							  (int)(points.lastElement().getY())).getRGB() != border.getRGB()
				&& this.getPixel((int)(points.lastElement().getX()), 
						  		 (int)(points.lastElement().getY())).getRGB() != cnew.getRGB()) {
				
				Point2D.Double point = points.lastElement();
				this.setPixel((int)(point.x), (int)(point.y), cnew);
				
				points.removeElementAt(points.size()-1);
				if (point.x+1 < this.image.getWidth())
					points.add(new Point2D.Double(point.x +1, point.y));
				if (point.x-1 >= 0)
					points.add(new Point2D.Double(point.x -1, point.y));
				if (point.y+1 < this.image.getHeight())
					points.add(new Point2D.Double(point.x, point.y +1));
				if (point.y-1 >= 0)
					points.add(new Point2D.Double(point.x, point.y -1));
			}
			else {
				points.removeElementAt(points.size()-1);
			}
		}
	}
	
	/**
	 * Colorea todos lo pixeles que son distintos al color del borde
	 * @param i
	 * @param j
	 * @param border
	 * @param cnew
	 */
	public void borderFill(Point2D.Double p, Color border, Color cnew) {
		Point2D.Double p1 = this.getPoint(p.x, p.y);
		p.x = this.getI(p1.x);
		p.y = this.getJ(p1.y);
		if (p.x < 0 || p.x >= this.image.getWidth() || p.y < 0 || p.y >= this.image.getHeight())
			return;
		Stack<Point2D.Double> points = new Stack<Point2D.Double>();
		//Vector<Point2D.Double> points = new Vector<Point2D.Double>();
		points.add(p);
		while (points.size() > 0) {
			if (this.getPixel((int)(points.lastElement().getX()), 
							  (int)(points.lastElement().getY())).getRGB() != border.getRGB()
				&& this.getPixel((int)(points.lastElement().getX()), 
						  		 (int)(points.lastElement().getY())).getRGB() != cnew.getRGB()) {
				
				Point2D.Double point = points.lastElement();
				this.setPixel((int)(point.x), (int)(point.y), cnew);
				
				points.removeElementAt(points.size()-1);
				if (point.x+1 < this.image.getWidth())
					points.add(new Point2D.Double(point.x +1, point.y));
				if (point.x-1 >= 0)
					points.add(new Point2D.Double(point.x -1, point.y));
				if (point.y+1 < this.image.getHeight())
					points.add(new Point2D.Double(point.x, point.y +1));
				if (point.y-1 >= 0)
					points.add(new Point2D.Double(point.x, point.y -1));
			}
			else {
				points.removeElementAt(points.size()-1);
			}
		}
	}

	
	/**
	 * Colorea todos lo pixeles que son distintos al color del borde
	 * @param i
	 * @param j
	 * @param border
	 * @param cnew
	 */
	public void borderFill(Point2D.Double p, Color border, Color[][] pattern) {
		Point2D.Double p1 = this.getPoint(p.x, p.y);
		p.x = this.getI(p1.x);
		p.y = this.getJ(p1.y);
		if (p.x < 0 || p.x >= this.image.getWidth() || p.y < 0 || p.y >= this.image.getHeight())
			return;
		Stack<Point2D.Double> points = new Stack<Point2D.Double>();
		int height = pattern.length;
		int width = pattern[0].length;
		//Vector<Point2D.Double> points = new Vector<Point2D.Double>();
		points.add(p);
		while (points.size() > 0) {
			int lepx = ((int)(points.lastElement().getX()))%width;
			int lepy = ((int)(points.lastElement().getY()))%height;
			if (this.getPixel((int)(points.lastElement().getX()), 
							  (int)(points.lastElement().getY())).getRGB() != border.getRGB()
				&& this.getPixel((int)(points.lastElement().getX()), 
						  		 (int)(points.lastElement().getY())).getRGB() != 
						  		       pattern[lepy][lepx].getRGB()) {
				
				Point2D.Double point = points.lastElement();
				this.setPixel((int)(point.x), (int)(point.y), pattern[lepy][lepx]);
				
				points.removeElementAt(points.size()-1);
				if (point.x+1 < this.image.getWidth())
					points.add(new Point2D.Double(point.x +1, point.y));
				if (point.x-1 >= 0)
					points.add(new Point2D.Double(point.x -1, point.y));
				if (point.y+1 < this.image.getHeight())
					points.add(new Point2D.Double(point.x, point.y +1));
				if (point.y-1 >= 0)
					points.add(new Point2D.Double(point.x, point.y -1));
			}
			else {
				points.removeElementAt(points.size()-1);
			}
		}
	}


	
	/**
	 * Colorea todos lo pixeles que son distintos al color del borde
	 * @param i
	 * @param j
	 * @param border
	 * @param cnew
	 */
	public void borderFill(int x, int y, Color border, Color[][] pattern) {
		Point2D.Double p = this.getPoint(x, y);
		x = this.getI(p.x);
		y = this.getJ(p.y);
		if (x < 0 || x >= this.image.getWidth() || y < 0 || y >= this.image.getHeight())
			return;
		Stack<Point2D.Double> points = new Stack<Point2D.Double>();
		int height = pattern.length;
		int width = pattern[0].length;
		//Vector<Point2D.Double> points = new Vector<Point2D.Double>();
		points.add(new Point2D.Double(x, y));
		while (points.size() > 0) {
			int lepx = ((int)(points.lastElement().getX()))%width;
			int lepy = ((int)(points.lastElement().getY()))%height;
			if (this.getPixel((int)(points.lastElement().getX()), 
							  (int)(points.lastElement().getY())).getRGB() != border.getRGB()
				&& this.getPixel((int)(points.lastElement().getX()), 
						  		 (int)(points.lastElement().getY())).getRGB() != 
						  		       pattern[lepy][lepx].getRGB()) {
				
				Point2D.Double point = points.lastElement();
				this.setPixel((int)(point.x), (int)(point.y), pattern[lepy][lepx]);
				
				points.removeElementAt(points.size()-1);
				if (point.x+1 < this.image.getWidth())
					points.add(new Point2D.Double(point.x +1, point.y));
				if (point.x-1 >= 0)
					points.add(new Point2D.Double(point.x -1, point.y));
				if (point.y+1 < this.image.getHeight())
					points.add(new Point2D.Double(point.x, point.y +1));
				if (point.y-1 >= 0)
					points.add(new Point2D.Double(point.x, point.y -1));
			}
			else {
				points.removeElementAt(points.size()-1);
			}
		}
	}
	
	/**
	 * Colorea todos lo pixeles que son del color del background
	 * @param i
	 * @param j
	 * @param border
	 * @param cnew
	 */
	public void floodFill(int i, int j, Color back, Color cnew) {
		Point2D.Double p = this.getPoint(i, j);
		i = this.getI(p.x);
		j = this.getJ(p.y);
		if (i < 0 || i >= this.image.getWidth() || j < 0 || j >= this.image.getHeight())
			return;
		Stack<Point2D.Double> points = new Stack<Point2D.Double>();
		//Vector<Point2D.Double> points = new Vector<Point2D.Double>();
		points.add(new Point2D.Double(i, j));
		while (points.size() > 0) {
			if (this.getPixel((int)(points.lastElement().getX()), 
							  (int)(points.lastElement().getY())).getRGB() == back.getRGB()
				&& this.getPixel((int)(points.lastElement().getX()), 
						  		 (int)(points.lastElement().getY())).getRGB() != cnew.getRGB()) {
				
				Point2D.Double point = points.lastElement();
				this.setPixel((int)(point.x), (int)(point.y), cnew);
				
				points.removeElementAt(points.size()-1);
				if (point.x+1 < this.image.getWidth())
					points.add(new Point2D.Double(point.x +1, point.y));
				if (point.x-1 >= 0)
					points.add(new Point2D.Double(point.x -1, point.y));
				if (point.y+1 < this.image.getHeight())
					points.add(new Point2D.Double(point.x, point.y +1));
				if (point.y-1 >= 0)
					points.add(new Point2D.Double(point.x, point.y -1));
			}
			else {
				points.removeElementAt(points.size()-1);
			}
		}
	}
	
	/**
	 * Colorea todos lo pixeles que son distintos al color del borde
	 * @param i
	 * @param j
	 * @param border
	 * @param cnew
	 */
	public void floodFill(Point2D.Double p, Color back, Color cnew) {
		Point2D.Double p1 = this.getPoint(p.x, p.y);
		p.x = this.getI(p1.x);
		p.y = this.getJ(p1.y);
		//If it is offscale just do nothing and return
		if (p.x < 0 || p.x >= this.image.getWidth() || p.y < 0 || p.y >= this.image.getHeight())
			return;
		Stack<Point2D.Double> points = new Stack<Point2D.Double>();
		//Vector<Point2D.Double> points = new Vector<Point2D.Double>();
		points.add(p);
		while (points.size() > 0) {
			if (this.getPixel((int)(points.lastElement().getX()), 
							  (int)(points.lastElement().getY())).getRGB() == back.getRGB()
				&& this.getPixel((int)(points.lastElement().getX()), 
						  		 (int)(points.lastElement().getY())).getRGB() != cnew.getRGB()) {
				
				Point2D.Double point = points.lastElement();
				this.setPixel((int)(point.x), (int)(point.y), cnew);
				
				points.removeElementAt(points.size()-1);
				if (point.x+1 < this.image.getWidth())
					points.add(new Point2D.Double(point.x +1, point.y));
				if (point.x-1 >= 0)
					points.add(new Point2D.Double(point.x -1, point.y));
				if (point.y+1 < this.image.getHeight())
					points.add(new Point2D.Double(point.x, point.y +1));
				if (point.y-1 >= 0)
					points.add(new Point2D.Double(point.x, point.y -1));
			}
			else {
				points.removeElementAt(points.size()-1);
			}
		}
	}
	
	/**
	 * Colorea todos lo pixeles que son distintos al color del borde
	 * @param i
	 * @param j
	 * @param border
	 * @param cnew
	 */
	public void floodFill(Point2D.Double p, Color back, Color[][] pattern) {
		Point2D.Double p1 = this.getPoint(p.x, p.y);
		p.x = this.getI(p1.x);
		p.y = this.getJ(p1.y);
		//If it is offscale just do nothing and return
		if (p.x < 0 || p.x >= this.image.getWidth() || p.y < 0 || p.y >= this.image.getHeight())
			return;
		Stack<Point2D.Double> points = new Stack<Point2D.Double>();
		//Vector<Point2D.Double> points = new Vector<Point2D.Double>();
		points.add(p);

		int height = pattern.length;
		int width = pattern[0].length;
		
		while (points.size() > 0) {
			int lepx = ((int)(points.lastElement().getX()))%width;
			int lepy = ((int)(points.lastElement().getY()))%height;
			if (this.getPixel((int)(points.lastElement().getX()), 
							  (int)(points.lastElement().getY())).getRGB() == back.getRGB()
				&& this.getPixel((int)(points.lastElement().getX()), 
						  		 (int)(points.lastElement().getY())).getRGB() != 
							  		       pattern[lepy][lepx].getRGB()) {
				
				Point2D.Double point = points.lastElement();
				this.setPixel((int)(point.x), (int)(point.y),  pattern[lepy][lepx]);
				
				points.removeElementAt(points.size()-1);
				if (point.x+1 < this.image.getWidth())
					points.add(new Point2D.Double(point.x +1, point.y));
				if (point.x-1 >= 0)
					points.add(new Point2D.Double(point.x -1, point.y));
				if (point.y+1 < this.image.getHeight())
					points.add(new Point2D.Double(point.x, point.y +1));
				if (point.y-1 >= 0)
					points.add(new Point2D.Double(point.x, point.y -1));
			}
			else {
				points.removeElementAt(points.size()-1);
			}
		}
	}

	
	/**
	 * Colorea todos lo pixeles que son del color del fondo
	 * @param i
	 * @param j
	 * @param border
	 * @param cnew
	 */
	public void floodFill(int x, int y, Color back, Color[][] pattern) {

		Point2D.Double p = this.getPoint(x, y);
		x = this.getI(p.x);
		y = this.getJ(p.y);
		//If it is offscale just do nothing and return
		if (x < 0 || x >= this.image.getWidth() || y < 0 || y >= this.image.getHeight())
			return;
		Stack<Point2D.Double> points = new Stack<Point2D.Double>();
		//Vector<Point2D.Double> points = new Vector<Point2D.Double>();
		points.add(new Point2D.Double(x, y));

		int height = pattern.length;
		int width = pattern[0].length;
		
		while (points.size() > 0) {
			int lepx = ((int)(points.lastElement().getX()))%width;
			int lepy = ((int)(points.lastElement().getY()))%height;
			if (this.getPixel((int)(points.lastElement().getX()), 
							  (int)(points.lastElement().getY())).getRGB() == back.getRGB()
				&& this.getPixel((int)(points.lastElement().getX()), 
						  		 (int)(points.lastElement().getY())).getRGB() != 
							  		       pattern[lepy][lepx].getRGB()) {
				
				Point2D.Double point = points.lastElement();
				this.setPixel((int)(point.x), (int)(point.y),  pattern[lepy][lepx]);
				
				points.removeElementAt(points.size()-1);
				if (point.x+1 < this.image.getWidth())
					points.add(new Point2D.Double(point.x +1, point.y));
				if (point.x-1 >= 0)
					points.add(new Point2D.Double(point.x -1, point.y));
				if (point.y+1 < this.image.getHeight())
					points.add(new Point2D.Double(point.x, point.y +1));
				if (point.y-1 >= 0)
					points.add(new Point2D.Double(point.x, point.y -1));
			}
			else {
				points.removeElementAt(points.size()-1);
			}
		}
	}
	
	
	public void drawCircleMidpoint(int radius) {
	  int error = -radius;
	  int x = radius;
	  int y = 0;
	 
	  //Este ciclo se repetir‡ hasta que x sea menor que y,
	  // lo cual significar’a que hemos llegado al final del primer cuadrante
	  //Se divide al c’rculo en 4 cuadrantes, y solo calculamos 1.
	  //Los dem‡s se dibujan a partir del c‡lculo del primero.
	  while (x >= y) {
	 
		this.setPoint(x, y);
	    if (x != 0) this.setPoint(-x, y);
	    if (y != 0) this.setPoint(x,  -y);
	    if (x != 0 && y != 0) this.setPoint(-x, -y);
	    
	    this.setPoint(y, x);
	    if (y != 0) this.setPoint(-y, x);
	    if (x != 0) this.setPoint(y,  -x);
	    if (x != 0 && y != 0) this.setPoint(-y, -x);
	    
	    error += y;
	    ++y;
	    error += y;
	 
	    //Cuando la suma excede al radio, significa que 
	    //tenemos que disminuir x para comenzar la curva
	    if (error >= 0) {
	      error -= x;
	      --x;
	      error -= x;
	    }
	  }
	}
	
	public void drawCircleMidpoint(int radius, Color c) {
	  int error = -radius;
	  int x = radius;
	  int y = 0;
	 
	  //Este ciclo se repetir‡ hasta que x sea menor que y,
	  // lo cual significar’a que hemos llegado al final del primer cuadrante
	  //Se divide al c’rculo en 4 cuadrantes, y solo calculamos 1.
	  //Los dem‡s se dibujan a partir del c‡lculo del primero.
	  while (x >= y) {
	 
		this.setPoint(x, y, c);
	    if (x != 0) this.setPoint(-x, y, c);
	    if (y != 0) this.setPoint(x,  -y, c);
	    if (x != 0 && y != 0) this.setPoint(-x, -y, c);
	    
	    this.setPoint(y, x, c);
	    if (y != 0) this.setPoint(-y, x, c);
	    if (x != 0) this.setPoint(y,  -x, c);
	    if (x != 0 && y != 0) this.setPoint(-y, -x, c);
	    
	    error += y;
	    ++y;
	    error += y;
	 
	    //Cuando la suma excede al radio, significa que 
	    //tenemos que disminuir x para comenzar la curva
	    if (error >= 0) {
	      error -= x;
	      --x;
	      error -= x;
	    }
	  }
	}
	
	/**
	 * Draws a circle and then fills it with the background color!
	 * @param radius
	 * @param c
	 * @param back
	 */
	public void drawCircle(int radius, Color c, Color back) {
	  int error = -radius;
	  int x = radius;
	  int y = 0;
	 
	  //Este ciclo se repetir‡ hasta que x sea menor que y,
	  // lo cual significar’a que hemos llegado al final del primer cuadrante
	  //Se divide al c’rculo en 4 cuadrantes, y solo calculamos 1.
	  //Los dem‡s se dibujan a partir del c‡lculo del primero.
	  while (x >= y) {
	 
		this.setPoint(x, y, c);
	    if (x != 0) this.setPoint(-x, y, c);
	    if (y != 0) this.setPoint(x,  -y, c);
	    if (x != 0 && y != 0) this.setPoint(-x, -y, c);
	    
	    this.setPoint(y, x, c);
	    if (y != 0) this.setPoint(-y, x, c);
	    if (x != 0) this.setPoint(y,  -x, c);
	    if (x != 0 && y != 0) this.setPoint(-y, -x, c);
	    
	    error += y;
	    ++y;
	    error += y;
	 
	    //Cuando la suma excede al radio, significa que 
	    //tenemos que disminuir x para comenzar la curva
	    if (error >= 0) {
	      error -= x;
	      --x;
	      error -= x;
	    }
	  }

		//Point2D.Double p = this.getPoint(0, 0);
	  //this.borderFill(new Point2D.Double(this.getI(p.x), this.getJ(p.y)), c, back);
	  this.borderFill(new Point2D.Double(0, 0), c, back);
	}
	
	/**
	 * Draws a circle and then fills it with the background color!
	 * @param radius
	 * @param c
	 * @param back
	 */
	public void drawCircle(int radius, Color c, Color[][] back) {
	  int error = -radius;
	  int x = radius;
	  int y = 0;
	 
	  //Este ciclo se repetir‡ hasta que x sea menor que y,
	  // lo cual significar’a que hemos llegado al final del primer cuadrante
	  //Se divide al c’rculo en 4 cuadrantes, y solo calculamos 1.
	  //Los dem‡s se dibujan a partir del c‡lculo del primero.
	  while (x >= y) {
	 
		this.setPoint(x, y, c);
	    if (x != 0) this.setPoint(-x, y, c);
	    if (y != 0) this.setPoint(x,  -y, c);
	    if (x != 0 && y != 0) this.setPoint(-x, -y, c);
	    
	    this.setPoint(y, x, c);
	    if (y != 0) this.setPoint(-y, x, c);
	    if (x != 0) this.setPoint(y,  -x, c);
	    if (x != 0 && y != 0) this.setPoint(-y, -x, c);
	    
	    error += y;
	    ++y;
	    error += y;
	 
	    //Cuando la suma excede al radio, significa que 
	    //tenemos que disminuir x para comenzar la curva
	    if (error >= 0) {
	      error -= x;
	      --x;
	      error -= x;
	    }
	  }

		//Point2D.Double p = this.getPoint(0, 0);
	  //this.borderFill(new Point2D.Double(this.getI(p.x), this.getJ(p.y)), c, back);
	  this.borderFill(new Point2D.Double(0, 0), c, back);
	}
	
	
	public void drawRectBre(int x1, int y1, int x2, int y2) {
		if (!this.generalRectMethods2(x1, y1, x2, y2)) {
			int dx = Math.abs(x2-x1);
			int dy = Math.abs(y2-y1);
			int sx;
			int sy;
			//This calculates the direction, right or left
			//up or down
			if (x1 < x2) sx = 1; else sx = -1;
			if (y1 < y2) sy = 1; else sy = -1;
			//if dx is bigger than dy, it means it is more horizontal
			//else it is more vertical
			int dir = dx-dy;
			
			this.setPoint(x1, y1);
			
			while (x1 != x2 && y1 != y2) {
				int e2 = 2*dir;
				//This part moves the pointer in x, and y
				if (e2 > -dy) {
					dir -= dy;
					x1 += sx;
				}
				if (e2 < dx) {
					dir += dx;
					y1 += sy;
				}
				
				this.setPoint(x1, y1);
			}
		}
	}
	
	public void drawRectBre2(int x1, int y1, int x2, int y2, Color c) {
		if (!this.generalRectMethods3(x1, y1, x2, y2, c)) {
			int dx = Math.abs(x2-x1);
			int dy = Math.abs(y2-y1);
			int sx;
			int sy;
			//This calculates the direction, right or left
			//up or down
			if (x1 < x2) sx = 1; else sx = -1;
			if (y1 < y2) sy = 1; else sy = -1;
			//if dx is bigger than dy, it means it is more horizontal
			//else it is more vertical
			int dir = dx-dy;
			
			this.setPoint(x1, y1, c);
			
			while (x1 != x2 && y1 != y2) {
				int e2 = 2*dir;
				//This part moves the pointer in x, and y
				if (e2 > -dy) {
					dir -= dy;
					x1 += sx;
				}
				if (e2 < dx) {
					dir += dx;
					y1 += sy;
				}
				
				this.setPoint(x1, y1, c);
			}
		}
	}
	

	
	public void drawRectBre(int x1, int y1, int x2, int y2, Color c) {
		if (!this.generalRectMethods2(x1, y1, x2, y2, c)) {
			int dx = Math.abs(x2-x1);
			int dy = Math.abs(y2-y1);
			int sx;
			int sy;
			//This calculates the direction, right or left
			//up or down
			if (x1 < x2) sx = 1; else sx = -1;
			if (y1 < y2) sy = 1; else sy = -1;
			//if dx is bigger than dy, it means it is more horizontal
			//else it is more vertical
			int dir = dx-dy;
			
			this.setPoint(x1, y1, c);
			
			while (x1 != x2 && y1 != y2) {
				int e2 = 2*dir;
				//This part moves the pointer in x, and y
				if (e2 > -dy) {
					dir -= dy;
					x1 += sx;
				}
				if (e2 < dx) {
					dir += dx;
					y1 += sy;
				}
				
				this.setPoint(x1, y1, c);
			}
		}
	}
	
	public void drawRectBre(double x1, double y1, double x2, double y2, Color c) {
		if (!this.generalRectMethods2(x1, y1, x2, y2, c)) {
			double dx = Math.abs(x2-x1);
			double dy = Math.abs(y2-y1);
			int sx;
			int sy;
			//This calculates the direction, right or left
			//up or down
			if (x1 < x2) sx = 1; else sx = -1;
			if (y1 < y2) sy = 1; else sy = -1;
			//if dx is bigger than dy, it means it is more horizontal
			//else it is more vertical
			double dir = dx-dy;
			
			this.setPoint(x1, y1, c);
			
			while (x1 != x2 && y1 != y2) {
				if (sx > 0 && x1 >= x2)
					return;
				else if (sx < 0 && x1 <= x2)
					return;
				if (sy > 0 && y1 >= y2)
					return;
			    else if (sy < 0 && y1 <= y2)
			    	return;
				
				double e2 = 2*dir;
				//This part moves the pointer in x, and y
				if (e2 > -dy) {
					dir -= dy;
					x1 += sx;
				}
				if (e2 < dx) {
					dir += dx;
					y1 += sy;
				}
				
				this.setPoint(x1, y1, c);
			}
		}
	}
	
	public void colorRectBre(Point2D p1, Point2D p2, Color c1, Color c2) {
		if (!this.generalRectMethods(p1, p2, c1, c2)) {
			double x1 = p1.getX();
			double y1 = p1.getY();
			double x2 = p2.getX();
			double y2 = p2.getY();
			
			double dx = Math.abs(x2-x1);
			double dy = Math.abs(y2-y1);
			int sx;
			int sy;
			//This calculates the direction, right or left
			//up or down
			if (x1 < x2) sx = 1; else sx = -1;
			if (y1 < y2) sy = 1; else sy = -1;
			
			double dr;
			double dg;
			double db;
			Color c;
			double cr;
			double cg;
			double cb;
			
			//if dx is bigger than dy, it means it is more horizontal
			//else it is more vertical
			if (dx > dy) {
				dr = (c2.getRed() - c1.getRed()) / dx;
				dg = (c2.getGreen() - c1.getGreen()) / dx;
				db = (c2.getBlue() - c1.getBlue()) / dx;
				c = new Color(c1.getRGB());
				cr = c.getRed();
				cg = c.getGreen();
				cb = c.getBlue();
			}
			else {
				dr = (c2.getRed() - c1.getRed()) / dy;
				dg = (c2.getGreen() - c1.getGreen()) / dy;
				db = (c2.getBlue() - c1.getBlue()) / dy;
				c = new Color(c1.getRGB());
				cr = c.getRed();
				cg = c.getGreen();
				cb = c.getBlue();
			}
			
			double dir = dx-dy;
			
			this.setPoint(x1, y1, c);
			
			while (x1 != x2 && y1 != y2) {
				if (sx > 0 && x1 >= x2)
					return;
				else if (sx < 0 && x1 <= x2)
					return;
				if (sy > 0 && y1 >= y2)
					return;
			    else if (sy < 0 && y1 <= y2)
			    	return;
				
				double e2 = 2*dir;
				//This part moves the pointer in x, and y
				if (e2 > -dy) {
					dir -= dy;
					x1 += sx;
					if (dx > dy) {
						cr += dr;
						cg += dg;
						cb += db;
						if (cr > 255) cr = 255;
						else if (cr < 0) cr = 0;
						if (cg > 255) cg = 255;
						else if (cg < 0) cg = 0;
						if (cb > 255) cb = 255;
						else if (cb < 0) cb = 0;
						c = new Color((int)(cr), (int)(cg), (int)(cb));
					}
				}
				if (e2 < dx) {
					dir += dx;
					y1 += sy;
					if (dy > dx) {
						cr += dr;
						cg += dg;
						cb += db;
						if (cr > 255) cr = 255;
						else if (cr < 0) cr = 0;
						if (cg > 255) cg = 255;
						else if (cg < 0) cg = 0;
						if (cb > 255) cb = 255;
						else if (cb < 0) cb = 0;
						c = new Color((int)(cr), (int)(cg), (int)(cb));
					}
				}
				
				this.setPoint(x1, y1, c);
			}
		}
	}
	
	public void scanLine (Vector<Point2D.Double> p, Color c) {
		Vector<Edge> edges = new Vector<Edge>();
		int miny;
		int maxy;
		
		//Cycle in which we get the edges!
		//One edge per 2 connecting points
		/*for (int i = 1; i < p.size(); i++)
			if (p.elementAt(i).getY() > p.elementAt(i-1).getY())
			edges.add( new Edge(p.elementAt(i).getY(), 
								p.elementAt(i-1).getY(), p.elementAt(i-1).getX(), 
								(p.elementAt(i).getX() - p.elementAt(i-1).getX()) / 
								(p.elementAt(i).getY() - p.elementAt(i-1).getY()), false)
					 );
			else
				edges.add( new Edge(p.elementAt(i-1).getY(), 
						p.elementAt(i).getY(), p.elementAt(i).getX(), 
						(p.elementAt(i-1).getX() - p.elementAt(i).getX()) / 
						(p.elementAt(i-1).getY() - p.elementAt(i).getY()), false)
			 );
		
		if (p.elementAt(0).getY() > p.elementAt(p.size()-1).getY())
			edges.add( new Edge(p.elementAt(0).getY(), 
								p.elementAt(p.size()-1).getY(), p.elementAt(p.size()-1).getX(), 
								(p.elementAt(0).getX() - p.elementAt(p.size()-1).getX()) / 
								(p.elementAt(0).getY() - p.elementAt(p.size()-1).getY()), false)
					 );
			else
				edges.add( new Edge(p.elementAt(p.size()-1).getY(), 
						p.elementAt(0).getY(), p.elementAt(0).getX(), 
						(p.elementAt(p.size()-1).getX() - p.elementAt(0).getX()) / 
						(p.elementAt(p.size()-1).getY() - p.elementAt(0).getY()), false)
			 );
		*/

		for (int i = 1; i < p.size(); i++) {
			Point2D.Double p1 = this.getPoint(p.elementAt(i-1).x, p.elementAt(i-1).y);
			p1.setLocation(this.getI(p1.x), this.getJ(p1.y));
			Point2D.Double p2 = this.getPoint(p.elementAt(i).x, p.elementAt(i).y);
			p2.setLocation(this.getI(p2.x), this.getJ(p2.y));
			//edges.add( new Edge(p.elementAt(i-1).x, p.elementAt(i-1).y, p.elementAt(i).x, p.elementAt(i).y));
			edges.add( new Edge(p1.x, p1.y, p2.x, p2.y));
		}
		{
			Point2D.Double p1 = this.getPoint(p.elementAt(0).x, p.elementAt(0).y);
			p1.setLocation(this.getI(p1.x), this.getJ(p1.y));
			Point2D.Double p2 = this.getPoint(p.elementAt(p.size()-1).x, p.elementAt(p.size()-1).y);
			p2.setLocation(this.getI(p2.x), this.getJ(p2.y));
			//edges.add( new Edge(p.elementAt(0).x, p.elementAt(0).y, p.elementAt(p.size()-1).x, p.elementAt(p.size()-1).y));
			edges.add( new Edge(p1.x, p1.y, p2.x, p2.y));
		}
		
		//Sort the edges
		this.quicksort(edges, 0, edges.size()-1);
		//Get the miny
		miny = (int) edges.firstElement().ymin;
		maxy = (int) edges.firstElement().ymax;
		//For cycle to get the maxy
		for (int i =1; i < edges.size(); i++) {
			if (maxy < edges.elementAt(i).ymax)
				maxy = (int) edges.elementAt(i).ymax;
		}
		
		//This cycle activates the required edges, and then adds them to the activeX list
		for (int y = miny; y < maxy; y++) {
			Vector<Integer> activeX = new Vector<Integer>();
			for (int i =0; i < edges.size(); i++) {
				if (edges.elementAt(i).ymax != edges.elementAt(i).ymin) {
					if (y == (int)(edges.elementAt(i).ymax)) {
						edges.elementAt(i).active = false;
					}
					else if ((int)edges.elementAt(i).ymin == y){
						edges.elementAt(i).active = true;
					}
					//If it is active add it to the activeX list!
					if (edges.elementAt(i).active) {
						activeX.add((int)(edges.elementAt(i).x));
						edges.elementAt(i).setX(edges.elementAt(i).x + edges.elementAt(i).lm);
					}
				}
			}
			
			//Sort the list accordingly
			this.quicksorti(activeX, 0, activeX.size()-1);
			//Draw all the rects!
			for (int i = 1; i < activeX.size(); i+=2) {
				this.drawRectBre2(activeX.elementAt(i-1), y, activeX.elementAt(i), y, c);
			}
		}
	}
	
	public void scanLine (Vector<Point2D.Double> p, Color[][] c) {
		Vector<Edge> edges = new Vector<Edge>();
		int miny;
		int maxy;
		
		//Cycle in which we get the edges!
		//One edge per 2 connecting points
		/*for (int i = 1; i < p.size(); i++)
			if (p.elementAt(i).getY() > p.elementAt(i-1).getY()) {
				edges.add( new Edge(p.elementAt(i).getY(), 
								p.elementAt(i-1).getY(), p.elementAt(i-1).getX(), 
								(p.elementAt(i).getX() - p.elementAt(i-1).getX()) / 
								(p.elementAt(i).getY() - p.elementAt(i-1).getY()), false)
					 );
				System.out.println(edges.lastElement().x);
			}
			else {
				edges.add( new Edge(p.elementAt(i-1).getY(), 
						p.elementAt(i).getY(), p.elementAt(i).getX(), 
						(p.elementAt(i-1).getX() - p.elementAt(i).getX()) / 
						(p.elementAt(i-1).getY() - p.elementAt(i).getY()), false)
			 );
				System.out.println(edges.lastElement().x);
			}
		
		if (p.elementAt(0).getY() > p.elementAt(p.size()-1).getY())
			edges.add( new Edge(p.elementAt(0).getY(), 
								p.elementAt(p.size()-1).getY(), p.elementAt(p.size()-1).getX(), 
								(p.elementAt(0).getX() - p.elementAt(p.size()-1).getX()) / 
								(p.elementAt(0).getY() - p.elementAt(p.size()-1).getY()), false)
					 );
			else
				edges.add( new Edge(p.elementAt(p.size()-1).getY(), 
						p.elementAt(0).getY(), p.elementAt(0).getX(), 
						(p.elementAt(p.size()-1).getX() - p.elementAt(0).getX()) / 
						(p.elementAt(p.size()-1).getY() - p.elementAt(0).getY()), false)
			 );
		 */

		for (int i = 1; i < p.size(); i++) {
			Point2D.Double p1 = this.getPoint(p.elementAt(i-1).x, p.elementAt(i-1).y);
			p1.setLocation(this.getI(p1.x), this.getJ(p1.y));
			Point2D.Double p2 = this.getPoint(p.elementAt(i).x, p.elementAt(i).y);
			p2.setLocation(this.getI(p2.x), this.getJ(p2.y));
			//edges.add( new Edge(p.elementAt(i-1).x, p.elementAt(i-1).y, p.elementAt(i).x, p.elementAt(i).y));
			edges.add( new Edge(p1.x, p1.y, p2.x, p2.y));
		}
		{
			Point2D.Double p1 = this.getPoint(p.elementAt(0).x, p.elementAt(0).y);
			p1.setLocation(this.getI(p1.x), this.getJ(p1.y));
			Point2D.Double p2 = this.getPoint(p.elementAt(p.size()-1).x, p.elementAt(p.size()-1).y);
			p2.setLocation(this.getI(p2.x), this.getJ(p2.y));
			//edges.add( new Edge(p.elementAt(0).x, p.elementAt(0).y, p.elementAt(p.size()-1).x, p.elementAt(p.size()-1).y));
			edges.add( new Edge(p1.x, p1.y, p2.x, p2.y));
		}
		
		System.out.println(edges.lastElement().x);
		//Sort the edges
		this.quicksort(edges, 0, edges.size()-1);
		//Get the miny
		miny = (int) edges.firstElement().ymin;
		maxy = (int) edges.firstElement().ymax;
		//For cycle to get the maxy
		for (int i =1; i < edges.size(); i++) {
			if (maxy < edges.elementAt(i).ymax)
				maxy = (int) edges.elementAt(i).ymax;
		}
		
		//This cycle activates the required edges, and then adds them to the activeX list
		for (int y = miny; y < maxy; y++) {
			Vector<Integer> activeX = new Vector<Integer>();
			for (int i =0; i < edges.size(); i++) {
				if (edges.elementAt(i).ymax != edges.elementAt(i).ymin) {
					if (y == (int)(edges.elementAt(i).ymax)) {
						edges.elementAt(i).active = false;
					}
					else if ((int)(edges.elementAt(i).ymin) == y){
						edges.elementAt(i).active = true;
					}
					//If it is active add it to the activeX list!
					if (edges.elementAt(i).active) {
						activeX.add((int)(edges.elementAt(i).x));
						edges.elementAt(i).setX(edges.elementAt(i).x + edges.elementAt(i).lm);
					}
				}
			}
			
			//Sort the list accordingly
			this.quicksorti(activeX, 0, activeX.size()-1);
			//Draw all the rects!
			for (int i = 1; i < activeX.size(); i+=2) {
				this.drawHorizontalRect(activeX.elementAt(i-1),  activeX.elementAt(i), y, c);
			}
		}
	}
	
	public void scanLine (Point2D.Double[] p, Color c) {
		//this.lineLoop(p, c);
		Vector<Edge> edges = new Vector<Edge>();
		int miny;
		int maxy;

		//Cycle in which we get the edges!
		//One edge per 2 connecting points
		/*for (int i = 1; i < p.length; i++)
			if (p[i].getY() > p[i-1].getY())
			edges.add( new Edge(p[i].getY(), 
								p[i-1].getY(), p[i-1].getX(), 
								(p[i].getX() - p[i-1].getX()) / 
								(p[i].getY() - p[i-1].getY()), false)
					 );
			else
				edges.add( new Edge(p[i-1].getY(), 
						p[i].getY(), p[i].getX(), 
						(p[i-1].getX() - p[i].getX()) / 
						(p[i-1].getY() - p[i].getY()), false)
			 );
		
		if (p[0].getY() > p[p.length-1].getY())
			edges.add( new Edge(p[0].getY(), 
								p[p.length-1].getY(), p[p.length-1].getX(), 
								(p[0].getX() - p[p.length-1].getX()) / 
								(p[0].getY() - p[p.length-1].getY()), false)
					 );
			else
				edges.add( new Edge(p[p.length-1].getY(), 
						p[0].getY(), p[0].getX(), 
						(p[p.length-1].getX() - p[0].getX()) / 
						(p[p.length-1].getY() - p[0].getY()), false)
			 );
		*/
		

		/*for (int i = 1; i < p.length; i++) {
			edges.add( new Edge(p[i-1].x, p[i-1].y, p[i].x, p[i].y));
		}
		edges.add( new Edge(p[0].x, p[0].y, p[p.length-1].x, p[p.length-1].y));
		*/
		for (int i = 1; i < p.length; i++) {
			Point2D.Double p1 = this.getPoint(p[i-1].x, p[i-1].y);
			p1.setLocation(this.getI(p1.x), this.getJ(p1.y));
			Point2D.Double p2 = this.getPoint(p[i].x, p[i].y);
			p2.setLocation(this.getI(p2.x), this.getJ(p2.y));
			//edges.add( new Edge(p.elementAt(i-1).x, p.elementAt(i-1).y, p.elementAt(i).x, p.elementAt(i).y));
			edges.add( new Edge(p1.x, p1.y, p2.x, p2.y));
		}
		{
			Point2D.Double p1 = this.getPoint(p[0].x, p[0].y);
			p1.setLocation(this.getI(p1.x), this.getJ(p1.y));
			Point2D.Double p2 = this.getPoint(p[p.length-1].x, p[p.length-1].y);
			p2.setLocation(this.getI(p2.x), this.getJ(p2.y));
			//edges.add( new Edge(p.elementAt(0).x, p.elementAt(0).y, p.elementAt(p.size()-1).x, p.elementAt(p.size()-1).y));
			edges.add( new Edge(p1.x, p1.y, p2.x, p2.y));
		}
		
		//Sort the edges to set the smallest y at the begining and the biggest at last
		this.quicksort(edges, 0, edges.size()-1);
		//Get the miny
		//And a for cycle to get maxy
		miny = (int) edges.firstElement().ymin;
		maxy = (int) edges.firstElement().ymax;
		for (int i =1; i < edges.size(); i++) {
			if (maxy < edges.elementAt(i).ymax)
				maxy = (int) edges.elementAt(i).ymax;
		}

		//This cycle activates the required edges, and then adds them to the activeX list
		for (int y = miny; y < maxy; y++) {
			Vector<Integer> activeX = new Vector<Integer>();
			for (int i =0; i < edges.size(); i++) {
				if (edges.elementAt(i).ymax != edges.elementAt(i).ymin) {
					if (y == (int)edges.elementAt(i).ymax) {
						edges.elementAt(i).active = false;
					}
					else if ((int)edges.elementAt(i).ymin == y){
						edges.elementAt(i).active = true;
					}
					//If the edge is active, add it to the activeX list!
					if (edges.elementAt(i).active) {
						activeX.add((int)(edges.elementAt(i).x));
						edges.elementAt(i).setX(edges.elementAt(i).x + edges.elementAt(i).lm);
					}
				}
			}
			
			//Sort the list
			this.quicksorti(activeX, 0, activeX.size()-1);
			//Draw the lines!
			for (int i = 1; i < activeX.size(); i+=2) {
				this.drawRectBre2(activeX.elementAt(i-1), y, activeX.elementAt(i), y, c);
			}
		}
	}

	
	public void scanLine (Point2D.Double[] p, Color[][] c) {
		//this.lineLoop(p, c);
		Vector<Edge> edges = new Vector<Edge>();
		int miny;
		int maxy;

		//Cycle in which we get the edges!
		//One edge per 2 connecting points
		/*for (int i = 1; i < p.length; i++) {
			if (p[i].getY() > p[i-1].getY())
			edges.add( new Edge(p[i].getY(), 
								p[i-1].getY(), p[i-1].getX(), 
								(p[i].getX() - p[i-1].getX()) / 
								(p[i].getY() - p[i-1].getY()), false)
					 );
			else
				edges.add( new Edge(p[i-1].getY(), 
						p[i].getY(), p[i].getX(), 
						(p[i-1].getX() - p[i].getX()) / 
						(p[i-1].getY() - p[i].getY()), false)
			 );
			System.out.println(edges.lastElement().x);
		}
		
		if (p[0].getY() > p[p.length-1].getY())
			edges.add( new Edge(p[0].getY(), 
								p[p.length-1].getY(), p[p.length-1].getX(), 
								(p[0].getX() - p[p.length-1].getX()) / 
								(p[0].getY() - p[p.length-1].getY()), false)
					 );
			else
				edges.add( new Edge(p[p.length-1].getY(), 
						p[0].getY(), p[0].getX(), 
						(p[p.length-1].getX() - p[0].getX()) / 
						(p[p.length-1].getY() - p[0].getY()), false)
			 );
		*/
		/*for (int i = 1; i < p.length; i++) {
			edges.add( new Edge(p[i-1].x, p[i-1].y, p[i].x, p[i].y));
		}
		edges.add( new Edge(p[0].x, p[0].y, p[p.length-1].x, p[p.length-1].y));
		*/
		for (int i = 1; i < p.length; i++) {
			Point2D.Double p1 = this.getPoint(p[i-1].x, p[i-1].y);
			p1.setLocation(this.getI(p1.x), this.getJ(p1.y));
			Point2D.Double p2 = this.getPoint(p[i].x, p[i].y);
			p2.setLocation(this.getI(p2.x), this.getJ(p2.y));
			//edges.add( new Edge(p.elementAt(i-1).x, p.elementAt(i-1).y, p.elementAt(i).x, p.elementAt(i).y));
			edges.add( new Edge(p1.x, p1.y, p2.x, p2.y));
		}
		{
			Point2D.Double p1 = this.getPoint(p[0].x, p[0].y);
			p1.setLocation(this.getI(p1.x), this.getJ(p1.y));
			Point2D.Double p2 = this.getPoint(p[p.length-1].x, p[p.length-1].y);
			p2.setLocation(this.getI(p2.x), this.getJ(p2.y));
			//edges.add( new Edge(p.elementAt(0).x, p.elementAt(0).y, p.elementAt(p.size()-1).x, p.elementAt(p.size()-1).y));
			edges.add( new Edge(p1.x, p1.y, p2.x, p2.y));
		}
		
		//Sort the edges to set the smallest y at the begining and the biggest at last
		this.quicksort(edges, 0, edges.size()-1);
		//Get the miny
		//And a for cycle to get maxy
		miny = (int) edges.firstElement().ymin;
		maxy = (int) edges.firstElement().ymax;
		for (int i =1; i < edges.size(); i++) {
			if (maxy < edges.elementAt(i).ymax)
				maxy = (int) edges.elementAt(i).ymax;
		}

		//This cycle activates the required edges, and then adds them to the activeX list
		for (int y = miny; y < maxy; y++) {
			Vector<Integer> activeX = new Vector<Integer>();
			for (int i =0; i < edges.size(); i++) {
				if (edges.elementAt(i).ymax != edges.elementAt(i).ymin) {
					if (y == (int)edges.elementAt(i).ymax) {
						edges.elementAt(i).active = false;
					}
					else if ((int)edges.elementAt(i).ymin == y){
						edges.elementAt(i).active = true;
					}
					//If the edge is active, add it to the activeX list!
					if (edges.elementAt(i).active) {
						activeX.add((int)(edges.elementAt(i).x));
						edges.elementAt(i).setX(edges.elementAt(i).x + edges.elementAt(i).lm);
					}
				}
			}
			
			//Sort the list
			this.quicksorti(activeX, 0, activeX.size()-1);
			//Draw the lines!
			for (int i = 1; i < activeX.size(); i+=2) {
				this.drawHorizontalRect(activeX.elementAt(i-1),  activeX.elementAt(i), y, c);
			}
		}
	}
	
	
	
	public Color getPointColor (double x, double y) {
		Point2D.Double p = this.getPoint(x, y);
		int i = this.getI(p.x);
		int j = this.getJ(p.y);
		if (i >= this.getWidth() || i < 0 || j >= this.getHeight() || j < 0)
			return null;
		return this.getPixel(i, j);
	}
	
	
	public Point2D.Double getPoint(double x, double y) {
		
		if (this.currMatrix == null) {
			this.currMatrix = new Matrix();
		}
		
		Matrix m2 = new Matrix(3, 1);
		m2.setCell(0, 0, x);
		m2.setCell(1, 0, y);
		m2.setCell(2, 0, 1);
		
		Matrix m3 = Matrix.product(this.currMatrix, m2);
		//System.out.println("MTRX COL = " +m3.columns);
		//System.out.println("MTRX ROW = " +m3.rows);
		return new Point2D.Double(m3.getCell(0, 0), m3.getCell(1, 0));
	}
	
	public void translate (double dx, double dy) {
		
		if (this.currMatrix == null) {
			this.currMatrix = new Matrix();
		}
		
		Matrix tm = new Matrix();
		tm.setCell(0, 2, dx);
		tm.setCell(1, 2, dy);
		
		this.currMatrix = Matrix.product(this.currMatrix, tm);
	}
	
	public void rotate (double angle) {
		
		if (this.currMatrix == null) {
			this.currMatrix = new Matrix();
		}
		
		double rad = Math.toRadians(angle);
		
		Matrix m = new Matrix();
		m.setCell(0, 0, Math.cos(rad));
		m.setCell(0, 1, -Math.sin(rad));
		m.setCell(1, 0, Math.sin(rad));
		m.setCell(1, 1, Math.cos(rad));
		
		this.currMatrix = Matrix.product(this.currMatrix, m);
	}
	
	public void scale (double sx, double sy) {
		
		if (this.currMatrix == null) {
			this.currMatrix = new Matrix();
		}
		
		Matrix m = new Matrix();
		m.setCell(0, 0,sx);
		m.setCell(1, 1, sy);
		
		this.currMatrix = Matrix.product(this.currMatrix, m);
	}
	
	public void loadIdentity() {
		this.matrixStack = new Stack<Matrix>();
		this.matrixStack.add(new Matrix());
	}
	
	public void pushMatrix() {
		if (this.matrixStack == null)
			this.matrixStack = new Stack<Matrix>();
		
		if (this.currMatrix == null)
			this.matrixStack.add(new Matrix());
		else
			this.matrixStack.add(this.currMatrix);
	}
	
	public void popMatrix() {
		if (this.matrixStack == null || this.matrixStack.size() == 0) {
			this.matrixStack = new Stack<Matrix>();
			this.currMatrix = new Matrix();
		}
		else {
			this.currMatrix = this.matrixStack.lastElement();
			this.matrixStack.removeElementAt(this.matrixStack.size() - 1);
		}
	}
	
	
	
	
	
	
	
	
	
	private void quicksort(Vector<Edge> a, int left, int right) {
		if (right <= left)
			return;
		int i = partition(a, left, right);
		quicksort(a, left, i - 1);
		quicksort(a, i + 1, right);
	}

	private int partition(Vector<Edge> a, int left, int right) {
		int i = left;
		int j = right;
		while (true) {
			while (a.get(i).ymin < a.get(right).ymin)
				i++;
			while (less(a.get(right).ymin, a.get(--j).ymin))
				if (j == left)
					break;
			if (i >= j)
				break;
			exch(a, i, j);
		}
		exch(a, i, right);
		return i;
	}

	private boolean less(double x, double y) {
		return (x < y);
	}

	private void exch(Vector<Edge> a, int i, int j) {
		Edge swapi = new Edge(a.get(i).ymax, a.get(i).ymin, a.get(i).x, a.get(i).lm, a.get(i).active);
		Edge swapj = new Edge(a.get(j).ymax, a.get(j).ymin, a.get(j).x, a.get(j).lm, a.get(j).active);
		a.remove(i);  a.add(i, swapj);
		a.remove(j);  a.add(j, swapi);
		//a.get(j) = swap;
	}

	
	private void quicksorti(Vector<Integer> a, int left, int right) {
		if (right <= left)
			return;
		int i = partitioni(a, left, right);
		quicksorti(a, left, i - 1);
		quicksorti(a, i + 1, right);
	}

	private int partitioni(Vector<Integer> a, int left, int right) {
		int i = left;
		int j = right;
		while (true) {
			while (a.get(i) < a.get(right))
				i++;
			while (a.get(right) < a.get(--j))
				if (j == left)
					break;
			if (i >= j)
				break;
			exchi(a, i, j);
		}
		exchi(a, i, right);
		return i;
	}

	private void exchi(Vector<Integer> a, int i, int j) {
		int swapi = a.get(i);
		int swapj = a.get(j);
		a.remove(i);  a.add(i, swapj);
		a.remove(j);  a.add(j, swapi);
		//a.get(j) = swap;
	}
	
}
