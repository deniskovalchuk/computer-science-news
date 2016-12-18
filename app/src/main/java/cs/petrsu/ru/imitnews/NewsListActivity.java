package cs.petrsu.ru.imitnews;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
    private static final String NEWS_INDEX_KEY = "news_index";
    private static final int PAGE_LOADER = 0;

    private NewsLab newsLab;
    private boolean isTwoPane;
    private int newsIndex;

    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        newsLab = NewsLab.getInstance();
        newsIndex = 0;

        if (savedInstanceState != null) {
            newsIndex = savedInstanceState.getInt(NEWS_INDEX_KEY);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.new_detail_container) != null) {
            isTwoPane = true;
        }

        if (HtmlPageLoader.isFailLoad()) {
            getSupportLoaderManager().initLoader(PAGE_LOADER, null, this).forceLoad();
        } else {
            onBindRecyclerView();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NEWS_INDEX_KEY, newsIndex);
    }

    private void startShareActivity() {
        if (newsLab.getNewsList().isEmpty()) {
            return;
        }
        Intent intent = ShareCompat.IntentBuilder.from(this).setType("text/plain")
                .setText(newsLab.getNews(newsIndex).getContent()).getIntent();
        intent = Intent.createChooser(intent, getString(R.string.send_to));
        startActivity(intent);
    }

    private void onBindRecyclerView() {
        RecyclerView newsRecyclerView = (RecyclerView) findViewById(R.id.news_list);
        assert newsRecyclerView != null;
        newsRecyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(newsLab.getNewsList()));
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

    private void createSnackbarNoNews() {
        snackbar = Snackbar.make(findViewById(R.id.activity_news_list),
                getString(R.string.no_news), Snackbar.LENGTH_INDEFINITE);
        View view = snackbar.getView();
        TextView text = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            text.setGravity(Gravity.CENTER_HORIZONTAL);
        }
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
            case R.id.menu_share:
                startShareActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Document> onCreateLoader(int id, Bundle args) {
        if (id == PAGE_LOADER) {
            return new HtmlPageLoader(this, PetrSU.getUrl());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Document> loader, Document data) {
        if (loader.getId() == PAGE_LOADER) {
            if (HtmlPageLoader.isFailLoad()) {
                createSnackbarReplyConnection();
                return;
            }
            if (snackbar != null && snackbar.isShown()) {
                snackbar.dismiss();
            }
            newsLab.setNewsList(NewsParser.createNewsList(data));
            onBindRecyclerView();
        }
    }

    @Override
    public void onLoaderReset(Loader<Document> loader) {

    }

    private void createFragment() {
        NewsDetailFragment fragment = NewsDetailFragment.newInstance(newsIndex);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.new_detail_container, fragment)
                .commitAllowingStateLoss();
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
            if (newsList.isEmpty()) {
                createSnackbarNoNews();
            } else if (isTwoPane) {
                createFragment();
            }
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.onBind(position);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newsIndex = position;
                    if (isTwoPane) {
                        NewsDetailFragment fragment = NewsDetailFragment.newInstance(position);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.new_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = NewsDetailActivity.newIntent(context, position);
                        context.startActivity(intent);
                    }
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
