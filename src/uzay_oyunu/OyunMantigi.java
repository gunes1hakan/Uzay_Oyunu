package uzay_oyunu;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Oyunun mantık katmanı (Model).
 * Tüm hesaplamalar, çarpışma kontrolleri, düşman oluşturma ve oyunun durumu
 * (kazanma/kaybetme) burada yönetilir.
 * Bu sınıf ekran çizimi yapmaz, sadece verileri günceller.
 */
public class OyunMantigi {
    // Oyun Varlıkları
    private UzayGemisi gemi;
    private Ufo ufo;
    private ArrayList<Mermi> mermiler; // Oyuncunun attığı mermilerin listesi
    private ArrayList<Roket> roketler; // Ufo'nun attığı roketlerin listesi
    private KaynakYoneticisi kaynaklar; // Görsel kaynaklara erişim için

    // İstatistikler
    private int gecenSure = 0; // Oyunun başından beri geçen süre (ms)
    private int harcananMermi = 0; // Atılan toplam mermi sayısı

    // Oyun Durum Bayrakları
    private boolean oyunBitti = false; // Oyunun sona erip ermediğini kontrol eder
    private boolean kazandi = false; // true if player wins, false if ufo wins

    // --- Bombalama Sistemi ---
    private Random rng = new Random();
    private long sonrakiBombaZamaniMs = 0;
    private final int bombaMinAralikMs = 1500; // Ufo en az 1.5 saniyede bir ateş eder
    private final int bombaMaxAralikMs = 2200; // Ufo en geç 2.2 saniyede bir ateş eder

    // --- Patlama Animasyonu Yönetimi ---
    // Patlamalar "fire-and-forget" değildir, oyunun akışını donduran özel
    // durumlardır.

    // Ufo Patlaması
    private boolean ufoPatlamaAktif = false;
    private int patlamaAdimSayaci = 0; // Animasyonun kaçıncı karesinde olduğu
    private final int patlamaMaksAdim = 10; // Animasyon toplam kaç kare sürecek (1 sn)
    private long patlamaBaslangicZamaniMs = 0;
    private final int patlamaArtisAraligiMs = 100; // Her karenin ekranda kalma süresi (ms)

    // Gemi Patlaması
    private boolean gemiPatlamaAktif = false;
    private int imhaAdimSayaci = 0;
    private final int imhaMaksAdim = 10;
    private long imhaBaslangicZamaniMs = 0;
    private final int imhaArtisAraligiMs = 100;

    /**
     * Oyun mantığını başlatır ve varlıkları oluşturur.
     */
    public OyunMantigi(KaynakYoneticisi kaynaklar) {
        this.kaynaklar = kaynaklar;
        baslat();
    }

    /**
     * Oyunu başlangıç durumuna resetler.
     */
    public void baslat() {
        gemi = new UzayGemisi(Ayarlar.GEMI_BASLANGIC_X, Ayarlar.GEMI_BASLANGIC_Y, kaynaklar.getGemiResim());
        ufo = new Ufo(Ayarlar.UFO_BASLANGIC_X, Ayarlar.UFO_BASLANGIC_Y, kaynaklar.getUfoResim());
        mermiler = new ArrayList<>();
        roketler = new ArrayList<>();

        gecenSure = 0;
        harcananMermi = 0;
        oyunBitti = false;

        ufoPatlamaAktif = false;
        patlamaAdimSayaci = 0;

        gemiPatlamaAktif = false;
        imhaAdimSayaci = 0;
    }

    /**
     * Oyunun ana döngüsü. Her timer tetiklenmesinde çağrılır.
     * Varlıkların pozisyonlarını günceller ve kuralları işletir.
     */
    public void guncelle() {
        if (oyunBitti)
            return; // Oyun bittiyse (animasyonlar dahil) işlem yapma.

        long simdi = System.currentTimeMillis();

        // Eğer Ufo vurulduysa patlama animasyonunu oynat ve diğer her şeyi dondur
        if (ufoPatlamaAktif) {
            animasyonGuncelle(simdi, true);
            return;
        }

        // Eğer Gemi vurulduysa patlama animasyonunu oynat
        if (gemiPatlamaAktif) {
            animasyonGuncelle(simdi, false);
            return;
        }

        // --- Normal Oyun Akışı ---
        gecenSure += Ayarlar.GECIKME;

        gemi.guncelle(); // Geminin hareketi
        ufo.guncelle(); // Ufo'nun hareketi (Sınır kontrolü sınıfın içinde yapılır)

        mermileriGuncelle(); // Mermilerin hareketi ve ekran dışına çıkması
        roketleriGuncelle(); // Roketlerin hareketi

        rastgeleBombala(simdi); // Ufo'nun ateş etme kararı
        carpismalariKontrolEt(); // Mermi-Ufo veya Roket-Gemi çarpışmaları
    }

