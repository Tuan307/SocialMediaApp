package com.base.app.ui.main.fragment.notification

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.base.app.R
import com.base.app.base.fragment.BaseFragment
import com.base.app.common.recycleview_utils.EndlessRecyclerViewScrollListener
import com.base.app.data.models.response.NotificationContent
import com.base.app.databinding.FragmentNotificationBinding
import dagger.hilt.android.AndroidEntryPoint
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

@AndroidEntryPoint
class NotificationFragment : BaseFragment<FragmentNotificationBinding>(),
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    private val viewModel by viewModels<NotificationViewModel>()
    private lateinit var notificationAdapter: NotificationAdapter
    private var notificationList = ArrayList<NotificationContent>()
    private lateinit var prettyTime: PrettyTime

    override fun getContentLayout(): Int {
        return R.layout.fragment_notification
    }

    override fun initView() {
        viewModel.getAllNotification(10, 1)
        prettyTime = PrettyTime(Locale.getDefault())
        with(binding) {
            swipeRefreshNotification.setOnRefreshListener(this@NotificationFragment)
            notificationAdapter = NotificationAdapter()
            listNotification.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = notificationAdapter
            }
            endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(
                listNotification.layoutManager as LinearLayoutManager
            ) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    if (page > 1) {
                        viewModel.getAllNotification(10, page)
                    }
                }
            }
            listNotification.addOnScrollListener(endlessRecyclerViewScrollListener)
        }
    }

    override fun initListener() {
    }

    override fun observerLiveData() {
        with(viewModel) {
            getAllNotificationResponse.observe(this@NotificationFragment) {
                val data = it.data?.content
                if (data != null) {
                    notificationList.clear()
                    notificationList.addAll(data.map { itemData ->
                        NotificationContent(
                            id = itemData.id,
                            isPost = itemData.isPost,
                            isInvitation = itemData.isInvitation,
                            text = itemData.text,
                            notificationPostId = itemData.notificationPostId,
                            notificationOwnerId = itemData.notificationOwnerId,
                            notificationTimeStamp = prettyTime.format(
                                Date(
                                    itemData.notificationTimeStamp.toString().toLong()
                                )
                            ),
                            notificationUserId = itemData.notificationUserId,
                            isRequest = itemData.isRequest,
                            notificationGroupId = itemData.notificationGroupId
                        )
                    })
                    notificationAdapter.submitList(notificationList.toList())
                }
            }
            getMoreNotificationResponse.observe(this@NotificationFragment) {
                val data = it.data?.content
                if (data != null) {
                    notificationList.addAll(data.map { itemData ->
                        NotificationContent(
                            id = itemData.id,
                            isPost = itemData.isPost,
                            isInvitation = itemData.isInvitation,
                            text = itemData.text,
                            notificationPostId = itemData.notificationPostId,
                            notificationOwnerId = itemData.notificationOwnerId,
                            notificationTimeStamp = prettyTime.format(
                                Date(
                                    itemData.notificationTimeStamp.toString().toLong()
                                )
                            ),
                            notificationUserId = itemData.notificationUserId,
                            isRequest = itemData.isRequest,
                            notificationGroupId = itemData.notificationGroupId
                        )
                    })
                    notificationAdapter.submitList(notificationList.toList())
                }
            }
        }
    }

    companion object {
        fun newInstance(): NotificationFragment {
            return NotificationFragment()
        }
    }

    override fun onRefresh() {
        loadRefreshData()
    }

    private fun loadRefreshData() {
        viewModel.getAllNotification(10, 1)
        endlessRecyclerViewScrollListener.resetState()
        notificationAdapter.submitList(null)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.swipeRefreshNotification.isRefreshing = false
        }, 1500)
    }
}