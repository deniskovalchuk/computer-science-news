package ru.petrsu.cs.news.petrsu;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;


/**
 * This class presents current url of page for downloading. Sending
 * {@link ru.petrsu.cs.news.SearchActivity} from {@link ru.petrsu.cs.news.NewsListActivity}.
 *
 * @author Kovalchuk Denis
 * @version 1.0
 */

public final class Url implements Parcelable {
    private static final String archivePath = "/news/archive.php.ru?q=news";
    private static final String baseUrl = "http://cs.petrsu.ru";
    private static final String filenameExtensionXml = ".xml";
    private static final int minimumYear = 2002;
    /**
     * Year for formation of url.
     */
    private int currentYear;
    private String url;

    public Url() {
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        url = baseUrl + archivePath + Integer.toString(currentYear) + filenameExtensionXml;
    }

    private Url(Parcel in) {
        currentYear = in.readInt();
        url = in.readString();
    }

    public static final Creator<Url> CREATOR = new Creator<Url>() {
        @Override
        public Url createFromParcel(Parcel in) {
            return new Url(in);
        }

        @Override
        public Url[] newArray(int size) {
            return new Url[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(currentYear);
        dest.writeString(url);
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Update url for downloading previous data.
     */
    public void update() {
        url = baseUrl + archivePath + Integer.toString(--currentYear) + filenameExtensionXml;
    }

    @Override
    public String toString() {
        return url;
    }

    public boolean isValid() {
        return currentYear >= minimumYear;
    }
}
