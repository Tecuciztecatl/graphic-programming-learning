import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Stack;

import Raster2d.Animation;


public class Aliasing extends Animation {

	public Stack<Color> cstack;
	
	public Aliasing(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		cstack = new Stack<Color>();
	}

	@Override
	public void display() {
		// TODO Auto-generated method stub
		
		//Save the original state!
		super.pushMatrix();
		
		//Move to the right
		super.translate(190, 0);
		//Save the right state!
		super.pushMatrix();
		//Scale for an horizontal oval
		super.scale(1.1, 0.8);
		//Draw the lines while saving each of its colors!
		for (int i = 0; i < 360; i+=5) {
			Point2D t = new Point2D.Double();	
			t.setLocation(170 * Math.cos(Math.toRadians(i)),
							170 * Math.sin(Math.toRadians(i)));
			this.cstack.add(super.randomColor());
			super.lineDDA(0, 0, t.getX(), t.getY(), this.cstack.lastElement());
		}
		//Draw the circle
		super.drawCirclePoli(170, new Color(240, 210, 250));
		//Pop to right state!
		super.popMatrix();
		//Scale for vertical oval and draw the circle
		super.scale(0.8, 1.1);
		super.drawCirclePoli(170, new Color(240, 210, 250));
		
		//Pop por original state!
		super.popMatrix();
		
		//Same steps as above but with Anti-Aliasing techniques
		super.translate(-190, 0);
		super.pushMatrix();
		super.scale(1.1, 0.8);
		//here we use the colors saved from the previously drawn lines
		for (int i = 0, c = 0; i < 360; i+=5, c++) {
			Point2D t = new Point2D.Double();	
			t.setLocation(170 * Math.cos(Math.toRadians(i)),
							170 * Math.sin(Math.toRadians(i)));
			//this.cstack.add(super.randomColor());
			super.lineAA(0, 0, t.getX(), t.getY(), this.cstack.elementAt(c));
		}
		super.circleAA(170, new Color(240, 210, 250));
		super.popMatrix();
		super.scale(0.8, 1.1);
		super.circleAA(170, new Color(240, 210, 250));
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Aliasing(800, 800);
	}

}
