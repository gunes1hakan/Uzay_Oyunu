package uzay_oyunu;

import java.awt.image.BufferedImage;

/**
 * Hediye kutusu içeren özel meteor sınıfı.
 * Köşelerden çapraz olarak hareket eder (güneydoğu veya güneybatı yönünde).
 * Patlatıldığında hediye kutusu düşürür.
 * 
 * Spawn sırası: SOL → SAĞ → SOL (döngüsel)
 * 
 * NOT: Bu sınıf şimdilik kullanılmaz. Klon olayı tanımlandığında aktif
 * edilecek.
 */
public class HediyeKutuluMeteor extends Meteor {

    private int yatayYon; // +1 = sağa (sol köşeden), -1 = sola (sağ köşeden)
    private int yatayHiz;

    /**
     * Hediye kutusu içeren meteor oluşturur.
     * 
     * @param solKoseden true ise sol üst köşeden başlar (güneydoğu yönünde),
     *                   false ise sağ üst köşeden başlar (güneybatı yönünde)
     * @param resim      Meteorun görseli (sol veya sağ versiyonu)
     */
    public HediyeKutuluMeteor(boolean solKoseden, BufferedImage resim) {
        super(0, 0, resim); // Geçici konum, aşağıda düzeltilecek

        // Yatay yön ve hız ayarla
        this.yatayYon = solKoseden ? 1 : -1;
        this.yatayHiz = Ayarlar.HEDIYE_METEOR_HIZ_X;

        // Can değerini hediye meteora göre güncelle
        this.can = Ayarlar.HEDIYE_METEOR_CAN;

        // Başlangıç pozisyonunu köşeye göre ayarla
        if (solKoseden) {
            // Sol üst köşe
            this.x = 0;
        } else {
            // Sağ üst köşe
            this.x = Ayarlar.EKRAN_GENISLIK - this.genislik;
        }
        this.y = 0;
    }

    @Override
    public void guncelle() {
        // Çapraz hareket: yatay + dikey
        x += yatayYon * yatayHiz;
        y += Ayarlar.HEDIYE_METEOR_HIZ_Y;

        // Ekrandan çıktıysa yok ol (alt, sol veya sağ kenar)
        if (y > Ayarlar.EKRAN_YUKSEKLIK || x < -genislik || x > Ayarlar.EKRAN_GENISLIK) {
            aktif = false;
        }
    }

    /**
     * Meteorun sol köşeden mi geldiğini döndürür.
     * Hediye kutusu spawn konumunu belirlemek için kullanılabilir.
     */
    public boolean isSolKoseden() {
        return yatayYon > 0;
    }
}
