package com.base.app.ui.main.fragment.notification

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.common.recycleview_utils.EndlessRecyclerViewScrollListener
import com.base.app.data.models.response.NotificationContent
import com.base.app.databinding.ActivityNotificationBinding
import dagger.hilt.android.AndroidEntryPoint
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

@AndroidEntryPoint
class NotificationActivity : BaseActivity<ActivityNotificationBinding>(),
    SwipeRefreshLayout.OnRefreshListener, NotificationAdapter.OnNotificationClick {

    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    private val viewModel by viewModels<NotificationViewModel>()
    private lateinit var notificationAdapter: NotificationAdapter
    private var notificationList = ArrayList<NotificationContent>()
    private lateinit var prettyTime: PrettyTime
    private var removeAtPosition = -1
    override fun getContentLayout(): Int {
        return R.layout.activity_notification
    }

    override fun initView() {
        viewModel.getAllNotification(10, 1)
        prettyTime = PrettyTime(Locale.getDefault())
        with(binding) {
            imageBack.setOnClickListener {
                finish()
            }
            swipeRefreshNotification.setOnRefreshListener(this@NotificationActivity)
            notificationAdapter = NotificationAdapter(this@NotificationActivity)
            listNotification.apply {
                layoutManager = LinearLayoutManager(this@NotificationActivity)
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
            getAllNotificationResponse.observe(this@NotificationActivity) {
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
            getMoreNotificationResponse.observe(this@NotificationActivity) {
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
            removeNotificationResponse.observe(this@NotificationActivity) {
                notificationList.removeAt(removeAtPosition)
                val list = arrayListOf<NotificationContent>()
                list.addAll(notificationList)
                notificationAdapter.submitList(list.toList())
            }
            removeNotificationErrorResponse.observe(this@NotificationActivity) {
                Toast.makeText(this@NotificationActivity, it, Toast.LENGTH_SHORT).show()
            }
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

    override fun onConfirm(data: NotificationContent, position: Int) {
        removeAtPosition = position
        data.notificationGroupId?.id?.let { data.id?.let { it1 -> viewModel.joinGroup(it, it1) } }
    }

    override fun onReject(data: NotificationContent, position: Int) {
        removeAtPosition = position
        data.notificationGroupId?.id?.let {
            data.id?.let { it1 ->
                viewModel.cancelJoinInvitation(
                    it,
                    it1
                )
            }
        }
    }
}