package com.base.app.ui.group.detail_group

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.data.models.response.post.ImagesList
import com.base.app.databinding.ActivitySearchGroupPostBinding
import com.base.app.ui.comment.CommentActivity
import com.base.app.ui.group.detail_group.adapter.GroupFeedAdapter
import com.base.app.ui.main.fragment.home.DetailHomePostActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchGroupPostActivity : AppCompatActivity(), GroupFeedAdapter.ISearchGroupPostCallBack {
    private lateinit var binding: ActivitySearchGroupPostBinding
    private val viewModel by viewModels<GroupDetailViewModel>()
    private lateinit var searchAdapter: GroupFeedAdapter
    private var groupId: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this@SearchGroupPostActivity,
            R.layout.activity_search_group_post
        )
        setContentView(binding.root)
        val intent = intent
        groupId = intent.getLongExtra("groupId", 0)
        searchAdapter = GroupFeedAdapter(viewModel, this@SearchGroupPostActivity)
        with(binding) {
            listOfGroupPost.apply {
                layoutManager = LinearLayoutManager(this@SearchGroupPostActivity)
                adapter = searchAdapter
            }
            imageBack.setOnClickListener {
                finish()
            }
            inputSearchGroupPost.setOnEditorActionListener { _, actionId, _ ->
                val keyword = inputSearchGroupPost.text.toString()
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (keyword.isNotEmpty()) {
                        viewModel.searchPostInGroup(groupId, keyword)
                    }
                }
                true
            }
        }
        observeData()
    }

    private fun observeData() = with(viewModel) {
        searchPostInGroupResponse.observe(this@SearchGroupPostActivity) {
            if (it.isNullOrEmpty()) {
                binding.listOfGroupPost.visibility = View.GONE
                binding.linearEmptyView.visibility = View.VISIBLE
            } else {
                binding.listOfGroupPost.visibility = View.VISIBLE
                binding.linearEmptyView.visibility = View.GONE
                searchAdapter.submitList(it.toList())
            }
        }
    }

    override fun clickPost(postId: String, publisherId: String) {
        val intent = Intent(this@SearchGroupPostActivity, CommentActivity::class.java)
        intent.putExtra("from", "group")
        intent.putExtra("postId", postId)
        intent.putExtra("publisherId", publisherId)
        startActivity(intent)
    }

    override fun clickToSeeDetail(listData: List<ImagesList>, position: Int) {
        val intent = Intent(this@SearchGroupPostActivity, DetailHomePostActivity::class.java)
        val bundle = Bundle()
        bundle.putInt("postPosition", position)
        bundle.putParcelableArrayList("postList", java.util.ArrayList(listData))
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun likePost(postId: String, status: String, publisherId: String) {
        viewModel.likeGroupPost(postId, status, publisherId)
        if (publisherId != viewModel.firebaseUser?.uid.toString()
            && status == "like"
        ) {
            viewModel.getReceiverToken(publisherId)
        }
    }

    override fun commentPost(postId: String, publisherId: String, imageUrl: String) {
        val intent = Intent(this@SearchGroupPostActivity, CommentActivity::class.java)
        intent.putExtra("from", "group")
        intent.putExtra("postId", postId)
        intent.putExtra("publisherId", publisherId)
        intent.putExtra("imageUrl", imageUrl)
        startActivity(intent)
    }

    override fun savePost(postId: String) {
    }

    override fun sharePost(post: Drawable) {
    }

    override fun doubleClickLikePost(postId: String, status: String, publisherId: String) {
    }

    override fun downloadImage(fileName: String, postId: String) {
    }

    override fun editImage(postId: String, view: View) {
    }

    override fun deleteImage(postId: String) {
        viewModel.deleteGroupPost(postId)
    }
}