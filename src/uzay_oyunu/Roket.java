package uzay_oyunu;

import java.awt.image.BufferedImage;

/**
 * Ufo tarafından oyuncuya atılan roket sınıfı.
 * Yukarıdan aşağıya doğru hareket eder.
 */
public class Roket extends Varlik {

	public Roket(int x, int y, BufferedImage resim) {
		super(x, y, resim);
		this.hiz = Ayarlar.ROKET_HIZ;
	}

	@Override
	public void guncelle() {
		y += hiz; // Aşağı doğru hareket

		// Ekranın altından çıkarsa yok olur
		if (y > Ayarlar.EKRAN_YUKSEKLIK) {
			aktif = false;
		}
	}
}
