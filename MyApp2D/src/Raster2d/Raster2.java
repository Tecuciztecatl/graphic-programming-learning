package Raster2d;


public class Raster2 extends Raster2d{


	
	public Raster2(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}
	
	public Raster2() {
		super(400, 400);
		// TODO Auto-generated constructor stub
		//super.setCentre(super.getWidth()/2, (super.getHeight()/2) + (super.getHeight()/3));
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.setCentre(200, (300));
	}

	@Override
	public void display() {
		// TODO Auto-generated method stub
		//super.setCentre(super.getWidth()/2, (super.getHeight()/2) + (super.getHeight()/3));
		double xi = -50;
		double xf = 50;
		double dx = .01;
		
		for (double x = xi; x <= xf; x+=dx) {
			super.setPoint(x, Math.pow(x, 2));
		}
		
		double pi = -Math.PI * 4;
		double pf = Math.PI * 4;
		double dp = .001;
		for (double p = pi; p <= pf; p+=dp) {
			super.setPoint(p*10, Math.sin(p)*100);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Raster2();
	}

	
}
