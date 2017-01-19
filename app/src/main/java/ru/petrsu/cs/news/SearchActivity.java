package ru.petrsu.cs.news;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import ru.petrsu.cs.news.petrsu.Url;

/**
 * Created by Kovalchuk Denis on 09.01.17.
 * Email: deniskk25@gmail.com
 */

public class SearchActivity extends SingleFragmentActivity {
    private static final String EXTRA_URL = "url";

    public static Intent newIntent(Context context, Url url) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    @Override
    protected Fragment getFragment() {
        return SearchFragment.newInstance((Url) getIntent().getParcelableExtra(EXTRA_URL));
    }

    @Override
    protected void createAppBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
