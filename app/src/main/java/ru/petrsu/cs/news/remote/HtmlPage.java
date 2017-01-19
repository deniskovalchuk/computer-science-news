package ru.petrsu.cs.news.remote;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Kovalchuk Denis on 22.11.16.
 * Email: deniskk25@gmail.com
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
