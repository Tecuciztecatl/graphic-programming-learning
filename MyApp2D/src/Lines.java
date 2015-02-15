import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.Calendar;
import java.util.GregorianCalendar;

import Raster2d.Animation;


public class Lines extends Animation {

	int angle;
	int dangle;
	int dangleh;
	int longsec;
	int longmin;
	int longhour;
	int anglemin;
	int angleh;
	
	public Lines(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}
	
	public Lines() {
		super(300, 300);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.setClearColor(Color.BLACK);
		super.setPixelColor(Color.WHITE);
		this.dangle = 360/60;
		this.dangleh = 360/12;
		super.setTitle("Clocking...");
		Calendar calendar = new GregorianCalendar();
		int h = calendar.get(Calendar.HOUR);
		//Inicia algorithmo para obtener el ‡ngulo de la hora
		if (h >= 9)
			this.angleh =  180-((h-9)*30);
		else if (h >= 6)
			this.angleh =  270 -((h - 6) * 30);
		else if (h >= 3)
			this.angleh = 360 -((h - 3) * 30);
		else if (h >= 0)
			this.angleh = 90 - (h * 30);
		 
		h = calendar.get(Calendar.MINUTE);
		//Inicia algorithmo para obtener el ‡ngulo de los minutos
		if (h >= 45)
			this.anglemin =  180-((h-45)*6);
		else if (h >= 30)
			this.anglemin =  270 -((h - 30) * 6);
		else if (h >= 15)
			this.anglemin = 360 -((h - 15) * 6);
		else if (h >= 0)
			this.anglemin = 90 - (h * 6);
		
		h = calendar.get(Calendar.SECOND) + 1;
		//Inicia algorithmo para obtener el ‡ngulo de los segundos
		if (h >= 45)
			this.angle =  180-((h-45)*6);
		else if (h >= 30)
			this.angle =  270 -((h - 30) * 6);
		else if (h >= 15)
			this.angle = 360 -((h - 15) * 6);
		else if (h >= 0)
			this.angle = 90 - (h * 6);
		
		//this.angle = Math.abs((calendar.get(Calendar.SECOND) * this.dangleh) - 90);
		super.start();
	}

