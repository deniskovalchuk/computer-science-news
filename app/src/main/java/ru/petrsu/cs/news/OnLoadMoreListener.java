package ru.petrsu.cs.news;

/**
 * Interface for downloading new data when scrolling {@link EndlessRecyclerViewFragment#endlessRecyclerView}.
 *
 * @author Kovalchuk Denis
 * @version 1.0
 */

interface OnLoadMoreListener {
    void onLoadMore();
}
