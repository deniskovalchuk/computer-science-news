package ru.petrsu.cs.news.remote;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Represents html page of http://cs.petrsu.ru.
 *
 * @author Kovalchuk Denis
 * @version 1.0
 */

class HtmlPage {
    private final String url;

    HtmlPage(String url) {
        this.url = url;
    }

    Document get() throws IOException {
        return Jsoup.connect(url).get();
    }
}
