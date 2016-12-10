package cs.petrsu.ru.imitnews.parser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.Spannable;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cs.petrsu.ru.imitnews.ImageFragment;


/**
 * Created by Kovalchuk Denis on 09.12.16.
 * Email: deniskk25@gmail.com
 */

public class SafeURLSpan extends URLSpan {
    private static final String TAG = "SafeUrlSpan";
    private static final String DIALOG_IMAGE = "dialog_image";
    private String url;

    private SafeURLSpan(String url) {
        super(url);
        this.url = url;
    }

    @Override
    public void onClick(View widget) {
        try {
            final Uri uri = Uri.parse(getURL());
            final Context context = widget.getContext();
            final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (context != null && intent != null) {
                if (isImage()) {
//                    FragmentActivity activity = (FragmentActivity) context;
//                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
//                    ImageFragment imageFragment = ImageFragment.newInstance("http://cs.petrsu.ru" + url);
//                    imageFragment.show(fragmentManager, DIALOG_IMAGE);
                    Toast.makeText(context, "Opening a image", Toast.LENGTH_SHORT).show();
                } else {
                    intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                    context.startActivity(intent);
                }
            }
        } catch (Throwable ex) {

        }
    }

    private boolean isImage() {
        Pattern pattern = Pattern.compile(".+\\.(png|jpg|PNG|JPG)");
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    public static CharSequence parseSafeHtml(Context context, TextView newsTextView, CharSequence html) {
        return replaceURLSpans(Html.fromHtml(html.toString(), new UILImageGetter(context, newsTextView), null));
    }

    private static CharSequence replaceURLSpans(CharSequence html) {
        if (html instanceof Spannable) {
            final Spannable spannable = (Spannable) html;
            final URLSpan[] spans = spannable.getSpans(0, spannable.length(), URLSpan.class);
            if (spans != null && spans.length > 0) {
                for (final URLSpan span : spans) {
                    final int start = spannable.getSpanStart(span);
                    final int end = spannable.getSpanEnd(span);
                    final int flags = spannable.getSpanFlags(span);
                    spannable.removeSpan(span);
                    spannable.setSpan(new SafeURLSpan(span.getURL()), start, end, flags);
                }
            }
        }
        return html;
    }
}