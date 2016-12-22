package cs.petrsu.ru.imitnews;

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

import org.jsoup.nodes.Document;

import java.util.List;

import cs.petrsu.ru.imitnews.news.News;
import cs.petrsu.ru.imitnews.news.NewsLab;
import cs.petrsu.ru.imitnews.parser.NewsParser;
import cs.petrsu.ru.imitnews.parser.PetrSU;
import cs.petrsu.ru.imitnews.remote.HtmlPageLoader;

/**
 * Created by Kovalchuk Denis on 28.11.16.
 * Email: deniskk25@gmail.com
 */

public class NewsListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Document> {
    private static final String TAG = "NewsListActivity";
    private static final int PAGE_LOADER = 0;
    private static final int ARCHIVE_PAGE_LOADER = 1;

    private SimpleItemRecyclerViewAdapter adapter;
    private ProgressBar progressBar;
    private Snackbar snackbar;

    private NewsLab newsLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        newsLab = NewsLab.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        if (HtmlPageLoader.isFailLoad()) {
            getSupportLoaderManager().initLoader(PAGE_LOADER, null, this).forceLoad();
        } else {
            onBindRecyclerView();
        }
    }

    private void onBindRecyclerView() {
        adapter = new SimpleItemRecyclerViewAdapter(newsLab.getNewsList());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(NewsListActivity.this);
        RecyclerView newsRecyclerView = (RecyclerView) findViewById(R.id.news_list);
        assert newsRecyclerView != null;
        newsRecyclerView.setAdapter(adapter);
        newsRecyclerView.setLayoutManager(layoutManager);
        newsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItems = layoutManager.findFirstVisibleItemPosition();
                if (!HtmlPageLoader.isLoading()) {
                    if ((visibleItemCount + firstVisibleItems) >= totalItemCount) {
                        HtmlPageLoader.setLoading(true);
                        if (getSupportLoaderManager().getLoader(ARCHIVE_PAGE_LOADER) != null) {
                            getSupportLoaderManager().restartLoader(ARCHIVE_PAGE_LOADER, null,
                                    NewsListActivity.this).forceLoad();
                        } else {
                            getSupportLoaderManager().initLoader(ARCHIVE_PAGE_LOADER, null,
                                    NewsListActivity.this).forceLoad();
                        }
                    }
                }
            }
        });
        progressBar.setVisibility(View.GONE);
        newsRecyclerView.setVisibility(View.VISIBLE);
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
    public Loader<Document> onCreateLoader(int id, Bundle args) {
        // TODO: pattern strategy for HtmlPageLoader
        if (id == PAGE_LOADER) {
            return new HtmlPageLoader(this, PetrSU.getUrl());
        } else if (id == ARCHIVE_PAGE_LOADER) {
            return new HtmlPageLoader(this, PetrSU.getNewsArchiveUrl());
        }
        return null;
    }

    // TODO: will fix loading bug when no connection
    @Override
    public void onLoadFinished(Loader<Document> loader, Document data) {
        if (HtmlPageLoader.isFailLoad()) {
            createSnackbarReplyConnection();
            return;
        }
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
        if (loader.getId() == PAGE_LOADER) {
            newsLab.setNewsList(NewsParser.createNewsList(data));
            onBindRecyclerView();
        } else if (loader.getId() == ARCHIVE_PAGE_LOADER) {
            newsLab.addNewsList(NewsParser.createNewsList(data));
            adapter.notifyDataSetChanged();
            HtmlPageLoader.setLoading(false);
        }
    }

    @Override
    public void onLoaderReset(Loader<Document> loader) {

    }

    private class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private final List<News> newsList;

        SimpleItemRecyclerViewAdapter(List<News> newsList) {
            this.newsList = newsList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_content,
                    parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.onBind(position);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = NewsDetailActivity.newIntent(context, position);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final View view;
            private final TextView titleTextView;
            private News news;

            ViewHolder(View view) {
                super(view);
                this.view = view;
                titleTextView = (TextView) view.findViewById(R.id.title_news_text);
            }

            void onBind(int position) {
                news = newsList.get(position);
                titleTextView.setText(news.getTitle());
            }
        }
    }
}
