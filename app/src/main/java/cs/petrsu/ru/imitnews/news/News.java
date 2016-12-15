package cs.petrsu.ru.imitnews.news;

/**
 * Created by Kovalchuk Denis on 22.11.16.
 * Email: deniskk25@gmail.com
 */

public class News {
    private String html;
    private String title;
    private String content;

    public News(String html, String title, String content) {
        this.html = html;
        this.title = title;
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
