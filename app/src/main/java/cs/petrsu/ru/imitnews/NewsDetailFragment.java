package cs.petrsu.ru.imitnews;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cs.petrsu.ru.imitnews.news.News;
import cs.petrsu.ru.imitnews.news.NewsLab;

public class NewsDetailFragment extends Fragment {
    public static final String ARG_NEWS_ID = "item_id";
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
        news = NewsLab.get(getContext()).getNews(position);
        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(getString(R.string.title_news_detail));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.news_detail, container, false);

        if (news != null) {
            ((TextView) rootView.findViewById(R.id.fragment_detail_title_text)).setText(news.getTitle());
            ((TextView) rootView.findViewById(R.id.fragment_detail_tag_text)).setText(news.getTag());
            ((TextView) rootView.findViewById(R.id.fragment_detail_content_text)).setText(news.getContent());
            ((TextView) rootView.findViewById(R.id.fragment_detail_date_text)).setText(news.getDate());
        }

        return rootView;
    }
}
