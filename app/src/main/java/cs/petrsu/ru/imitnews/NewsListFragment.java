package cs.petrsu.ru.imitnews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

/**
 * Created by Kovalchuk Denis on 22.11.16.
 * Email: deniskk25@gmail.com
 */
public class NewsListFragment extends android.support.v4.app.Fragment {
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_news, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_list_news_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new NewsAdapter());
        return view;
    }

    protected class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {
        @Override
        public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_news, parent, false);
            return new NewsHolder(view);
        }

        @Override
        public void onBindViewHolder(NewsHolder holder, int position) {
            holder.titleText.setText("text");
        }

        @Override
        public int getItemCount() {
            return 100;
        }

        class NewsHolder extends RecyclerView.ViewHolder {
            public TextView titleText;

            public NewsHolder(View itemView) {
                super(itemView);
                titleText = (TextView) itemView.findViewById(R.id.title_news_text);
            }
        }
    }



}
