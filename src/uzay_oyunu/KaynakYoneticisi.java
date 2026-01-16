package uzay_oyunu;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Oyunun görsel kaynaklarını (resimleri) yükleyen ve yöneten sınıf.
 * Performans için tüm resimler oyun başında bir kez yüklenir ve hafızada
 * tutulur.
 */
public class KaynakYoneticisi {
    private BufferedImage gemiResim;
    private BufferedImage ufoResim;
    private BufferedImage mermiResim;
    private BufferedImage patlamaResim;
    private BufferedImage roketResim;
    private BufferedImage gemiPatlamaResim;
    private BufferedImage dumanResim;

    public KaynakYoneticisi() {
        try {
            // Resimlerin diskten okunması işlemi
            // ImageIO.read(File) metodu, arka planda en uygun okuma yöntemini otomatik
            // seçer.

            gemiResim = ImageIO.read(dosyaBul(Ayarlar.PATH_GEMI));
            ufoResim = ImageIO.read(dosyaBul(Ayarlar.PATH_UFO));
            mermiResim = ImageIO.read(dosyaBul(Ayarlar.PATH_MERMI));
            patlamaResim = ImageIO.read(dosyaBul(Ayarlar.PATH_PATLAMA));
            roketResim = ImageIO.read(dosyaBul(Ayarlar.PATH_ROKET));
            gemiPatlamaResim = ImageIO.read(dosyaBul(Ayarlar.PATH_GEMI_PATLAMA));
            dumanResim = ImageIO.read(dosyaBul(Ayarlar.PATH_KIVILCIM));

        } catch (IOException e) {
            System.err.println("Kaynaklar yüklenirken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Dosyayı önce mevcut dizinde, bulamazsa "Uzay_Oyunu/" alt klasöründe arar.
     * Bu sayede oyun farklı dizinlerden çalıştırılsa bile dosyalar bulunur.
     */
    private File dosyaBul(String yol) {
        File dosya = new File(yol);
        if (!dosya.exists()) {
            // Eğer dosya kök dizinde yoksa, proje klasörüne bakmayı dene
            dosya = new File("Uzay_Oyunu/" + yol);
        }
        return dosya;
    }

    // --- Getter Metotları ---
    public BufferedImage getGemiResim() {
        return gemiResim;
    }

    public BufferedImage getUfoResim() {
        return ufoResim;
    }

    public BufferedImage getMermiResim() {
        return mermiResim;
    }

    public BufferedImage getPatlamaResim() {
        return patlamaResim;
    }

    public BufferedImage getRoketResim() {
        return roketResim;
    }

    public BufferedImage getGemiPatlamaResim() {
        return gemiPatlamaResim;
    }

    public BufferedImage getDumanResim() {
        return dumanResim;
    }
}
