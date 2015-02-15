package Raster2d;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ImageHandler {

	private BufferedImage image;
	private int x = 0;
	private int y = 0;

	public ImageHandler(BufferedImage image) {
		super();
		this.image = image;
	}	
	
	public ImageHandler() {
		super();
	}

	public void saveImage(Color[] table){
		int result = JOptionPane.showOptionDialog(null, 
				"Select desired format to save the image.",
				"Save image.", 0, JOptionPane.QUESTION_MESSAGE, null, 
				new Object[]{"RGB","CMY","GS","BW","LT"}, "RGB");
		if (result < 0) return;
		JFileChooser choose = new JFileChooser();

		if (choose.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		String file = choose.getSelectedFile().getAbsolutePath();
		System.out.println(file);

		switch (result) {
		case 0:
			saveRGB(file);
			break;
		case 1:
			saveCMY(file);
			break;
		case 2:
			saveGS(file);
			break;
		case 3:
			saveBW(file);
			break;
		case 4:
			saveLT(file, table);
			break;
		}
	}
	
	public void saveRGB(String filename) {
		FileOutputStream fos;
		try {
			if (filename.toLowerCase().contains(".ppl"))
				fos = new FileOutputStream(filename);
			else
				fos = new FileOutputStream(filename+".ppl");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeChars("PPL");
			oos.writeByte(0);
			oos.writeInt(this.image.getWidth());
			oos.writeInt(this.image.getHeight());
			for (int i = 0; i < this.image.getWidth(); i++) {
				for (int j = 0; j < this.image.getHeight(); j++) {
					Color colour = new Color(this.image.getRGB(i, j));
					oos.writeByte(colour.getRed());
					oos.writeByte(colour.getGreen());
					oos.writeByte(colour.getBlue());
				}
			}
			oos.close();
						
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveCMY(String filename) {
		FileOutputStream fos;
		try {
			if (filename.toLowerCase().contains(".ppl"))
				fos = new FileOutputStream(filename);
			else
				fos = new FileOutputStream(filename+".ppl");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeChars("PPL");
			oos.writeByte(1);	//CMY
			oos.writeInt(this.image.getWidth());
			oos.writeInt(this.image.getHeight());
			for (int i = 0; i < this.image.getWidth(); i++) {
				for (int j = 0; j < this.image.getHeight(); j++) {
					Color colour = new Color(this.image.getRGB(i, j));
					oos.writeByte(256-colour.getRed());
					oos.writeByte(256-colour.getGreen());
					oos.writeByte(256-colour.getBlue());
				}
			}
			oos.close();
					
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveGS(String filename) {
		FileOutputStream fos;
		try {
			if (filename.toLowerCase().contains(".ppl"))
				fos = new FileOutputStream(filename);
			else
				fos = new FileOutputStream(filename+".ppl");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeChars("PPL");
			oos.writeByte(2);	//GS
			oos.writeInt(this.image.getWidth());
			oos.writeInt(this.image.getHeight());
			for (int i = 0; i < this.image.getWidth(); i++) {
				for (int j = 0; j < this.image.getHeight(); j++) {
					Color colour = new Color(this.image.getRGB(i, j));
					oos.writeByte((int) ((colour.getRed() * 0.299) + 
										(colour.getGreen() * 0.587) + 
										(256-colour.getBlue()) * 0.114));
				}
			}
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveBW(String filename) {
		FileOutputStream fos;
		try {
			if (filename.toLowerCase().contains(".ppl"))
				fos = new FileOutputStream(filename);
			else
				fos = new FileOutputStream(filename+".ppl");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeChars("PPL");
			oos.writeByte(3);	//GS
			oos.writeInt(this.image.getWidth());
			oos.writeInt(this.image.getHeight());
			int turn = 0;
			byte towrite = 0;
			int iter = 0;
			for (int i = 0; i < this.image.getWidth(); i++) {
				for (int j = 0; j < this.image.getHeight(); j++, turn++) {
					if (turn > 7) {
						iter++;
						oos.writeByte(towrite);
						towrite = 0;
						turn = 0;
					}
						
					Color colour = new Color(this.image.getRGB(i, j));
					int inten = (int) ((colour.getRed() * 0.299) + 
							(colour.getGreen() * 0.587) + 
							(256-colour.getBlue()) * 0.114);
					if (inten < 128)
						towrite = (byte) (towrite << 1);
					else {
						towrite = (byte) (towrite << 1);
						towrite += 1;// << 1;
					}
						//oos.writeByte(1);
				}
			}
			System.out.println("num de iter por cada 8 bits:" + iter);
			oos.close();
					
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveLT(String filename, Color[] table) {

		if (table.length > 16)	return;
		
		FileOutputStream fos;
		try {
			if (filename.toLowerCase().contains(".ppl"))
				fos = new FileOutputStream(filename);
			else
				fos = new FileOutputStream(filename+".ppl");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeChars("PPL");
			oos.writeByte(4);
			oos.writeInt(this.image.getWidth());
			oos.writeInt(this.image.getHeight());
			oos.writeByte(table.length);
			for (int i = 0; i < table.length; i++) {
				oos.writeByte(table[i].getRed());
				oos.writeByte(table[i].getGreen());
				oos.writeByte(table[i].getBlue());
			}
				
			for (int i = 0; i < this.image.getWidth(); i++) {
				for (int j = 0; j < this.image.getHeight(); j++) {
					Color colour = new Color(this.image.getRGB(i, j));
					byte pos = getPosinTable(colour, table);
					oos.writeByte(pos);
				}
			}
			oos.close();
						
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public byte getPosinTable(Color colour, Color[] table) {
		byte by = 0;
		
		for (int i = 0; i < table.length; i++) {
			if (colour.equals(table[i]))
				by = (byte) i;
		}
		
		return by;
	}
	
	public BufferedImage loadRGB(ObjectInputStream ois) {
		BufferedImage Image;
		try {
			int width = ois.readInt();
			int height = ois.readInt();
			System.out.println("Width: " + width);
			System.out.println("Width: " + height);
			int iter = (width * height);
			Image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			for (int b = 0; b < iter; b++) {
				byte colour = ois.readByte();
				int color = (int) colour;
				if (color < 0) 
					color += 256;
				//Color col = new Color(color, 0, 0);
				colour = ois.readByte();
				int color1 = (int) colour;
				if (color1 < 0) 
					color1 += 256;
				//col = new Color(col.getRed(), color, b);
				colour = ois.readByte();
				int color2 = (int) colour;
				if (color2 < 0) 
					color2 += 256;
				
				System.out.println("=========================================");
				System.out.println("		colour: " + color);
				System.out.println("		colour1: " + color1);
				System.out.println("		colour2: " + color2);
				Image = setNextPixel(new Color(color, color1, color2), Image);

			}

			return Image;	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public BufferedImage loadCMY(ObjectInputStream ois) {
		BufferedImage Image;
		try {
			int width = ois.readInt();
			int height = ois.readInt();
			System.out.println("Width: " + width);
			System.out.println("Width: " + height);
			int iter = (width * height);
			Image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			for (int b = 0; b < iter; b++) {
				byte colour = ois.readByte();
				int color = (int) colour;
				if (color < 0) 
					color += 256;
				//Color col = new Color(color, 0, 0);
				colour = ois.readByte();
				int color1 = (int) colour;
				if (color1 < 0) 
					color1 += 256;
				//col = new Color(col.getRed(), color, b);
				colour = ois.readByte();
				int color2 = (int) colour;
				if (color2 < 0) 
					color2 += 256;
				
				System.out.println("=========================================");
				System.out.println("		colour: " + color);
				System.out.println("		colour1: " + color1);
				System.out.println("		colour2: " + color2);
				Image = setNextPixel(new Color(color, color1, color2), Image);

			}

			return Image;	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public BufferedImage loadGS (ObjectInputStream ois) {
		BufferedImage image;
		try {
			int width = ois.readInt();
			int height = ois.readInt();
			System.out.println("Width: " + width);
			System.out.println("Width: " + height);
			int iter = (width * height);
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			for (int b = 0; b < iter; b++) {
				byte colour = ois.readByte();
				int color = (int) colour;
				if (colour == 65) {
					System.out.println("b = " + b);
				}
				if (color < 0) 
				{
					color += 256;
				}
				System.out.println("=========================================");
				System.out.println("		colours byte: " + colour);
				image = setNextPixel(new Color(color, color, color), image);

			}

			return image;	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public BufferedImage loadBW (ObjectInputStream ois) {
		BufferedImage image;
		try {
			int width = ois.readInt();
			int height = ois.readInt();
			System.out.println("Width: " + width);
			System.out.println("Width: " + height);
			int iter = (width * height) / 8;
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			for (int b = 0; b < iter-1; b++) {
				byte colours = ois.readByte();
				int colors = (int)colours;
				if (colors < 0) colors += 256;
				System.out.println("=========================================");
				System.out.println("NUM iter: " + b);
				System.out.println("Total iter: " + iter);
				System.out.println("		colours byte: " + colours);
				boolean[] bits = new boolean[8];
				String color = Integer.toBinaryString(colors);
				
				for (int i = 7, j = 0; i >= 0; i--) {
				//for(int i=1; i<8; i*=2) {
				//	if(colours & i)
					//bits[i] = (colours%2 == 1);
					//int colour = getBit(colours, i);
					if ( i >= color.length()) {
						//System.out.println("colour in i = "+i+": 0");
						image = setNextPixel('0', image);
					}
					else {
						//System.out.println("colour in i = "+i+": " + color.charAt(j));
						image = setNextPixel(color.charAt(j), image);
						j++;
					}
				}
			}

			return image;	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private BufferedImage loadLT(ObjectInputStream ois) {
		BufferedImage Image;
		try {
			int width = ois.readInt();
			int height = ois.readInt();
			System.out.println("Width: " + width);
			System.out.println("Width: " + height);
			int iter = (width * height);
			int tsize = (int) ois.readByte();
			Color[] colours = new Color[tsize];
			for (int i = 0; i < tsize; i++) {
				int c1 = (int) ois.readByte();
				int c2 = (int) ois.readByte();
				int c3 = (int) ois.readByte();
				if (c1 < 0)	c1+=256;;
				if (c2 < 0)	c2+=256;;
				if (c3 < 0)	c3+=256;;
				colours[i] = new Color(c1, c2, c3);
			}
			
			Image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			for (int b = 0; b < iter; b++) {
				int colourpos = (int)ois.readByte();
				if (colourpos < 0)colourpos += 256;
								
				if (colourpos > colours.length) colourpos = 0;
				System.out.println("=========================================");
				Image = setNextPixel(colours[colourpos], Image);

			}

			return Image;	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private BufferedImage setNextPixel(char bw, BufferedImage image) {
		if (bw == '0')
			image.setRGB(this.x, this.y, Color.BLACK.getRGB());
		else
			image.setRGB(this.x, this.y, Color.WHITE.getRGB());
		this.y++;
		if (this.y >= image.getHeight()) {
			this.x++;
			this.y = 0;
		}
		return image;
	}
	
	private BufferedImage setNextPixel(Color colour, BufferedImage image) {
		image.setRGB(this.x, this.y, colour.getRGB());
		this.y++;
		if (this.y >= image.getHeight()) {
			this.x++;
			this.y = 0;
		}
		return image;
	}
	
	public BufferedImage open() {
		//String path = new File("").getAbsolutePath();
		JFileChooser choose = new JFileChooser(); //Removed path from the chooser!

		if (choose.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
			return null;
		String file = choose.getSelectedFile().getAbsolutePath();
		try {
			this.x = 0;
			this.y = 0;
			FileInputStream fis = new FileInputStream(file); 
			ObjectInputStream ois = new ObjectInputStream(fis);
			String iden = "";
			iden += ois.readChar();
			iden += ois.readChar();
			iden += ois.readChar();
			
			if (iden.compareTo("PPL") != 0)	return null;
			byte by = ois.readByte();
			System.out.println(by);
			
			switch (by) {
				case 0:
					this.image = loadRGB(ois);
					break;
				case 1:
					this.image = loadCMY(ois);
					break;
				case 2:
					this.image = loadGS(ois);
					break;
				case 3:
					this.image = loadBW(ois);
					break;
				case 4:
					this.image = loadLT(ois);
					break;
			}
			ois.close();
			//int b = ois.readByte(); //lee 8 bits, lee -16, y no 240
			//si el bit mas significativo del byte antes de introducirse fue 1, el numero leido se interpreta como negativo
			//if(b < 0) b += 256; //para leer bytes sin signo
			//int i = ois.readInt(); //lee 32 bits, lee 3500
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}

	
}
