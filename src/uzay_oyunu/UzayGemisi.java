package uzay_oyunu;

import java.awt.image.BufferedImage;

/**
 * Oyuncunun kontrol ettiği Uzay Gemisi sınıfı.
 */
public class UzayGemisi extends Varlik {
    private int hareketYonuX = 0; // 0: Duruyor, Pozitif: Sağ, Negatif: Sol

    public UzayGemisi(int x, int y, BufferedImage resim) {
        super(x, y, resim);
        this.hiz = Ayarlar.GEMI_HIZ;
    }

    @Override
    public void guncelle() {
        x += hareketYonuX;

        // Geminin ekran sınırları dışına çıkmasını engelle
        if (x <= 0) {
            x = 0;
        } else if (x >= Ayarlar.EKRAN_GENISLIK - genislik - 10) {
            // -10 payı, görselin tam sınıra yapışmaması için orijinal koddan gelmektedir.
            x = Ayarlar.EKRAN_GENISLIK - genislik - 10;
        }
    }

    public void hareketEt(int yon) {
        this.hareketYonuX = yon * hiz;
    }

    public void dur() {
        this.hareketYonuX = 0;
    }

    // --- Sağlık Sistemi ---
    private int can = Ayarlar.GEMI_MAX_CAN;

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
}
