package com.base.app.ui.main.fragment.search

import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.CustomApplication.Companion.dataManager
import com.base.app.R
import com.base.app.base.fragment.BaseFragment
import com.base.app.common.CommonUtils.hideSoftKeyboard
import com.base.app.data.models.User
import com.base.app.databinding.FragmentSearchBinding
import com.base.app.ui.main.MainViewModel

class SearchFragment : BaseFragment<FragmentSearchBinding>(),
    SearchAdapter.ICallBack {
    private val viewModel by viewModels<SearchViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()
    private lateinit var searchAdapter: SearchAdapter
    private var lists = ArrayList<User>()

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    override fun getContentLayout(): Int {
        return R.layout.fragment_search
    }

    override fun initView() {
        viewModel.getRecentSearchKey()
        searchAdapter = SearchAdapter(lists, requireContext(), this)
        binding.apply {
            rcvSearch.layoutManager =
                LinearLayoutManager(requireContext())
            rcvSearch.setHasFixedSize(true)
            rcvSearch.adapter = searchAdapter
        }
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
        if (s != "") {
            searchAdapter.isRecent = false
            viewModel.searchUser(s)
        }
    }

    override fun observerLiveData() {
        viewModel.getUserResponse.observe(this@SearchFragment) {
            lists.clear()
            lists.addAll(it)
            searchAdapter.notifyDataSetChanged()
        }
        viewModel.searchKeyResponse.observe(this@SearchFragment) {
            if (it.isNotEmpty()) {
                searchAdapter.isRecent = true
                viewModel.getRecentSearch(it)
            }
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

}