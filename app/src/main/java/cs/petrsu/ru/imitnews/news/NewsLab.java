package cs.petrsu.ru.imitnews.news;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kovalchuk Denis on 22.11.16.
 * Email: deniskk25@gmail.com
 */

public class NewsLab {
    private static final NewsLab newsLab = new NewsLab();
    private List<News> newsList;

    private NewsLab() {
        newsList = new ArrayList<>();
    }

    public static NewsLab getInstance() {
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

    public void addNews(String html, String title, String content) {
        News news = new News(html, title, content);
        newsList.add(news);
    }
}
