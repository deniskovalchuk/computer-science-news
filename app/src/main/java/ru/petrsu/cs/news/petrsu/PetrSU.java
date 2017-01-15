package ru.petrsu.cs.news.petrsu;

import java.util.Calendar;


/**
 * Created by Kovalchuk Denis on 13.12.16.
 * Email: deniskk25@gmail.com
 */

public class PetrSU {
    private static final String archivePath = "/news/archive.php.ru?q=news";
    private static final String defaultUrl = "http://cs.petrsu.ru";
    private static final String filenameExtensionXml = ".xml";
    private static final int firstYear = 2002;
    private static int currentNewsYear;
    private static String url;

    static {
        currentNewsYear = Calendar.getInstance().get(Calendar.YEAR);
        url = defaultUrl + archivePath + Integer.toString(currentNewsYear) + filenameExtensionXml;
    }

    private PetrSU() {

    }

    public static void updateUrl() {
        url = defaultUrl + archivePath + Integer.toString(--currentNewsYear) + filenameExtensionXml;
    }

    public static String getUrl() {
        return url;
    }

    public static boolean isValidUrl() {
        return currentNewsYear >= firstYear;
    }
}