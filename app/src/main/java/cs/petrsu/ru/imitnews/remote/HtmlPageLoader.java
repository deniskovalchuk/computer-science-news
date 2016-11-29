package cs.petrsu.ru.imitnews.remote;

import android.content.Context;

import org.jsoup.nodes.Document;

/**
 * Created by Kovalchuk Denis on 22.11.16.
 * Email: deniskk25@gmail.com
 */

public class HtmlPageLoader extends android.content.AsyncTaskLoader {
    private HtmlPage htmlPage;

    public HtmlPageLoader(Context context, String url) {
        super(context);
        htmlPage = new HtmlPage(context, url);
    }

    @Override
    public Document loadInBackground() {
        return htmlPage.get();
    }
}
