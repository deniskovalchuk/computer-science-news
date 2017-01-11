package ru.petrsu.cs.news;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by Kovalchuk Denis on 09.01.17.
 * Email: deniskk25@gmail.com
 */

public class SearchActivity extends SingleFragmentActivity {
    @Override
    protected Fragment getFragment() {
        return new SearchFragment();
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
