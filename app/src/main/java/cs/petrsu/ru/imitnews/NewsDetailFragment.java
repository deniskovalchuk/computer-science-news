package cs.petrsu.ru.imitnews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cs.petrsu.ru.imitnews.news.News;
import cs.petrsu.ru.imitnews.news.NewsLab;

/**
 * Created by Kovalchuk Denis on 22.11.16.
 * Email: deniskk25@gmail.com
 */

public class NewsDetailFragment extends Fragment {
    private static final String TAG = "NewsDetailFragment";
    private static final String ARG_NEWS_POSITION = "newsPosition";
    private News news;

    private TextView newsTitleTextView;
    private TextView newsDateTextView;
    private TextView newsTagTextView;
    private TextView newsContentTextView;

    public static NewsDetailFragment newInstance(int position) {
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_NEWS_POSITION, position);
        NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
        newsDetailFragment.setArguments(arguments);
        return newsDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int position = getArguments().getInt(ARG_NEWS_POSITION);
        news = NewsLab.get(getContext()).getNews(position);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_news, container, false);

        newsTitleTextView = (TextView) view.findViewById(R.id.fragment_detail_title_text);
        newsDateTextView = (TextView) view.findViewById(R.id.fragment_detail_date_text);
        newsTagTextView = (TextView) view.findViewById(R.id.fragment_detail_tag_text);
        newsContentTextView = (TextView) view.findViewById(R.id.fragment_detail_content_text);
        updateUI();

        return view;
    }

    private void updateUI() {
        newsTitleTextView.setText(news.getTitle());
        newsDateTextView.setText(news.getDate());
        newsTagTextView.setText(news.getTag());
        newsContentTextView.setText(news.getContent());
    }
}
