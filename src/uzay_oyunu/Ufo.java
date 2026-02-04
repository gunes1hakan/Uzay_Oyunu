package uzay_oyunu;

import java.awt.image.BufferedImage;

/**
 * Düşman birimi olan UFO sınıfı.
 * Ekranın üst kısmında sağa-sola devriye gezer.
 */
public class Ufo extends Varlik {
    private int yonX = 1; // 1: Sağa, -1: Sola hareket

    public Ufo(int x, int y, BufferedImage resim) {
        super(x, y, resim);
        this.hiz = Ayarlar.UFO_HIZ;
    }

    @Override
    public void guncelle() {
        x += yonX * hiz;

        // Ekranın sağ veya sol sınırına çarpınca yön değiştir
        if (x >= Ayarlar.EKRAN_GENISLIK - genislik) {
            yonX = -1; // Sola dön
        }
        if (x <= 0) {
            yonX = 1; // Sağa dön
        }
    }

    // --- Sağlık Sistemi ---
    private int can = Ayarlar.UFO_MAX_CAN;

    public void hasarAl() {
        if (can > 0) {
            can--;
        }
    }

    public boolean oluMu() {
        return can <= 0;
    }

    public int getCan() {
        return can;
    }

    /**
     * UFO'nun hareket yönünü ayarlar.
     * 
     * @param yon 1 = sağa, -1 = sola
     */
    public void setYonX(int yon) {
        this.yonX = yon;
    }
}
