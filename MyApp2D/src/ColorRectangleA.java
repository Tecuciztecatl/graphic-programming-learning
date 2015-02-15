import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Vector;

import Raster2d.Animation;


public class ColorRectangleA extends Animation {

	public int maxwidth = 600;
	public int maxheight = 300;
	public int currwidth = 600;
	public int currheight = 300;
	public int incx = -10;
	public int incy = -10;
	public Color c1;
	public Color c2;
	public Color c3;
	public Color c4;
	public double radius;
	public double cradius;
	public double craddec;
	
	
	public ColorRectangleA(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub

	}
	
	public ColorRectangleA() {
		super(800, 600);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.setClearColor(Color.WHITE);
		this.c1 = new Color(Color.WHITE.getRGB());
		this.c2 = new Color(Color.GREEN.getRGB());
		this.c3 = new Color(Color.RED.getRGB());
		this.c4 = new Color(Color.BLUE.getRGB());
		//this.currwidth = super.getWidth() - super.getInsets().left;
		//this.currheight = super.getHeight() - super.getInsets().top;
		this.incx = -10;
		this.incy = -10;
		this.maxwidth = 600;
		this.maxheight = 300;
		this.currwidth = 600;
		this.currheight = 300;

		if (super.getWidth() < super.getHeight())
			this.radius = super.getWidth() - (super.getWidth()/8);
		else
			this.radius = super.getHeight() - (super.getHeight()/8);
		this.cradius = this.radius;
		this.craddec = 0;
	}

	public void rectAnim() {

		//Checking that the width of the color area is valid
		if (this.currwidth <= 0) {
			if (this.incx < 0)
				this.incx = 5;
			//Changing the colors
			Color t = new Color(this.c1.getRGB());
			this.c1 = new Color(this.c2.getRGB());
			this.c2 = new Color(t.getRGB());
			t = new Color(this.c3.getRGB());
			this.c3 = new Color(this.c4.getRGB());
			this.c4 = new Color(t.getRGB());
		}
		else if (this.currwidth >= this.maxwidth)
			if (this.incx > 0)
				this.incx = -5;
		//Check if the height is valid
		if (this.currheight <= 0) {
			if (this.incy < 0)
				this.incy = 5;
			Color t = new Color(this.c1.getRGB());
			this.c1 = new Color(this.c3.getRGB());
			this.c3 = new Color(t.getRGB());
			t = new Color(this.c2.getRGB());
			this.c2 = new Color(this.c4.getRGB());
			this.c4 = new Color(t.getRGB());
		}
		else if (this.currheight >= this.maxheight)
			if (this.incy > 0)
				this.incy = -5;
		
		//Create the point for the color area according to the width and height
		Point2D t3 = new Point2D.Double(-(this.currwidth/2), -(this.currheight/2));
		Point2D t4 = new Point2D.Double((this.currwidth/2), (this.currheight/2));
		super.colorArea(t3, t4, c1, c2, c3, c4);
		
		//increment the width and height according to the increments
		this.currwidth += this.incx;
		this.currheight += this.incy;
		super.setDelay(60);
	}
	
	public void lineSeq() {
		
		//Al radio le restamos el decreciente de radio para simular que avanza
		this.cradius -= this.craddec;

		//Si después de la resta el radio es menor o igual que radio/2
		//reiniciamos el radio
		if (this.cradius <= this.radius /2)
			this.cradius = this.radius;
		int rad = (int) (this.cradius);
		while (rad > 2) {
			super.drawPolygon(rad*2, 4, Color.pink);
			super.drawRectangle(rad*2, rad*2);
			super.drawCircleMidpoint(rad, Color.blue);
			super.drawPolygon(rad, 4, Color.pink);
			rad = rad /2;
		}
		this.craddec = 3;
		//Sin delay por que los draw son muy lentos, lo alenta lo suficiente!
		//super.setDelay(10);
	}
	
	public void fillIT() {
		super.drawCirclePoli(60, Color.BLACK);
		super.drawPolygon(70, 8, Color.BLACK);
		super.setPixel(super.getI(10), super.getJ(10), Color.BLACK);
		//super.borderFill(super.getI(50), super.getJ(50), Color.BLACK, Color.LIGHT_GRAY);
		super.borderFill(super.getI(30), super.getJ(0), Color.BLACK, Color.CYAN);
		super.floodFill(super.getI(30), super.getJ(0), Color.CYAN, Color.RED);
	}
	
