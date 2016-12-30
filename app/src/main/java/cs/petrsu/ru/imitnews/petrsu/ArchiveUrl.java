package cs.petrsu.ru.imitnews.petrsu;

/**
 * Created by Kovalchuk Denis on 23.12.16.
 * Email: deniskk25@gmail.com
 */

public class ArchiveUrl extends NewsUrl {
    private static final String archivePath = "/news/archive.php.ru?q=news";
    private static final String filenameExtensionXml = ".xml";

    @Override
    public String getUrl() {
        return url + archivePath + Integer.toString(currentYear) + filenameExtensionXml;
    }

    @Override
    public void setPreviousYear() {
        currentYear--;
    }
}
