package cs.petrsu.ru.imitnews.parser;

import java.util.Calendar;

/**
 * Created by Kovalchuk Denis on 13.12.16.
 * Email: deniskk25@gmail.com
 */

public class PetrSU {
    private static final String TAG = "PetrSU";
    private static final String url = "http://cs.petrsu.ru";
    private static final String newsArchiveUrl = "http://cs.petrsu.ru/news/archive.php.ru?q=news";
    private static final String filenameExtensionXml = ".xml";
    private static int currentYear = Calendar.getInstance().get(Calendar.YEAR) - 1;

    public static String getUrl() {
        return url;
    }

    public static String getNewsArchiveUrl() {
        currentYear--;
        return newsArchiveUrl + Integer.toString(currentYear + 1) + filenameExtensionXml;
    }
}
