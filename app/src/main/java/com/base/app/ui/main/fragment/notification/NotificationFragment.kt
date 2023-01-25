package com.base.app.ui.main.fragment.notification

import com.base.app.R
import com.base.app.base.fragment.BaseFragment
import com.base.app.databinding.FragmentNotificationBinding


class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {
    companion object {
        fun newInstance(): NotificationFragment {
            return NotificationFragment()
        }
    }

    override fun getContentLayout(): Int {
        return R.layout.fragment_notification
    }

    override fun initView() {
    }

    override fun initListener() {
    }

    override fun observerLiveData() {
    }

}