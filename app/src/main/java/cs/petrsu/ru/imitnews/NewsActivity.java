package cs.petrsu.ru.imitnews;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.jsoup.nodes.Document;

import cs.petrsu.ru.imitnews.news.NewsLab;
import cs.petrsu.ru.imitnews.parser.NewsParser;
import cs.petrsu.ru.imitnews.remote.HtmlPageLoader;

/**
 * Created by Kovalchuk Denis on 22.11.16.
 * Email: deniskk25@gmail.com
 */

public class NewsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Document> {
    private static final String TAG = "NewsActivity";
    private static final int PAGE_LOADER = 0;
    private NewsParser newsParser;
    private NewsLab newsLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        newsLab = NewsLab.get(NewsActivity.this);
        getLoaderManager().initLoader(PAGE_LOADER, null, this).forceLoad();
    }

    private void createFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragmentListNews = fragmentManager.findFragmentById(R.id.fragment_list_container);
        Fragment fragmentDetailNews = fragmentManager.findFragmentById(R.id.fragment_detail_container);
        if (fragmentListNews == null || fragmentDetailNews == null) {
            fragmentListNews = new NewsListFragment();
            fragmentDetailNews = NewsDetailFragment.newInstance(0);
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_list_container, fragmentListNews)
                    .add(R.id.fragment_detail_container, fragmentDetailNews)
                    .commit();
        }
    }

    @Override
    public Loader<Document> onCreateLoader(int i, Bundle bundle) {
        if (i == PAGE_LOADER) {
            return new HtmlPageLoader(NewsActivity.this, NewsParser.getUrl());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Document> loader, Document document) {
        if (loader.getId() == PAGE_LOADER) {
            newsParser = new NewsParser(NewsActivity.this, document);
            newsLab.setListNews(newsParser.getListNews());
            createFragments();
        }
    }

    @Override
    public void onLoaderReset(Loader<Document> loader) {

    }
}
