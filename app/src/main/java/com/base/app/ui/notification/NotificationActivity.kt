//package com.base.app.ui.notification
//
//import android.view.View
//import androidx.activity.viewModels
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.base.app.R
//import com.base.app.base.activities.BaseActivity
//import com.base.app.common.CommonUtils
//import com.base.app.common.recycleview_utils.EndlessRecyclerViewScrollListener
//import com.base.app.data.models.constants.StatusNotificationType
//import com.base.app.common.DEFAULT_PAGE
//import com.base.app.common.recycleview_utils.setupLinearLayoutRecyclerView
//import com.base.app.data.models.response.notification.Data
//import com.base.app.databinding.ActivityNotificationBinding
//import com.google.android.material.tabs.TabLayout
//import dagger.hilt.android.AndroidEntryPoint
//
//@AndroidEntryPoint
//class NotificationActivity : BaseActivity<ActivityNotificationBinding>(),
//    NotificationAdapter.INotificationCallback,
//    BottomSheetDialogOptionNotification.DialogOptionNotificationCallBack {
//
//    private val viewModel by viewModels<NotificationViewModel>()
//    private lateinit var notificationAdapter: NotificationAdapter
//    private var listNotification: MutableList<Data?>? = ArrayList()
//    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
//
//    private var all = true
//    private var notSeen = false
//    private var transaction = false
//    private val typeTransaction = "transaction"
//    private val tabAll = 0
//    private val tabNotSeen = 1
//    private val tabTransaction = 2
//    private var positionItemClick = 0
//    private lateinit var bottomSheetDialogOptionNotification: BottomSheetDialogOptionNotification
//    override fun getContentLayout(): Int = R.layout.activity_notification
//
//    override fun initView() {
//        CommonUtils.showCustomUI(this)
//        paddingStatusBar(binding.layoutToolbar.layoutToolbar)
//        binding.layoutToolbar.imgBack.setOnClickListener {
//            finish()
//        }
//        //binding.layoutToolbar.tvNumber.visibility = View.VISIBLE
//        binding.layoutToolbar.tvTitle.text = getString(R.string.str_all_notification)
//
//    }
//
//    override fun initListener() {
//        viewModel.getNumberUnreadNotification()
//        viewModel.getListNotification(DEFAULT_PAGE, null, null)
//        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                endlessRecyclerViewScrollListener.resetState()
//                when (tab.position) {
//                    tabAll -> {
//                        all = true
//                        notSeen = false
//                        transaction = false
//                        viewModel.getListNotification(DEFAULT_PAGE, null, null)
//                    }
//                    tabNotSeen -> {
//                        all = false
//                        notSeen = true
//                        transaction = false
//                        viewModel.getListNotification(
//                            DEFAULT_PAGE,
//                            StatusNotificationType.NEW.value,
//                            null
//                        )
//                    }
//                    tabTransaction -> {
//                        all = false
//                        notSeen = false
//                        transaction = true
//                        viewModel.getListNotification(DEFAULT_PAGE, null, typeTransaction)
//                    }
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {}
//            override fun onTabReselected(tab: TabLayout.Tab) {}
//        })
//    }
//
//    override fun observerLiveData() {
//        viewModel.apply {
//            getNotificationResponse.observe(this@NotificationActivity) {
//                listNotification?.clear()
//                setupLinearLayoutRecyclerView(this@NotificationActivity, binding.rcvNotification)
//                listNotification?.addAll(it?.data?.toMutableList() ?: ArrayList())
//                notificationAdapter =
//                    NotificationAdapter(listNotification, this@NotificationActivity)
//                binding.rcvNotification.adapter = notificationAdapter
//
//                endlessRecyclerViewScrollListener = object :
//                    EndlessRecyclerViewScrollListener(binding.rcvNotification.layoutManager as LinearLayoutManager) {
//                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
//                        when {
//                            all -> {
//                                viewModel.getListNotification(page, null, null)
//                            }
//                            notSeen -> {
//                                viewModel.getListNotification(
//                                    page,
//                                    StatusNotificationType.NEW.value,
//                                    null
//                                )
//                            }
//                            else -> {
//                                viewModel.getListNotification(page, null, typeTransaction)
//                            }
//                        }
//                    }
//                }
//                binding.rcvNotification.addOnScrollListener(endlessRecyclerViewScrollListener)
//            }
//
//            getNotificationLoadMore.observe(this@NotificationActivity) {
//                listNotification?.addAll(it.data?.toMutableList() ?: ArrayList())
//                notificationAdapter.notifyDataSetChanged()
//            }
//            getNumberUnreadNotification.observe(this@NotificationActivity) {
//                if (it.count != null && it.count ?: 0 > 0) {
//                    binding.layoutToolbar.tvNumber.visibility = View.VISIBLE
//                    binding.layoutToolbar.tvNumber.text = it.count.toString()
//                } else {
//                    binding.layoutToolbar.tvNumber.visibility = View.GONE
//                }
//            }
//            getDeleteNotification.observe(this@NotificationActivity) {
//                viewModel.getNumberUnreadNotification()
//                notificationAdapter.removeItem(positionItemClick)
//                notificationAdapter.notifyDataSetChanged()
//            }
//            getReadNotification.observe(this@NotificationActivity) {
//                viewModel.getNumberUnreadNotification()
//                notificationAdapter.getDataSet()?.get(positionItemClick)?.statusNotification =
//                    StatusNotificationType.READ.value
//                notificationAdapter.notifyDataSetChanged()
//            }
//        }
//    }
//
//    override fun itemOptionsClick(id: String?, isRead: String?, position: Int) {
//        if (!isDoubleClick()) {
//            positionItemClick = position
//            bottomSheetDialogOptionNotification =
//                BottomSheetDialogOptionNotification.newInstance(id, isRead, this)
//            bottomSheetDialogOptionNotification.show(supportFragmentManager, null)
//        }
//    }
//
//    override fun readNotification(id: String?) {
//        if (!isDoubleClick()) {
//            bottomSheetDialogOptionNotification.dismiss()
//            viewModel.readNotification(id)
//        }
//    }
//
//    override fun deleteNotification(id: String?) {
//        if (!isDoubleClick()) {
//            bottomSheetDialogOptionNotification.dismiss()
//            viewModel.deleteNotification(id)
//        }
//    }
//
//}