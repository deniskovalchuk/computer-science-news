package cs.petrsu.ru.imitnews.news;

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

    public News getNews(int position) {
        return newsList.get(position);
    }
}
