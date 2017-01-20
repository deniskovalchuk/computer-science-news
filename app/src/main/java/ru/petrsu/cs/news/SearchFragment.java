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

import java.util.ArrayList;
import java.util.List;

import ru.petrsu.cs.news.news.News;
import ru.petrsu.cs.news.news.NewsLab;
import ru.petrsu.cs.news.news.NewsSearcher;
import ru.petrsu.cs.news.petrsu.BadUrlException;
import ru.petrsu.cs.news.petrsu.Url;
import ru.petrsu.cs.news.remote.HtmlPageLoader;

/**
 * Created by Kovalchuk Denis on 09.01.17.
 * Email: deniskk25@gmail.com
 */

public class SearchFragment extends EndlessRecyclerViewFragment implements LoaderManager.LoaderCallbacks<List<News>> {
    private static final String TAG = "SearchFragment";
    private static final String KEY_INF_TEXT = "informationText";
    private static final String KEY_SEARCH_QUERY = "searchQuery";
    private static final String ARG_URL = "url";

    private TextView informationTextView;
    private ProgressBar progressBar;
    private View rootView;

    private String searchQuery;
    private String informationText;
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
            searchQuery = savedInstanceState.getString(KEY_SEARCH_QUERY);
            url = savedInstanceState.getParcelable(KEY_URL);
            informationText = savedInstanceState.getString(KEY_INF_TEXT);
            setLoading(savedInstanceState.getBoolean(KEY_LOADING));
        } else {
            newsLab.clearSearchResult();
            url.setPrimaryYearToCurrentYear();
            informationText = getString(R.string.search_hint);
            setLoading(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news_list, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        informationTextView = (TextView) rootView.findViewById(R.id.information_text_view);

        if (!isEmptyRecyclerView()) {
            progressBar.setVisibility(View.GONE);
            createRecyclerView(rootView, newsLab.getSearchResult());
        } else {
            if (isLoading()) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                informationTextView.setVisibility(View.VISIBLE);
                informationTextView.setText(informationText);
            }
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
        outState.putParcelable(KEY_URL, url);
        outState.putBoolean(KEY_LOADING, isLoading());
        outState.putString(KEY_INF_TEXT, informationText);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.setMaxWidth(Integer.MAX_VALUE);

        if (searchQuery != null)
            searchView.setQuery(searchQuery, false);

        /* remove a search icon  */
        ImageView searchViewIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        ViewGroup linearLayoutSearchView = (ViewGroup) searchViewIcon.getParent();
        linearLayoutSearchView.removeView(searchViewIcon);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                informationTextView.setVisibility(View.GONE);

                if (hasRecyclerViewCreated()) {
                    clearRecyclerView();
                }

                List<News> searchResult = new ArrayList<>();
                searchResult.addAll(NewsSearcher.find(newsLab.getFullData(), searchQuery));
                searchResult.addAll(NewsSearcher.find(newsLab.getSearchData(), searchQuery));
                newsLab.updateSearchResult(searchResult);
                createRecyclerView(rootView, newsLab.getSearchResult());

                if (searchResult.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    try {
                        startLoad();
                    } catch (BadUrlException ignored) {
                        progressBar.setVisibility(View.GONE);
                        informationText = getString(R.string.no_result);
                        informationTextView.setText(informationText);
                        informationTextView.setVisibility(View.VISIBLE);
                    }
                }

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

    private void showNoResultMessage() {
        progressBar.setVisibility(View.GONE);
        informationText = getString(R.string.no_result);
        informationTextView.setText(informationText);
        informationTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Loader<List<News>> loader = null;
        if (id == PAGE_LOADER) {
            loader = new HtmlPageLoader(getActivity(), url.get());
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> loadData) {
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

        newsLab.updateSearchData(loadData);

        List<News> searchResult = NewsSearcher.find(loadData, searchQuery);
        if (searchResult.isEmpty()) {
            try {
                startLoad();
            } catch (BadUrlException e) {
                removeProgressItem();
                if (isEmptyRecyclerView()) {
                    showNoResultMessage();
                }
            }
        } else {
            progressBar.setVisibility(View.GONE);
            if (!hasRecyclerViewCreated()) {
                destroySnackBarReplyConnection();
                createRecyclerView(rootView, newsLab.getSearchData());
            } else {
                removeProgressItem();
            }
            updateRecyclerView(searchResult);
            if (searchResult.size() == 1) {
                addProgressItem();
                try {
                    startLoad();
                } catch (BadUrlException e) {
                    removeProgressItem();
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
