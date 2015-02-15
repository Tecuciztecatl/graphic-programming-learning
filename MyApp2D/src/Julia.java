import java.awt.Color;

import Raster2d.Raster2d;


public class Julia extends Raster2d {

	private final double rmin = -1.2;
	private final double rmax = 1.2;
	private final double imin = -1.2;
	private final double imax = 1.2;
	private double delta;
	private final double N = 128;
	
	public Julia(int width, int height) {
		super(width, height);
	}
	
	public Julia() {
		super(400, 400);
	}
	
	/**
	 * This method does the Julia set in B&W
	 */
	public void Juliag() {
		this.delta = (this.rmax - this.rmin)/super.getWidth();
		Complex z = new Complex(this.rmin, this.imin);
		Complex x0 = new Complex(-0.74543, 0.11301);
		int gray = 0;
		
		for (int i = 0; i<super.getWidth(); i++, z.setR(z.getR()+this.delta)) {
			z.setI(this.imin);
			
			for (int j = 0; j<super.getHeight(); j++, z.setI(z.getI()+this.delta)) {
				int count = 0;
				Complex x = new Complex(z.getR(), z.getI());
				
				while (x.magnitude() <= 2.0 && count < this.N) {
					x = Complex.sum(x.square(), x0);
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
	 * This method does the Julia set in Colours
	 */
	public void Juliac() {
		this.delta = (this.rmax - this.rmin)/super.getWidth();
		Color c;
		Complex z = new Complex(this.rmin, this.imin);
		Complex x0 = new Complex(-0.74543, 0.11301);
		
		for (int i = 0; i<super.getWidth(); i++, z.setR(z.getR()+this.delta)) {
			z.setI(this.imin);
			
			for (int j = 0; j<super.getHeight(); j++, z.setI(z.getI()+this.delta)) {
				int count = 0;
				Complex x = new Complex(z.getR(), z.getI());
				
				while (x.magnitude() <= 2.0 && count < this.N) {
					x = Complex.sum(x.square(), x0);
					count++;
				}
				
				if (x.magnitude() <= 2.0) c = new Color(10, 40, 20);
				else {
					//This is the part in which the colour is set depending on
					// how near the count is to N
					if (count == this.N)
						c = new Color(230,250,170);
					else if(count == 0)
						c = new Color(60,30,30);
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
		Juliac();
	}

	public static void main(String[] args) {
		new Julia();
	}

}