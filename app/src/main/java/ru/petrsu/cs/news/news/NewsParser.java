package ru.petrsu.cs.news.news;

import android.os.Build;
import android.text.Html;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


/**
 * Html parser for news pages of http://cs.petrsu.ru.
 *
 * @author Kovalchuk Denis
 * @version 1.0
 */

public final class NewsParser {
    public static final String TAG = "NewsParser";

    private NewsParser() {

    }

    /**
     * Create news list using download html page.
     *
     * @param document Html page contains news.
     * @return data News list.
     */
    public static List<News> createNewsList(Document document) {
        Elements allNews = document.select("div[id]");
        List<News> data = new ArrayList<>();

        for (Element currentNews : allNews) {
            Element newsHtml = currentNews.select("div").get(0);
            String title = parseTitle(newsHtml);
            String date = parseDate(newsHtml);
            String content = parseContent(newsHtml);
            data.add(new News(newsHtml.toString(), title, date, content));
        }

        return data;
    }

    /**
     * @param newsHtml Html of a news.
     * @return title News title. May be empty.
     */
    private static String parseTitle(Element newsHtml) {
        return newsHtml.select("span.title").text();
    }

    /**
     * @param newsHtml Html of a news.
     * @return date News date.
     */
    private static String parseDate(Element newsHtml) {
        return newsHtml.select("b").get(0).text();
    }

    /**
     * Get content from news without media data (img/video).
     *
     * @param newsHtml Html of a news.
     * @return content News content.
     */
    private static String parseContent(Element newsHtml) {
        String contentWithoutMedia = newsHtml.toString();
        contentWithoutMedia = contentWithoutMedia.replaceAll("<img[\\s\\S]*?>", "");
        contentWithoutMedia = contentWithoutMedia.replaceAll("<video[\\s\\S]*?</video>", "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(contentWithoutMedia, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(contentWithoutMedia).toString();
        }
    }
}
