package cs.petrsu.ru.imitnews.remote;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Kovalchuk Denis on 22.11.16.
 * Email: deniskk25@gmail.com
 */

public class HtmlPageLoader extends AsyncTaskLoader {
    private static final String TAG = "HtmlPageLoader";
    private HtmlPage htmlPage;
    private static boolean failLoad = true;
    private static boolean isLoading = false;

    public HtmlPageLoader(Context context, String url) {
        super(context);
        htmlPage = new HtmlPage(url);
    }

    @Override
    public Document loadInBackground() {
        Document document = null;
        try {
            document = htmlPage.get();
            failLoad = false;
        } catch (IOException exc) {
            failLoad = true;
        }
        return document;
    }

    public static boolean isFailLoad() {
        return failLoad;
    }

    public static boolean isLoading() {
        return isLoading;
    }

    public static void setLoading(boolean isLoading) {
        HtmlPageLoader.isLoading = isLoading;
    }
}
