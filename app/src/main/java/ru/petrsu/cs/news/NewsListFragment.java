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
import android.widget.ProgressBar;

import java.util.List;

import ru.petrsu.cs.news.news.News;
import ru.petrsu.cs.news.news.NewsLab;
import ru.petrsu.cs.news.petrsu.BadUrlException;
import ru.petrsu.cs.news.petrsu.Url;
import ru.petrsu.cs.news.remote.HtmlPageLoader;

/**
 * Created by Kovalchuk Denis on 09.01.17.
 * Email: deniskk25@gmail.com
 */

public class NewsListFragment extends EndlessRecyclerViewFragment
        implements LoaderManager.LoaderCallbacks<List<News>> {
    private static final String TAG = "NewsListFragment";

    private ProgressBar progressBar;
    private View rootView;

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

        url = new Url();

        if (savedInstanceState != null) {
            url = savedInstanceState.getParcelable(KEY_URL);
        }

        setLoading(savedInstanceState != null && savedInstanceState.getBoolean(KEY_LOADING));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news_list, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

        if (isEmptyRecyclerView()) {
            progressBar.setVisibility(View.VISIBLE);
            try {
                startLoad();
            } catch (BadUrlException e) {
                progressBar.setVisibility(View.GONE);
            }
        } else {
            progressBar.setVisibility(View.GONE);
            createRecyclerView(rootView, newsLab.getFullData());
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
                Intent intent = SearchActivity.newIntent(getActivity(), url);
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
            loader = new HtmlPageLoader(getActivity(), url.get());
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, final List<News> loadData) {
        setLoading(false);

        if (loadData == null) {
            if (!hasRecyclerViewCreated()) {
                createSnackbarReplyConnection();
            } else {
                removeProgressItem();
            }
            return;
        }

        url.update();

        if (loadData.isEmpty()) {
            try {
                startLoad();
            } catch (BadUrlException ignored) {

            }
            return;
        }

        if (!hasRecyclerViewCreated()) {
            destroySnackBarReplyConnection();
            createRecyclerView(rootView, newsLab.getFullData());
            progressBar.setVisibility(View.GONE);
        } else {
            removeProgressItem();
        }

        updateRecyclerView(loadData);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

    }
}
