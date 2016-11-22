package cs.petrsu.ru.imitnews.parser;

import android.content.Context;
import android.util.Log;

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
    private static final String url = "http://cs.petrsu.ru";
    private Context context;
    private Document htmlPage;
    private Elements newsHtml;

    public NewsParser(Context context, Document htmlPage) {
        this.context = context;
        this.htmlPage = htmlPage;
    }

    public static String getUrl() {
        return url;
    }

    public List<News> getListNews() {
        List<News> newsList = new ArrayList<>();

        newsHtml = htmlPage.select("div[id]");
        for (Element currentNewsHtml : newsHtml) {
            News news = new News();

            // Title, date, tag
            Element newsHeadline = currentNewsHtml.select("div").get(1);
            // Get and set a date
            String date = newsHeadline.select("b").text();
            news.setDate(date);

            // Get and set a title
            String title = newsHeadline.select("span.title").text();
            if (title == "") {
                news.setTitle(context.getString(R.string.default_title) + date);
                news.setDate("");
            } else {
                news.setTitle(title);
            }

            // Get and set a tag
            news.setTag(newsHeadline.select("span[style]").text());

            // Content
            Element newsContent = currentNewsHtml.select("div").get(2);
            news.setContent(newsContent.text());
            newsList.add(news);
        }
        return newsList;
    }
}

