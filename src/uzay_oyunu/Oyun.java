package uzay_oyunu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Oyunun Görünüm (View) ve Kontrolcü (Controller) Katmanı.
 * Bu sınıf JPanel üzerinden ekran çizimini yapar (paint) ve klavye girdilerini
 * dinler (KeyListener).
 */
public class Oyun extends JPanel implements KeyListener, ActionListener {

	// Oyun Mantığı ve Kaynak Yönetimi
	private Timer timer;
	private OyunMantigi mantik;
	private KaynakYoneticisi kaynaklar;

	public Oyun() {
		// Kaynakları yükler ve mantık motorunu başlatır.
		kaynaklar = new KaynakYoneticisi();
		mantik = new OyunMantigi(kaynaklar);

		setBackground(Color.black);

		// Oyun döngüsünü başlatan Timer.
		// Ayarlar.GECIKME (5ms) süresinde bir actionPerformed metodunu tetikler.
		timer = new Timer(Ayarlar.GECIKME, this);
		timer.start();
	}

	/**
	 * Ekranın yeniden çizilmesinden sorumlu metot.
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		// Mantık katmanından güncel verileri alıp sırasıyla çiziyoruz.
		// Çizim sırası (Z-Order) önemlidir: Arkadakiler önce çizilmelidir.

		// 1. Ufo (En arkada)
		mantik.getUfo().ciz(g);

		// 2. Gemi (Eğer patlamıyorsa çiz)
		// Patlama efekti gemiyi tam örtmediği için, patlama anında gemiyi gizliyoruz.
		if (!mantik.isGemiPatlamaAktif()) {
			mantik.getGemi().ciz(g);
		}

		// 3. Mermiler
		for (Mermi m : mantik.getMermiler()) {
			m.ciz(g);
		}

		// 4. Roketler
		for (Roket r : mantik.getRoketler()) {
			r.ciz(g);
		}

		// 5. Patlama Efektleri (En üstte)
		if (mantik.isUfoPatlamaAktif()) {
			cizPatlama(g, mantik.getPatlamaAdimSayaci(), true);
		}

		if (mantik.isGemiPatlamaAktif()) {
			cizPatlama(g, mantik.getImhaAdimSayaci(), false);
		}
	}

	/**
	 * Patlama animasyonunu kare kare çizen yardımcı metot.
	 * Zamanla büyüyen (scale) bir efekt uygular.
	 */
	private void cizPatlama(Graphics g, int adim, boolean ufoMu) {
		// Efekt her adımda %1 büyür
		double olcek = 1.0 + adim * 0.01;

		java.awt.image.BufferedImage resim;
		int resimGenislik, resimYukseklik, merkezX, merkezY;

		if (ufoMu) {
			resim = kaynaklar.getPatlamaResim();
			if (resim == null)
				return;

			// Orijinal boyutlandırma mantığı korunmuştur (/10)
			resimGenislik = resim.getWidth() / 10;
			resimYukseklik = resim.getHeight() / 10;

			Ufo ufo = mantik.getUfo();
			merkezX = ufo.getX() + ufo.getGenislik() / 2;
			merkezY = ufo.getY() + ufo.getYukseklik() / 2;
		} else {
			resim = kaynaklar.getGemiPatlamaResim();
			if (resim == null)
				return;

			resimGenislik = resim.getWidth() / 8; // Gemi patlaması biraz daha büyük (/8)
			resimYukseklik = resim.getHeight() / 8;

			UzayGemisi gemi = mantik.getGemi();
			merkezX = gemi.getX() + gemi.getGenislik() / 2;
			merkezY = gemi.getY() + gemi.getYukseklik() / 2;
		}

		int genislik = (int) Math.round(resimGenislik * olcek);
		int yukseklik = (int) Math.round(resimYukseklik * olcek);

		// Resmi merkeze göre konumlandır
		int solUstX = merkezX - genislik / 2;
		int solUstY = merkezY - yukseklik / 2;

		g.drawImage(resim, solUstX, solUstY, genislik, yukseklik, this);
	}

	@Override
	public void repaint() {
		super.repaint();
	}

	/**
	 * Oyun Döngüsü (Game Loop)
	 * Timer tarafından her periyotta çağrılır.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// 1. Oyun mantığını güncelle (Fizik, Çarpışma, Yapay Zeka)
		mantik.guncelle();

		// 2. Oyun bitiş kontrolü
		if (mantik.isOyunBitti()) {
			timer.stop();
			String sonucMesaji = mantik.isKazandi() ? "Kazandınız..." : "Kaybettiniz...";
			String mesaj = sonucMesaji + "\n"
					+ "harcanan mermi: " + mantik.getHarcananMermi()
					+ "\ngeçen süre: " + mantik.getGecenSure() / 1000.0 + " s";
			JOptionPane.showMessageDialog(this, mesaj);
			System.exit(0);
		}

		// 3. Ekranı yenile (paint metodunu tetikler)
		repaint();
	}

	/**
	 * Klavye Girdilerini Dinleyen Metot
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int c = e.getKeyCode();

		if (c == KeyEvent.VK_LEFT) {
			// Sola Hareket
			UzayGemisi gemi = mantik.getGemi();
			gemi.setX(gemi.getX() - Ayarlar.GEMI_HIZ);

			// Ekranın solundan taşmayı engeller
			if (gemi.getX() < 0)
				gemi.setX(0);

		} else if (c == KeyEvent.VK_RIGHT) {
			// Sağa Hareket
			UzayGemisi gemi = mantik.getGemi();
			gemi.setX(gemi.getX() + Ayarlar.GEMI_HIZ);

			// Ekranın sağından taşmayı engelle
			if (gemi.getX() >= Ayarlar.EKRAN_GENISLIK - gemi.getGenislik() - 10)
				gemi.setX(Ayarlar.EKRAN_GENISLIK - gemi.getGenislik() - 10);

		} else if (c == KeyEvent.VK_SPACE) {
			// Ateş Etme
			mantik.atesEt();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
