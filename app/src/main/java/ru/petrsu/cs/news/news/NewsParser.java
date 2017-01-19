package ru.petrsu.cs.news.news;

import android.os.Build;
import android.text.Html;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Kovalchuk Denis on 17.01.17.
 * Email: deniskk25@gmail.com
 */

public final class NewsParser {
    public static List<News> createNewsList(Document document) {
        Elements allNews = document.select("div[id]");
        List<News> data = new ArrayList<>();

        for (Element currentNews : allNews) {
            Element newsHtml = currentNews.select("div").get(0);
            String title = parseTitle(newsHtml);
            String date = parseDate(newsHtml);
            String content = parseContent(newsHtml);
            data.add(new News(newsHtml.toString(), title, date, content));
        }

        return data;
    }

    private static String parseTitle(Element newsHtml) {
        return newsHtml.select("span.title").text();
    }

    private static String parseDate(Element newsHtml) {
        return newsHtml.select("b").text();
    }

    private static String parseContent(Element newsHtml) {
        String contentWithoutImg = newsHtml.toString().replaceAll("<img.+?>", "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(contentWithoutImg, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(contentWithoutImg).toString();
        }
    }
}
