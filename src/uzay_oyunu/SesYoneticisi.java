package uzay_oyunu;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Oyun içi ses efektlerini yöneten sınıf.
 * Ses dosyalarını yükler, önbelleğe alır ve çalma isteklerini yönetir.
 * "Pooling" mantığı ile aynı sesin üst üste binmesini (polyphony) destekler.
 * Ayrıca her ses türü için farklı ses seviyesi (volume) ayarı sunar.
 */
public class SesYoneticisi {

    // Ses Klipleri Havuzu
    private Map<String, Clip[]> sesHavuzlari;

    // Ses Seviyeleri (Desibel cinsinden Gain)
    // 0.0 dB = Orijinal ses seviyesi
    // -6.0 dB = Ses şiddetinin yarısı
    // -10.0 dB veya daha düşük = Arka plan sesi
    private static final float GAIN_PATLAMA = 2.0f; // En gürültülü (Biraz boostlanmış)
    private static final float GAIN_ROKET = -5.0f; // Orta seviye
    private static final float GAIN_KIVILCIM = -8.0f; // Orta-Düşük seviye
    private static final float GAIN_MERMI = -12.0f; // En kısık (Çok tekrar ettiği için)

    public SesYoneticisi() {
        sesHavuzlari = new HashMap<>();

        // Sesleri Önden Yükle ve Seviyelerini Ayarla
        // Mermi: En kısık, en çok kopya (seri atış için)
        sesYukle(Ayarlar.SES_ATES, 6, GAIN_MERMI);

        // Patlama: En yüksek, standart kopya
        sesYukle(Ayarlar.SES_PATLAMA, 3, GAIN_PATLAMA);

        // Roket: Orta seviye
        sesYukle(Ayarlar.SES_ROKET, 3, GAIN_ROKET);

        // Kıvılcım (Vuruş): Orta seviye, çok kopya (çünkü çok sık çarpabilir)
        sesYukle(Ayarlar.SES_KIVILCIM, 4, GAIN_KIVILCIM);
    }

    /**
     * Belirtilen ses dosyasını belirtilen kopya sayısı kadar hafızaya yükler
     * ve ses seviyesini ayarlar.
     */
    private void sesYukle(String dosyaAdi, int kopyaSayisi, float desibel) {
        Clip[] klipler = new Clip[kopyaSayisi];

        File sesDosyasi = dosyaBul(dosyaAdi);
        if (!sesDosyasi.exists()) {
            return;
        }

        try {
            for (int i = 0; i < kopyaSayisi; i++) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(sesDosyasi);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);

                // Ses Seviyesi Ayarı (Master Gain Control)
                if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    // Belirtilen desibel değerini uygula ancak donanımın limitlerini aşma
                    float yeniGain = Math.min(Math.max(desibel, gainControl.getMinimum()), gainControl.getMaximum());
                    gainControl.setValue(yeniGain);
                }

                klipler[i] = clip;
            }

            sesHavuzlari.put(dosyaAdi, klipler);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Ses yüklenirken hata (" + dosyaAdi + "): " + e.getMessage());
        }
    }

    /**
     * İlgili ses efektini çalar.
     * Her çağrıldığında çalmaya çalışır (Cooldown YOK).
     */
    public void oynat(String dosyaAdi) {
        // 1. Havuzda bu ses var mı?
        if (!sesHavuzlari.containsKey(dosyaAdi))
            return;

        // 2. Boş Bir Klip Bul ve Çal
        Clip[] havuz = sesHavuzlari.get(dosyaAdi);
        for (Clip clip : havuz) {
            if (clip == null)
                continue;

            // Eğer klip çalışmıyorsa (bitmişse veya hiç başlamamışsa) kullan
            if (!clip.isRunning()) {
                clip.setFramePosition(0); // Başa sar
                clip.start();
                return; // Bir tane çaldık, metottan çık
            }
        }

        // Eğer tüm kanallar doluysa, yapacak bir şey yok.
        // Mermi sesleri için bu iyi bir şeydir, ses kartını boğmamış oluruz.
        // Çok önemli sesler için (Patlama gibi) "eskiyi durdur çal" mantığı eklenebilir
        // ama şu anki "sessiz kalma" stratejisi performans için daha iyidir.
    }

    private File dosyaBul(String yol) {
        File dosya = new File(yol);
        if (!dosya.exists()) {
            dosya = new File("Uzay_Oyunu/" + yol);
        }
        return dosya;
    }
}
