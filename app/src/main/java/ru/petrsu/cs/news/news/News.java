package ru.petrsu.cs.news.news;

/**
 * Created by Kovalchuk Denis on 22.11.16.
 * Email: deniskk25@gmail.com
 */

public class News {
    private static final String defaultTitle = "Новость от ";
    private String html;
    private String title;
    private String content;

    News(String html, String title, String date, String content) {
        this.html = html;
        this.title = title.isEmpty() ? defaultTitle + date : title;
        this.content = content;
    }

    public String getHtml() {
        return html;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
