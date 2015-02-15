import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import Raster2d.Animation;


public class Squares2 extends Animation {

	private int currside;
	private Color squareColor;
	private List<Color> colors;
	
	public Squares2(int width, int height, Color sqcolor) {
		super(width, height);
	}

	public Squares2() {
		super(300, 300);
	}
	
	public void drawSquare(int width, int height, int heightoffset) {
		int centrei = width/2; //The start will be the middle of the 'i' axis
		int centrej = ((height - heightoffset)/2) + heightoffset;;	//The start will be the middle of the 'j' axis
		//The end parts will start being the same as the start
		int side = this.currside/2;
		
				//TOP line
				//BOTTOM line
		for (int i = 0 - side; i <= side; i++) {
			super.setPixel(i + centrei, centrej - side);
			super.setPixel(i + centrei, centrej + side);
			super.setPixel(centrei - side, i + centrej);
			super.setPixel(centrei + side, i + centrej);
		}
		//LEFT line
		//RIGHT line
		this.currside++;
		if (this.currside >= width || this.currside >= height-heightoffset)
			this.currside = 10;

	}
	
	public void drawSquares(int width, int height, int heightoffset) {
		int centrei = width/2; //The start will be the middle of the 'i' axis
		int centrej = ((height - heightoffset)/2) + heightoffset;;	//The start will be the middle of the 'j' axis
		//The end parts will start being the same as the start
		int l = 0;
		int side = this.currside/2;
		Color c;
		
		
		//for (l = 0; ; l++) 
		do {
			if (l >= colors.size())
				c= super.randomColor();
			else
				c = this.colors.get(l);
			
			side = (this.currside + l)/2;
				//TOP line
				//BOTTOM line
			for (int i = 0 - side; i <= side; i++) {
				super.setPixel(i + centrei, centrej - side, c);
				super.setPixel(i + centrei, centrej + side, c);
				super.setPixel(centrei - side, i + centrej, c);
				super.setPixel(centrei + side, i + centrej, c);
			}
			//LEFT line
			//RIGHT line
			//this.currside++;
			l++;
			if (this.currside + l >= width || this.currside + l >= height-heightoffset) {
				this.colors.clear();
				return;
			}
		}while(l < this.colors.size());
		c = super.randomColor();
		this.colors.add(c);
	}
	
	@Override
	//INIT SE LLAMAN ANTES QUE EL CONSTRUCTOR
	public void init() {
		this.squareColor = Color.RED;
		super.setClearColor(Color.GRAY);
		super.setDelay(20);
		this.currside = 10;
		super.setPixelColor(this.squareColor);
		colors = new ArrayList<Color>();
		super.start();
	}

	@Override
	public void display() {
		this.drawSquares(super.getSize().width, super.getSize().height, super.getInsets().top);

	}
	
	public static void main(String[] args) {
		new Squares2();
	}

}
