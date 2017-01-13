package ru.petrsu.cs.news;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

/**
 * Created by Kovalchuk Denis on 28.11.16.
 * Email: deniskk25@gmail.com
 */

public class NewsListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment getFragment() {
        return new NewsListFragment();
    }

    @Override
    protected void createAppBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
    }
}

