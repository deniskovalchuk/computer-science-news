package ru.petrsu.cs.news.news;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kovalchuk Denis on 20.01.17.
 * Email: deniskk25@gmail.com
 */

public final class NewsSearcher {
    private NewsSearcher() {

    }

    public static List<News> find(List<News> data, String query) {
        List<News> searchResult = new ArrayList<>();

        if (query == null) {
            return searchResult;
        }

        query = query.toUpperCase();
        for (News news : data) {
            if (news == null) {
                continue;
            }

            String content = news.getContent().toUpperCase();

            if (content.contains(query)) {
                searchResult.add(news);
            }
        }
        return searchResult;
    }
}
