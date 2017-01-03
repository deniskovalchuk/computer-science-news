package ru.petrsu.cs.news.news;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kovalchuk Denis on 22.11.16.
 * Email: deniskk25@gmail.com
 */

public class NewsLab {
    private static final NewsLab newsLab = new NewsLab();
    private static List<News> newsList;

    private NewsLab() {
        newsList = new ArrayList<>();
    }

    public static NewsLab getInstance() {
        return newsLab;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        NewsLab.newsList = newsList;
    }

    public void addNewsList(List<News> newsList) {
        NewsLab.newsList.addAll(newsList);
    }

    public News getNews(int position) {
        return newsList.get(position);
    }

    public void addNews(News news) {
        newsList.add(news);
    }

    public void addNewsListToEnd(Document document) {
        newsList.addAll(createNewsList(document));
    }

    public void addNewsListToEnd(List<News> newsList) {
        NewsLab.newsList.addAll(newsList);
    }

    public List<News> createNewsList(Document document) {
        List<News> newsList = new ArrayList<>();
        Elements allNews = document.select("div[id]");
        for (Element currentNews : allNews) {
            Element newsHtml = currentNews.select("div").get(0);
            newsList.add(new News(newsHtml));
        }
        return newsList;
    }
}
