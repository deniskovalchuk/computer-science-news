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

import java.util.ArrayList;
import java.util.List;

import ru.petrsu.cs.news.news.News;
import ru.petrsu.cs.news.news.NewsLab;
import ru.petrsu.cs.news.news.NewsSearcher;
import ru.petrsu.cs.news.petrsu.Url;
import ru.petrsu.cs.news.remote.HtmlPageLoader;


/**
 * @author Kovalchuk Denis
 * @version 1.0
 */

public class SearchFragment extends EndlessRecyclerViewFragment implements LoaderManager.LoaderCallbacks<List<News>> {
    private static final String KEY_INF_TEXT_RESOURCE = "informationText";
    private static final String KEY_SEARCH_VIEW_TEXT = "searchViewText";
    private static final String KEY_SEARCH_QUERY = "searchQuery";
    private static final String ARG_URL = "url";

    private SearchView searchView;

    private int informationTextResource;
    private String searchQuery;
    private String searchViewText;
    private NewsLab newsLab;

    public static SearchFragment newInstance(Url url) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(ARG_URL, url);
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(arguments);
        return searchFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        newsLab = NewsLab.getInstance();
        newsLab.setSearchMode();
        url = new Url((Url) getArguments().getParcelable(ARG_URL));

        if (savedInstanceState != null) {
            searchViewText = savedInstanceState.getString(KEY_SEARCH_VIEW_TEXT);
            searchQuery = savedInstanceState.getString(KEY_SEARCH_QUERY);
            url = savedInstanceState.getParcelable(KEY_URL);
            informationTextResource = savedInstanceState.getInt(KEY_INF_TEXT_RESOURCE);
            setLoading(savedInstanceState.getBoolean(KEY_LOADING));
        } else {
            newsLab.clearSearchResult();
            informationTextResource = R.string.search_hint;
            setLoading(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = initView(R.layout.fragment_news_list, inflater, container);
        if (isEmptyRecyclerView()) {
            if (isLoading()) {
                showCentralProgressBar();
            } else {
                showInformationTextView(informationTextResource);
            }
        } else {
            showRecyclerView(newsLab.getSearchResult());
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
        if (isLoading())
            getActivity().getSupportLoaderManager().initLoader(PAGE_LOADER, null, this).forceLoad();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity().isFinishing()) {
            newsLab.clearSearchData();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_SEARCH_QUERY, searchQuery);
        outState.putString(KEY_SEARCH_VIEW_TEXT, searchView.getQuery().toString());
        outState.putParcelable(KEY_URL, getUrl());
        outState.putBoolean(KEY_LOADING, isLoading());
        outState.putInt(KEY_INF_TEXT_RESOURCE, informationTextResource);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);

        searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.setMaxWidth(Integer.MAX_VALUE);

        if (searchViewText != null)
            searchView.setQuery(searchViewText, false);

        ImageView searchViewIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        ViewGroup linearLayoutSearchView = (ViewGroup) searchViewIcon.getParent();
        linearLayoutSearchView.removeView(searchViewIcon);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;

                if (wasRecyclerViewCreated()) {
                    clearRecyclerView();
                }

                List<News> searchResult = new ArrayList<>();
                searchResult.addAll(NewsSearcher.find(newsLab.getFullData(), searchQuery));
                searchResult.addAll(NewsSearcher.find(newsLab.getSearchData(), searchQuery));
                if (searchResult.isEmpty()) {
                    if (isValidUrl()) {
                        startLoad();
                    } else {
                        informationTextResource = R.string.no_result;
                        showInformationTextView(informationTextResource);
                    }
                } else {
                    newsLab.updateSearchResult(searchResult);
                    showRecyclerView(newsLab.getSearchResult());
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Loader<List<News>> loader = null;
        if (id == PAGE_LOADER) {
            loader = new HtmlPageLoader(getActivity(), url.toString());
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> loadData) {
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
        newsLab.updateSearchData(loadData);
        List<News> searchResult = NewsSearcher.find(loadData, searchQuery);
        if (searchResult.isEmpty()) {
            if (isValidUrl()) {
                startLoad();
            } else {
                removeProgressItem();
                if (isEmptyRecyclerView()) {
                    informationTextResource = R.string.no_result;
                    showInformationTextView(informationTextResource);
                }
            }
        } else {
            if (!wasRecyclerViewCreated()) {
                showRecyclerView(newsLab.getSearchResult());
            } else {
                removeProgressItem();
            }
            updateRecyclerView(searchResult);
            if (searchResult.size() == 1) {
                startLoad();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
