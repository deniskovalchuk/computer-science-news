package cs.petrsu.ru.imitnews.parser;

import android.os.Build;
import android.text.Html;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cs.petrsu.ru.imitnews.news.NewsLab;

/**
 * Created by Kovalchuk Denis on 22.11.16.
 * Email: deniskk25@gmail.com
 */

public class NewsParser {
    private static final String TAG = "NewsParser";
    private static final String defaultTitle = "Новость от ";
    private static Element newsHtml;

    public static void createListNews(Document document) {
        NewsLab newsLab = NewsLab.getInstance();
        Elements allNews = document.select("div[id]");
        for (Element currentNews : allNews) {
            newsHtml = currentNews.select("div").get(0);
            newsLab.addNews(newsHtml.toString(), parseTitle(), parseContent());
        }
    }

    private static String parseTitle() {
        String title = newsHtml.select("span.title").text();
        String date = newsHtml.select("b").text();
        return title.isEmpty() ? defaultTitle + date : title;
    }

    private static String parseContent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(newsHtml.toString(), Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(newsHtml.toString()).toString();
        }
    }
}