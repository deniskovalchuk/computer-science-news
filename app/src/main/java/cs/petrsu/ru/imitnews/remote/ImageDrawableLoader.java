package cs.petrsu.ru.imitnews.remote;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.drawable.Drawable;

import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Kovalchuk Denis on 10.12.16.
 * Email: deniskk25@gmail.com
 */

public class ImageDrawableLoader extends android.content.AsyncTaskLoader {
    private ImageDrawable imageDrawable;

    public ImageDrawableLoader(Context context, String url) {
        super(context);
        imageDrawable = new ImageDrawable(url);
    }

    @Override
    public Drawable loadInBackground() {
        try {
            return imageDrawable.drawableFromUrl();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
