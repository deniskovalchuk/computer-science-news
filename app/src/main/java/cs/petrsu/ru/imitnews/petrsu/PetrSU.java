package cs.petrsu.ru.imitnews.petrsu;

/**
 * Created by Kovalchuk Denis on 13.12.16.
 * Email: deniskk25@gmail.com
 */

public class PetrSU {
    private static final String TAG = "PetrSU";
    private static NewsUrl url = new DefaultUrl();

    private PetrSU() {

    }

    public static void setUrlStrategy(NewsUrl url) {
        PetrSU.url = url;
    }

    public static String getUrl() {
        return url.getUrl();
    }

    public static void setPreviousYear() {
        url.setPreviousYear();
    }

    public static boolean isValidYear() {
        return url.isValidYear();
    }
}
