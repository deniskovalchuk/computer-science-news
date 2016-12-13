package cs.petrsu.ru.imitnews.parser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Browser;
import android.text.Html;
import android.text.Spannable;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

    public static CharSequence parseSafeHtml(Context context, TextView newsTextView, CharSequence html) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return replaceURLSpans(Html.fromHtml(html.toString(),
                    Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST_ITEM | Html.FROM_HTML_SEPARATOR_LINE_BREAK_LIST,
                    new UILImageGetter(context, newsTextView),
                    null));
        } else {
            return replaceURLSpans(Html.fromHtml(html.toString(),
                    new UILImageGetter(context, newsTextView),
                    null));
        }
    }

    private static CharSequence replaceURLSpans(CharSequence html) {
        if (html instanceof Spannable) {
            Spannable spannable = (Spannable) html;
            URLSpan[] spans = spannable.getSpans(0, spannable.length(), URLSpan.class);
            if (spans != null && spans.length > 0) {
                for (URLSpan span : spans) {
                    int start = spannable.getSpanStart(span);
                    int end = spannable.getSpanEnd(span);
                    int flags = spannable.getSpanFlags(span);
                    spannable.removeSpan(span);
                    spannable.setSpan(new SafeURLSpan(span.getURL()), start, end, flags);
                }
            }
        }
        return html;
    }

    @Override
    public void onClick(View widget) {
        Uri uri = Uri.parse(getURL());
        Context context = widget.getContext();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (context != null) {
            Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
            if (isImage()) {
                /*
                FragmentActivity activity = (FragmentActivity) context;
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                ImageFragment imageFragment = ImageFragment.newInstance("http://cs.petrsu.ru" + url);
                imageFragment.show(fragmentManager, DIALOG_IMAGE);
                */
            } else {
                try {
                    intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                    context.startActivity(intent);
                } catch (Throwable ex) {

                }
            }
        }
    }

    private boolean isImage() {
        Pattern pattern = Pattern.compile(".+\\.(png|jpg|PNG|JPG)");
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }
}