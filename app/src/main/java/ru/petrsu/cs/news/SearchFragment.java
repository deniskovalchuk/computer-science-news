package ru.petrsu.cs.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private static final String KEY_SEARCH_QUERY = "searchQuery";
    private TextView informationTextView;

    private NewsLab newsLab;
    private String searchQuery;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        newsLab = NewsLab.getInstance();
        newsLab.setSearchMode();
        isLoading = false;

        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(KEY_SEARCH_QUERY);
            isLoading = savedInstanceState.getBoolean(KEY_LOADING);
        }

        if (isLoading) {
            getActivity().getSupportLoaderManager().initLoader(PAGE_LOADER, null, this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news_list, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        informationTextView = (TextView) rootView.findViewById(R.id.information_text_view);

        if (newsLab.getSearchData().isEmpty()) {
            informationTextView.setText(getString(R.string.search_hint));
            informationTextView.setVisibility(View.VISIBLE);
        } else {
            createRecyclerView(newsLab.getSearchData());
        }

        return rootView;
    }

    @Override
    public LoaderManager.LoaderCallbacks getLoaderContext() {
        return this;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isLoading) {
            getActivity().getSupportLoaderManager().getLoader(PAGE_LOADER).forceLoad();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_SEARCH_QUERY, searchQuery);
        outState.putBoolean(KEY_LOADING, isLoading);
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
        isLoading = false;
        if (loadData == null) {
            adapter.removeProgressItem();
            return;
        }

        PetrSU.updateUrl();
        newsLab.addDataToFullData(loadData);

        if (loadData.isEmpty()) {
            if (!PetrSU.isValidUrl()) {
                adapter.removeProgressItem();
                return;
            }
            startLoad();
            return;
        }

        adapter.removeProgressItem();

        List<News> searchResult = newsLab.find(loadData, searchQuery);

        adapter.addData(searchResult);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity().isFinishing()) {
            if (adapter != null) {
                adapter.clear();
            }
        }
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

        if (searchQuery != null) {
            searchView.setQuery(searchQuery, false);
        }

        ImageView searchViewIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        ViewGroup linearLayoutSearchView = (ViewGroup) searchViewIcon.getParent();
        linearLayoutSearchView.removeView(searchViewIcon);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adapter != null) {
                    adapter.clear();
                }

                searchQuery = query;

                List<News> searchResult = newsLab.find(query);

                if (searchResult.isEmpty()) {
                    informationTextView.setVisibility(View.VISIBLE);
                    informationTextView.setText(getString(R.string.no_result));
                } else {
                    informationTextView.setVisibility(View.GONE);
                }

                newsLab.addDataToSearchData(searchResult);
                createRecyclerView(newsLab.getSearchData());

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
