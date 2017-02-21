package ru.petrsu.cs.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * @author Kovalchuk Denis
 * @version 1.0
 */

public class NewsDetailActivity extends AppCompatActivity {
    private static final String EXTRA_NEWS_ID = "itemId";

    public static Intent newIntent(Context context, int position) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(EXTRA_NEWS_ID, position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        int newsId = getIntent().getIntExtra(EXTRA_NEWS_ID, 0);
        if (savedInstanceState == null) {
            NewsDetailFragment fragment = NewsDetailFragment.newInstance(newsId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.new_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, getApplicationContext().getClass()));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
