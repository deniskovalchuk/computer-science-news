package cs.petrsu.ru.imitnews.parser;

import android.content.Context;
import android.os.Build;
import android.text.Html;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import cs.petrsu.ru.imitnews.R;
import cs.petrsu.ru.imitnews.news.News;

/**
 * Created by Kovalchuk Denis on 22.11.16.
 * Email: deniskk25@gmail.com
 */

public class NewsParser {
    private static final String TAG = "NewsParser";
    private static Element newsHtml;

    public static List<News> createListNews(Context context, Document document) {
        List<News> newsList = new ArrayList<>();
        Elements allNews = document.select("div[id]");
        for (Element currentNews : allNews) {
            newsHtml = currentNews.select("div").get(0);
            News news = new News(newsHtml.toString(), parseTitle(context), parseContent());
            newsList.add(news);
        }
        return newsList;
    }

    private static String parseTitle(Context context) {
        String title = newsHtml.select("span.title").text();
        String date = newsHtml.select("b").text();
        return title.isEmpty() ? context.getString(R.string.default_title) + date : title;
    }

    private static String parseContent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(newsHtml.toString(), Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(newsHtml.toString()).toString();
        }
    }
}