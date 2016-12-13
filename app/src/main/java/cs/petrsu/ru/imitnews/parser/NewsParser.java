package cs.petrsu.ru.imitnews.parser;

import android.content.Context;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import cs.petrsu.ru.imitnews.news.News;

/**
 * Created by Kovalchuk Denis on 22.11.16.
 * Email: deniskk25@gmail.com
 */

public class NewsParser {
    private static final String TAG = "NewsParser";

    public static List<News> createListNews(Context context, Document html) {
        List<News> newsList = new ArrayList<>();
        Elements allNews = html.select("div[id]");

        for (Element currentNews : allNews) {
            Element newsHtml = currentNews.select("div").get(0);
            News news = new News(context, newsHtml);
            newsList.add(news);
        }

        return newsList;
    }
}

