package cs.petrsu.ru.imitnews.remote;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Kovalchuk Denis on 22.11.16.
 * Email: deniskk25@gmail.com
 */

public class HtmlPage {
    private final String url;

    HtmlPage(String url) {
        this.url = url;
    }

    Document get() {
        Document doc;

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            return null;
        }
        return doc;
    }
}
