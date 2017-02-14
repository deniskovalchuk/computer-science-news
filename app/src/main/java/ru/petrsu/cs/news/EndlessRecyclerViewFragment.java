package ru.petrsu.cs.news;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import ru.petrsu.cs.news.petrsu.Url;


/**
 * Base class for fragments containing {@link #endlessRecyclerView} with News.
 *
 * @author Kovalchuk Denis
 * @version 1.0
 */

public abstract class EndlessRecyclerViewFragment extends Fragment {
    protected static final String KEY_LOADING = "isLoading";
    protected static final String KEY_URL = "url";
    protected static final int PAGE_LOADER = 0;

    private LinearLayoutManager layoutManager;
    private RecyclerView endlessRecyclerView;
    private RecyclerViewAdapter adapter;
    private View rootView;
    private ProgressBar centralProgressBar;
    private TextView informationTextView;
    private TextView replayLoadTextView;

    private boolean isLoading;
    protected Url url;

    protected abstract android.support.v4.app.LoaderManager.LoaderCallbacks getLoaderContext();

    protected View initView(int resource, LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(resource, container, false);
        centralProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        informationTextView = (TextView) rootView.findViewById(R.id.information_text_view);
        replayLoadTextView = (TextView) rootView.findViewById(R.id.replay_load_text_view);
        replayLoadTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startLoad();
            }
        });
        return rootView;
    }

    protected void showCentralProgressBar() {
        if (endlessRecyclerView != null) {
            endlessRecyclerView.setVisibility(View.GONE);
        }
        informationTextView.setVisibility(View.GONE);
        replayLoadTextView.setVisibility(View.GONE);
        centralProgressBar.setVisibility(View.VISIBLE);
    }

    protected void showInformationTextView(int textResource) {
        if (endlessRecyclerView != null) {
            endlessRecyclerView.setVisibility(View.GONE);
        }
        centralProgressBar.setVisibility(View.GONE);
        replayLoadTextView.setVisibility(View.GONE);
        informationTextView.setText(getText(textResource));
        informationTextView.setVisibility(View.VISIBLE);
    }

    protected void updateRecyclerView(List<News> data) {
        if (adapter != null) {
            adapter.addData(data);
        }
    }

    protected void clearRecyclerView() {
        if (adapter != null) {
            adapter.clear();
        }
    }

    protected boolean wasRecyclerViewCreated() {
        return endlessRecyclerView != null;
    }

    protected boolean isEmptyRecyclerView() {
        return NewsLab.getInstance().getCurrentData().isEmpty();
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    protected void removeProgressItem() {
        if (adapter != null) {
            adapter.removeProgressItem();
        }
    }

    protected boolean isLoading() {
        return isLoading;
    }

    protected void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    protected void startLoad() {
        if (!isOnline()) {
            removeProgressItem();
            if (isEmptyRecyclerView()) {
                showLoadError();
            }
            return;
        }
        if (!isValidUrl()) {
            removeProgressItem();
            return;
        }
        showProgressLoad();
        isLoading = true;
        getActivity()
                .getSupportLoaderManager()
                .restartLoader(PAGE_LOADER, null, getLoaderContext())
                .forceLoad();
    }

    private void showProgressLoad() {
        if (adapter != null) {
            adapter.addProgressItem();
        } else {
            showCentralProgressBar();
        }
    }

    protected Url getUrl() {
        return url;
    }

    protected boolean isValidUrl() {
        return url != null && url.isValid();
    }

    protected void updateUrl() {
        if (url != null) {
            url.update();
        }
    }

    protected void showRecyclerView(final List<News> newsList) {
        centralProgressBar.setVisibility(View.GONE);
        informationTextView.setVisibility(View.GONE);
        replayLoadTextView.setVisibility(View.GONE);
        endlessRecyclerView = (RecyclerView) rootView.findViewById(R.id.news_list);
        endlessRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        endlessRecyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter();
        adapter.setData(newsList);
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                startLoad();
            }
        });
        endlessRecyclerView.setAdapter(adapter);
        endlessRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_LOADING, isLoading);
        outState.putParcelable(KEY_URL, url);
    }

    public void showLoadError() {
        showInformationTextView(R.string.no_connection);
        replayLoadTextView.setVisibility(View.VISIBLE);
    }

    protected class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final int VIEW_PROGRESS_BAR = 0;
        private final int VIEW_ITEM = 1;

        private int lastVisibleItem, totalItemCount;
        private int visibleThreshold = 5;
        private List<News> newsList;
        private OnLoadMoreListener onLoadMoreListener;

        RecyclerViewAdapter() {
            if (endlessRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                endlessRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        totalItemCount = layoutManager.getItemCount();
                        lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                        if (!isLoading && (totalItemCount <= (lastVisibleItem + visibleThreshold))) {
                            if (onLoadMoreListener != null) {
                                onLoadMoreListener.onLoadMore();
                            }
                        }
                    }
                });
            }
        }

        List<News> getData() {
            if (newsList == null) {
                newsList = new ArrayList<>();
            }
            return newsList;
        }

        public void setData(List<News> data) {
            if (data == null) {
                return;
            }
            newsList = data;
            notifyDataSetChanged();
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
                int count = newsList.size();
                newsList.clear();
                notifyItemRangeRemoved(0, count);
            }
        }

        void addProgressItem() {
            if (newsList != null && newsList.size() > 0
                    && newsList.get(newsList.size() - 1) != null) {
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
