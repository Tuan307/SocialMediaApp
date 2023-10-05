package com.base.app.ui.main.fragment.search

import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.app.CustomApplication.Companion.dataManager
import com.base.app.R
import com.base.app.base.fragment.BaseFragment
import com.base.app.common.CommonUtils.hideSoftKeyboard
import com.base.app.common.recycleview_utils.EndlessRecyclerViewScrollListener
import com.base.app.data.models.dating_app.DatingUser
import com.base.app.databinding.FragmentSearchBinding
import com.base.app.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(),
    SearchAdapter.ICallBack {
    private val viewModel by viewModels<SearchViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()
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
                LinearLayoutManager(requireContext())
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
        viewModel.getRecentSearchKey()
    }

    override fun initListener() {
        binding.apply {
            imgSearch.setOnClickListener {
                activity?.hideSoftKeyboard()
            }
            edtSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    searchUser(p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
        }
    }

    private fun searchUser(s: String) {
        if (s == "" || s.isEmpty()) {
            viewModel.getRecentSearchKey()
        } else {
            searchAdapter.isRecent = false
            viewModel.searchUser(s, 10, 1)
        }
    }

    override fun observerLiveData() = with(viewModel) {
        searchUserResponse.observe(this@SearchFragment) {
            userList.clear()
            userList.addAll(it)
            searchAdapter.submitList(userList.toList())
        }
        searchUserLoadMoreResponse.observe(this@SearchFragment) {
            userList.addAll(it)
            searchAdapter.submitList(userList.toList())
        }
        searchRecentKeyResponse.observe(this@SearchFragment) {
            if (it.isNotEmpty()) {
                searchAdapter.isRecent = true
                viewModel.getRecentSearch(it)
            }
        }
        recentSearchListResponse.observe(this@SearchFragment) {
            searchAdapter.submitList(emptyList())
            searchAdapter.submitList(it.toList())
        }
    }

    override fun itemClick(id: String) {
        activity?.hideSoftKeyboard()
        if (dataManager.getString("id") != null) {
            dataManager.remove("id")
        }
        dataManager.save("id", id)
        mainViewModel.setCurrentIndex(4)
        mainViewModel.setSomething(true)
        viewModel.setRecent(id)
    }

    override fun removeSearch(id: String) {
        viewModel.removeRecent(id)
    }

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }
}