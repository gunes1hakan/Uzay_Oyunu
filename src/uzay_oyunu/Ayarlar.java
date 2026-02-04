package uzay_oyunu;

/**
 * Oyunun genel yapılandırma ayarlarını ve sabitlerini içeren sınıf.
 * Ekran boyutları, dosya yolları ve oyun mekaniği ile ilgili hız/konum
 * değerleri burada tutulur.
 */
public class Ayarlar {
    // --- Ekran Ayarları ---
    public static final int EKRAN_GENISLIK = 800; // Oyun penceresinin genişliği
    public static final int EKRAN_YUKSEKLIK = 600; // Oyun penceresinin yüksekliği
    public static final int GECIKME = 5; // Timer yenileme hızı (ms cinsinden). 5ms = akıcı animasyon.

    // --- Dosya Yolları (Assets) ---
    public static final String PATH_GEMI = "uzaygemisi.png";
    public static final String PATH_UFO = "uzayliUfo.png";
    public static final String PATH_MERMI = "bullet_plasma_cyan.png";
    public static final String PATH_PATLAMA = "PatlamaEfekti.png";
    public static final String PATH_ROKET = "UzayliRoketi.png";
    public static final String PATH_GEMI_PATLAMA = "UzayGemisiPatlamaEfekti.png";
    public static final String PATH_KIVILCIM = "kivilcim.png";
    public static final String PATH_GEMI_VURUS = "gemi_vurus.png";

    // --- Ses Dosyaları ---
    public static final String SES_ATES = "ates_sesi.wav";
    public static final String SES_PATLAMA = "yok_olus.wav"; // Gemi veya Ufo yok olduğunda
    public static final String SES_ROKET = "roket_sesi.wav";
    public static final String SES_KIVILCIM = "metal_vurus.wav"; // Mermi ufoya çarptığında
    public static final String SES_BOMBA = "bomba_sesi.wav"; // Roket gemiye çarptığında

    // --- Oyun Mekaniği Sabitleri ---
    // Oyuncunun (Uzay Gemisi) başlangıç konumu ve hızı
    public static final int GEMI_BASLANGIC_X = 0;
    public static final int GEMI_BASLANGIC_Y = 490;
    public static final int GEMI_HIZ = 20; // Her tuş basımında geminin kat edeceği piksel sayısı

    // Düşmanın (UFO) başlangıç konumu ve hızı
    public static final int UFO_BASLANGIC_X = 0;
    public static final int UFO_BASLANGIC_Y = 0;
    public static final int UFO_HIZ = 2; // Ufo'nun yatay hareket hızı

    // Mermi ve Roket Hızları
    public static final int MERMI_HIZ = 5; // Oyuncunun ateşlediği merminin yukarı çıkış hızı
    public static final int ROKET_HIZ = 4; // Ufo'nun attığı roketin aşağı iniş hızı

    // --- Yeni Özellikler ---
    public static final int CAN_BARI_YUKSEKLIK = 50; // Üstteki sağlık barı için ayrılan yükseklik
    public static final int UFO_MAX_CAN = 10; // Ufo'nun canı
    public static final int GEMI_MAX_CAN = 4; // Geminin canı

    // --- Meteor Ayarları ---
    public static final String PATH_METEOR = "meteor.png";
    public static final int METEOR_HIZ = 3; // Roketlerden biraz daha yavaş
    public static final int METEOR_CAN = 3; // Patlatmak için gereken mermi sayısı
}
