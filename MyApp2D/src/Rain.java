import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Vector;

import Raster2d.Animation;


public class Rain extends Animation {

	private Vector<rainpos> drops;
	private Vector<Integer> dropsx;
	private Vector<Integer> dropsy;
	private ArrayList<Integer> colors;
	private double ndrops;
	private final int maxdrops = 5000;
	private final int dropsize = 6;
	private final int dropspeed = 30;
	private double r = 190;
	private double g = 240;
	private double b = 240;
	private long currtime;
	private long time = 1000;
	private boolean time2 = false;
	
	public Rain(int width, int height) {
		super(width, height);
	}
	
	public Rain() {
		super(600, 300);
	}

	public void drawRain(int width, int height) {
		if ((int)this.ndrops > this.dropsx.size()) {
			dropsx.add((int) ((width-1) * Math.random()));
			dropsy.add(0);
			colors.add(200 + (int) (55 * Math.random()));
		}
		
		Color c;
		int dposy;
		int dposx;
		int col;
		for(int i = 0; i < this.dropsx.size(); i++) {
			col = this.colors.get(i);
			c = new Color(col, col, col);
			dposx = this.dropsx.elementAt(i);
			dposy = this.dropsy.elementAt(i);
			this.dropsy.removeElementAt(i);
			for (int j = 0; j <= this.dropsize; j++) {
				if (dposy+j < height-1) {
					//System.out.println("i= " + i + " posy= "+dposy+ j+ " posx= "+dposx);
					super.setPixel(dposx, dposy + j, c);
				}
				else {
					break;
				}
			}
			this.dropsy.insertElementAt(dposy+this.dropspeed, i);
		}
		if (this.ndrops < this.maxdrops)
			this.ndrops += this.ndrops * .35;
		if (this.g > 140 && this.b > 140) {
			this.r -= this.r * .01;
			this.g -= this.g * .01;
			this.b -= this.b * .01;
			super.setClearColor(new Color((int)this.r, (int)this.g, (int)this.b));
		}
	}
	
	public void drawRainC(int width, int height) {
		if ((int)this.ndrops > this.drops.size()) {
			int s = 200 + (int) (55 * Math.random());
			int sp = (int) (55*Math.random());
			if (sp < 15)
				sp = 15;
			Color c = new Color(s, s, s);
			drops.add(new rainpos((int) ((super.getWidth()-1) * Math.random()), 0, c, sp));
		}
		
		rainpos rp;
		for(int i = 0; i < this.drops.size(); i++) {
			rp = this.drops.get(i);
			if (rp.y + this.dropsize < height-1) {
				for (int j = 0; j <= this.dropsize; j++) {
						//System.out.println("i= " + i + " posy= "+rp.y+ j+ " posx= "+rp.x);
						super.setPixel(rp.x, rp.y + j, rp.c);
				}
			}
			else {
				this.drops.get(i).y = 0;
			}
			this.drops.get(i).y +=this.drops.get(i).dropspeed;
		}
		if (this.ndrops < this.maxdrops)
			this.ndrops += this.ndrops * .35;
		if (this.g > 140 && this.b > 140) {
			this.r -= this.r * .01;
			this.g -= this.g * .01;
			this.b -= this.b * .01;
			super.setClearColor(new Color((int)this.r, (int)this.g, (int)this.b));
		}
	}
	
	public void drawRains(int width, int height) {
		/*if ((int)this.ndrops > this.dropsx.size()) {
			dropsx.add((int) ((width-1) * Math.random()));
			dropsy.add(0);
			colors.add(200 + (int) (55 * Math.random()));
		}*/
		Color c;
		int dposy;
		int dposx;
		int col;
		/*if (time2) {
			time2 = false;
			super.setClearColor(new Color((int)this.r, (int)this.g, (int)this.b));
		}
		if (System.currentTimeMillis() - currtime > time) {
			System.out.println("HMM");
			super.setClearColor(Color.WHITE);
			time2 = true;
			currtime = System.currentTimeMillis();
			//return;
		}*/
		for(int i = 0; i < (int)this.ndrops; i++) {
			col = 200 + (int) (55 * Math.random());
			c = new Color(col, col, col);
			dposx = (int) (width * Math.random());
			dposy = (int) (height * Math.random());
			//this.dropsy.removeElementAt(i);
			for (int j = 0; j <= this.dropsize; j++) {
				if (dposy+j < height-1) {
					//System.out.println("i= " + i + " posy= "+dposy+ j+ " posx= "+dposx);
					super.setPixel(dposx, dposy + j, c);
				}
				else {
					break;
				}
			}
			//this.dropsy.insertElementAt(dposy+this.dropspeed, i);
		}
		if (this.ndrops < this.maxdrops)
			this.ndrops += this.ndrops * .05;
		if (this.g > 120 && this.b > 120) {
			this.r -= this.r * .01;
			this.g -= this.g * .01;
			this.b -= this.b * .01;
			super.setClearColor(new Color((int)this.r, (int)this.g, (int)this.b));
		}
	}
	
	@Override
	public void init() {
		//super.setClearColor(new Color(190, 240, 240));
		super.setTitle("DAAAAANCING IN THE RAIN!");
		super.setClearColor(Color.BLACK);
		super.setDelay(100);
		drops = new Vector<rainpos>();
		dropsx = new Vector<Integer>();
		dropsy = new Vector<Integer>();
		colors = new ArrayList<Integer>();
		int s = 200 + (int) (55 * Math.random());
		Color c = new Color(s, s, s);
		int x = (int) ((super.getWidth()-1) * Math.random());
		int sp = (int) (55*Math.random());
		if (sp < 25)
			sp = 25;
		drops.add(new rainpos(x, 0, c, sp));
		dropsx.add((int) ((super.getWidth()-1) * Math.random()));
		dropsy.add(0);
		colors.add(200 + (int) (55 * Math.random()));
		ndrops = 1;
		currtime = System.currentTimeMillis();
		super.start();
	}

	@Override
	public void display() {
		super.stop();
		this.drawRainC(super.getSize().width, super.getSize().height);
		super.start();
	}

/*	@Override
	public void paint(Graphics g) {
		if ((int)this.ndrops > this.dropsx.size()) {
			dropsx.add((int) ((super.getWidth()-1) * Math.random()));
			dropsy.add(0);
			colors.add(200 + (int) (55 * Math.random()));
		}
		
		Color c;
		int dposy;
		int dposx;
		for(int i = 0; i < this.dropsx.size(); i++) {
			c = new Color(this.colors.get(i), this.colors.get(i), this.colors.get(i));
			dposx = dropsx.elementAt(i);
			dposy = dropsy.elementAt(i);
			dropsy.removeElementAt(i);
			//for (int j = 0; j <= this.dropsize; j++) {
				if (dposy+this.dropsize < super.getHeight()-1) {
					//System.out.println("i= " + i + " posy= "+dposy+ j+ " posx= "+dposx);
					g.setColor(c);
					g.drawLine(dposx, dposy, dposx, dposy + this.dropsize);//(dposx, dposy + j, c);
				}
				else {
					break;
				}
			//}
			this.dropsy.insertElementAt(dposy+this.dropspeed, i);
		}
		if (this.ndrops < this.maxdrops)
			this.ndrops += this.ndrops * .35;
	}
	*/
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Rain();
	}
	
	public class rainpos {
		public int x;
		public int y;
		public int dropspeed;
		public Color c;
		
		public rainpos(int x, int y, Color c, int speed) {
			this.x = x;
			this.y = y;
			this.c = c;
			this.dropspeed = speed;
		}
	}

}
