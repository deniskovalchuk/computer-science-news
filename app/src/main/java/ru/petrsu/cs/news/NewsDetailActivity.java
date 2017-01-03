package ru.petrsu.cs.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import ru.petrsu.cs.news.news.News;
import ru.petrsu.cs.news.news.NewsLab;

/**
 * Created by Kovalchuk Denis on 28.11.16.
 * Email: deniskk25@gmail.com
 */

public class NewsDetailActivity extends AppCompatActivity {
    private static final String TAG = "NewsDetailActivity";
    private static final String EXTRA_NEWS_ID = "item_id";

    public static Intent newIntent(Context context, int position) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(EXTRA_NEWS_ID, position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        NewsLab newsLab = NewsLab.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        int newsId = getIntent().getIntExtra(EXTRA_NEWS_ID, 0);
        final News news = newsLab.getNews(newsId);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ShareCompat.IntentBuilder.from(NewsDetailActivity.this)
                        .setType("text/plain")
                        .setText(news.getContent())
                        .getIntent();
                intent = Intent.createChooser(intent, getString(R.string.send_to));
                startActivity(intent);
            }
        });

        if (savedInstanceState == null) {
            NewsDetailFragment fragment = NewsDetailFragment.newInstance(newsId);
            getSupportFragmentManager().beginTransaction().add(R.id.new_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, NewsListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
