package uzay_oyunu_;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.JPanel;

class Mermi{
	private int x;		//merminin yönergesi
	private int y;
	
	public Mermi(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	
	
}

public class Oyun extends JPanel implements KeyListener,ActionListener{		
	
	private int gecenSure=0;			//topun vurulmasına kadar geçen süre
	private int harcananMermi=0;			
	private BufferedImage imageUzayGemisi;		
	private BufferedImage imageUzayliUfo;		
	private ArrayList<Mermi> mermiler=new ArrayList<>();			//uzay gemisinden çıkan mermileri tutacak olan list
	private int mermidirY=1;				//merminin piksel cinsinden hareket hızı
	private int uzayliGemisiX=0;					//uyzayliGemisini koordinatı
	private int uzayliGemisidirX=2;				//uzayliGemisinin piksel cinsinden hareket hızı
	private int uzayGemisiX=0;			//uzay Gemisinin x konumunu belirtiyoruz.
	private int dirUzayX=20;			//uzay gemisnin piksel cinsinden hareket hızı
	
	

	public Oyun() {
		try {
			imageUzayGemisi=ImageIO.read(new FileImageInputStream(new File("uzaygemisi.png")));		
			imageUzayliUfo=ImageIO.read(new FileImageInputStream(new File("uzayliUfo.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setBackground(Color.black);
		
		
		
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		g.drawImage(imageUzayGemisi,uzayGemisiX, 490, imageUzayGemisi.getWidth()/10, imageUzayGemisi.getHeight()/10,this);	    
		g.drawImage(imageUzayliUfo,uzayliGemisiX, 0, imageUzayliUfo.getWidth()/10, imageUzayliUfo.getHeight()/10,this);
	}
	
	

	@Override
	public void repaint() {						
												
		super.repaint();						
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}			
																				

	
}
