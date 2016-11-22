package cs.petrsu.ru.imitnews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cs.petrsu.ru.imitnews.news.News;
import cs.petrsu.ru.imitnews.news.NewsLab;


/**
 * Created by Kovalchuk Denis on 22.11.16.
 * Email: deniskk25@gmail.com
 */

public class NewsListFragment extends Fragment {
    private static final String TAG = "NewsListFragment";
    private NewsLab newsLab;

    private RecyclerView newsRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsLab = NewsLab.get(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_news, container, false);

        newsRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_list_news_recycler_view);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsRecyclerView.setAdapter(new NewsAdapter(newsLab.getListNews()));
        return view;
    }

    private class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {
        List<News> newsList;

        public NewsAdapter(List<News> newsList) {
            this.newsList = newsList;
        }

        @Override
        public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_news, parent, false);
            return new NewsHolder(view);
        }

        @Override
        public void onBindViewHolder(NewsHolder holder, int position) {
            holder.bindNews(position);
        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }

        class NewsHolder extends RecyclerView.ViewHolder {
            private int position;
            private TextView titleText;

            public NewsHolder(View itemView) {
                super(itemView);
                titleText = (TextView) itemView.findViewById(R.id.title_news_text);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment newsDetailFragment = NewsDetailFragment.newInstance(position);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragment_detail_container, newsDetailFragment)
                                .commit();
                    }
                });
            }

            public void bindNews(int newsPosition) {
                position = newsPosition;
                titleText.setText(newsList.get(position).getTitle());
            }
        }
    }
}
