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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;





public class Oyun extends JPanel implements KeyListener,ActionListener{
	
	Timer timer =new Timer(5,this);		//5 sn'de bir arkaplanın yeniden çizilmesi sağlandı.
	
	private int gecenSure=0;			//topun vurulmasına kadar geçen süre
	private int harcananMermi=0;			
	private BufferedImage imageUzayGemisi;		
	private BufferedImage imageUzayliUfo;	
	private BufferedImage imageBulletPlasmaCyan;		//mermi fotoğrafının değişkeni	
	private BufferedImage imagePatlama;
	private BufferedImage imageUzayliRoketi;
	private BufferedImage imageUzayGemisiPatlama;
	
	private ArrayList<Roket> roketler=new ArrayList<>();
	private ArrayList<Mermi> mermiler=new ArrayList<>();			//uzay gemisinden çıkan mermileri tutacak olan list
	private int mermidirY=5;				//merminin piksel cinsinden hareket hızı
	private int uzayliGemisiX=0;					//uzayliGemisini koordinatı
	private int uzayliGemisidirX=2;				//uzayliGemisinin piksel cinsinden hareket hızı
	private int uzayGemisiX=0;			//uzay Gemisinin x konumunu belirtiyoruz.
	private int dirUzayX=20;			//uzay gemisinin piksel cinsinden hareket hızı

	private int rokettirY=4;
	private boolean imhaEtkinMi=false;
	private int imhaAdimSayaci=0;				//if kontrolu için oluşturulmuş bir değişkendir
	private long imhaBaslangicZamaniMs=0L;		
	private final int imhaMaksAdim=10;			//patlamanın toplamda 1 saniye boyunca sürmesini ve fotoğrafımızın toplamda 10 adım büyümesini istediğimiz için 10 adım girdik ve de aşağıdaki değişkende bu yüzden 1000/10'dan 100 oldu.
	private final int imhaArtisAraligiMs=100;
	private int imhaMerkezX=0;
	private int imhaMerkezY=0;
	private final double imhaAdimYuzde = 0.01;
	
	private boolean patlamaEtkinMi=false;			//patlama durumunu kontrol etmek için boolean cinsinden bir tane değişken oluşturduk.
	private int patlamaAdimSayaci=0;				//if kontrolu için oluşturulmuş bir değişkendir
	private long patlamaBaslangicZamaniMs=0L;		
	private final int patlamaMaksAdim=10;			//patlamanın toplamda 1 saniye boyunca sürmesini ve fotoğrafımızın toplamda 10 adım büyümesini istediğimiz için 10 adım girdik ve de aşağıdaki değişkende bu yüzden 1000/10'dan 100 oldu.
	private final int patlamaArtisAraligiMs=100;
	private int patlamaMerkezX=0;
	private int patlamaMerkezY=0;
	private final double patlamaAdimYuzde = 0.01; 	//her adımda PatlamaEfekti adlı fotoğrafın %1 büyümesini istediğimiz için bu değeri girdik.
	
	private final int bombaMinAralikMs = 1500;    //en az 0.7 sn
	private final int bombaMaxAralikMs = 2200;   //en çok 1.8 sn
	private long sonrakiBombaZamaniMs = 0L;
	private final Random rng = new Random();
	private boolean gemiGorunumu=true;

	private final ZamanSaglayici zaman = ZamanSaglayici.sistem();		//şu anki zamanı güncel olarak alabilmek için ZamanSaglayici interface'inden bir ref oluşturduk.
	private static final double daraltilmisRoketOrani = 0.6;			//roket ile uzayGemisinin çarpışıp patlama aşamasındaki hesaplamalarını daha isabetli bir şekilde yapabilmek için Rectangle ifadesine bu değişkenleri yolluyoruz.
	private static final double daraltilmisGemiOrani  = 0.8;
	
