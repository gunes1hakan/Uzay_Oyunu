package uzay_oyunu_;

import java.awt.Rectangle;

public final class CarpismaPolitikasi {	    //bu sınıfı yalnızca static metotlardan oluşturduğumuz için direkt erişebililir yapıları vardır,bu sebepten bu sınıfa ait obje oluşturulmasını engellemek	
    private CarpismaPolitikasi() {}			//amacı ile sınıfı final,constructor'ı ise private olarak oluşturdum.

    public static Rectangle daralt(Rectangle r, double oran) {  		//yapısına girilmiş olan Rectangle sınıfı üyelerinin tekrardan yükselik gibi verilerini alarak daraltılmış bir değer varsa bunun değerlerini hesaplamaktadır.
        int yeniGenislik = (int)Math.round(r.width  * oran);
        int yeniYukseklik = (int)Math.round(r.height * oran);
        int x = r.x + (r.width  - yeniGenislik)/2;						.
        int y = r.y + (r.height - yeniYukseklik)/2;
        return new Rectangle(x, y, yeniGenislik, yeniYukseklik);
    }

    public static boolean kesisir(Rectangle a, double oranA, Rectangle b, double oranB) {		//yapısına yollanmış iki ayrı Rectangle sınıfını ve bu sınıfta yer alan nesneleri(mermi ya da roket) daraltmak istiyorsak daraltma oranları yolluyoruz,daraltma yapılmayacaksa 1 girilmesi gerekiyor.
        return daralt(a, oranA).intersects(daralt(b, oranB));									
    }
}
