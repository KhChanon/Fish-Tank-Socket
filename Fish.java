import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Fish extends JPanel implements Serializable {
    private static final long serialVersionUID = 1L;
	private transient BufferedImage img;
	public int imgSize;
	public int speedX;
	public int speedY;
	public int imgStyle;

	public Fish(String img) {
		this(0,0,randomSpeed(),randomSpeed(),randomStyle(),randomSize(),new ImageIcon(img).getImage());
	}
	
	
	public Fish(int x, int y,int SpeedX,int speedY,int style,int imgSize,Image image) {

		setLocation(x, y);
		this.speedX = SpeedX;
		this.speedY = speedY;
		this.imgStyle = style;
		this.imgSize = randomSize();
		if(speedX <= 0)
			image = flipImage(image);
		BufferedImage scaledImage = resizeImage(image,imgSize,imgSize);
		this.img = scaledImage;
		Dimension size = new Dimension(imgSize, imgSize);
    	setSize(size);
    	setLayout(null);
	}
	

	public Fish(int x, int y) {

		setLocation(x, y);
		this.speedX = randomSpeed();
		this.speedY = randomSpeed();
		this.imgStyle = randomStyle();
		this.imgSize = randomSize();
		ImageIcon icon = new ImageIcon("C:\\Users\\Chanon\\Desktop\\Work\\Java\\Assignment 3\\src\\Fish\\Fish"+imgStyle+".png");
		Image img = icon.getImage();
		if(speedX <= 0)
			img = flipImage(img);
		BufferedImage scaledImage = resizeImage(img,imgSize,imgSize);
		this.img = scaledImage;
		Dimension size = new Dimension(imgSize, imgSize);
    	setSize(size);
    	setLayout(null);
	}
	
	public Fish(int x, int y,BufferedImage img) {

		setLocation(x, y);
		this.imgSize = randomSize();
		this.imgStyle = randomStyle();
		this.imgSize = randomSize();
		BufferedImage scaledImage = resizeImage(img,imgSize,imgSize);
		this.img = scaledImage;
		Dimension size = new Dimension(imgSize, imgSize);
    	setSize(size);
    	setLayout(null);
	}

	public Fish(int x, int y,int imgSize,BufferedImage img) {

		setLocation(x, y);
		this.imgStyle = randomStyle();
		this.imgSize = imgSize;
		BufferedImage scaledImage = resizeImage(img,imgSize,imgSize);
		this.img = scaledImage;
		Dimension size = new Dimension(imgSize, imgSize);
    	setSize(size);
    	setLayout(null);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}
	
	public static int randomSpeed() {
		int randomSpeed = (int) (Math.random() * 20);
		randomSpeed = randomSpeed - 10;
		return randomSpeed;
	}
	public static int randomStyle() {
		int randomStyle = (int) (Math.random() * 8);
		return randomStyle;
	}
	
	public static int randomSize() {
		int randomSize = (int) (Math.random() * 60);
		randomSize = randomSize +5;
		return randomSize;
	}
	
	public static BufferedImage resizeImage(Image image, int width, int height) {
		BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
	}
	
	public BufferedImage flipImage(Image image) {
		BufferedImage flippedImage = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB);
		Graphics g = flippedImage.createGraphics();
		g.drawImage(image, imgSize, 0, -imgSize, imgSize, null);
		g.dispose();
		return flippedImage;
	}
	
	public void setImage(String imgPath) {
		ImageIcon icon = new ImageIcon(imgPath);
		Image img = icon.getImage();
		if(speedX <= 0)
			img = flipImage(img);
		BufferedImage scaledImage = resizeImage(img,imgSize,imgSize);
		this.img = scaledImage;
	}

}
