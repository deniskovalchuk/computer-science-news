package cs.petrsu.ru.imitnews.petrsu;

import java.util.Calendar;

/**
 * Created by Kovalchuk Denis on 23.12.16.
 * Email: deniskk25@gmail.com
 */

public class ArchiveUrl implements UrlStrategy {
    private static final String newsArchiveUrl = "http://cs.petrsu.ru/news/archive.php.ru?q=news";
    private static final String filenameExtensionXml = ".xml";
    private static int currentYear = Calendar.getInstance().get(Calendar.YEAR) - 1;

    @Override
    public String getUrl() {
        return newsArchiveUrl + Integer.toString(currentYear) + filenameExtensionXml;
    }

    @Override
    public void setPreviousYearUrl() {
        currentYear--;
    }
}
