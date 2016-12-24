package cs.petrsu.ru.imitnews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;

import cs.petrsu.ru.imitnews.news.NewsLab;
import cs.petrsu.ru.imitnews.petrsu.NewsParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestNewsParser {

    @Test
    public void testCreateListNews() {
        Document document = Jsoup.parse("Fooz! Bar!");
        NewsParser.createListNews(document);
        assertTrue(NewsLab.getInstance().getNewsList().isEmpty());
    }

    @Test
    public void testCreateListNewsFailHtml() {
        String badHtml = "";

        try(FileReader reader = new FileReader("src/test/java/cs/petrsu/ru/imitnews/failHtml")) {
            int c;
            while((c = reader.read()) != -1) {
                badHtml += (char) c;
            }
        } catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        Document document = Jsoup.parse(badHtml);
        NewsParser.createListNews(document);
        assertEquals(NewsLab.getInstance().getNewsList().size(), 5);
    }
}