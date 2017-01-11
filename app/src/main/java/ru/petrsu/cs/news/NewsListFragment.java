package ru.petrsu.cs.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
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
    private NewsLab newsLab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newsLab = NewsLab.getInstance();
        newsLab.clearSearchList();
        newsLab.setAllData();

        if (savedInstanceState != null) {
            isFirstLoad = savedInstanceState.getBoolean(KEY_FIRST_LOAD);
            isLoading = savedInstanceState.getBoolean(KEY_LOADING);
        } else {
            isFirstLoad = true;
            isLoading = false;
        }
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

        if (isFirstLoad) {
            getActivity().getSupportLoaderManager().initLoader(PAGE_LOADER, null, this).forceLoad();
        } else {
            createRecyclerView(newsLab.getData());
        }

        return rootView;
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
    public void onLoadFinished(Loader<List<News>> loader, final List<News> newsList) {
        if (newsList == null) {
            if (isFirstLoad) {
                createSnackbarReplyConnection();
            } else {
                newsLab.getData().remove(newsLab.getData().size() - 1);
                adapter.notifyItemRemoved(newsLab.getData().size() + 1);
            }
            return;
        }

        if (newsList.isEmpty()) {
            PetrSU.setPreviousYear();
            getActivity()
                    .getSupportLoaderManager()
                    .restartLoader(PAGE_LOADER, null, getLoaderContext())
                    .forceLoad();
            return;
        }

        if (isFirstLoad) {
            if (snackbar != null && snackbar.isShown()) {
                snackbar.dismiss();
            }
            isFirstLoad = false;
            createRecyclerView(newsLab.getData());
        } else {
            newsLab.getData().remove(newsLab.getData().size() - 1);
        }

        PetrSU.setPreviousYear();
        int startPosition = newsLab.getData().size() + 1;
        int endPosition = startPosition + newsList.size();
        newsLab.addCurrentDataToEnd(newsList);
        for (int i = startPosition; i < endPosition; i++) {
            adapter.notifyItemInserted(startPosition);
        }
        isLoading = false;
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

    }
}
