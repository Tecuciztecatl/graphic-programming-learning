import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Vector;

import Raster2d.Animation;


public class Animooting extends Animation{

	public int frames = 11;
	public int cframe;
	
	public Animooting() {
		super(400, 400);
		// TODO Auto-generated constructor stub
	}
	
	public Animooting(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Animooting();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.cframe = 0;
	}

	@Override
	public void display() {
		// TODO Auto-generated method stub
		super.start();
		animoot();

		//super.drawCircle(100, Color.BLACK, Color.GREEN);
		//super.drawPolygon(150, 8, Color.BLACK, Color.GRAY);
	}

	public void animoot () {
		if (cframe == 0) {
			super.drawCircle(100, Color.BLACK, Color.GREEN);
		}
		else if (cframe == 1) {
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
			super.drawCircle(100, Color.BLACK, pattern);
		}
		else if (cframe == 2) {
			super.drawRectangle(150, 100, Color.BLACK, Color.BLUE);
		}
		else if (cframe == 3) {
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
			super.drawRectangle(150, 100, Color.BLACK, pattern);
		}
		else if (cframe == 4) {
			super.drawPolygon(150, 8, Color.BLACK, Color.GRAY);
		}
		else if (cframe == 5) {
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
			super.drawPolygon(100, 4, Color.BLACK, pattern);
		}
		else if (cframe == 6) {
			Point2D.Double[] p = {new Point2D.Double(0, 0),
					new Point2D.Double(33, 33),
					new Point2D.Double(60, 180),
					new Point2D.Double(-60, 100)};
			super.lineLoop(p, Color.BLACK, Color.CYAN);
		}
		else if (cframe == 7) {
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
			Point2D.Double[] p = {new Point2D.Double(0, 0),
					new Point2D.Double(33, 33),
					new Point2D.Double(60, 180),
					new Point2D.Double(-60, 100)};
			super.lineLoop(p, Color.BLACK, pattern);
		} 
		else if (cframe == 8) {
			super.drawPolygon(150, 7, Color.BLACK);
			super.drawPolygon(190, 3, Color.BLACK);
			super.floodFill(new Point2D.Double(0, 0), Color.MAGENTA, Color.orange);
		} 
		else if (cframe == 9) {
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
			super.drawPolygon(150, 7, Color.BLACK);
			super.drawPolygon(190, 3, Color.BLACK);
			super.floodFill(new Point2D.Double(0, 0), Color.MAGENTA, pattern);
			
		} 
		else if (cframe >= 10) {
			this.cframe = -1;
		} 
		this.cframe++;
		super.setDelay(500);
	}
	
}
