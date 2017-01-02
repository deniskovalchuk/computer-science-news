package cs.petrsu.ru.imitnews.news;

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

    private void parseTitle() {
        String date = html.select("b").text();
        String title = html.select("span.title").text();
        this.title = title.isEmpty() ? defaultTitle + date : title;
    }

    private void parseContent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            content = Html.fromHtml(html.toString().replaceAll("<img.+?>", ""),
                    Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            content = Html.fromHtml(html.toString().replaceAll("<img.+?>", "")).toString();
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
