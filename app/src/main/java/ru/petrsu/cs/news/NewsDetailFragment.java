package ru.petrsu.cs.news;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
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

    private WebView webView;

    private static News news;
    private Url url;

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

        int position = getArguments().getInt(ARG_NEWS_ID);
        NewsLab newsLab = NewsLab.getInstance();
        news = newsLab.getNews(position);
        url = new Url();

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(getString(R.string.app_name));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.news_detail, container, false);

        if (news != null) {
            webView = (WebView) rootView.findViewById(R.id.news_web_view);
            webView.loadDataWithBaseURL(url.toString(), news.getHtml(),
                    "text/html; charset = utf-8;", "utf-8", null);
        }

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }
}
