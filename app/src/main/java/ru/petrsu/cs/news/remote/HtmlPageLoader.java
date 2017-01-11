package ru.petrsu.cs.news.remote;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import ru.petrsu.cs.news.news.News;
import ru.petrsu.cs.news.news.NewsLab;

/**
 * Created by Kovalchuk Denis on 22.11.16.
 * Email: deniskk25@gmail.com
 */

public class HtmlPageLoader extends AsyncTaskLoader {
    private static final String TAG = "HtmlPageLoader";
    private NewsLab newsLab;
    private HtmlPage htmlPage;

    public HtmlPageLoader(Context context, String url) {
        super(context);
        newsLab = NewsLab.getInstance();
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
        return newsLab.createData(document);
    }
}
