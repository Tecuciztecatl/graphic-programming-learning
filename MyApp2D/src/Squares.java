import java.awt.Color;

import Raster2d.Raster2d;


public class Squares extends Raster2d {

	public int nsquares;
	
	public Squares(int width, int height, int nsquare) {
		super(width, height);
		this.nsquares = nsquare;
	}

	public Squares(int nsquare) {
		super(300, 300);
		this.nsquares = nsquare;
	}

	public Squares() {
		super(300, 300);
		this.nsquares = 4;
	}

	public void squarecolorsUE(int m, int fwidth, int fheight, int heightoffset) {
		int starti = m;
		int startj = m + heightoffset;
		int sqwidth = 2;//fwidth/(this.nsquares*2);
		int sqheight = 2;//fheight/(this.nsquares*2);
		Color c = super.randomColor();
		int to = fwidth /2;
		
		for (int count = 0; count < to; count++) {
			for (int i = starti; i < fwidth; i++) {
				for (int j = startj; j < fheight; j++) {
					super.setPixel(i, j, c);
				}
			}
			starti+=sqwidth;
			startj+=sqheight;
			fwidth-=sqwidth;
			fheight-=sqheight;
			//select = !select;
			//c = select? Color.GREEN : Color.RED;
			c = super.randomColor();
			//System.out.println("Si entra!");
		}
	}
	
	/**
	 * This method draws the squares starting from the middle ending with the outer squares
	 * This squares will always be perfect squares
	 * @param m
	 * @param fwidth
	 * @param fheight
	 * @param heightoffset
	 */
	public void squarecolorsE(int m, int fwidth, int fheight, int heightoffset) {
		int starti = fwidth/2; //The start will be the middle of the 'i' axis
		int startj = (fheight + heightoffset)/2;	//The start will be the middle of the 'j' axis
		//The end parts will start being the same as the start
		int endi = starti;
		int endj = startj;
		
		Color c = super.randomColor();

		//This while will be effective untill one or the margins is reached
		while (starti > m && startj > heightoffset + m) {
			//this for is to give the width/height of the rect
			for (int count = 0; count < 2; count++) {
				//TOP line
				//BOTTOM line
				for (int i = starti; i <= endi; i++) {
					super.setPixel(i, startj, c);
					super.setPixel(i, endj, c);
				}
				//LEFT line
				//RIGHT line
				for (int j = startj; j <= endj; j++) {
					super.setPixel(starti, j, c);
					super.setPixel(endi, j, c);
				}
				starti--;
				startj--;
				endi++;
				endj++;
			}
			c = super.randomColor();
		}
		
	}
	
	/**
	 * this method draws the squares starting from the outer squares ending with the middle ones
	 * It also draws the lines all along the margins, so the squares can be stretched
	 * @param m
	 * @param fwidth
	 * @param fheight
	 * @param heightoffset
	 */
	public void squarecolorsES(int m, int fwidth, int fheight, int heightoffset) {
		int sqwidth = 2; 	//Line size
		int starti = m;		//Lets start from where we can start painting, the margin!
		int startj = m + heightoffset;	//From the margin we add the offset
		int middlei = fwidth /2;	//We calculate the middle 'i' of the window
		int middlej = ((fheight - heightoffset)/2) + heightoffset;	// We calculate the middle of the window in 'j'
		fwidth = fwidth -m; 	//We substract the margin of the 'i' end
		fheight = fheight -m;	//We substract the margin of the 'j' end
		
		Color c = super.randomColor();
		
		//This while will be effective untill a middle part is reached
		while (starti <= middlei && startj <= middlej) {
			//this for is to give the width/height of the rect
			for (int w = 0; w < sqwidth; w++) {
				//TOP line
				//BOTTOM line
				for (int i = starti; i <= fwidth; i++) {
					super.setPixel(i, startj, c);
					super.setPixel(i, fheight, c);
				}
				//LEFT line
				//RIGHT line
				for (int j = startj; j <= fheight; j++) {
					super.setPixel(starti, j, c);
					super.setPixel(fwidth, j, c);
				}
				starti++;
				startj++;
				fwidth--;
				fheight--;
			}
			c = super.randomColor();
		}
	}
	
	public void squarecolorsEwCS(int m, int fwidth, int fheight, int heightoffset) {
		int sqwidth = 2;
		int sqheight = 2;
		int starti = m;
		int startj = m + heightoffset;
		int middlei = fwidth /2;
		int middlej = (fheight/2) + heightoffset;
		//middlej = fheight/2;
		fwidth = fwidth -m;
		fheight = fheight -m;
		
		Color c = super.randomColor();
		
		while (starti <= middlei && startj <= middlej) {
			for (int w = 0; w < sqwidth; w++) {
				//TOP
				//BOTTOM
				for (int i = starti; i <= fwidth; i++) {
					super.setPixel(i, startj, c);
					super.setPixel(i, fheight, c);
				}
				//LEFT
				//RIGHT
				for (int j = startj; j <= fheight; j++) {
					super.setPixel(starti, j, c);
					super.setPixel(fwidth, j, c);
				}
				starti++;
				startj++;
				fwidth--;
				fheight--;
			}
			//c = select? Color.GREEN : Color.RED;
			c = super.randomColor();
			//System.out.println("Si entra!");
		}
	}
	
	@Override
	public void init() {
		this.nsquares = 4;
		super.setClearColor(Color.BLACK);
	}

	@Override
	public void display() {
		//int fwidth = super.getSize().width-super.getInsets().right -10;
		//int fheight = super.getSize().height-super.getInsets().bottom -10;
		int fwidth = super.getSize().width;
		int fheight = super.getSize().height;

		//squarecolorsEwCS(starti, startj, fwidth, fheight, (int) (super.getSize().getWidth()/2));
		//squarecolorsE(10, fwidth, fheight, super.getInsets().top);
		squarecolorsES(10,fwidth,fheight,super.getInsets().top);
				
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Squares(5);
	}

}
