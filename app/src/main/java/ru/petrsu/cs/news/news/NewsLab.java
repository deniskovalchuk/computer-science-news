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
    private static List<News> currentData;
    private static List<News> searchData;
    private static List<News> allData;

    private NewsLab() {
        currentData = new ArrayList<>();
        searchData = new ArrayList<>();
        allData = new ArrayList<>();
        currentData = allData;
    }

    public static NewsLab getInstance() {
        return newsLab;
    }

    public List<News> getData() {
        return currentData;
    }

    public void setSearchData() {
        currentData = searchData;
    }

    public void setAllData() {
        currentData = allData;
    }

    public void clearSearchList() {
        searchData.clear();
    }

    public void setCurrentData(List<News> currentData) {
        NewsLab.currentData = currentData;
    }

    public void addCurrentData(List<News> currentData) {
        NewsLab.currentData.addAll(currentData);
    }

    public News getNews(int position) {
        return currentData.get(position);
    }

    public void addNews(News news) {
        currentData.add(news);
    }

    public void addCurrentDataToEnd(Document document) {
        currentData.addAll(createData(document));
    }

    public void addCurrentDataToEnd(List<News> currentData) {
        NewsLab.currentData.addAll(currentData);
    }

    public void addData(List<News> data) {
        NewsLab.allData.addAll(data);
    }

    public List<News> createData(Document document) {
        List<News> currentData = new ArrayList<>();
        Elements allNews = document.select("div[id]");
        for (Element currentNews : allNews) {
            Element newsHtml = currentNews.select("div").get(0);
            currentData.add(new News(newsHtml));
        }
        return currentData;
    }

    public List<News> find(String query) {
        List<News> searchResult = new ArrayList<>();
        query = query.toUpperCase();
        for (News news : allData) {
            String content = news.getContent().toUpperCase();
            if (content.contains(query)) {
                searchResult.add(news);
            }
        }
        return searchResult;
    }

    public List<News> find(List<News> data, String query) {
        List<News> searchResult = new ArrayList<>();
        query = query.toUpperCase();
        for (News news : data) {
            String content = news.getContent().toUpperCase();
            if (content.contains(query)) {
                searchResult.add(news);
            }
        }
        return searchResult;
    }
}
