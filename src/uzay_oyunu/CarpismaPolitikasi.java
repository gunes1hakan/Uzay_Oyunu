package uzay_oyunu;

import java.awt.Rectangle;

/**
 * Çarpışma algılama mantığını barındıran yardımcı sınıf.
 * İki dikdörtgenin kesişip kesişmediğini kontrol eder.
 * Oyunun hassasiyetini artırmak için nesnelerin çarpışma alanlarını daraltma
 * yeteneğine sahiptir.
 */
public final class CarpismaPolitikasi {

    // Yardımcı sınıf olduğu için nesne üretilmesini engelliyoruz.
    private CarpismaPolitikasi() {
    }

    /**
     * Bir dikdörtgeni belirtilen oranda küçültür (Daraltır).
     * Bu işlem, görselin şeffaf kenarlarının çarpışmaya dahil edilmesini önlemek
     * için kullanılır.
     * 
     * @param r    Orijinal dikdörtgen
     * @param oran Daraltma oranı (Ör: 0.8 = %80 boyutunda)
     * @return Merkezi aynı kalan, küçültülmüş yeni dikdörtgen
     */
    public static Rectangle daralt(Rectangle r, double oran) {
        int yeniGenislik = (int) Math.round(r.width * oran);
        int yeniYukseklik = (int) Math.round(r.height * oran);

        // Yeni dikdörtgeni orijinalinin tam ortasına hizala
        int x = r.x + (r.width - yeniGenislik) / 2;
        int y = r.y + (r.height - yeniYukseklik) / 2;

        return new Rectangle(x, y, yeniGenislik, yeniYukseklik);
    }

    /**
     * İki dikdörtgenin kesişip kesişmediğini kontrol eder.
     * 
     * @param a     Birinci nesnenin sınırları
     * @param oranA Birinci nesne için daraltma oranı (1.0 = daraltma yok)
     * @param b     İkinci nesnenin sınırları
     * @param oranB İkinci nesne için daraltma oranı
     * @return Kesişim varsa true, yoksa false
     */
    public static boolean kesisir(Rectangle a, double oranA, Rectangle b, double oranB) {
        // İki dikdörtgeni de daraltıp, Java'nın yerleşik kesişim kontrolünü
        // kullanıyoruz.
        return daralt(a, oranA).intersects(daralt(b, oranB));
    }
}
