package ru.petrsu.cs.news.news;

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
    private static List<News> searchResult;
    private static List<News> fullData;

    private NewsLab() {
        searchData = new ArrayList<>();
        searchResult = new ArrayList<>();
        fullData = new ArrayList<>();
        currentData = fullData;
    }

    public static NewsLab getInstance() {
        return newsLab;
    }

    public void setSearchMode() {
        currentData = searchResult;
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

    public List<News> getSearchResult() {
        return searchResult;
    }

    public List<News> getCurrentData() {
        return currentData;
    }

    public void clearSearchData() {
        searchData.clear();
    }

    public void clearSearchResult() {
        searchResult.clear();
    }

    public News getNews(int position) {
        return currentData.get(position);
    }

    public void updateSearchData(List<News> data) {
        searchData.addAll(data);
    }

    public void updateSearchResult(List<News> data) {
        searchResult.addAll(data);
    }
}
