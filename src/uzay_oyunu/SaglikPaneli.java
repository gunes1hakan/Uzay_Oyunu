package uzay_oyunu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Oyunun üst kısmında duran ve oyun alanından bağımsız olan bilgi paneli (HUD).
 * UFO'nun kalan canını bir bar şeklinde gösterir.
 */
public class SaglikPaneli extends JPanel implements ActionListener {

    // Oyun mantığına erişmemiz gerekiyor ki can değerini okuyabilelim
    private OyunMantigi mantik;
    private Timer timer;

    public SaglikPaneli(OyunMantigi mantik) {
        this.mantik = mantik;

        // Panelin boyutu (Genişlik: Ekran, Yükseklik: Ayarlardaki değer)
        this.setPreferredSize(new Dimension(Ayarlar.EKRAN_GENISLIK, Ayarlar.CAN_BARI_YUKSEKLIK));
        this.setBackground(Color.DARK_GRAY); // Arka plan

        // Ekranı sürekli güncellemek için timer
        timer = new Timer(50, this); // 20 FPS yeterli
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Ufo ufo = mantik.getUfo();
        UzayGemisi gemi = mantik.getGemi();
        Ufo klon = mantik.getUfoKlon();

        if (ufo == null || gemi == null)
            return;

        // Klon aktif mi kontrol et
        boolean klonAktif = klon != null && klon.isAktif() && !klon.oluMu();

        // Bar boyutları (klon varsa daha dar barlar)
        int barGenislik = klonAktif ? 200 : 300;
        int barYukseklik = 20;
        int araBosluk = klonAktif ? 30 : 50;

        // Toplam genişliği hesapla
        int toplamGenislik = klonAktif
                ? (barGenislik * 3 + araBosluk * 2) // 3 bar
                : (barGenislik * 2 + araBosluk); // 2 bar

        int baslangicX = (Ayarlar.EKRAN_GENISLIK - toplamGenislik) / 2;
        int barY = (Ayarlar.CAN_BARI_YUKSEKLIK - barYukseklik) / 2;

        // --- 1. SOL TARAFA GEMİ CANI (PLAYER) ---
        drawBar(g, baslangicX, barY, barGenislik, barYukseklik,
                gemi.getCan(), Ayarlar.GEMI_MAX_CAN, "PLAYER", Color.BLUE);

        // --- 2. ORTAYA UFO CANI (ENEMY) ---
        int ufoBarX = baslangicX + barGenislik + araBosluk;

        Color ufoRenk = Color.GREEN;
        double ufoOran = (double) ufo.getCan() / Ayarlar.UFO_MAX_CAN;
        if (ufoOran <= 0.3)
            ufoRenk = Color.RED;
        else if (ufoOran <= 0.6)
            ufoRenk = Color.YELLOW;

        drawBar(g, ufoBarX, barY, barGenislik, barYukseklik,
                ufo.getCan(), Ayarlar.UFO_MAX_CAN, "ENEMY", ufoRenk);

        // --- 3. SAĞ TARAFA KLON CANI (CLONE) - Sadece aktifse ---
        if (klonAktif) {
            int klonBarX = ufoBarX + barGenislik + araBosluk;

            Color klonRenk = Color.ORANGE;
            double klonOran = (double) klon.getCan() / Ayarlar.UFO_MAX_CAN;
            if (klonOran <= 0.3)
                klonRenk = Color.RED;
            else if (klonOran <= 0.6)
                klonRenk = Color.YELLOW;

            drawBar(g, klonBarX, barY, barGenislik, barYukseklik,
                    klon.getCan(), Ayarlar.UFO_MAX_CAN, "CLONE", klonRenk);
        }
    }

    private void drawBar(Graphics g, int x, int y, int w, int h, int can, int maxCan, String label, Color renk) {
        // Çerçeve
        g.setColor(Color.BLACK);
        g.drawRect(x, y, w, h);

        // Doluluk
        double oran = (double) can / maxCan;
        if (oran < 0)
            oran = 0;
        int doluW = (int) (w * oran);

        g.setColor(renk);
        g.fillRect(x, y, doluW, h);

        // Yazılar
        g.setColor(Color.WHITE);
        g.drawString(label, x, y - 5);

        // Yüzdelik Gösterim (Örn: %100)
        int yuzde = (int) (oran * 100);
        String yuzdeYazi = "%" + yuzde;

        // Yazıyı barın içine ortala
        int yaziGenislik = g.getFontMetrics().stringWidth(yuzdeYazi);
        g.setColor(Color.WHITE); // Veya siyah, kontrasta göre
        if (renk == Color.YELLOW || renk == Color.GREEN)
            g.setColor(Color.BLACK); // Açık renk üstüne siyah yazı

        g.drawString(yuzdeYazi, x + (w - yaziGenislik) / 2, y + h - 5);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Her timer tikinde paneli yeniden çiz
        repaint();
    }
}
