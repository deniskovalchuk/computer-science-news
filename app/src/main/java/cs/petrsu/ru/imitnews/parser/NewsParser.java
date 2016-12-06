package cs.petrsu.ru.imitnews.parser;

import android.content.Context;

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

    public NewsParser(Context context, Document htmlPage) {
        this.context = context;
        this.htmlPage = htmlPage;
    }

    public static String getUrl() {
        return url;
    }

    public List<News> getListNews() {
        List<News> newsList = new ArrayList<>();

        Elements newsHtml = htmlPage.select("div[id]");
        for (Element currentNewsHtml : newsHtml) {
            News news = new News();

            // Get news div
            Element newsHeadline = currentNewsHtml.select("div").get(1);
            // Get and set a date
            news.setDate(newsHeadline.select("b").text());

            // Get and set a title
            String title = newsHeadline.select("span.title").text();
            if (title.isEmpty()) {
                news.setTitle(context.getString(R.string.default_title) + news.getDate());
                news.setDate("");
            } else {
                news.setTitle(title);
            }

            // Get and set a tag 'new'
            news.setTag(newsHeadline.select("span[style]").text());
            // Get and set news content
            news.setContent(currentNewsHtml.select("div").get(2).text());
            newsList.add(news);
        }
        return newsList;
    }
}

