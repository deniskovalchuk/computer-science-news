package cs.petrsu.ru.imitnews.parser;

import android.content.Context;
import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cs.petrsu.ru.imitnews.R;
import cs.petrsu.ru.imitnews.news.News;

/**
 * Created by Kovalchuk Denis on 22.11.16.
 * Email: deniskk25@gmail.com
 */

public class NewsParser {
    private static final String TAG = "NewsParser";
    private static final String petrsuUrl = "http://cs.petrsu.ru";
    private static String currentUrl = "http://cs.petrsu.ru";
    private static News news;

    public static String getUrl() {
        return currentUrl;
    }

    public static List<News> createListNews(Context context, Document html) {
        List<News> newsList = new ArrayList<>();

        Elements allNews = html.select("div[id]");
        for (Element currentNews : allNews) {
            news = new News();

            // Get news
            Element newsHtml = currentNews.select("div").get(0);
            // Set title for news recyclerview
            setTitleNews(context, newsHtml);

            // Set correct image url
            int numberImages = currentNews.select("img").size();
            for (int i = 0; i < numberImages; i++) {
                String imageUrl = currentNews.select("img").get(i).attr("src");
                currentNews.html(currentNews.toString().replace(imageUrl, petrsuUrl + imageUrl));
            }

            // Set correct url
            int numberReference = currentNews.select("a").size();
            for (int i = 0; i < numberReference; i++) {
                // Get url
                String referenceUrl = currentNews.select("a").get(i).attr("href");

                if (!referenceUrl.isEmpty()) {
                    if (isMail(referenceUrl)) {
                        continue;
                    }

                    // if relative url
                    if (!isAbsoluteUrl(referenceUrl)) {
                        currentNews.html(currentNews
                                .toString()
                                .replace(referenceUrl, petrsuUrl + getPrettyUrl(referenceUrl)));
                        Log.d(TAG, currentNews.toString());
                    }
                }
            }
            news.setContent(currentNews.toString());
            newsList.add(news);
        }
        return newsList;
    }

    private static boolean isMail(String url) {
        Pattern patternMailto = Pattern.compile("^mailto:.+");
        Matcher matcherMailto = patternMailto.matcher(url);
        return matcherMailto.matches();
    }

    private static String getPrettyUrl(String url) {
        return url.replaceAll(" ", "%20");
    }

    private static boolean isAbsoluteUrl(String referenceUrl) {
        Pattern pattern = Pattern.compile("^(http|https).+");
        Matcher matcher = pattern.matcher(referenceUrl);
        return matcher.matches();
    }

    private static void setTitleNews(Context context, Element newsHeadline) {
        String date = newsHeadline.select("b").text();
        String title = newsHeadline.select("span.title").text();
        if (title.isEmpty()) {
            news.setTitle(context.getString(R.string.default_title) + date);
        } else {
            news.setTitle(title);
        }
    }
}

