package ru.petrsu.cs.news.news;

import android.os.Build;
import android.text.Html;

import org.jsoup.nodes.Element;

/**
 * Created by Kovalchuk Denis on 22.11.16.
 * Email: deniskk25@gmail.com
 */

public class News {
    private static final String defaultTitle = "Новость от ";
    private Element html;
    private String title;
    private String content;

    public News(Element document) {
        this.html = document;
        parseTitle();
        parseContent();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        News news = (News) o;

        if (html != null ? !html.toString().equals(news.html.toString()) : news.html != null) return false;
        if (title != null ? !title.equals(news.title) : news.title != null) return false;
        return content != null ? content.equals(news.content) : news.content == null;
    }

    @Override
    public int hashCode() {
        int result = html.toString().hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + content.hashCode();
        return result;
    }

    private void parseTitle() {
        String date = html.select("b").text();
        String title = html.select("span.title").text();
        this.title = title.isEmpty() ? defaultTitle + date : title;
    }

    private void parseContent() {
        String contentWithoutImg = html.toString().replaceAll("<img.+?>", "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            content = Html.fromHtml(contentWithoutImg, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            content = Html.fromHtml(contentWithoutImg).toString();
        }
    }

    public String getHtml() {
        return html.toString();
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
