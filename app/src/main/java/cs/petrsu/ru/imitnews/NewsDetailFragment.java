package cs.petrsu.ru.imitnews;

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

import cs.petrsu.ru.imitnews.news.News;
import cs.petrsu.ru.imitnews.news.NewsLab;
import cs.petrsu.ru.imitnews.parser.PetrSU;

/**
 * Created by Kovalchuk Denis on 28.11.16.
 * Email: deniskk25@gmail.com
 */

public class NewsDetailFragment extends Fragment {
    private static final String TAG = "NewsDetailFragment";
    private static final String ARG_NEWS_ID = "item_id";
    private News news;

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
        news = NewsLab.get().getNews(position);
        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(getString(R.string.title_news_list));
        }
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
                intent = intent.createChooser(intent, getString(R.string.send_to));
                startActivity(intent);
            }
        });

        if (news != null) {
            WebView webView = (WebView) rootView.findViewById(R.id.fragment_detail_web_view);
            webView.loadDataWithBaseURL(PetrSU.getUrl(),
                    news.getHtml(),
                    "text/html; charset = utf-8;",
                    "utf-8",
                    null);
        }

        return rootView;
    }
}
