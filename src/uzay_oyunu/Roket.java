package uzay_oyunu;

import java.awt.image.BufferedImage;
import javax.sound.sampled.Clip;

/**
 * Ufo tarafından oyuncuya atılan roket sınıfı.
 * Yukarıdan aşağıya doğru hareket eder.
 */
public class Roket extends Varlik {

	private Clip sesKlibi;

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

	public Clip getSesKlibi() {
		return sesKlibi;
	}

	public void setSesKlibi(Clip sesKlibi) {
		this.sesKlibi = sesKlibi;
	}
}
