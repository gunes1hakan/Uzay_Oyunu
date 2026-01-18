package uzay_oyunu;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Mermi bir hedefe çarptığında oluşan kısa süreli görsel efekt (kıvılcım).
 */
public class VurusEfekti {
    protected int x, y; // Efektin konumu
    protected long baslangicZamani; // Efektin başladığı zaman
    protected int sureMs = 150; // Efektin ekranda kalma süresi (Kısa sürsün, kıvılcım gibi)
    protected BufferedImage resim;
    protected int genislik, yukseklik;

    public VurusEfekti(int x, int y, BufferedImage resim) {
        this.x = x;
        this.y = y;
        this.resim = resim;
        this.baslangicZamani = System.currentTimeMillis();

        if (resim != null) {
            // Kıvılcımı daha da büyütelim (50 -> 75)
            this.genislik = 75;
            this.yukseklik = 75;
        }
    }

    public void ciz(Graphics g) {
        if (resim != null) {
            // Efekti merkeze göre çizmek için offset
            g.drawImage(resim, x - genislik / 2, y - yukseklik / 2, genislik, yukseklik, null);
        }
    }

    /**
     * Efektin süresi doldu mu?
     */
    public boolean isAktif() {
        return (System.currentTimeMillis() - baslangicZamani) < sureMs;
    }
}
