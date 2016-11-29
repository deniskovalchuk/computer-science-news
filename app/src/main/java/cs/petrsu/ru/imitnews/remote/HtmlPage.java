package cs.petrsu.ru.imitnews.remote;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import cs.petrsu.ru.imitnews.R;

import static android.content.ContentValues.TAG;

/**
 * Created by Kovalchuk Denis on 22.11.16.
 * Email: deniskk25@gmail.com
 */

public class HtmlPage {
    private String url;
    private Context context;

    HtmlPage(Context context, String url) {
        this.context = context;
        this.url = url;
    }

    Document get() {
        Document doc;

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            return null;
        }
        return doc;
    }
}
