package ru.petrsu.cs.news;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

/**
 * Launch activity.
 *
 * @author Kovalchuk Denis
 * @version 1.0
 */

public class NewsListActivity extends SingleFragmentActivity {
    /**
     * {@inheritDoc}
     */
    @Override
    protected Fragment getFragment() {
        return new NewsListFragment();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void createAppBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
    }
}

