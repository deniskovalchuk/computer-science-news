package ru.petrsu.cs.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.petrsu.cs.news.news.News;
import ru.petrsu.cs.news.news.NewsLab;
import ru.petrsu.cs.news.petrsu.Url;
import ru.petrsu.cs.news.remote.HtmlPageLoader;


/**
 * @author Kovalchuk Denis
 * @version 1.0
 */

public class NewsListFragment extends EndlessRecyclerViewFragment
        implements LoaderManager.LoaderCallbacks<List<News>> {
    private NewsLab newsLab;

    @Override
    public LoaderManager.LoaderCallbacks getLoaderContext() {
        return this;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        newsLab = NewsLab.getInstance();
        newsLab.setFullDataMode();
        setLoading(false);

        if (savedInstanceState != null) {
            url = savedInstanceState.getParcelable(KEY_URL);
            setLoading(savedInstanceState.getBoolean(KEY_LOADING));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = initView(R.layout.fragment_news_list, inflater, container);

        if (isEmptyRecyclerView()) {
            url = new Url();
            showCentralProgressBar();
            startLoad();
        } else {
            showRecyclerView(newsLab.getFullData());
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        newsLab.setFullDataMode();
        if (isLoading()) {
            getActivity().getSupportLoaderManager().initLoader(PAGE_LOADER, null, this).forceLoad();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.news_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_find:
                Intent intent = SearchActivity.newIntent(getActivity(), getUrl());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Loader<List<News>> loader = null;
        if (id == PAGE_LOADER) {
            loader = new HtmlPageLoader(getActivity(), getUrl().toString());
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, final List<News> loadData) {
        setLoading(false);
        if (loadData == null) {
            if (!wasRecyclerViewCreated()) {
                showLoadError();
            } else {
                removeProgressItem();
            }
            return;
        }
        updateUrl();
        if (loadData.isEmpty()) {
            startLoad();
            return;
        }
        if (!wasRecyclerViewCreated()) {
            showRecyclerView(newsLab.getFullData());
        } else {
            removeProgressItem();
        }
        updateRecyclerView(loadData);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

    }
}
