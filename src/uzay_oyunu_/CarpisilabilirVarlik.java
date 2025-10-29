package uzay_oyunu_;

import java.awt.Rectangle;

public abstract class CarpisilabilirVarlik {        //kesişim hesaplarını yapabileceğimiz ortak bir abstract yapısı oluşturuyorum.
    protected abstract int getXDegeri();		
    protected abstract int getYDegeri();		
    protected abstract int getGenişlik();		
    protected abstract int getYukseklik();

    public Rectangle sinirDikdortgeni() {
        return new Rectangle(getXDegeri(), getYDegeri(), getGenişlik(), getYukseklik());
    }
}

