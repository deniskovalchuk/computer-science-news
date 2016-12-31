package cs.petrsu.ru.imitnews.petrsu;

import java.util.Calendar;

/**
 * Created by Kovalchuk Denis on 13.12.16.
 * Email: deniskk25@gmail.com
 */

public class PetrSU {
    private static final String TAG = "PetrSU";
    private static final String defaultUrl = "http://cs.petrsu.ru";
    private static final String archivePath = "/news/archive.php.ru?q=news";
    private static final String filenameExtensionXml = ".xml";
    private static final int firstYear = 2002;
    private static int currentNewsYear = Calendar.getInstance().get(Calendar.YEAR) - 1;
    private static String url = defaultUrl;

    private PetrSU() {

    }

    public static void setArchiveUrl() {
        url = defaultUrl + archivePath + Integer.toString(currentNewsYear) + filenameExtensionXml;
    }

    public static String getUrl() {
        return url;
    }

    public static void setPreviousYear() {
        currentNewsYear--;
        setArchiveUrl();
    }

    public static boolean isValidYear() {
        return currentNewsYear >= firstYear;
    }
}
