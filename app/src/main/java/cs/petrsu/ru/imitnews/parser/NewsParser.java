package cs.petrsu.ru.imitnews.parser;

import android.os.Build;
import android.text.Html;

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
    private static Element newsHtml;

    private NewsParser() {

    }

    public static List<News> createNewsList(Document document) {
        List<News> newsList = new ArrayList<>();
        Elements allNews = document.select("div[id]");
        for (Element currentNews : allNews) {
            newsHtml = currentNews.select("div").get(0);
            String title = newsHtml.select("span.title").text();
            String date = newsHtml.select("b").text();
            String content = parseContent();
            newsList.add(new News(newsHtml.toString(), title, date, content));
        }
        return newsList;
    }

    private static String parseContent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(newsHtml.toString(), Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(newsHtml.toString()).toString();
        }
    }
}