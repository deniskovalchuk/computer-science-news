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

import org.jsoup.nodes.Document;

import java.util.List;

import cs.petrsu.ru.imitnews.news.News;
import cs.petrsu.ru.imitnews.news.NewsLab;
import cs.petrsu.ru.imitnews.parser.NewsParser;
import cs.petrsu.ru.imitnews.remote.HtmlPageLoader;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        newsLab = NewsLab.get(NewsListActivity.this);
        getLoaderManager().initLoader(PAGE_LOADER, null, this).forceLoad();

        if (findViewById(R.id.new_detail_container) != null) {
            isTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(NewsLab.get(this).getListNews()));
    }

    @Override
    public Loader<Document> onCreateLoader(int i, Bundle bundle) {
        if (i == PAGE_LOADER) {
            return new HtmlPageLoader(NewsListActivity.this, NewsParser.getUrl());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Document> loader, Document document) {
        if (loader.getId() == PAGE_LOADER) {
            NewsParser newsParser = new NewsParser(NewsListActivity.this, document);
            newsLab.setListNews(newsParser.getListNews());

            View recyclerView = findViewById(R.id.new_list);
            assert recyclerView != null;
            setupRecyclerView((RecyclerView) recyclerView);
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
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.news = newsList.get(position);
            holder.titleTextView.setText(newsList.get(position).getTitle());

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

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View view;
            public final TextView titleTextView;
            public News news;

            public ViewHolder(View view) {
                super(view);
                this.view = view;
                titleTextView = (TextView) view.findViewById(R.id.title_news_text);
            }
        }
    }
}
