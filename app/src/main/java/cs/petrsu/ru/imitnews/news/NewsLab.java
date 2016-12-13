package cs.petrsu.ru.imitnews.news;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kovalchuk Denis on 22.11.16.
 * Email: deniskk25@gmail.com
 */

public class NewsLab {
    private static NewsLab newsLab;
    private List<News> newsList;

    private NewsLab() {
        newsList = new ArrayList<>();
    }

    public static NewsLab get() {
        if (newsLab == null) {
            newsLab = new NewsLab();
        }
        return newsLab;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> news) {
        this.newsList = news;
    }

    public News getNews(int position) {
        return newsList.get(position);
    }
}
