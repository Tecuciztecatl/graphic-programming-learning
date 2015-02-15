import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Raster2d.ImageHandler;
import Raster2d.Raster2d;


public class UseSaveMethods extends Raster2d {

	private final double rmin = -2.0;
	private final double rmax = 0.5;
	private final double imin = -1.25;
	private final double imax = 1.25;
	private double delta;
	private final double N = 16;
	private Color[] table;
	private ArrayList<Color> tablee = new ArrayList<Color>();
	
	public UseSaveMethods(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}	
	
	public UseSaveMethods() {
		super(400, 400);
		// TODO Auto-generated constructor stub
	}

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
					addColorToARR(c);
				}
				
				super.setPixelColor(c);
				super.setPixel(i, j);
			}
		}
	}
	
	private void addColorToARR(Color colour) {
		boolean has = false;
		if (this.tablee.size() > 16)
			return;
		if (this.tablee.size() < 1) {
			this.tablee.add(colour);
			return;
		}
		for (int i = 0; i < this.tablee.size(); i ++) {
			if (this.tablee.get(i).getRGB() == colour.getRGB()) {
				has = true;
				return;
			}
		}
		if (!has) {
			this.tablee.add(colour);
		}
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.tablee = new ArrayList<Color>();
		System.out.println("HMMM");
		super.setClearColor(Color.BLACK);
		super.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() ==2 && e.getButton() == 3) {
					saveFile(alistToA());
					System.out.println("done!");
					
				}
				//System.out.println(e.getButton());
				if (e.getClickCount() != 2 || e.getButton() != 1) return;
				
				openFile();
			}
		});
	}

	protected Color[] alistToA() {
		Color[] colours = new Color[this.tablee.size()];
		for (int i = 0; i < colours.length; i++) {
			colours[i] = this.tablee.get(i);
		}
		return colours;
	}

	@Override
	public void display() {
		// TODO Auto-generated method stub
		mandelbrotc();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new UseSaveMethods();
	}

}
