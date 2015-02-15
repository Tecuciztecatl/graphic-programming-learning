import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Vector;

import Raster2d.Animation;


public class SolarSystem extends Animation{

	public Vector<Point2D.Double> points;
	public int da =0;
	public int dg=0;
	public double ddg = 0;
	public int dr=0;
	
	public SolarSystem() {
		super(800, 800);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new SolarSystem();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		//super.start();
		
		points = new Vector<Point2D.Double>();
		
		double x30 = 150 * Math.cos(Math.toRadians(30)); 
		double y30 = 150 * Math.sin(Math.toRadians(30));
		
		points.add(new Point2D.Double(x30, y30));
		
		double x150 = 150 * Math.cos(Math.toRadians(150)); 
		double y150 = 150 * Math.sin(Math.toRadians(150));
		
		points.add(new Point2D.Double(x150, y150));
		
		double x270 = 150 * Math.cos(Math.toRadians(270)); 
		double y270 = 150 * Math.sin(Math.toRadians(270));
		
		points.add(new Point2D.Double(x270, y270));
	}
	
	public void SG (int lvl, Color c) {
		if (lvl == 6)
			return;
		
		super.lineLoop(points, c, c);
		
		//Save the current state!
		super.pushMatrix();
		super.translate(150*Math.cos(Math.toRadians(90)), 
						Math.sin(Math.toRadians(90))*150);		
		super.scale(0.5, 0.5);

		if (c.getRGB() == Color.YELLOW.getRGB())
			this.SG(lvl+1, Color.BLUE);
		else
			this.SG(lvl+1, Color.YELLOW);
		
		
		//Return to previous state then save it!
		super.popMatrix();
		//Save current State
		super.pushMatrix();
		super.translate(150*Math.cos(Math.toRadians(210)), 
						Math.sin(Math.toRadians(210))*150);
		super.scale(0.5, 0.5);
		
		if (c.getRGB() == Color.YELLOW.getRGB())
			this.SG(lvl+1, Color.BLUE);
		else
			this.SG(lvl+1, Color.YELLOW);

		
		//Return to previous state
		super.popMatrix();
		super.translate(150*Math.cos(Math.toRadians(330)), 
						Math.sin(Math.toRadians(330))*150);
		super.scale(0.5, 0.5);
		
		if (c.getRGB() == Color.YELLOW.getRGB())
			this.SG(lvl+1, Color.BLUE);
		else
			this.SG(lvl+1, Color.YELLOW);
	}
	

	public void SS () {

		if(da > 360) da = 0;
		if(dr > 360) dr = 0;
		if(dg > 360) dg = 0;
		if(ddg > 360) ddg = 0;
		
		Color [][] sp={ {Color.YELLOW, Color.YELLOW, Color.ORANGE, Color.ORANGE, Color.YELLOW},
							{Color.YELLOW, Color.ORANGE, Color.ORANGE, Color.YELLOW, Color.YELLOW},
							{Color.YELLOW, Color.ORANGE, Color.ORANGE, Color.ORANGE, Color.YELLOW}
							};
		super.drawCircle(50, Color.ORANGE, sp);
		//Save state! centre state!
		super.pushMatrix();
		
		//Set the angle of the planet!
		super.rotate(da);
		//Set de distance of the planet from the sun!
		super.translate(200, 0);
		//Draw the planet! ;)
		super.drawPolygon(25, 45, new Color(147, 215, 224), Color.BLUE);
		//Set the angle of the moon, according to the planet!
		super.rotate(-dg);
		//Set the distance of the moon from the planet!
		super.translate(30,-30);
		//Draw the moon...
		super.drawPolygon(8, 20, Color.LIGHT_GRAY, Color.GRAY);
		
		//We then return to the sun-state (AKA the centre state)
		super.popMatrix();
		//Then we save that state! grrr!
		super.pushMatrix();
		
		//Set the red planet angle =/
		super.rotate(dr);
		//Set the red planet distance from the sun!
		super.translate(-100,0);
		//Draw the red planet!
		super.drawPolygon(17, 20, new Color(244, 64, 22), new Color(244, 64, 22));
		
		//We then return to the original state! the sun-state!
		super.popMatrix();
		
		da += 1;
		dg += 15;
		ddg += 1.7;
		dr = (int)ddg;
	}
	
