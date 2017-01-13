package ru.petrsu.cs.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.petrsu.cs.news.news.News;
import ru.petrsu.cs.news.news.NewsLab;
import ru.petrsu.cs.news.petrsu.PetrSU;

/**
 * Created by Kovalchuk Denis on 09.01.17.
 * Email: deniskk25@gmail.com
 */

public abstract class RecyclerViewFragment extends Fragment {
    private static final String TAG = "RecyclerViewFragment";
    protected static final String KEY_LOADING = "isLoading";
    protected static final int PAGE_LOADER = 0;
    protected boolean isLoading;

    private LinearLayoutManager layoutManager;
    protected RecyclerView newsRecyclerView;
    protected RecyclerViewAdapter adapter;
    protected ProgressBar progressBar;
    protected Snackbar snackbar;
    protected View rootView;

    public abstract android.support.v4.app.LoaderManager.LoaderCallbacks getLoaderContext();

    protected void createRecyclerView(final List<News> newsList) {
        newsRecyclerView = (RecyclerView) rootView.findViewById(R.id.news_list);
        newsRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        newsRecyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter();
        adapter.setData(newsList);
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                adapter.addProgressItem();
                startLoad();
            }
        });
        newsRecyclerView.setAdapter(adapter);
        newsRecyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    protected void createSnackbarReplyConnection() {
        snackbar = Snackbar.make(getActivity().findViewById(R.id.activity_news_list),
                getString(R.string.no_connection), Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.replay, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startLoad();
                    }
                });
        snackbar.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_LOADING, isLoading);
    }

    protected boolean isFirstLoad() {
        return NewsLab.getInstance().getFullData().isEmpty();
    }

    protected void startLoad() {
        isLoading = true;
        getActivity()
                .getSupportLoaderManager()
                .restartLoader(PAGE_LOADER, null, getLoaderContext())
                .forceLoad();
    }

    protected class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final int VIEW_PROGRESS_BAR = 0;
        private final int VIEW_ITEM = 1;

        private int lastVisibleItem, totalItemCount;
        private int visibleThreshold = 5;
        private List<News> newsList;
        private OnLoadMoreListener onLoadMoreListener;

        RecyclerViewAdapter() {
            if (newsRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                newsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        totalItemCount = layoutManager.getItemCount();
                        lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                        if (PetrSU.isValidUrl() && !isLoading && (totalItemCount <= (lastVisibleItem + visibleThreshold))) {
                            isLoading = true;
                            if (onLoadMoreListener != null) {
                                onLoadMoreListener.onLoadMore();
                            }
                        }
                    }
                });
            }
        }

        public void setData(List<News> data) {
            if (data == null) {
                return;
            }
            newsList = data;
            notifyDataSetChanged();
        }

        List<News> getData() {
            if (newsList == null) {
                newsList = new ArrayList<>();
            }
            return newsList;
        }

        void addData(List<News> data) {
            if (data != null && newsList != null && !data.isEmpty()) {
                int startPosition = newsList.size();
                int endPosition = newsList.size() + data.size();
                newsList.addAll(data);
                for (int i = startPosition; i < endPosition; i++) {
                    notifyItemInserted(i);
                }
            }
        }

        void clear() {
            if (newsList != null) {
                newsList.clear();
                notifyDataSetChanged();
            }
        }

        void addProgressItem() {
            if (newsList != null) {
                newsList.add(null);
                notifyItemInserted(newsList.size() - 1);
            }
        }

        void removeProgressItem() {
            if (newsList != null
                    && !newsList.isEmpty()
                    && newsList.get(newsList.size() - 1) == null) {
                newsList.remove(newsList.size() - 1);
                notifyItemRemoved(newsList.size());
            }
        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }

        @Override
        public int getItemViewType(int position) {
                return newsList.get(position) != null ? VIEW_ITEM : VIEW_PROGRESS_BAR;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder;
            if (viewType == VIEW_ITEM) {
                View v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.news_list_content, parent, false);
                holder = new ViewHolder(v);
            } else {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item,
                        parent, false);
                holder = new ProgressViewHolder(v);
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof ViewHolder) {
                News news = newsList.get(position);
                ((ViewHolder) holder).titleTextView.setText(news.getTitle());
                ((ViewHolder) holder).view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = NewsDetailActivity.newIntent(context, holder.getAdapterPosition());
                        context.startActivity(intent);
                    }
                });
            } else {
                ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
            }
        }

        void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
            this.onLoadMoreListener = onLoadMoreListener;
        }

        private class ProgressViewHolder extends RecyclerView.ViewHolder {
            ProgressBar progressBar;

            ProgressViewHolder(View view) {
                super(view);
                progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_item);
            }
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            final View view;
            final TextView titleTextView;

            ViewHolder(View view) {
                super(view);
                this.view = view;
                titleTextView = (TextView) view.findViewById(R.id.title_news_text);
            }
        }
    }
}
