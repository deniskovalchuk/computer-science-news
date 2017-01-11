package ru.petrsu.cs.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ru.petrsu.cs.news.news.News;
import ru.petrsu.cs.news.news.NewsLab;
import ru.petrsu.cs.news.petrsu.PetrSU;
import ru.petrsu.cs.news.remote.HtmlPageLoader;

/**
 * Created by Kovalchuk Denis on 09.01.17.
 * Email: deniskk25@gmail.com
 */

public class SearchFragment extends RecyclerViewFragment implements LoaderManager.LoaderCallbacks<List<News>> {
    private static final String TAG = "SearchFragment";
    private TextView informationTextView;

    private NewsLab newsLab;
    private String searchQuery;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        newsLab = NewsLab.getInstance();
        newsLab.setSearchData();

        if (savedInstanceState != null) {
            isLoading = savedInstanceState.getBoolean(KEY_LOADING);
        } else {
            isLoading = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news_list, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        informationTextView = (TextView) rootView.findViewById(R.id.information_text_view);

        informationTextView.setText(getString(R.string.search_hint));
        informationTextView.setVisibility(View.VISIBLE);

        if (isLoading) {
            getActivity().getSupportLoaderManager().initLoader(PAGE_LOADER, null, this);
        }

        return rootView;
    }

    @Override
    public LoaderManager.LoaderCallbacks getLoaderContext() {
        return this;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Loader<List<News>> loader = null;
        if (id == PAGE_LOADER) {
            loader = new HtmlPageLoader(getActivity(), PetrSU.getUrl());
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> loadData) {
        if (loadData == null) {
            adapter.getData().remove(adapter.getData().size() - 1);
            adapter.notifyItemRemoved(adapter.getData().size() + 1);
            return;
        }

        PetrSU.setPreviousYear();
        if (loadData.isEmpty()) {
            if (!PetrSU.isValidYear()) {
                adapter.getData().remove(adapter.getData().size() - 1);
                adapter.notifyItemRemoved(adapter.getData().size() + 1);
                return;
            }
            getActivity()
                    .getSupportLoaderManager()
                    .restartLoader(PAGE_LOADER, null, getLoaderContext())
                    .forceLoad();
            return;
        }

        adapter.getData().remove(adapter.getData().size() - 1);
        int startPosition = adapter.getData().size() + 1;

        newsLab.addData(loadData);
        List<News> searchResult = newsLab.find(loadData, searchQuery);

        if (searchResult.isEmpty()) {
            adapter.notifyItemRemoved(adapter.getData().size() + 1);
            return;
        }

        int endPosition = startPosition + searchResult.size();

        adapter.addData(searchResult);
        for (int i = startPosition; i < endPosition; i++) {
            adapter.notifyItemInserted(startPosition);
        }
        isLoading = false;
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        ImageView searchViewIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        ViewGroup linearLayoutSearchView = (ViewGroup) searchViewIcon.getParent();
        linearLayoutSearchView.removeView(searchViewIcon);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<News> searchResult = newsLab.find(query);

                searchQuery = query;

                if (searchResult.isEmpty()) {
                    informationTextView.setVisibility(View.VISIBLE);
                    informationTextView.setText(getString(R.string.no_result));
                } else {
                    informationTextView.setVisibility(View.GONE);
                }

                newsLab.addCurrentDataToEnd(searchResult);
                createRecyclerView(newsLab.getData());

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}
