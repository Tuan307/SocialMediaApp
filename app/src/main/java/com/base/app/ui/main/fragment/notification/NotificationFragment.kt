package com.base.app.ui.main.fragment.notification

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.base.fragment.BaseFragment
import com.base.app.data.models.NotificationItem
import com.base.app.databinding.FragmentNotificationBinding
import com.base.app.ui.main.fragment.notification.adapter.NotificationAdapter


class NotificationFragment : BaseFragment<FragmentNotificationBinding>(),
    NotificationAdapter.ICallBack {

    private val viewModel by viewModels<NotificationViewModel>()
    private lateinit var notificationAdapter: NotificationAdapter
    private var list = ArrayList<NotificationItem>()

    companion object {
        fun newInstance(): NotificationFragment {
            return NotificationFragment()
        }
    }

    override fun getContentLayout(): Int {
        return R.layout.fragment_notification
    }

    override fun initView() {
        //registerObserverLoadingEvent(viewModel,this@NotificationFragment)
        binding.apply {
            rcvNotification.layoutManager = LinearLayoutManager(requireContext())
            rcvNotification.setHasFixedSize(true)
            notificationAdapter =
                NotificationAdapter(requireContext(), list, viewModel, this@NotificationFragment)
            rcvNotification.adapter = notificationAdapter
        }
        viewModel.readNotification()
    }

    override fun initListener() {
    }

    override fun observerLiveData() {
        viewModel.apply {
            listNotificationResponse.observe(this@NotificationFragment) {
                list.clear()
                list.addAll(it)
                notificationAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onItemClick(data: NotificationItem) {
        //DO SOMETHING LATER
    }

}