	private int ufoGenislik, ufoYukseklik;				//Her seferinde aşağıdaki kod yapılarında tek tek ufoGenislik veya aşağıdaki diğer değişkenlerin hesabını uzunuzadıya imageUzayliUfo.getWidth()/10 yazarak hesaplamak yerine bu değişkenleri kullanmak kod yazımı  açısından
	private int roketGenislik, roketYukseklik;			//daha iyi olacağından bu değişkenleri oluşturduk.
	private int gemiGenislik,  gemiYukseklik;
	private int mermiGenislik, mermiYukseklik;
	private int patlamaGenislik,patlamaYukseklik;
	private int	gemiPatlamaGenislik,gemiPatlamaYukseklik;

	public void rastgeleBombala() {
		 long simdi = zaman.simdiMs();		//ZamanSaglayici interface'i içerisindeki simdiMs() soyut metotu sayesinde güncel olan değeri aldık.

		    if (sonrakiBombaZamaniMs == 0L) {																
		        int aralik = rng.nextInt(bombaMaxAralikMs - bombaMinAralikMs + 1) + bombaMinAralikMs;		//ilk bombalama işleminde bu if bloğuna girdikten sonra bombalama işleminin 1500-2200 ms aralığında olması sağlandı.	
		        sonrakiBombaZamaniMs = simdi + aralik;														
		        return;																						
		    }

		    if (simdi >= sonrakiBombaZamaniMs) {					//şimdiki zaman artık yukarıda oluşturduğumuzsonrakiBombaZamaniMs değerine eşit olduğunda veya geçtiğinde artık bombalama işlemini yapmaya başlayabiliriz.
		        int baslangicX = uzayliGemisiX + (ufoGenislik - roketGenislik) / 2;		//Roketin UzayliUfosunun ortasından çıkmasını sağladık
		        int baslangicY = 0 + ufoYukseklik; 										//Roketin UzayliUfosunun altından çıkmasını sağladık.

		        roketler.add(new Roket(baslangicX, baslangicY, roketGenislik, roketYukseklik));

		       
		        int aralik = rng.nextInt(bombaMaxAralikMs - bombaMinAralikMs + 1) + bombaMinAralikMs; //Bir sonraki atışı yeniden kurduk.
		        sonrakiBombaZamaniMs = simdi + aralik;
		    }
	}
	
	
	
	public boolean imhaOlduMu() {
	    Rectangle gemiRect = new Rectangle(uzayGemisiX, 490, gemiGenislik, gemiYukseklik );

	    //Her roket için daraltılmış dikdörtgen oluştuyoruz ve kesişimi kontrol ediyoruz.
	    for (Roket roket : roketler) {

	        if (CarpismaPolitikasi.kesisir(roket.sinirDikdortgeni(), daraltilmisRoketOrani, gemiRect, daraltilmisGemiOrani)) {		//CarpismaPolitikasi class'ınde yer alan metotlar sayesinde kesisim olup olmadığı kontrolunu yapıyorum.
	            return true;
	        }
	    }
	    return false;
	}
	
	public boolean kontrolEt() {		
		for(Mermi mermi : mermiler) {
			if(CarpismaPolitikasi.kesisir(mermi.sinirDikdortgeni(), 1.0,new Rectangle(uzayliGemisiX,0,ufoGenislik,ufoYukseklik), 1.0)) {								
				return true;			
			}							
		}								
		return false;
	}	
	
	

