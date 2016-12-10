package cs.petrsu.ru.imitnews.parser;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import cs.petrsu.ru.imitnews.R;

public class UILImageGetter implements Html.ImageGetter {
    private static final String TAG = "UILImageGetter";
    private Context context;
    private TextView newsTextView;

    public UILImageGetter(Context context, View textView) {
        this.context = context;
        this.newsTextView = (TextView) textView;
    }

    @Override
    public Drawable getDrawable(String source) {
        UrlImageDownloader urlDrawable = new UrlImageDownloader(context.getResources(), source);
        urlDrawable.drawable = context.getResources().getDrawable(R.drawable.ic_news);
        ImageLoader.getInstance().loadImage(source, new SimpleListener(urlDrawable));
        return urlDrawable;
    }

    private class SimpleListener extends SimpleImageLoadingListener {
        private UrlImageDownloader urlImageDownloader;

        public SimpleListener(UrlImageDownloader downloader) {
            super();
            urlImageDownloader = downloader;
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            int width = loadedImage.getWidth();
            int height = loadedImage.getHeight();

            int newWidth = width;
            int newHeight = height;

            if (width > newsTextView.getWidth()) {
                newWidth = newsTextView.getWidth();
                newHeight = (newWidth * height) / width;
            }

            if (view != null) {
                view.getLayoutParams().width = newWidth;
                view.getLayoutParams().height = newHeight;
            }

            Drawable result = new BitmapDrawable(context.getResources(), loadedImage);
            result.setBounds(0, 0, newWidth, newHeight);

            urlImageDownloader.setBounds(0, 0, newWidth, newHeight);
            urlImageDownloader.drawable = result;

            newsTextView.setHeight((newsTextView.getHeight() + result.getIntrinsicHeight()));
            newsTextView.invalidate();
        }
    }

    private class UrlImageDownloader extends BitmapDrawable {
        public Drawable drawable;

        private UrlImageDownloader(Resources res, String filepath) {
            super(res, filepath);
            drawable = new BitmapDrawable(res, filepath);
        }

        @Override
        public void draw(Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }
    }
}