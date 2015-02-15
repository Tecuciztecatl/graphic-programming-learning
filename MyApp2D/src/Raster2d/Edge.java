package Raster2d;

public class Edge {
	public double ymax;
	public double ymin;
	public double x;
	public double lm;
	public boolean active;

	public Edge () {}
	
	public Edge (double ymax, double ymin, double x, double lm, boolean active) {
		this.ymax = ymax;
		this.ymin = ymin;
		this.x = x;
		this.lm = lm;
		this.active = active;
	}
	
	public Edge (double x1, double y1, double x2, double y2) {
		if (y1 < y2) {
			this.ymax = y2;
			this.x = x1;
			this.ymin = y1;
			this.lm = (x2 - x1)/(y2 - y1);
		}
		else {
			this.ymax = y1;
			this.x = x2;
			this.ymin = y2;
			this.lm = (x1 - x2)/(y1 - y2);
		}
	}
	
	public void setX (double x) {
		this.x = x;
	}
	
}
