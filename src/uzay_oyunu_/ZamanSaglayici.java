package uzay_oyunu_;

@FunctionalInterface
public interface ZamanSaglayici {						//Şu an ki zamanı dinamik olarak çekebilecek şekilde yaptık,ilerde oyuna pause gibi olaylar eklendiği zaman bu ifadeleri Oyun adlı class yapımda bir değişken olarak oluşturmam sıkıntı çıkaracaktı.
    long simdiMs();										

    static ZamanSaglayici sistem() {					
    	return () -> System.currentTimeMillis();		
    }
}
