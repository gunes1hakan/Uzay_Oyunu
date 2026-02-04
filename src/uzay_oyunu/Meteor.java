package uzay_oyunu;

import java.awt.image.BufferedImage;

/**
 * Yukarıdan aşağıya doğru düz hareket eder.
 * Belirli sayıda mermi ile patlatılabilir.
 * 
 * NOT: Bu sınıf şimdilik kullanılmaz. Klon olayı tanımlandığında aktif
 * edilecek.
 */
public class Meteor extends Varlik {

    protected int can;

    public Meteor(int x, int y, BufferedImage resim) {
        super(x, y, resim);
        this.hiz = Ayarlar.METEOR_HIZ;
        this.can = Ayarlar.METEOR_CAN;
    }

    @Override
    public void guncelle() {
        y += hiz; // Düz aşağı hareket

        // Ekranın altından çıkarsa yok olur
        if (y > Ayarlar.EKRAN_YUKSEKLIK) {
            aktif = false;
        }
    }

    /**
     * Meteora hasar verir. Can sıfıra düşerse meteor patlar.
     */
    public void hasarAl() {
        can--;
        if (can <= 0) {
            aktif = false;
        }
    }

    /**
     * Meteorun kalan canını döndürür.
     */
    public int getCan() {
        return can;
    }
}
