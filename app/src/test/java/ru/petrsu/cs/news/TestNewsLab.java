package ru.petrsu.cs.news;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import ru.petrsu.cs.news.news.News;
import ru.petrsu.cs.news.news.NewsLab;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class TestNewsLab {
    private NewsLab newsLab;

    @Before
    public void before() {
        newsLab = NewsLab.getInstance();
    }

    @Test
    public void createNewsList() {
        Document document = Jsoup.parse("Fooz! Bar!");
        newsLab.createNewsList(document);
        assertTrue(newsLab.getNewsList().isEmpty());
    }

    @Test
    public void createNewsListFailHtml() {
        Document document = getFailDocument();
        List<News> newsList = newsLab.createNewsList(document);
        assertEquals(newsList.size(), 5);
    }

    public Document getFailDocument() {
        String badHtml = "";

        try(FileReader reader = new FileReader("src/test/java/cs/petrsu/ru/imitnews/failHtml")) {
            int c;
            while((c = reader.read()) != -1) {
                badHtml += (char) c;
            }
        } catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        return Jsoup.parse(badHtml);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void outOfBounds() {
        Document document = getFailDocument();
        List<News> newsList = newsLab.createNewsList(document);
        newsList.get(6);
    }

    @Test
    public void addNewsListToEnd() {
        Document document = getFailDocument();
        newsLab.addNewsListToEnd(document);
        assertEquals(newsLab.getNewsList().size(), 5);
    }
}