	public void drawClock(int width, int height) {
		/*Point2D.Double[] points = { new Point2D.Double(-110, 50), new Point2D.Double(-20, 10),
				new Point2D.Double( 10, -70), new Point2D.Double( 75, 20),
				 new Point2D.Double(30, 80)
				};
		super.lineLoop(points, Color.GREEN);*/
		//super.drawVerticalRect(super.getJ(0), super.getJ(90), super.getI(5), Color.RED, Color.WHITE);
		//super.drawVerticalRect(super.getJ(-90), super.getJ(0), super.getI(5), Color.RED, Color.WHITE);
		//Point2D t = new Point2D.Double();	t.setLocation(0, 0);
		//Point2D t2 = new Point2D.Double();	t2.setLocation(90,
		//		-10);
		//super.colorRect(t, t2, Color.RED, Color.WHITE);
		//super.drawRectangle(200, 100, Color.GREEN);
		//super.drawPolygon(90, 5);
		//super.drawPolygon(80, 3, Color.RED);
		//Point2D t3 = new Point2D.Double(-100, -100);
		//Point2D t4 = new Point2D.Double(100, 100);
		//super.colorArea(t3, t4, Color.WHITE, Color.RED, Color.GREEN, Color.BLUE);
		if (width < height) {
			longhour = (width / 2) - (width/3);
			longmin = (width / 2) - (width/6);
			longsec = (width / 2) - (width/6);
			super.drawCirclePoli((width / 2) - (width/7));
		}
		else {
			longhour = (height / 2) - (height/3);
			longmin = (height / 2) - (height/6);
			longsec = (height / 2) - (height/6);
			super.drawCirclePoli((height / 2) - (height/7));
		}

		//Checo los ‡ngulos para saber si tengo que reiniciarlos
		//o "aumentarlos" dependiendo de la hora
		if (this.angle <= 0) {
			this.angle = 360;
		}
		if (angle == 90) {
			this.anglemin -= this.dangle;
		}
		if (this.anglemin <= 0) {
			this.anglemin = 360;
			this.angleh -= this.dangle;
		}
		if (anglemin == 90) {
			this.angleh -= this.dangleh;
		}
		
		//Dibujo cada l’nea del reloj
		//super.setPixelColor(Color.ORANGE);
		//super.lineDDA(0, 0, this.longsec * Math.cos(Math.toRadians(this.angle)), this.longsec * Math.sin(Math.toRadians(this.angle)));
		
		//super.setPixelColor(Color.WHITE);
		//super.lineDDA(0, 0, this.longmin * Math.cos(Math.toRadians(this.anglemin)), this.longmin * Math.sin(Math.toRadians(this.anglemin)));
		//super.setPixelColor(Color.GREEN);
		//super.lineDDA(0, 0, this.longhour * Math.cos(Math.toRadians(this.angleh)), this.longhour * Math.sin(Math.toRadians(this.angleh)));
		
		Point2D t = new Point2D.Double();	t.setLocation(0, 0);
		Point2D t2 = new Point2D.Double();	t2.setLocation(this.longsec * Math.cos(Math.toRadians(this.angle)),
											this.longsec * Math.sin(Math.toRadians(this.angle)));
		super.colorRectBre(t, t2, Color.RED, Color.WHITE);

		t.setLocation(0, 0);
		t2.setLocation(this.longmin * Math.cos(Math.toRadians(this.anglemin)),
						this.longmin * Math.sin(Math.toRadians(this.anglemin)));
		super.colorRectBre(t, t2, Color.WHITE, Color.BLUE);
		t.setLocation(0, 0);
		t2.setLocation(this.longhour * Math.cos(Math.toRadians(this.angleh)),
						this.longhour * Math.sin(Math.toRadians(this.angleh)));
		super.colorRectBre(t, t2, Color.BLACK, Color.WHITE);
		
		this.angle -= this.dangle;
		//super.drawVerticalRect(30, 150, 15, Color.BLACK, Color.BLUE);
		
		//Espero 999 millisegundos d‡ndole 1 millisegundo de offset pensando
		//que eso tarda en hacer todo lo anterior xD
		super.setDelay(999);
	}
	
	@Override
	public void display() {

		//super.stop();
		this.drawClock(super.getSize().width, super.getSize().height);
		//super.start();
		
		//super.lineLE(0, 0, 0, 100);
		
		/*
		super.lineLE(16, 46, 16, 46);
		super.lineLE(15, 45, 35, 45);
		super.lineLE(15, 35, 15, 25);
		super.lineLE(100, 100, 50, 50);
		super.lineLE(100, 100, 50, 150);
		super.lineLE(100, 100, 150, 150);
		super.lineLE(100, 100, 150, 50);

		//super.lineLE(0, 0, -153, 89);
		//super.lineLE(0, 0, -153, -89);
		//super.lineLE(0, 0, -89, -153);
		//super.lineLE(0, 0, -89, 153);

		//super.lineLE(0, 0, 153, 89);
		//super.lineLE(0, 0, 153, -89);
		//super.lineLE(0, 0, 89, -153);
		//super.lineLE(0, 0, 89, 153);

		

		super.lineDDA(0, 0, -153, 89);
		super.lineDDA(0, 0, -153, -89);
		super.lineDDA(0, 0, -89, -153);
		super.lineDDA(0, 0, -89, 153);

		super.lineDDA(0, 0, 153, 89);
		super.lineDDA(0, 0, 153, -89);
		super.lineDDA(0, 0, 89, -153);
		super.lineDDA(0, 0, 89, 153);
		*/

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Lines();

	}

}
