package ru.petrsu.cs.news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import ru.petrsu.cs.news.news.News;
import ru.petrsu.cs.news.news.NewsLab;
import ru.petrsu.cs.news.petrsu.Url;

/**
 * @author Kovalchuk Denis
 * @version 1.0
 */

public class NewsDetailFragment extends Fragment {
    private static final String ARG_NEWS_ID = "itemId";
    private static final String KEY_NEWS = "news";
    private static News news;
    private WebView webView;

    public static NewsDetailFragment newInstance(int position) {
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_NEWS_ID, position);
        NewsDetailFragment newDetailFragment = new NewsDetailFragment();
        newDetailFragment.setArguments(arguments);
        return newDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            news = savedInstanceState.getParcelable(KEY_NEWS);
        } else {
            int position = getArguments().getInt(ARG_NEWS_ID);
            NewsLab newsLab = NewsLab.getInstance();
            news = newsLab.getNews(position);
        }

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(getString(R.string.app_name));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_NEWS, news);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.news_detail, container, false);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(news.getContent())
                        .getIntent();
                intent = Intent.createChooser(intent, getString(R.string.send_to));
                startActivity(intent);
            }
        });
        webView = (WebView) rootView.findViewById(R.id.news_web_view);
        webView.loadDataWithBaseURL(Url.getBaseUrl(), news.getHtml(),
                "text/html; charset = utf-8;", "utf-8", null);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
        }
    }
}
