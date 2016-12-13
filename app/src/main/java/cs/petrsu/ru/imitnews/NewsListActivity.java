package cs.petrsu.ru.imitnews;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.jsoup.nodes.Document;

import java.util.List;

import cs.petrsu.ru.imitnews.news.News;
import cs.petrsu.ru.imitnews.news.NewsLab;
import cs.petrsu.ru.imitnews.parser.PetrSU;
import cs.petrsu.ru.imitnews.remote.HtmlPageLoader;

/**
 * Created by Kovalchuk Denis on 28.11.16.
 * Email: deniskk25@gmail.com
 */

public class NewsListActivity extends AppCompatActivity
        implements android.app.LoaderManager.LoaderCallbacks<Document> {
    private static final String TAG = "NewsListActivity";
    private static final int PAGE_LOADER = 0;
    private boolean isTwoPane;
    private NewsLab newsLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        newsLab = NewsLab.get();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.new_detail_container) != null) {
            isTwoPane = true;
        } else {
            // Show fab only for two pane format
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setVisibility(View.INVISIBLE);
        }

        if (newsLab.getNewsList().isEmpty()) {
            getLoaderManager().initLoader(PAGE_LOADER, null, this).forceLoad();
        } else {
            onBindRecyclerView();
        }
    }

    private void onBindRecyclerView() {
        View recyclerView = findViewById(R.id.new_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(newsLab.getNewsList()));
    }

    @Override
    public Loader<Document> onCreateLoader(int i, Bundle bundle) {
        if (i == PAGE_LOADER) {
            return new HtmlPageLoader(this, PetrSU.getUrl());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Document> loader, Document document) {
        if (loader.getId() == PAGE_LOADER) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_news_list),
                    "Not connection",
                    Snackbar.LENGTH_INDEFINITE);
            if (document == null) {
                snackbar.show();
                return;
            }
            if (snackbar.isShown()) {
                snackbar.dismiss();
            }
            newsLab.setNewsList(PetrSU.createNewsList(this, document));
            onBindRecyclerView();
        }
    }

    @Override
    public void onLoaderReset(Loader<Document> loader) {

    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private final List<News> newsList;

        public SimpleItemRecyclerViewAdapter(List<News> newsList) {
            this.newsList = newsList;
            if (isTwoPane) {
                NewsDetailFragment fragment = NewsDetailFragment.newInstance(0);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.new_detail_container, fragment)
                        .commit();
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.onBind(position);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isTwoPane) {
                        NewsDetailFragment fragment = NewsDetailFragment.newInstance(position);
                        getSupportFragmentManager().beginTransaction()
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
            private final TextView titleTextView;
            private News news;
            final View view;

            ViewHolder(View view) {
                super(view);
                this.view = view;
                titleTextView = (TextView) view.findViewById(R.id.title_news_text);
            }

            public void onBind(int position) {
                news = newsList.get(position);
                titleTextView.setText(news.getTitle());
            }
        }
    }
}
