import java.awt.Color;

import Raster2d.Raster2d;


public class Mandelbrot extends Raster2d {

	private final double rmin = -2.0;
	private final double rmax = 0.5;
	private final double imin = -1.25;
	private final double imax = 1.25;
	private double delta;
	private final double N = 64;
	
	public Mandelbrot(int width, int height) {
		super(width, height);
	}
	
	public Mandelbrot() {
		super(400, 400);
	}

	/**
	 * This method does the Mandelbrot set in B&W
	 */
	public void mandelbrotg() {
		this.delta = (this.rmax - this.rmin)/super.getWidth();
		Complex z = new Complex(this.rmin, this.imin);
		int gray = 0;
		
		for (int i = 0; i<super.getWidth(); i++, z.setR(z.getR()+this.delta)) {
			z.setI(this.imin);
			
			for (int j = 0; j<super.getHeight(); j++, z.setI(z.getI()+this.delta)) {
				int count = 0;
				Complex x = new Complex();
				
				while (x.magnitude() <= 2.0 && count < this.N) {
					x = Complex.sum(x.square(), z);
					count++;
				}
				
				if (x.magnitude() <= 2.0) gray = 0;
				else	gray = (int) (255 * count / this.N);
				
				super.setPixelColor(new Color(gray, gray, gray));
				super.setPixel(i, j);
			}
		}
	}

	/**
	 * This method calculates by the rule of 3 which colour should be
	 * @param min
	 * @param max
	 * @param where
	 * @return int (colour R, G, or B)
	 */
	public int calcApprox (int min, int max, int where) {
		if (max <= min)
			return -1;
		int m0t = max-min;
		return (int) (((where * m0t)/this.N) + min);
	}

	/**
	 * This method does the Mandelbrot set in Colours
	 */
	public void mandelbrotc() {
		this.delta = (this.rmax - this.rmin)/super.getWidth();
		Complex z = new Complex(this.rmin, this.imin);
		//int gray = 0;
		Color c;
		
		for (int i = 0; i<super.getWidth(); i++, z.setR(z.getR()+this.delta)) {
			z.setI(this.imin);
			
			for (int j = 0; j<super.getHeight(); j++, z.setI(z.getI()+this.delta)) {
				int count = 0;
				Complex x = new Complex();
				
				while (x.magnitude() <= 2.0 && count < this.N) {
					x = Complex.sum(x.square(), z);
					count++;
				}
				
				if (x.magnitude() <= 2.0) c = new Color(60, 20, 40);
				else {
					//This is the part in which the colour is set depending on
					// how near the count is to N
					if (count == this.N)
						c = new Color(120,240,170);
					else if(count == 0)
						c = new Color(100,0,50);
					else
						c = new Color(this.calcApprox(100, 120, count), 
										this.calcApprox(0, 240, count), 
										this.calcApprox(50, 170, count));
				}
				
				super.setPixelColor(c);
				super.setPixel(i, j);
			}
		}
	}
	

	@Override
	public void init() {
		//this.delta = (this.rmax - this.rmin)/super.getWidth();
		super.setClearColor(Color.BLACK);
	}

	@Override
	public void display() {
		mandelbrotc();
	}

	public static void main(String[] args) {
		new Mandelbrot();
	}
	
}