	public void Tree() {
		super.translate(0, -120);
		this.SGT(0, new Color(130, 80, 30), new Color(50, 150, 70));
	}
	
	public double colorP(int lvl) {
		return (lvl *100) / 10;
	}
	
	public Color colourP(int lvl, Color c1, Color c2) {
		//double perc = this.colorP(lvl);
		if (lvl == 0) {
			return c1;
		}
		else if (lvl == 10) {
			return c2;
		}
		else {
			double perc = lvl /10;
			double dr = (c2.getRed() - c1.getRed()) / 10;
			double dg = (c2.getGreen() - c1.getGreen()) / 10;
			double db = (c2.getBlue() - c1.getBlue()) / 10;
			/*
			int r = (int) ((c2.getRed() * perc) + (c1.getRed() * (1-perc)));
			int g = (int) ((c2.getGreen() * perc) + (c1.getGreen() * (1-perc)));
			int b = (int) ((c2.getBlue() * perc) + (c1.getBlue() * (1-perc)));
			*/
			int r = (int) (c1.getRed() + (dr * lvl));
			int g = (int) (c1.getGreen() + (dg * lvl));
			int b = (int) (c1.getBlue() + (db * lvl));
			if (r > 255) r = 255;
			if (g > 255) g = 255;
			if (b > 255) b = 255;
			if (r < 0) r = 0;
			if (g < 0) g = 0;
			if (b < 0) b = 0;
			return new Color(r, g, b);
		}
	}
	
	public void SGT (int lvl, Color c1, Color c2) {
		if (lvl == 11)
			return;
		
		//super.pushMatrix();
		
		
		Color c = this.colourP(lvl, c1, c2);
		
		super.drawRectangle(20, 120, c, c);
		
		super.scale(0.7, 0.8);

		super.translate(0, 90);
		
		//Save the current state!
		super.pushMatrix();
		
		super.translate(-25, 0);
		
		super.rotate(30);
		
		this.SGT(lvl + 1, c1, c2);
		
		//Return to previous state then save it!
		super.popMatrix();
		//Save current State
		//super.pushMatrix();
		
		
		super.translate(25, 0);
		
		super.rotate(-30);
		
		this.SGT(lvl + 1, c1, c2);
		
	}
	
	@Override
	public void display() {
		// TODO Auto-generated method stub
//		this.SS(); //Solar System
		//this.Tree(); //Tree
//		this.SG(0, Color.YELLOW); //Triangles (tri-force like)
//		Color [][] sunPat={ {new Color(207, 181, 48),
//			new Color(207, 200, 32),
//			new Color(245, 250, 20),		
//			new Color(207, 190, 55),
//			new Color(170, 160, 34)},
//			{new Color(190, 176, 48),
//			new Color(207, 191, 47),
//			new Color(150, 141, 40),
//			new Color(189, 198, 23),
//			new Color(207, 208, 67)},
//			{new Color(178, 151, 58),
//			new Color(249, 208, 70),
//			new Color(207, 200, 90),
//			new Color(230, 220, 47),
//			new Color(234, 229, 49)},};
//		super.drawCircle(50, Color.BLACK, Color.BLUE);
//		super.rotate(25);
//		super.translate(50, 50);
//		super.drawPolygon(50, 25, Color.BLACK, Color.black);
//		
//		super.rotate(45);
//		super.translate(50, 50);
//
//		super.drawCirclePoli(50, Color.BLACK);
//		super.circleAA(180, Color.GREEN);
		
	}

}