	public Oyun() {
		try {
			imageUzayGemisi=ImageIO.read(new FileImageInputStream(new File("uzaygemisi.png")));		//yukarıda tanımladığımız image'e klasörümüzde yer alan uzayGemisi adlı fotoyu yolluyoruz.
			imageUzayliUfo=ImageIO.read(new FileImageInputStream(new File("uzayliUfo.png")));
			imageBulletPlasmaCyan=ImageIO.read(new FileImageInputStream(new File("bullet_plasma_cyan.png")));
			imagePatlama=ImageIO.read(new FileImageInputStream(new File("PatlamaEfekti.png")));
			imageUzayliRoketi=ImageIO.read(new FileInputStream(new File("UzayliRoketi.png")));
			imageUzayGemisiPatlama=ImageIO.read(new FileInputStream(new File("UzayGemisiPatlamaEfekti.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	 	ufoGenislik    = imageUzayliUfo.getWidth()  / 10;							
	    ufoYukseklik   = imageUzayliUfo.getHeight() / 10;					
	    roketGenislik  = imageUzayliRoketi.getWidth()  / 10;				
	    roketYukseklik = imageUzayliRoketi.getHeight() / 10;
	    gemiGenislik   = imageUzayGemisi.getWidth() / 10;
	    gemiYukseklik  = imageUzayGemisi.getHeight()/ 10;
	    mermiGenislik  = imageBulletPlasmaCyan.getWidth()/ 10;
	    mermiYukseklik = imageBulletPlasmaCyan.getHeight()/ 10;
	    patlamaGenislik= imagePatlama.getWidth()/ 10;
	    patlamaYukseklik=imagePatlama.getHeight()/ 10;
	    gemiPatlamaGenislik=imageUzayGemisiPatlama.getWidth()/8;
	    gemiPatlamaYukseklik=imageUzayGemisiPatlama.getHeight()/8;
		
		setBackground(Color.black);
		
		timer.start();  		//klasör başlatıldığı anda timerin çalıştırılması sağlandı.
		
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Iterator<Mermi> it=mermiler.iterator();			//ekranın dışına doğru çıkan ateşleri arrayList yapısından çıkarttık.	
		while (it.hasNext()) {							
		    Mermi i=it.next();							
		    if (i.getY()<0) {
		        it.remove();   
		    }
		}
		
		Iterator<Roket> itr=roketler.iterator();
		while(itr.hasNext()) {
			Roket i=itr.next();
			if(i.getY()>600) {
				itr.remove();
			}
		}

		
		if(gemiGorunumu) {				//patlama olduktan sonra patlamanın olduğu yerde resim tam olarak gemiyi kaplamadığı için arka planda gemi görünerek çirkin bir görüntü oluşturuyor,bu görüntüyü engellemek amacıyla eğer ki imha olayı gerçekleşir ise gemiGorunumu adlı değişkeni
										//false yaparak artık geminin oluşmasını engellemiş oluyoruz.
		g.drawImage(imageUzayGemisi,uzayGemisiX, 490,gemiGenislik, gemiYukseklik,this);	   			
		}
		g.drawImage(imageUzayliUfo,uzayliGemisiX, 0, ufoGenislik, ufoYukseklik,this);
		
		for(Mermi mermi:mermiler) {			//mermilerin ekranda görünmesini sağladım.
			g.drawImage(imageBulletPlasmaCyan,mermi.getX(), mermi.getY(), mermiGenislik, mermiYukseklik,this);
			
		}
		
		for(Roket roket:roketler) {
			g.drawImage(imageUzayliRoketi, roket.getX(), roket.getY(), roketGenislik, roketYukseklik, this);
		}
		

		if (!patlamaEtkinMi&&kontrolEt()) {		//çarpışma olduğu zaman oyunu bitirmeyip patlama efektini veriyoruz.Patlama durumunu da alıyoruz ki true yaptıktan sonra yine bu if koşulu durumuna girmesin.
		    patlamaEtkinMi=true;					
		    patlamaBaslangicZamaniMs=zaman.simdiMs();			

		    patlamaMerkezX=uzayliGemisiX+ufoGenislik/2;		//Patlamayı uzaylı ufosunun merkezine yerleştiriyoruz.
		    patlamaMerkezY=0+ufoYukseklik/2;
		}
		
		if (patlamaEtkinMi) {		
		    double olcek = 1.0 + patlamaAdimSayaci * patlamaAdimYuzde;  		//her 100 ms'de bir fotoğrafı %1 büyüterek patlama efekti veriyoruz
		    int genislik  = (int) Math.round(patlamaGenislik  * olcek);		
		    int yukseklik = (int) Math.round(patlamaYukseklik * olcek);		 
		    																			

		    int solUstX = patlamaMerkezX - genislik / 2;							//patlamayı ufonun ortasına koyuyoruz.	
		    int solUstY = patlamaMerkezY - yukseklik / 2;								
		    																		
		    g.drawImage(imagePatlama, solUstX, solUstY, genislik, yukseklik, this);		
		}
		
		if(!imhaEtkinMi&&imhaOlduMu()) {
			imhaEtkinMi=true;
			gemiGorunumu=false;
			imhaBaslangicZamaniMs=zaman.simdiMs();
			
			imhaMerkezX=uzayGemisiX+gemiGenislik/2;
			imhaMerkezY=490+gemiYukseklik/2;
		}
		
		if(imhaEtkinMi) {
			 double olcek = 1.0 + imhaAdimSayaci * imhaAdimYuzde;  				
			 int genislik  = (int) Math.round(gemiPatlamaGenislik  * olcek);		
			 int yukseklik = (int) Math.round(gemiPatlamaYukseklik * olcek);		

			 int solUstX = imhaMerkezX - genislik / 2;								
			 int solUstY = imhaMerkezY - yukseklik / 2;								
			    																			
			 g.drawImage(imageUzayGemisiPatlama, solUstX, solUstY, genislik, yukseklik, this);	
		}
		
		

	}
	
	

	@Override
	public void repaint() {						
		super.repaint();						

	@Override
	public void actionPerformed(ActionEvent e) {
		
		rastgeleBombala();
		
		for(Mermi mermi:mermiler) {					//mermilerin yukarı doğru hareket ettirilmesi sağlandı
			mermi.setY(mermi.getY()-mermidirY);
		}
		
		for(Roket roket:roketler) {
			roket.setY(roket.getY()+rokettirY);
		}
		
		if (patlamaEtkinMi) {
	        long simdi = zaman.simdiMs();									
	        if (simdi - patlamaBaslangicZamaniMs >= patlamaArtisAraligiMs) {		//her 100 ms de bir if kontrolune girerek patlama efekti yapılıyor.
	            patlamaAdimSayaci = Math.min(patlamaAdimSayaci + 1, patlamaMaksAdim);
	            patlamaBaslangicZamaniMs = simdi;

	            if (patlamaAdimSayaci >= patlamaMaksAdim) {				//patlamaAdım sayımıza ulaşıldığı zaman oyun sonlanıyor.
	                timer.stop();
	                String message = "Kazandınız...\n"
	                        + "harcanan mermi: " + harcananMermi
	                        + "\ngeçen süre: " + gecenSure / 1000.0 + " s";
	                JOptionPane.showMessageDialog(this, message);
	                System.exit(0);
	                return;
	            }
	        }
	    }
		else {					//patlama olduğu zaman uzayliGemisinin hareket etmesini engelliyoruz.
			uzayliGemisiX+=uzayliGemisidirX;				//uzayli ufosunun sınırlara geldiği zaman yönünün değiştirilmesi sağlanıyor.
			if(uzayliGemisiX>=709) {				
				uzayliGemisidirX=-uzayliGemisidirX;		
			}
			if(uzayliGemisiX<=0) {
				uzayliGemisidirX=-uzayliGemisidirX;
			}
		}
		
		if (imhaEtkinMi) {
	        long simdi = zaman.simdiMs();									
	        if (simdi - imhaBaslangicZamaniMs >= imhaArtisAraligiMs) {		//her 100 ms de bir if kontrolune girerek patlama efekti yapılıyor.
	            imhaAdimSayaci = Math.min(imhaAdimSayaci + 1, imhaMaksAdim);
	            imhaBaslangicZamaniMs = simdi;

	            if (imhaAdimSayaci >= imhaMaksAdim) {
	                timer.stop();
	                String message = "Kaybettiniz...\n"
	                        + "harcanan mermi: " + harcananMermi
	                        + "\ngeçen süre: " + gecenSure / 1000.0 + " s";
	                JOptionPane.showMessageDialog(this, message);
	                System.exit(0);
	                return;
	            }
	        }
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
		if(!imhaEtkinMi) {
		if(c==KeyEvent.VK_LEFT)	{		///yön tuşları ile uzay gemimizi hareket ettiyoruz.
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
		else if(c==KeyEvent.VK_SPACE) {				//space tuşu ile yeni bir mermi oluşturduk
			mermiler.add(new Mermi(uzayGemisiX+15,470,
					imageBulletPlasmaCyan.getWidth()/10, imageBulletPlasmaCyan.getHeight()/10));		
			harcananMermi++;
		}
		}
	}
		
	

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}			
																				

	
}


