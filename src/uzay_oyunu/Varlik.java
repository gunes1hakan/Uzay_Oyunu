package uzay_oyunu;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Oyun içindeki tüm hareketli nesnelerin (Entity) türediği temel sınıf.
 * Konum, boyut, görsel ve hareket gibi ortak özellikleri barındırır.
 */
public abstract class Varlik {
    protected int x, y; // Nesnenin koordinatları
    protected int genislik, yukseklik; // Nesnenin boyutları
    protected BufferedImage resim; // Nesnenin görseli
    protected int hiz; // Nesnenin varsayılan hızı
    protected boolean aktif = true; // Nesnenin oyunda olup olmadığı

    public Varlik(int x, int y, BufferedImage resim) {
        this.x = x;
        this.y = y;
        this.resim = resim;
        if (resim != null) {
            // Görsel boyutlarını küçülterek oyun boyutlarına uyarlıyoruz.
            // Orijinal grafikler büyük olduğu için 10'a bölme mantığı korunmuştur.
            this.genislik = resim.getWidth() / 10;
            this.yukseklik = resim.getHeight() / 10;
        }
    }

    /**
     * Her oyun döngüsünde (tick) nesnenin ne yapacağını belirleyen metot.
     * Alt sınıflar (Gemi, Ufo vb.) bu metodu kendi hareket mantıklarına göre
     * doldurur.
     */
    public abstract void guncelle();

    /**
     * Nesneyi ekrana çizer.
     */
    public void ciz(Graphics g) {
        if (aktif && resim != null) {
            g.drawImage(resim, x, y, genislik, yukseklik, null);
        }
    }

    /**
     * Nesnenin çarpışma sınırlarını (Bounding Box) döndürür.
     * Çarpışma kontrollerinde bu dikdörtgen kullanılır.
     */
    public Rectangle getSinirlar() {
        return new Rectangle(x, y, genislik, yukseklik);
    }

    // --- Getter ve Setter Metotları ---
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getGenislik() {
        return genislik;
    }

    public int getYukseklik() {
        return yukseklik;
    }

    public boolean isAktif() {
        return aktif;
    }

    public void setAktif(boolean aktif) {
        this.aktif = aktif;
    }
}
