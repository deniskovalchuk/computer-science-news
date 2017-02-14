package ru.petrsu.cs.news;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import ru.petrsu.cs.news.petrsu.Url;

/**
 * This activity contains one fragment {@link ru.petrsu.cs.news.SearchFragment}.
 *
 * @author Kovalchuk Denis
 * @version 1.0
 */

public class SearchActivity extends SingleFragmentActivity {
    private static final String EXTRA_URL = "url";

    /**
     * Creates Intent for transfer to SearchActivity.
     *
     * @param context Current context.
     * @param url Actually url for load data.
     * @return intent
     */
    public static Intent newIntent(Context context, Url url) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Fragment getFragment() {
        return SearchFragment.newInstance((Url) getIntent().getParcelableExtra(EXTRA_URL));
    }

    /**
     * {@inheritDoc}
     */
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
