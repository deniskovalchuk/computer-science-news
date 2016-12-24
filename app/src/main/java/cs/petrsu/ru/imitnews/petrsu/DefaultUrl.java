package cs.petrsu.ru.imitnews.petrsu;

/**
 * Created by Kovalchuk Denis on 23.12.16.
 * Email: deniskk25@gmail.com
 */

public class DefaultUrl implements UrlStrategy {
    private static final String url = "http://cs.petrsu.ru";

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setPreviousYearUrl() {

    }
}
