package cs.petrsu.ru.imitnews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import cs.petrsu.ru.imitnews.news.News;
import cs.petrsu.ru.imitnews.parser.NewsParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestNewsParser {

    @Test
    public void testCreateListNews() {
        List<News> newsList;
        Document document = Jsoup.parse("Fooz! Bar!");
        newsList = NewsParser.createListNews(null, document);
        assertTrue(newsList.isEmpty());
    }

    @Test
    public void testCreateListNewsFailHtml() {
        List<News> newsList;
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
        newsList = NewsParser.createListNews(null, document);
        assertEquals(newsList.size(), 5);
    }
}