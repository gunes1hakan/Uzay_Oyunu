package uzay_oyunu;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

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
    private static final float GAIN_PATLAMA = 2.0f;
    private static final float GAIN_ROKET = -18.0f;
    private static final float GAIN_KIVILCIM = -12.0f;
    private static final float GAIN_MERMI = -12.0f;
    private static final float GAIN_BOMBA = -8.0f;
    private static final float GAIN_METEOR = -6.0f; // Meteor çarpma sesi

    public SesYoneticisi() {
        sesHavuzlari = new HashMap<>();

        // 1. Sesleri Yükle (Standart)
        sesYukle(Ayarlar.SES_ATES, 6, GAIN_MERMI);
        sesYukle(Ayarlar.SES_PATLAMA, 3, GAIN_PATLAMA);
        sesYukle(Ayarlar.SES_KIVILCIM, 4, GAIN_KIVILCIM);
        sesYukle(Ayarlar.SES_ROKET, 20, GAIN_ROKET);
        sesYukle(Ayarlar.SES_BOMBA, 3, GAIN_BOMBA);
        sesYukle(Ayarlar.SES_METEOR, 3, GAIN_METEOR);
    }

    private void sesYukle(String dosyaAdi, int kopyaSayisi, float desibel) {
        Clip[] klipler = new Clip[kopyaSayisi];
        File sesDosyasi = dosyaBul(dosyaAdi);
        if (!sesDosyasi.exists())
            return;

        try {
            for (int i = 0; i < kopyaSayisi; i++) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(sesDosyasi);
                Clip clip = AudioSystem.getClip();
                clip.open(ais); // Dosyayı belleğe yükler (Burada gecikme olmaz)

                if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    float yeniGain = Math.min(Math.max(desibel, gainControl.getMinimum()), gainControl.getMaximum());
                    gainControl.setValue(yeniGain);
                }

                klipler[i] = clip;
            }
            sesHavuzlari.put(dosyaAdi, klipler);
        } catch (Exception e) {
            System.err.println("Ses yüklenirken hata (" + dosyaAdi + "): " + e.getMessage());
        }
    }

    public void oynat(String dosyaAdi) {
        if (!sesHavuzlari.containsKey(dosyaAdi))
            return;

        Clip[] havuz = sesHavuzlari.get(dosyaAdi);
        for (Clip clip : havuz) {
            if (clip != null && !clip.isRunning()) {
                clip.setFramePosition(0);
                clip.start();
                return;
            }
        }
    }

    /**
     * Boşta olan bir klibi bulur, döngüsel olarak başlatır ve o klibi döndürür.
     * Bu sayede çağıran kişi (Roket objesi) bu sesi ne zaman durduracağını bilir.
     */
    public Clip loopBaslatGetir(String dosyaAdi) {
        if (!sesHavuzlari.containsKey(dosyaAdi))
            return null;

        Clip[] havuz = sesHavuzlari.get(dosyaAdi);
        for (Clip clip : havuz) {
            if (clip != null && !clip.isRunning()) {
                clip.setFramePosition(0);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                return clip;
            }
        }
        return null; // Boş yer yoksa ses çalmaz
    }

    public void loopBaslat(String dosyaAdi) {
        loopBaslatGetir(dosyaAdi);
    }

    public void loopDurdur(String dosyaAdi) {
        if (!sesHavuzlari.containsKey(dosyaAdi))
            return;

        Clip[] havuz = sesHavuzlari.get(dosyaAdi);
        for (Clip clip : havuz) {
            if (clip.isRunning()) {
                clip.stop();
            }
        }
    }

    public void tumSesleriDurdur(String haricTutulacakSes) {
        for (Map.Entry<String, Clip[]> entry : sesHavuzlari.entrySet()) {
            // Eğer bu ses hariç tutulacaksa döngüyü atla
            if (haricTutulacakSes != null && entry.getKey().equals(haricTutulacakSes)) {
                continue;
            }

            for (Clip clip : entry.getValue()) {
                if (clip != null && clip.isRunning()) {
                    clip.stop();
                }
            }
        }
    }

    // Geriye dönük uyumluluk için parametresiz versiyonu da tutabiliriz veya
    // silebiliriz.
    // Şimdilik temiz olması için sadece parametreliyi kullanacağız.
    public void tumSesleriDurdur() {
        tumSesleriDurdur(null);
    }

    private File dosyaBul(String yol) {
        File dosya = new File(yol);
        if (!dosya.exists()) {
            dosya = new File("Uzay_Oyunu/" + yol);
        }
        return dosya;
    }
}
