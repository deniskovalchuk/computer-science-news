package cs.petrsu.ru.imitnews.news;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kovalchuk Denis on 22.11.16.
 * Email: deniskk25@gmail.com
 */

public class NewsLab {
    private static NewsLab newsLab;
    private List<News> listNews;

    private NewsLab() {
        listNews = new ArrayList<>();
    }

    public static NewsLab get(Context context) {
        if (newsLab == null) {
            newsLab = new NewsLab();
        }
        return newsLab;
    }

    public List<News> getListNews() {
        return listNews;
    }

    public void setListNews(List<News> news) {
        this.listNews = news;
    }

    public News getNews(int position) {
        return listNews.get(position);
    }

    public void addNews(News news) {
        this.listNews.add(news);
    }
}
