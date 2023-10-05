package com.base.app.ui.main.fragment.notification

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.base.fragment.BaseFragment
import com.base.app.data.models.NotificationItem
import com.base.app.data.models.response.NotificationContent
import com.base.app.databinding.FragmentNotificationBinding
import dagger.hilt.android.AndroidEntryPoint
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

@AndroidEntryPoint
class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {

    private val viewModel by viewModels<NotificationViewModel>()
    private lateinit var notificationAdapter: NotificationAdapter
    private var list = ArrayList<NotificationItem>()
    private lateinit var prettyTime: PrettyTime

    override fun getContentLayout(): Int {
        return R.layout.fragment_notification
    }

    override fun initView() {
        prettyTime = PrettyTime(Locale.getDefault())
        with(binding) {
            notificationAdapter = NotificationAdapter()
            listNotification.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = notificationAdapter
            }
        }
        viewModel.getAllNotification()
    }

    override fun initListener() {
    }

    override fun observerLiveData() {
        with(viewModel) {
            getAllNotificationResponse.observe(this@NotificationFragment) {
                val data = it.data?.content
                if (data != null) {
                    notificationAdapter.submitList(data.map { itemData ->
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
                        )
                    })
                }
            }
        }
    }

    companion object {
        fun newInstance(): NotificationFragment {
            return NotificationFragment()
        }
    }
}