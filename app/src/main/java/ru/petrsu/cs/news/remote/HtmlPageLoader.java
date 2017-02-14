package ru.petrsu.cs.news.remote;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import ru.petrsu.cs.news.news.News;
import ru.petrsu.cs.news.news.NewsParser;

/**
 * Loader for html pages of http://cs.petrsu.ru.
 *
 * @author Kovalchuk Denis
 * @version 1.0
 */

public class HtmlPageLoader extends AsyncTaskLoader {
    private HtmlPage htmlPage;

    public HtmlPageLoader(Context context, String url) {
        super(context);
        htmlPage = new HtmlPage(url);
    }

    @Override
    public List<News> loadInBackground() {
        Document document;
        try {
            document = htmlPage.get();
        } catch (IOException exc) {
            return null;
        }
        return NewsParser.createNewsList(document);
    }
}
