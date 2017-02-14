package ru.petrsu.cs.news.news;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for searching a query in news list.
 *
 * @author Kovalchuk Denis
 * @version 1.0
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
