package com.base.app.ui.main.fragment.search

import android.content.Intent
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.common.CommonUtils.hideSoftKeyboard
import com.base.app.common.recycleview_utils.EndlessRecyclerViewScrollListener
import com.base.app.data.models.dating_app.DatingUser
import com.base.app.databinding.FragmentSearchBinding
import com.base.app.ui.profile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchForFriendActivity : BaseActivity<FragmentSearchBinding>(),
    SearchAdapter.ICallBack {
    private val viewModel by viewModels<SearchForFriendViewModel>()
    private lateinit var searchAdapter: SearchAdapter
    private val userList = ArrayList<DatingUser>()
    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    override fun getContentLayout(): Int {
        return R.layout.fragment_search
    }

    override fun initView() {
        searchAdapter = SearchAdapter(this)
        binding.rcvSearch.apply {
            layoutManager =
                LinearLayoutManager(this@SearchForFriendActivity)
            setHasFixedSize(true)
            adapter = searchAdapter
        }
        endlessRecyclerViewScrollListener = object :
            EndlessRecyclerViewScrollListener(binding.rcvSearch.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                if (page > 1) {
                    viewModel.searchUser(binding.edtSearch.text.toString(), 10, page)
                }
            }
        }
        binding.rcvSearch.addOnScrollListener(endlessRecyclerViewScrollListener)
    }

    override fun initListener() {
        with(binding) {
            toolbarSearchForFriend.setNavigationOnClickListener {
                finish()
            }
            imgSearch.setOnClickListener {
                hideSoftKeyboard()
            }
            edtSearch.setOnEditorActionListener { text, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.searchUser(text.text.toString(), 10, 1)
                }
                true
            }
        }
    }

    override fun observerLiveData() = with(viewModel) {
        searchUserResponse.observe(this@SearchForFriendActivity) {
            userList.clear()
            userList.addAll(it)
            searchAdapter.submitList(userList.toList())
        }
        searchUserLoadMoreResponse.observe(this@SearchForFriendActivity) {
            userList.addAll(it)
            searchAdapter.submitList(userList.toList())
        }
    }

    override fun itemClick(id: String) {
        hideSoftKeyboard()
        val intent = Intent(this@SearchForFriendActivity, ProfileActivity::class.java)
        intent.putExtra("userId", id)
        startActivity(intent)
    }

    override fun removeSearch(id: String) {
    }
}