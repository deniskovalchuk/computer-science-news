package cs.petrsu.ru.imitnews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;


public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragmentListNews = fragmentManager.findFragmentById(R.id.fragment_list_container);
        Fragment fragmentDetailNews = fragmentManager.findFragmentById(R.id.fragment_detail_container);

        if (fragmentListNews == null || fragmentDetailNews == null) {
            fragmentListNews = new NewsListFragment();
            fragmentDetailNews = new NewsDetailFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_list_container, fragmentListNews)
                    .add(R.id.fragment_detail_container, fragmentDetailNews)
                    .commit();
        }
    }
}
