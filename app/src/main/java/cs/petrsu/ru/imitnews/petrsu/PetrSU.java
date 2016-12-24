package cs.petrsu.ru.imitnews.petrsu;

/**
 * Created by Kovalchuk Denis on 13.12.16.
 * Email: deniskk25@gmail.com
 */

public class PetrSU {
    private static final String TAG = "PetrSU";
    private static UrlStrategy urlStrategy = new DefaultUrl();

    public static void setUrlStrategy(UrlStrategy urlStrategy) {
        PetrSU.urlStrategy = urlStrategy;
    }

    public static String getUrl() {
        return urlStrategy.getUrl();
    }

    public static void setPreviousYearUrl() {
        urlStrategy.setPreviousYearUrl();
    }
}
