import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/***
 * To open a FILE, double left click and select an image.
 * To save a File, double right click
 * @author Tecuciztecatl
 *
 */
public class ImageViewer extends JFrame {

	public BufferedImage image = null;

	public void paint(Graphics g) {
		super.paint(g); //Paint, even if we are not really painting anything
		if (image != null)
			g.drawImage(image, super.getInsets().left, super.getInsets().top, this);
	}

	public ImageViewer() {

		super.setTitle("ImageViewer");
		super.setSize(600, 600);
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		super.setResizable(false);
		super.setLocationRelativeTo(null);
		//super.setUndecorated(true);
		super.addFocusListener(new FocusAdapter() {
			public void focusGained (FocusEvent e) {
				repaint();
			}
		});
		super.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() ==2 && e.getButton() == 3)
					saveFile();
				//System.out.println(e.getButton());
				if (e.getClickCount() != 2 || e.getButton() != 1) return;
				
				openFile();
			}
		});
		super.setVisible(true);
	}

	/**
	 * Loads an image file supported by java
	 */
	public void openFile() {
		String path = new File("").getAbsolutePath();
		JFileChooser choose = new JFileChooser(path);

		if (choose.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		String file = choose.getSelectedFile().getAbsolutePath();
		//System.out.println(file);
		try {
			image = ImageIO.read(new File(file));
			setSize(image.getWidth() + getInsets().left + getInsets().right,
					image.getHeight() + getInsets().top + getInsets().bottom);
			//repaint();
		} catch (IOException ex) {
		}
	}
	
	/**
	 * Saves the image in the specified directory with the specified method in png format
	 */
	public void saveFile() {
		String path = new File("").getAbsolutePath();
		JFileChooser choose = new JFileChooser(path);
		int result = JOptionPane.showOptionDialog(null, 
				"Select desired format to save the image.",
				"Save image.", 0, JOptionPane.QUESTION_MESSAGE, null, 
				new Object[]{"Half-Toning","Dithering", "Primary Colours"}, "RGB");

		if (choose.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		String file = choose.getSelectedFile().getAbsolutePath();
		System.out.println(file);

		switch (result) {
		case 0:
			saveHT(file);
			break;
		case 1:
			saveDithering(file);
			break;
		case 2:
			savePrimaryColours(file);
			break;
		}
		if (!file.contains(".png"))
			file = file + ".png";
		 File outputfile = new File(file);
		    try {
				ImageIO.write(this.image, "png", outputfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		repaint();
	}
	
	/**
	 * Saves the image with half toning methodology
	 * @param filename
	 */
	public void saveHT(String filename) {
		
		//For every 2x2 square we take the colours and make them to grayscale
		//Then we make a prom of grayscale to determine the masking
		for (int i = 0; i < image.getWidth(); i+=2) {
			for (int j = 0; j < image.getHeight(); j+=2) {
				Color colour1 = new Color(image.getRGB(i, j));
				int gray1 = (int) ((colour1.getRed() * 0.299) + (colour1.getGreen() * 0.587) + (colour1.getBlue() * 0.114));
				Color colour2 = new Color(image.getRGB(i+1, j)); 
				int gray2 = (int) ((colour2.getRed() * 0.299) + (colour2.getGreen() * 0.587) + (colour2.getBlue() * 0.114));
				Color colour3 = new Color(image.getRGB(i, j+1)); 
				int gray3 = (int) ((colour3.getRed() * 0.299) + (colour3.getGreen() * 0.587) + (colour3.getBlue() * 0.114));
				Color colour4 = new Color(image.getRGB(i+1, j+1)); 
				int gray4 = (int) ((colour4.getRed() * 0.299) + (colour4.getGreen() * 0.587) + (colour4.getBlue() * 0.114));
				
				double prom = (gray1 + gray2 + gray3 + gray4)/4;
				doGSMask(i,j,prom);
			}
		}
		repaint();
	}

	public void doGSMask(int i, int j, double prom) {
		double percentage = (prom * 100) / 255;
		if (percentage > 87.5) {
			this.image.setRGB(i, j, Color.WHITE.getRGB());
			this.image.setRGB(i+1, j, Color.WHITE.getRGB());
			this.image.setRGB(i, j+1, Color.WHITE.getRGB());
			this.image.setRGB(i+1, j+1, Color.WHITE.getRGB());
		}
		else if (percentage > 62.5) {
			this.image.setRGB(i, j, Color.WHITE.getRGB());
			this.image.setRGB(i+1, j, Color.BLACK.getRGB());
			this.image.setRGB(i, j+1, Color.WHITE.getRGB());
			this.image.setRGB(i+1, j+1, Color.WHITE.getRGB());
		}
		else if (percentage > 37.5) {
			this.image.setRGB(i, j, Color.WHITE.getRGB());
			this.image.setRGB(i+1, j, Color.BLACK.getRGB());
			this.image.setRGB(i, j+1, Color.BLACK.getRGB());
			this.image.setRGB(i+1, j+1, Color.WHITE.getRGB());
		}
		else if (percentage > 12.5) {
			this.image.setRGB(i, j, Color.BLACK.getRGB());
			this.image.setRGB(i+1, j, Color.WHITE.getRGB());
			this.image.setRGB(i, j+1, Color.BLACK.getRGB());
			this.image.setRGB(i+1, j+1, Color.BLACK.getRGB());
		}
		else {
			this.image.setRGB(i, j, Color.BLACK.getRGB());
			this.image.setRGB(i+1, j, Color.BLACK.getRGB());
			this.image.setRGB(i, j+1, Color.BLACK.getRGB());
			this.image.setRGB(i+1, j+1, Color.BLACK.getRGB());
		}
	}
	
	/**
	 * Saves the image in Dithering Methology
	 * @param filename
	 */
	public void saveDithering(String filename) {
		//We get the prom of R, G, and B to make a mask to each colour
		for (int i = 0; i < image.getWidth(); i+=2) {
			for (int j = 0; j < image.getHeight(); j+=2) {
				Color colour1 = new Color(image.getRGB(i, j));
				Color colour2 = new Color(image.getRGB(i+1, j));
				Color colour3 = new Color(image.getRGB(i, j+1));
				Color colour4 = new Color(image.getRGB(i+1, j+1));

				double promr = (colour1.getRed() + colour2.getRed() + colour3.getRed() + colour4.getRed())/4.0;
				double promg = (colour1.getGreen() + colour2.getGreen() + colour3.getGreen() + colour4.getGreen())/4.0;
				double promb = (colour1.getBlue() + colour2.getBlue() + colour3.getBlue() + colour4.getBlue())/4.0;
				doDitheringMask(i,j,promr, promg, promb);
			}
		}
		repaint();
	}

	public void doDitheringMask(int i, int j, double promr, double promg, double promb) {
		double percentager = (promr * 100) / 255;
		double percentageg = (promg * 100) / 255;
		double percentageb = (promb * 100) / 255;
		
		Color one;
		Color two;
		Color three;
		Color four;
		//RED BATCH
		if (percentager > 87.5) {
			one =  new Color(255, 0, 0);
			two = new Color(255, 0, 0);
			three = new Color(255, 0, 0);
			four = new Color(255, 0, 0);
		}
		else if (percentager > 62.5) {
			one = new Color(255, 0, 0);
			two = new Color(0, 0, 0);
			three = new Color(255, 0, 0);
			four = new Color(255, 0, 0);
		}
		else if (percentager > 37.5) {
			one = new Color(255, 0, 0);
			two = new Color(0, 0, 0);
			three = new Color(0, 0, 0);
			four = new Color(255, 0, 0);
		}
		else if (percentager > 12.5) {
			one = new Color(0, 0, 0);
			two = new Color(255, 0, 0);
			three = new Color(0, 0, 0);
			four = new Color(0, 0, 0);
		}
		else {
			one = new Color(0, 0, 0);
			two = new Color(0, 0, 0);
			three = new Color(0, 0, 0);
			four = new Color(0, 0, 0);
		}
		
		//GREEN BATCH
		if (percentageg > 87.5) {
			one = new Color(one.getRed(), 255, 0);
			two = new Color(two.getRed(), 255, 0);
			three = new Color(three.getRed(), 255, 0);
			four = new Color(four.getRed(), 255, 0);
		}
		else if (percentageg > 62.5) {
			one = new Color(one.getRed(), 255, 0);
			two = new Color(two.getRed(), 0, 0);
			three = new Color(three.getRed(), 255, 0);
			four = new Color(four.getRed(), 255, 0);
		}
		else if (percentageg > 37.5) {
			one = new Color(one.getRed(), 255, 0);
			two = new Color(two.getRed(), 0, 0);
			three = new Color(three.getRed(), 0, 0);
			four = new Color(four.getRed(), 255, 0);
		}
		else if (percentageg > 12.5) {
			one = new Color(one.getRed(), 0, 0);
			two = new Color(two.getRed(), 255, 0);
			three = new Color(three.getRed(), 0, 0);
			four = new Color(four.getRed(), 0, 0);
		}
		else {
			one = new Color(one.getRed(), 0, 0);
			two = new Color(two.getRed(), 0, 0);
			three = new Color(three.getRed(), 0, 0);
			four = new Color(four.getRed(), 0, 0);
		}
		
		//BLUE BATCH
		if (percentageb > 87.5) {
			one = new Color(one.getRed(), one.getGreen(), 255);
			two = new Color(two.getRed(), two.getGreen(), 255);
			three = new Color(three.getRed(), three.getGreen(), 255);
			four = new Color(four.getRed(), four.getGreen(), 255);
		}
		else if (percentageb > 62.5) {
			one = new Color(one.getRed(), one.getGreen(), 255);
			two = new Color(two.getRed(), two.getGreen(), 0);
			three = new Color(three.getRed(), three.getGreen(), 255);
			four = new Color(four.getRed(), four.getGreen(), 255);
		}
		else if (percentageb > 37.5) {
			one = new Color(one.getRed(), one.getGreen(), 255);
			two = new Color(two.getRed(), two.getGreen(), 0);
			three = new Color(three.getRed(), three.getGreen(), 0);
			four = new Color(four.getRed(), four.getGreen(), 255);
		}
		else if (percentageb > 12.5) {
			one = new Color(one.getRed(), one.getGreen(), 0);
			two = new Color(two.getRed(), two.getGreen(), 255);
			three = new Color(three.getRed(), three.getGreen(), 0);
			four = new Color(four.getRed(), four.getGreen(), 0);
		}
		else {
			one = new Color(one.getRed(), one.getGreen(), 0);
			two = new Color(two.getRed(), two.getGreen(), 0);
			three = new Color(three.getRed(), three.getGreen(), 0);
			four = new Color(four.getRed(), four.getGreen(), 0);
		}

		this.image.setRGB(i, j, one.getRGB());
		this.image.setRGB(i+1, j, two.getRGB());
		this.image.setRGB(i, j+1, three.getRGB());
		this.image.setRGB(i+1, j+1, four.getRGB());
	}

	public void savePrimaryColours(String filename) {
		//We make a round up of colours to determine if it should have full red or 0 red
		//It is done with each part: R, G, and B
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				Color colour1 = new Color(image.getRGB(i, j));

				int promr = (int) (colour1.getRed());
				int promg = (int) (colour1.getGreen());
				int promb = (int) (colour1.getBlue());
				
				if (promr < (256/2))
					promr = 0;
				else
					promr = 255;
				if (promg < (256/2))
					promg = 0;
				else
					promg = 255;
				if (promb < (256/2))
					promb = 0;
				else
					promb = 255;
				
				Color c = new Color(promr, promg, promb);
				this.image.setRGB(i, j, c.getRGB());
			}
		}
		repaint();
	}
	
	public static void main(String[] args) {
		new ImageViewer();
	}

}
