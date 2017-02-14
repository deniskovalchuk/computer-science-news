package ru.petrsu.cs.news.news;

import java.util.ArrayList;
import java.util.List;

/**
 * Use this class for control news data.
 *
 * @author Kovalchuk Denis
 * @version 1.0
 */

public class NewsLab {
    private static NewsLab newsLab;

    /**
     * Links to {@link #fullData} or {@link #searchResult} according to current activity.
     */
    private static List<News> currentData;

    /**
     * Data obtained during search, without filtration.
     */
    private static List<News> searchData;

    /**
     * Data obtained during search, with filtration, shown on {@link ru.petrsu.cs.news.SearchActivity}
     */
    private static List<News> searchResult;

    /**
     * Data shown on {@link ru.petrsu.cs.news.NewsListActivity }
     */
    private static List<News> fullData;

    private NewsLab() {
        searchData = new ArrayList<>();
        searchResult = new ArrayList<>();
        fullData = new ArrayList<>();
        currentData = fullData;
    }

    public static NewsLab getInstance() {
        if (newsLab == null) {
            newsLab = new NewsLab();
        }
        return newsLab;
    }

    /**
     * Call this method, when start {@link ru.petrsu.cs.news.SearchActivity}
     */
    public void setSearchMode() {
        currentData = searchResult;
    }

    /**
     * Call this method, when start {@link ru.petrsu.cs.news.NewsListActivity}
     */
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
