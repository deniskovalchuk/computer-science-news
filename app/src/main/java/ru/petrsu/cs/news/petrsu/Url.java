package ru.petrsu.cs.news.petrsu;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;


/**
 * Created by Kovalchuk Denis on 13.12.16.
 * Email: deniskk25@gmail.com
 */

public final class Url implements Parcelable {
    private static final String archivePath = "/news/archive.php.ru?q=news";
    private static final String defaultUrl = "http://cs.petrsu.ru";
    private static final String filenameExtensionXml = ".xml";
    private static final int minimumYear = 2002;
    private int currentYear;
    private int primaryYear;
    private String url;

    public Url() {
        primaryYear = currentYear = Calendar.getInstance().get(Calendar.YEAR);
        url = defaultUrl + archivePath + Integer.toString(currentYear) + filenameExtensionXml;
    }

    public Url(Url in) {
        currentYear = in.currentYear;
        primaryYear = in.primaryYear;
        url = in.url;
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

    public void update() {
        url = defaultUrl + archivePath + Integer.toString(--currentYear) + filenameExtensionXml;
    }

    public String get() {
        return url;
    }

    public boolean isValid() {
        return currentYear >= minimumYear;
    }

    public void setPrimaryYearToCurrentYear() {
        primaryYear = currentYear;
    }
}
