package ru.petrsu.cs.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Base class for activity containing single fragment.
 *
 * @author Kovalchuk Denis
 * @version 1.0
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {
    /**
     * @return fragment Fragment contains in activity.
     */
    protected abstract Fragment getFragment();

    /**
     * Creates application bar current activity.
     */
    protected abstract void createAppBar();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        createAppBar();

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.news_list_container);
        if (fragment == null) {
            fragment = getFragment();
            fm.beginTransaction()
                    .add(R.id.news_list_container, fragment)
                    .commit();
        }
    }
}