	@Override
	public void display() {
		// TODO Auto-generated method stub
		//this.rectAnim();
		//this.lineSeq();
		//super.start();
		//this.fillIT();
		Vector<Point2D.Double> p = new Vector<Point2D.Double>();
		p.add(new Point2D.Double(0, 0));
		p.add(new Point2D.Double(33, 33));
		p.add(new Point2D.Double(60, 180));
		p.add(new Point2D.Double(-60, 100));
		//super.lineLoop(p, Color.BLACK, Color.BLUE);
		/*super.setPixelColor(Color.GREEN);
		super.drawElipsePoli(100, 200);
		super.drawElipsePoli(200, 100);
		super.setPixelColor(Color.BLACK);
		super.drawCircleMidpoint(80);
		super.drawCircleMidpoint(20);
		super.drawCircleMidpoint(200);
		Color[][] pattern = {{Color.BLUE, Color.BLACK}, 
							 {Color.BLACK, Color.RED}, 
							 {Color.BLACK, Color.RED}};
*/
		Color[][] pattern = {{Color.BLACK, Color.RED, Color.BLUE, Color.WHITE, Color.GRAY}, 
							{Color.BLACK, Color.RED, Color.BLUE, Color.WHITE, Color.GRAY}, 
							{Color.BLACK, Color.RED, Color.BLUE, Color.WHITE, Color.GRAY}, 
							{Color.BLACK, Color.RED, Color.BLUE, Color.WHITE, Color.GRAY}, 
							{Color.BLACK, Color.RED, Color.BLUE, Color.WHITE, Color.GRAY}, 
							{Color.BLACK, Color.RED, Color.BLUE, Color.WHITE, Color.GRAY}, 
							{Color.BLACK, Color.RED, Color.BLUE, Color.WHITE, Color.GRAY}, 
							{Color.BLACK, Color.RED, Color.BLUE, Color.WHITE, Color.GRAY}, 
							{Color.BLACK, Color.RED, Color.BLUE, Color.WHITE, Color.GRAY}, 
							{Color.BLACK, Color.RED, Color.BLUE, Color.WHITE, Color.GRAY}, 
							{Color.BLACK, Color.RED, Color.BLUE, Color.WHITE, Color.GRAY}, 
							{Color.BLACK, Color.RED, Color.BLUE, Color.WHITE, Color.GRAY}, 
							{Color.BLACK, Color.RED, Color.BLUE, Color.WHITE, Color.GRAY}, 
							{Color.BLACK, Color.RED, Color.BLUE, Color.WHITE, Color.GRAY},
							{Color.BLACK, Color.RED, Color.BLUE, Color.WHITE, Color.GRAY}, 
							{Color.BLACK, Color.RED, Color.BLUE, Color.WHITE, Color.GRAY}
				};
		Color[][] pattern2 = {{Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK},
							  {Color.BLACK, Color.RED, Color.RED, Color.RED, Color.BLACK},
							  {Color.BLACK, Color.RED, Color.RED, Color.RED, Color.BLACK},
							  {Color.BLACK, Color.RED, Color.RED, Color.RED, Color.BLACK},
							  {Color.BLACK, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLACK},
							  {Color.BLACK, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLACK},
							  {Color.BLACK, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLACK},
							  {Color.BLACK, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLACK},
							  {Color.BLACK, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLACK},
							  {Color.BLACK, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLACK},
							  {Color.BLACK, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLACK},
							  {Color.BLACK, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLACK},
							  {Color.BLACK, Color.RED, Color.RED, Color.RED, Color.BLACK},
							  {Color.BLACK, Color.RED, Color.RED, Color.RED, Color.BLACK},
							  {Color.BLACK, Color.RED, Color.RED, Color.RED, Color.BLACK},
							  {Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK}};
	/*	
		super.borderFill(new Point2D.Double(super.getI(0), super.getJ(0)), Color.BLACK, pattern2);

		super.floodFill(new Point2D.Double(0, 0), Color.WHITE, pattern2);
		*/
		super.lineLoop(p, Color.BLACK, pattern2);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ColorRectangleA();
	}

}
