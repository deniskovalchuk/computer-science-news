package ru.petrsu.cs.news.news;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class represents any news on http://cs.petrsu.ru.
 *
 * @author Kovalchuk Denis
 * @version 1.0
 */

public class News implements Parcelable {
    /**
     * Some news don't have titles, in case the title will consist of defaultTitle and news date.
     */
    private static final String defaultTitle = "Новость от ";

    /**
     * News html for {@link ru.petrsu.cs.news.NewsDetailFragment#webView}.
     */
    private String html;

    /**
     * News title shown on {@link ru.petrsu.cs.news.NewsListFragment#endlessRecyclerView}.
     */
    private String title;

    /**
     * Share content.
     */
    private String content;

    News(String html, String title, String date, String content) {
        this.html = html;
        this.title = title.isEmpty() ? defaultTitle + date : title;
        this.content = content;
    }

    private News(Parcel in) {
        html = in.readString();
        title = in.readString();
        content = in.readString();
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    public String getHtml() {
        return html;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(html);
        dest.writeString(title);
        dest.writeString(content);
    }
}