    /**
     * Patlama animasyonlarının kare kare ilerlemesini yönetir.
     * 
     * @param simdi Şu anki zaman (ms)
     * @param ufoMu Patlayan şey Ufo mu? (false ise gemidir)
     */
    private void animasyonGuncelle(long simdi, boolean ufoMu) {
        long baslangic = ufoMu ? patlamaBaslangicZamaniMs : imhaBaslangicZamaniMs;
        int adim = ufoMu ? patlamaAdimSayaci : imhaAdimSayaci;
        int aralik = ufoMu ? patlamaArtisAraligiMs : imhaArtisAraligiMs;

        if (simdi - baslangic >= aralik) {
            adim++;
            if (ufoMu) {
                patlamaAdimSayaci = adim;
                patlamaBaslangicZamaniMs = simdi;
            } else {
                imhaAdimSayaci = adim;
                imhaBaslangicZamaniMs = simdi;
            }

            // Animasyon bittiğinde oyun da biter
            if (adim >= (ufoMu ? patlamaMaksAdim : imhaMaksAdim)) {
                oyunBitti = true;
                kazandi = ufoMu; // Eğer Ufo patladıysa oyuncu kazanmışdır.
            }
        }
    }

    private void mermileriGuncelle() {
        Iterator<Mermi> it = mermiler.iterator();
        while (it.hasNext()) {
            Mermi m = it.next();
            m.guncelle();
            // Ekran dışına çıkan mermileri bellekten sil
            if (!m.isAktif())
                it.remove();
        }
    }

    private void roketleriGuncelle() {
        Iterator<Roket> it = roketler.iterator();
        while (it.hasNext()) {
            Roket r = it.next();
            r.guncelle();
            if (!r.isAktif())
                it.remove();
        }
    }

    /**
     * Ufo'nun belirli aralıklarla rastgele roket atmasını sağlar.
     */
    private void rastgeleBombala(long simdi) {
        // İlk atış zamanını belirle
        if (sonrakiBombaZamaniMs == 0) {
            sonrakiBombaZamaniMs = simdi + rng.nextInt(bombaMaxAralikMs - bombaMinAralikMs + 1) + bombaMinAralikMs;
            return;
        }

        // Zamanı geldiyse ateş et
        if (simdi >= sonrakiBombaZamaniMs) {
            // Roketi Ufo'nun tam ortasından ve altından çıkart
            int roketX = ufo.getX() + (ufo.getGenislik() / 2) - (kaynaklar.getRoketResim().getWidth() / 10 / 2);
            int roketY = ufo.getY() + ufo.getYukseklik();

            roketler.add(new Roket(roketX, roketY, kaynaklar.getRoketResim()));

            // Bir sonraki atış zamanını kur
            sonrakiBombaZamaniMs = simdi + rng.nextInt(bombaMaxAralikMs - bombaMinAralikMs + 1) + bombaMinAralikMs;
        }
    }

    /**
     * Tüm çarpışma senaryolarını kontrol eder.
     */
    private void carpismalariKontrolEt() {
        // 1. Senaryo: Bizim mermimiz Ufo'yu vurdu mu?
        for (Mermi m : mermiler) {
            // Mermi ve Ufo'nun sınırlarını (Rectangle) alıp kesişim var mı bakıyoruz.
            if (CarpismaPolitikasi.kesisir(m.getSinirlar(), 1.0, ufo.getSinirlar(), 1.0)) {
                ufoPatlamaAktif = true;
                patlamaBaslangicZamaniMs = System.currentTimeMillis();
                break; // Tek bir vuruş yeterli
            }
        }

        // 2. Senaryo: Ufo'nun roketi bizi vurdu mu?
        for (Roket r : roketler) {
            // Çarpışma hassasiyetini artırmak için sınırlar daraltılır (0.6 ve 0.8
            // oranları).
            // Bu sayede "sıyırıp geçme" hissi daha gerçekçi olur.
            if (CarpismaPolitikasi.kesisir(r.getSinirlar(), 0.6, gemi.getSinirlar(), 0.8)) {
                gemiPatlamaAktif = true;
                imhaBaslangicZamaniMs = System.currentTimeMillis();
                break;
            }
        }
    }

    /**
     * Oyuncunun ateş etme eylemini gerçekleştirir (Space tuşu).
     */
    public void atesEt() {
        // Zaten patlama varsa ateş edilemez
        if (!ufoPatlamaAktif && !gemiPatlamaAktif) {
            // Mermiyi geminin tam ortasından çıkart
            int mermiX = gemi.getX() + 15; // +15 görsel ayarlama
            int mermiY = gemi.getY() - kaynaklar.getMermiResim().getHeight() / 10;
            mermiler.add(new Mermi(mermiX, mermiY, kaynaklar.getMermiResim()));
            harcananMermi++;
        }
    }

    public void gemiHareket(int yon) {
        if (!gemiPatlamaAktif)
            gemi.hareketEt(yon);
    }

    public void ufoHareket() {
        if (!ufoPatlamaAktif) {
            ufo.guncelle();
        }
    }

    public void gemiDur() {
        gemi.dur();
    }

    public UzayGemisi getGemi() {
        return gemi;
    }

    public Ufo getUfo() {
        return ufo;
    }

    public ArrayList<Mermi> getMermiler() {
        return mermiler;
    }

    public ArrayList<Roket> getRoketler() {
        return roketler;
    }

    public boolean isUfoPatlamaAktif() {
        return ufoPatlamaAktif;
    }

    public int getPatlamaAdimSayaci() {
        return patlamaAdimSayaci;
    }

    public boolean isGemiPatlamaAktif() {
        return gemiPatlamaAktif;
    }

    public int getImhaAdimSayaci() {
        return imhaAdimSayaci;
    }

    public boolean isOyunBitti() {
        return oyunBitti;
    }

    public boolean isKazandi() {
        return kazandi;
    }

    public int getHarcananMermi() {
        return harcananMermi;
    }

    public int getGecenSure() {
        return gecenSure;
    }
}
