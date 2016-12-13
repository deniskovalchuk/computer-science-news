package cs.petrsu.ru.imitnews.news;

import android.content.Context;
import android.os.Build;
import android.text.Html;

import org.jsoup.nodes.Element;

import cs.petrsu.ru.imitnews.R;

/**
 * Created by Kovalchuk Denis on 22.11.16.
 * Email: deniskk25@gmail.com
 */

public class News {
    private Context context;
    private String title;
    private String content;
    private String html;

    public News(Context context, Element html) {
        this.context = context;
        this.html = html.toString();
        setTitle(html);
        setContent();
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(Element html) {
        String date = html.select("b").text();
        title = html.select("span.title").text();
        if (title.isEmpty()) {
            title = context.getString(R.string.default_title) + date;
        }
    }

    public String getContent() {
        return content;
    }

    private void setContent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            content = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            content = Html.fromHtml(html).toString();
        }
    }

    public String getHtml() {
        return html;
    }
}
