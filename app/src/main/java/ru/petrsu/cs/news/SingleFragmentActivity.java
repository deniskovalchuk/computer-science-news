package ru.petrsu.cs.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Kovalchuk Denis on 09.01.17.
 * Email: deniskk25@gmail.com
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment getFragment();
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
