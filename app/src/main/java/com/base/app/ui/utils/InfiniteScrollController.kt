package com.base.app.ui.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author tuanpham
 * @since 5/21/2024
 */
abstract class InfiniteScrollController(
    private val layoutManager: LinearLayoutManager,
    private val visibleThreshold: Int = 1
) :
    RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisible = layoutManager.findFirstVisibleItemPosition()
        if (isLoading() || isLastPage()) {
            return
        }
        if (firstVisible >= 0 && (firstVisible + visibleItemCount) >= totalItemCount - visibleThreshold) {
            loadMore()
        }
    }

    abstract fun loadMore()
    abstract fun isLoading(): Boolean
    abstract fun isLastPage(): Boolean
}