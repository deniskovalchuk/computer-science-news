package cs.petrsu.ru.imitnews.parser;

import android.content.Context;

import org.jsoup.nodes.Document;

import java.util.List;

import cs.petrsu.ru.imitnews.news.News;

/**
 * Created by Kovalchuk Denis on 13.12.16.
 * Email: deniskk25@gmail.com
 */

public class PetrSU {
    private static final String TAG = "PetrSU";
    private static final String url = "http://cs.petrsu.ru";

    public static String getUrl() {
        return url;
    }

    public static List<News> createNewsList(Context context, Document html) {
        return NewsParser.createListNews(context, html);
    }
}
