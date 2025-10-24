package uzay_oyunu_;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;





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
	
	Timer timer =new Timer(5,this);		//5 sn'de bir arkaplanın yeniden çizilmesi sağlandı.
	
	private int gecenSure=0;			//topun vurulmasına kadar geçen süre
	private int harcananMermi=0;			
	private BufferedImage imageUzayGemisi;		
	private BufferedImage imageUzayliUfo;	
	private BufferedImage imageBulletPlasmaCyan;		//mermi fotoğrafının değişkeni	
	private ArrayList<Mermi> mermiler=new ArrayList<>();			//uzay gemisinden çıkan mermileri tutacak olan list
	private int mermidirY=1;				//merminin piksel cinsinden hareket hızı
	private int uzayliGemisiX=0;					//uzayliGemisini koordinatı
	private int uzayliGemisidirX=2;				//uzayliGemisinin piksel cinsinden hareket hızı
	private int uzayGemisiX=0;			//uzay Gemisinin x konumunu belirtiyoruz.
	private int dirUzayX=20;			//uzay gemisinin piksel cinsinden hareket hızı
	
	public boolean kontrolEt() {		//Merminin uzayliUfosuna değip değmediği kontrolu yapılmaktadır.
		int mermiGenislik=imageBulletPlasmaCyan.getWidth()/10;   
		int mermiYukseklik=imageBulletPlasmaCyan.getHeight()/10;  
		int ufoGenislik=imageUzayliUfo.getWidth()/10;          
		int ufoYukseklik=imageUzayliUfo.getHeight()/10;         
		
		int daraltilmismermiGenislik=(int)(mermiGenislik*0.85);			//yukarıda yazan değişkenleri direkt kullanırsam mermi ufonun yakınından geçerken mermi ufoya değmiş gibi oyun sonlanıyor,aynı şekilde Ufo'nun ayakları arasına geldiği zaman tam ufoya isabet etmeden	
		int daraltilmismermiYukseklik=(int)(mermiYukseklik*0.85);		//ufoya isabet etmiş gibi oyun sonlanıyor bu sorunları çözmek için soldaki gibi mermi ve ufo'nun çarpışma esnasında hesaplanan en-boy piksellerini daraltarak göze daha iyi gelen bir çarpışma görseli
		int daraltilmisufoGenislik=(int)(ufoGenislik*0.6);				//elde ediyoruz
		int daraltilmisufoYukseklik=(int)(ufoYukseklik*0.6);			
																		
		int xkenarindanmermiBoslugu=(mermiGenislik-daraltilmismermiGenislik)/2;    //soldaki değişkenler daraltılmış yeni piksellerin ortalandığında eski piksel boyutlarına göre ne kadar kaydığını gösterir.Böylece aşağıda piksel kesişimini hesaplayan if bloğunda eski
		int ykenarindanmermiBoslugu=(mermiYukseklik-daraltilmismermiYukseklik)/2;  //pikselin sağ üst köşesine bu değerleri ekleyerek yapılan hesaplamanın artık yukarıda değerleri hesaplanmış daraltılmış piksellere göre yapılması sağlanır.
		int xkenarindanUfoBoslugu=(ufoGenislik-daraltilmisufoGenislik)/2;			
		int ykenarindanUfoBoslugu=(ufoYukseklik-daraltilmisufoYukseklik)/2;
		
		
		for(Mermi mermi:mermiler) {		
			//aşağıda cisimlerin birbirlerine isabet ettiğini iki cismin piksellerinin kesişimine göre hesaplıyoruz. 
			if(new Rectangle(mermi.getX()+xkenarindanmermiBoslugu,mermi.getY()+ykenarindanmermiBoslugu,daraltilmismermiGenislik,daraltilmismermiYukseklik).intersects(new Rectangle(uzayliGemisiX+xkenarindanUfoBoslugu,0+ykenarindanUfoBoslugu,daraltilmisufoGenislik,daraltilmisufoYukseklik))) {		
										
				return true;			
			}							
		}								
		return false;
	}
	
	

	public Oyun() {
		try {
			imageUzayGemisi=ImageIO.read(new FileImageInputStream(new File("uzaygemisi.png")));		
			imageUzayliUfo=ImageIO.read(new FileImageInputStream(new File("uzayliUfo.png")));
			imageBulletPlasmaCyan=ImageIO.read(new FileImageInputStream(new File("bullet_plasma_cyan.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setBackground(Color.black);
		
		timer.start();  		
		
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Iterator<Mermi> it=mermiler.iterator();			//ekranın dışına doğru çıkan ateşleri arrayList yapısından çıkarttık.	
		while (it.hasNext()){								//Önceki mermi kontrolunda java,ArrayList yapısında hem gezinmeye çalışıyorsun hem de veri silmeye çalışıyorsun bu durumda herhangi bir veri çakışması olabilir deyip hata fırlatıyordu,bu hatadan kurtulmak için hem dolaşma işlemini hem  
		    Mermi i=it.next();							//de veri silme işlemini aynı anda yapabildiğimiz için Iterator interface'ini kullandık.
		    if(i.getY()<0) {
		        it.remove();   
		    }
		}


		g.drawImage(imageUzayGemisi,uzayGemisiX, 490, imageUzayGemisi.getWidth()/10, imageUzayGemisi.getHeight()/10,this);	    
		
		g.drawImage(imageUzayliUfo,uzayliGemisiX, 0, imageUzayliUfo.getWidth()/10, imageUzayliUfo.getHeight()/10,this);
		
		for(Mermi mermi:mermiler) {			//mermilerin ekranda görünmesini sağladım.
			g.drawImage(imageBulletPlasmaCyan,mermi.getX(), mermi.getY(), imageBulletPlasmaCyan.getWidth()/10, imageBulletPlasmaCyan.getHeight()/10,this);
			
		}
		
		if(kontrolEt()) {				//çarpışma olduğu zaman mesaj bastırıp oyunu sonlandırıyoruz.											
			timer.stop();														
			String message="Kazandınız...\n"+
						   "harcanan mermi: "+harcananMermi+
						   "\ngeçen süre: "+gecenSure/1000.0+" s";
			JOptionPane.showMessageDialog(this, message);
			System.exit(0);
		}
	}
	
	

	@Override
	public void repaint() {						
												
		super.repaint();						
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		for(Mermi mermi:mermiler) {					//mermilerin yukarı doğru hareket ettirilmesi sağlandı
			mermi.setY(mermi.getY()-mermidirY);
		}
		
		uzayliGemisiX+=uzayliGemisidirX;				//uzayli ufosunun sınırlara geldiği zaman yönünün değiştirilmesi sağlanıyor.
		if(uzayliGemisiX>=709) {				
			uzayliGemisidirX=-uzayliGemisidirX;		
		}
		if(uzayliGemisiX<=0) {
			uzayliGemisidirX=-uzayliGemisidirX;
		}

		gecenSure+=5;
		repaint();
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int c=e.getKeyCode();			
		
		if(c==KeyEvent.VK_LEFT)	{		//yön tuşları ile uzay gemimizi hareket ettiyoruz.
			if(uzayGemisiX<=0) {		
				uzayGemisiX=0;
				return;
			}
			else {
				uzayGemisiX-=dirUzayX;	
			}
			if(uzayGemisiX<10) {		//Bu kontrolu yapmamızın sebebi,uzay gemimiz tam 0. piksele yaklaşmadan bir önceki adımda uzay gemimizin bir kısmı ekran dışına taşıyor,bu hatayı bu kontrolle çözüyoruz.
				uzayGemisiX=0;
			}
			
		}
		else if(c==KeyEvent.VK_RIGHT) {
			if(uzayGemisiX>=742) {
				uzayGemisiX=742;
				return;						
			}
			else {
				uzayGemisiX+=dirUzayX;
			}
			if(uzayGemisiX>=732) {			
				uzayGemisiX=742;
			}
		}
		else if(c==KeyEvent.VK_SPACE) {			//space tuşu ile yeni bir mermi oluşturduk
			mermiler.add(new Mermi(uzayGemisiX+15,470));		
			harcananMermi++;
		}
	}
		
	

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}			
																				

	
}







