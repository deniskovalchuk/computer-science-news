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
import ru.petrsu.cs.news.petrsu.PetrSU;
import ru.petrsu.cs.news.remote.HtmlPageLoader;

/**
 * Created by Kovalchuk Denis on 09.01.17.
 * Email: deniskk25@gmail.com
 */

public class NewsListFragment extends RecyclerViewFragment implements LoaderManager.LoaderCallbacks<List<News>> {
    private static final String TAG = "NewsListFragment";
    private NewsLab newsLab;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        newsLab = NewsLab.getInstance();

        isLoading = savedInstanceState != null && savedInstanceState.getBoolean(KEY_LOADING);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news_list, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        if (isLoading) {
            getActivity().getSupportLoaderManager().initLoader(PAGE_LOADER, null, this);
        }

        if (isFirstLoad()) {
            startLoad();
        } else {
            createRecyclerView(newsLab.getFullData());
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_LOADING, isLoading);
    }

    @Override
    public void onResume() {
        super.onResume();
        newsLab.setFullDataMode();

        if (adapter != null) {
            adapter.setData(newsLab.getFullData());
        }

        if (isLoading) {
            getActivity().getSupportLoaderManager().getLoader(PAGE_LOADER).forceLoad();
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
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public LoaderManager.LoaderCallbacks getLoaderContext() {
        return this;
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Loader<List<News>> loader = null;
        if (id == PAGE_LOADER) {
            loader = new HtmlPageLoader(getActivity(), PetrSU.getUrl());
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, final List<News> loadData) {
        isLoading = false;

        if (loadData == null) {
            if (adapter.getData().isEmpty()) {
                createSnackbarReplyConnection();
            } else {
                adapter.removeProgressItem();
            }
            return;
        }

        PetrSU.updateUrl();

        if (loadData.isEmpty() && PetrSU.isValidUrl()) {
            startLoad();
            return;
        }

        if (newsRecyclerView == null) {
            if (snackbar != null && snackbar.isShown()) {
                snackbar.dismiss();
            }
            createRecyclerView(newsLab.getFullData());
        } else {
            adapter.removeProgressItem();
        }
        adapter.addData(loadData);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

    }
}
