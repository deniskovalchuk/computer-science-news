package ru.petrsu.cs.news.news;

/**
 * This class represents any news on http://cs.petrsu.ru.
 *
 * @author Kovalchuk Denis
 * @version 1.0
 */

public class News {
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

    public String getHtml() {
        return html;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
