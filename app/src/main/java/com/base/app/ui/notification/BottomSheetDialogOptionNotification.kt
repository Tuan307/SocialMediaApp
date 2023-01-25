//package com.base.app.ui.notification
//
//import android.os.Bundle
//import android.view.View
//import com.base.app.R
//import com.base.app.base.dialogs.BaseBottomSheetDialog
//import com.base.app.data.models.constants.StatusNotificationType
//import com.base.app.databinding.LayoutOptionsNotificationBinding
//import com.base.app.common.EMPTY_STRING
//
//class BottomSheetDialogOptionNotification :
//    BaseBottomSheetDialog<LayoutOptionsNotificationBinding>() {
//    private lateinit var dialogOptionNotificationCallBack: DialogOptionNotificationCallBack
//    private var id: String? = EMPTY_STRING
//    private var isRead: String? = EMPTY_STRING
//
//    companion object {
//        fun newInstance(
//            id: String?,
//            isRead: String?,
//            dialogOptionNotificationCallBack: DialogOptionNotificationCallBack
//        ): BottomSheetDialogOptionNotification {
//            val fragment = BottomSheetDialogOptionNotification()
//            fragment.id = id
//            fragment.isRead = isRead
//            fragment.dialogOptionNotificationCallBack = dialogOptionNotificationCallBack
//            return fragment
//        }
//    }
//
//    override fun getLayoutResource(): Int {
//        return R.layout.layout_options_notification
//    }
//
//    override fun initView(saveInstanceState: Bundle?, view: View?) {
//        if (isRead == StatusNotificationType.READ.value) {
//            binding.tvRead.visibility = View.GONE
//            binding.view.visibility = View.GONE
//        }
//
//    }
//
//    override fun initListener(view: View?) {
//        binding.tvCancel.setOnClickListener {
//            dismiss()
//        }
//        binding.tvDelete.setOnClickListener {
//            dialogOptionNotificationCallBack.deleteNotification(id)
//        }
//        binding.tvRead.setOnClickListener {
//            dialogOptionNotificationCallBack.readNotification(id)
//        }
//    }
//
//    interface DialogOptionNotificationCallBack {
//        fun readNotification(id: String?)
//        fun deleteNotification(id: String?)
//    }
//}