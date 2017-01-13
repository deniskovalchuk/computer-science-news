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
    private static List<News> fullData;

    private NewsLab() {
        searchData = new ArrayList<>();
        fullData = new ArrayList<>();
        currentData = fullData;
    }

    public static NewsLab getInstance() {
        return newsLab;
    }

    public void setSearchMode() {
        currentData = searchData;
    }

    public void setFullDataMode() {
        currentData = fullData;
    }

    public List<News> getFullData() {
        return fullData;
    }

    public List<News> getSearchData() {
        return searchData;
    }

    public void clearSearchData() {
        searchData.clear();
    }

    public News getNews(int position) {
        return currentData.get(position);
    }

    public void addDataToFullData(List<News> data) {
        fullData.addAll(data);
    }

    public void addDataToSearchData(List<News> data) {
        searchData.addAll(data);
    }

    public List<News> createData(Document document) {
        List<News> data = new ArrayList<>();
        Elements allNews = document.select("div[id]");
        for (Element currentNews : allNews) {
            Element newsHtml = currentNews.select("div").get(0);
            data.add(new News(newsHtml));
        }
        return data;
    }

    public List<News> find(String query) {
        return find(fullData, query);
    }

    public List<News> find(List<News> data, String query) {
        List<News> searchResult = new ArrayList<>();
        if (query == null) {
            return searchResult;
        }
        query = query.toUpperCase();
        for (News news : data) {
            if (news == null) {
                continue;
            }
            String content = news.getContent().toUpperCase();
            if (content.contains(query)) {
                searchResult.add(news);
            }
        }
        return searchResult;
    }
}
