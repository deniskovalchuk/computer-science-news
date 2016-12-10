package cs.petrsu.ru.imitnews.parser;

import android.content.Context;

import org.jsoup.Jsoup;
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

        Elements allNews = htmlPage.select("div[id]");
        for (Element currentNews : allNews) {
            News news = new News();

            // Get news div
            Element newsHtml = currentNews.select("div").get(0);
            // Set title for news recyclerview
            setTitleNews(news, newsHtml);

            // Set correct image url
            int countImages = currentNews.select("img").size();
            for (int i = 0; i < countImages; i++) {
                String imageUrl = currentNews.select("img").get(i).attr("src");
                currentNews.html(currentNews.toString().replace(imageUrl, url + imageUrl));
            }

            news.setContent(currentNews.toString());
            newsList.add(news);
        }
        return newsList;
    }

    private void setTitleNews(News news, Element newsHeadline) {
        String date = newsHeadline.select("b").text();
        String title = newsHeadline.select("span.title").text();
        if (title.isEmpty()) {
            news.setTitle(context.getString(R.string.default_title) + date);
        } else {
            news.setTitle(title);
        }
    }
}

