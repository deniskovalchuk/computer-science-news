package ru.petrsu.cs.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ru.petrsu.cs.news.news.News;
import ru.petrsu.cs.news.news.NewsLab;
import ru.petrsu.cs.news.petrsu.PetrSU;
import ru.petrsu.cs.news.remote.HtmlPageLoader;

/**
 * Created by Kovalchuk Denis on 28.11.16.
 * Email: deniskk25@gmail.com
 */

public class NewsListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<News>> {
    private static final String TAG = "NewsListActivity";
    private static final String KEY_FIRST_LOAD = "isFirstLoad";
    private static final String KEY_LOADING = "isLoading";
    private static final int PAGE_LOADER = 0;

    private boolean isFirstLoad;
    private boolean isLoading;
    private NewsLab newsLab;

    private LinearLayoutManager layoutManager;
    private RecyclerView newsRecyclerView;
    private RecyclerViewAdapter adapter;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        newsLab = NewsLab.getInstance();

        if (savedInstanceState != null) {
            isFirstLoad = savedInstanceState.getBoolean(KEY_FIRST_LOAD);
            isLoading = savedInstanceState.getBoolean(KEY_LOADING);
        } else {
            isFirstLoad = true;
            isLoading = false;
        }

        if (isLoading) {
            getSupportLoaderManager().initLoader(PAGE_LOADER, null, this);
        }

        if (isFirstLoad) {
            getSupportLoaderManager().initLoader(PAGE_LOADER, null, this).forceLoad();
        } else {
            createRecyclerView();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLoading) {
            getSupportLoaderManager().getLoader(PAGE_LOADER).forceLoad();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_FIRST_LOAD, isFirstLoad);
        outState.putBoolean(KEY_LOADING, isLoading);
    }

    private void createRecyclerView() {
        newsRecyclerView = (RecyclerView) findViewById(R.id.news_list);
        newsRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        newsRecyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(newsLab.getNewsList());
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                // add progress bar item
                newsLab.getNewsList().add(null);
                adapter.notifyItemInserted(newsLab.getNewsList().size() - 1);
                getSupportLoaderManager().restartLoader(PAGE_LOADER, null, NewsListActivity.this)
                        .forceLoad();
            }
        });
        newsRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_find:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Loader<List<News>> loader = null;
        if (id == PAGE_LOADER) {
            loader = new HtmlPageLoader(this, PetrSU.getUrl());
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, final List<News> newsList) {
        if (newsList == null || newsList.isEmpty()) {
            if (isFirstLoad) {
                createSnackbarReplyConnection();
            } else {
                newsLab.getNewsList().remove(newsLab.getNewsList().size() - 1);
                adapter.notifyItemRemoved(newsLab.getNewsList().size() + 1);
            }
        } else {
            if (isFirstLoad) {
                if (snackbar != null && snackbar.isShown()) {
                    snackbar.dismiss();
                }
                isFirstLoad = false;
                PetrSU.setArchiveUrl();
                createRecyclerView();
            } else {
                newsLab.getNewsList().remove(newsLab.getNewsList().size() - 1);
                PetrSU.setPreviousYear();
            }
            int startPosition = newsLab.getNewsList().size() + 1;
            int endPosition = startPosition + newsList.size();
            newsLab.addNewsListToEnd(newsList);
            for (int i = startPosition; i < endPosition; i++) {
                adapter.notifyItemInserted(startPosition);
            }
        }
        isLoading = false;
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

    }

    private void createSnackbarReplyConnection() {
        snackbar = Snackbar.make(findViewById(R.id.activity_news_list),
                getString(R.string.no_connection), Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.replay, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getSupportLoaderManager().restartLoader(PAGE_LOADER, null,
                                NewsListActivity.this).forceLoad();
                    }
                });
        snackbar.show();
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final int VIEW_ITEM = 1;
        private final int VIEW_PROGRESS_BAR = 0;
        private final List<News> newsList;

        private int visibleThreshold = 5;
        private int lastVisibleItem, totalItemCount;
        private OnLoadMoreListener onLoadMoreListener;

        RecyclerViewAdapter(List<News> newsList) {
            this.newsList = newsList;

            if (newsRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                newsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        totalItemCount = layoutManager.getItemCount();
                        lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                        if (PetrSU.isValidYear() && !isLoading && (totalItemCount <= (lastVisibleItem + visibleThreshold))) {
                            isLoading = true;
                            if (onLoadMoreListener != null) {
                                onLoadMoreListener.onLoadMore();
                            }
                        }
                    }
                });
            }
        }

        @Override
        public int getItemViewType(int position) {
            return newsList.get(position) != null ? VIEW_ITEM : VIEW_PROGRESS_BAR;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder;
            if (viewType == VIEW_ITEM) {
                View v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.news_list_content, parent, false);
                holder = new ViewHolder(v);
            } else {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item,
                        parent, false);
                holder = new ProgressViewHolder(v);
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof ViewHolder) {
                News news = newsList.get(position);
                ((ViewHolder) holder).titleTextView.setText(news.getTitle());
                ((ViewHolder) holder).view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = NewsDetailActivity.newIntent(context, position);
                        context.startActivity(intent);
                    }
                });
            } else {
                ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
            }
        }

        private void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
            this.onLoadMoreListener = onLoadMoreListener;
        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }

        private class ProgressViewHolder extends RecyclerView.ViewHolder {
            ProgressBar progressBar;

            ProgressViewHolder(View view) {
                super(view);
                progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_item);
            }
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            final View view;
            final TextView titleTextView;

            ViewHolder(View view) {
                super(view);
                this.view = view;
                titleTextView = (TextView) view.findViewById(R.id.title_news_text);
            }
        }
    }
}

