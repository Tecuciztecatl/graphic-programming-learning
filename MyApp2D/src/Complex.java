
public class Complex {

	private double r;
	private double i;
	
	public Complex() {
		this.r = 0.0;
		this.i = 0.0;
	}
	
	public Complex(double r, double i) {
		this.r = r;
		this.i = i;
	}
	
	public void setR(double r) {
		this.r = r;
	}
	public double getR() {
		return r;
	}
	
	public void setI(double i) {
		this.i = i;
	}
	public double getI() {
		return i;
	}
	
	/**
	 * This method calculates the magnitude of the complex number
	 * @return double, the magnitude of the complex number
	 */
	public double magnitude() {
		return Math.sqrt((Math.pow(this.getR(), 2)+Math.pow(this.getI(), 2)));
	}

	/**
	 * This method calculates the magnitude of the specified complex number
	 * @return double, the magnitude of the complex number
	 */
	public static double magnitude(Complex c) {
		return Math.sqrt((Math.pow(c.getR(), 2)+Math.pow(c.getI(), 2)));
	}

	/**
	 * This method calculates the square of the complex number
	 * @return Complex, the square of the complex number
	 */
	public Complex square() {
		return new Complex(Math.pow(this.r, 2)-Math.pow(this.i,2), 2 * this.r * this.i);
	}
	

	/**
	 * This method calculates the square of the complex number
	 * @return Complex, the square of the complex number
	 */
	public static Complex square(Complex c) {
		return new Complex(Math.pow(c.r, 2)-Math.pow(c.i,2), 2 * c.r * c.i);
	}
	

	/**
	 * This method calculates the sum of the 2 complex numbers
	 * @return Complex, the sum of the 2 complex numbers
	 */
	public static Complex sum(Complex c1, Complex c2) {
		return new Complex(c1.getR() + c2.getR(), c1.getI() + c2.getI());
	}
	
}
