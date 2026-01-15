package uzay_oyunu;

import java.awt.image.BufferedImage;

/**
 * Oyuncunun uzay gemisinden ateşlediği mermi sınıfı.
 * Aşağıdan yukarıya doğru hareket eder.
 */
public class Mermi extends Varlik {

    public Mermi(int x, int y, BufferedImage resim) {
        super(x, y, resim);
        this.hiz = Ayarlar.MERMI_HIZ;
    }

    @Override
    public void guncelle() {
        y -= hiz; // Yukarı doğru hareket

        // Ekranın tepesinden çıkarsa yok olur
        if (y < 0) {
            aktif = false;
        }
    }
}
