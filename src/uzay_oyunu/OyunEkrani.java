package uzay_oyunu;

import java.awt.HeadlessException;

import javax.swing.JFrame;

public class OyunEkrani extends JFrame {

	public OyunEkrani(String title) throws HeadlessException {
		super(title);
	}

	public static void main(String[] args) {
		OyunEkrani ekran = new OyunEkrani("Uzay Oyunu");

		ekran.setResizable(false);
		ekran.setFocusable(false);

		// YENİ: Boyut arttırıldı (HUD için)
		ekran.setSize(Ayarlar.EKRAN_GENISLIK, Ayarlar.EKRAN_YUKSEKLIK + Ayarlar.CAN_BARI_YUKSEKLIK);

		ekran.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// YENİ: Layout yönetimi
		ekran.setLayout(new java.awt.BorderLayout());

		Oyun oyun = new Oyun();

		oyun.addKeyListener(oyun);
		oyun.setFocusable(true);
		oyun.setFocusTraversalKeysEnabled(false);

		// YENİ: Sağlık panelini ekle
		SaglikPaneli saglikPaneli = new SaglikPaneli(oyun.getMantik());

		// Panelleri yerleştir (HUD yukarıda, Oyun aşağıda)
		ekran.add(saglikPaneli, java.awt.BorderLayout.NORTH);
		ekran.add(oyun, java.awt.BorderLayout.CENTER);

		ekran.setVisible(true);

		oyun.requestFocus();
	}
}
