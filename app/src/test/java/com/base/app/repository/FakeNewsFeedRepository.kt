package com.base.app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.base.app.data.models.response.post.PostContent
import com.base.app.data.repositories.feed.NewsRepo

/**
 * @author tuanpham
 * @since 5/7/2024
 */
class FakeNewsFeedRepository :NewsRepo{
    private val newsFeedItems = mutableListOf<PostContent>()
    val observableNewsFeed = MutableLiveData<List<PostContent>>(newsFeedItems)
    private var shouldReturnNetworkError = false


    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    suspend fun getNewsFeed(): LiveData<List<PostContent>> {
        return observableNewsFeed
    }
}