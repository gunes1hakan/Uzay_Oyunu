package uzay_oyunu;

import java.awt.image.BufferedImage;

/**
 * Uzay gemisine roket çarptığında oluşan özel görsel efekt.
 * VurusEfekti sınıfından türetilmiştir.
 */
public class GemiVurusEfekti extends VurusEfekti {

    public GemiVurusEfekti(Roket roket, BufferedImage resim) {
        // Roketin tam merkezini hesapla ve üst sınıfa gönder
        super(
                roket.getX() + roket.getGenislik() / 2,
                roket.getY() + roket.getYukseklik() / 2,
                resim);
    }
}
