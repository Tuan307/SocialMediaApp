//package com.base.app.ui.notification
//
//import android.view.View
//import androidx.core.content.ContextCompat
//import androidx.databinding.ViewDataBinding
//import androidx.recyclerview.widget.RecyclerView
//import com.base.app.R
//import com.base.app.base.holder.BaseRecyclerAdapter
//import com.base.app.common.DATE_TIME_FORMAT2
//import com.base.app.common.EMPTY_STRING
//import com.base.app.common.extension.convertToTimeAgo
//import com.base.app.data.models.constants.StatusNotificationType
//import com.base.app.data.models.response.notification.Data
//import com.base.app.databinding.ItemNotificationBinding
//
//class NotificationAdapter(
//    dataSet: MutableList<Data?>?,
//    var iNotificationCallback: INotificationCallback
//) :
//    BaseRecyclerAdapter<Data, NotificationAdapter.ViewHolder>(dataSet) {
//
//    override fun getLayoutResourceItem(): Int {
//        return R.layout.item_notification
//    }
//
//    override fun onCreateBasicViewHolder(binding: ViewDataBinding?): ViewHolder {
//        return ViewHolder(binding as ItemNotificationBinding)
//    }
//
//    override fun onBindBasicItemView(holder: ViewHolder, position: Int) {
//        getDataSet()?.get(position)?.let { holder.fillData(position) }
//    }
//
//    inner class ViewHolder(val binding: ItemNotificationBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        private val mContext = binding.root.context
//        fun fillData(position: Int) {
//            val data = getDataSet()?.get(position)
//            binding.imgOptions.setOnClickListener {
//                iNotificationCallback.itemOptionsClick(data?.id, data?.statusNotification, position)
//            }
//            binding.tvTitle.text = data?.title?.trim()
//            binding.tvDes.text = data?.content?.trim()
//            binding.tvTime.text =
//                convertToTimeAgo(
//                    data?.createdAt?.trim() ?: EMPTY_STRING,
//                    DATE_TIME_FORMAT2
//                )
//            if (convertToTimeAgo(
//                    data?.createdAt?.trim() ?: EMPTY_STRING,
//                    DATE_TIME_FORMAT2
//                ) == mContext.getString(R.string.str_just_now)
//            ) {
//                binding.tvTime.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
//            } else {
//                binding.tvTime.setTextColor(
//                    ContextCompat.getColor(
//                        mContext,
//                        R.color.color_body_text
//                    )
//                )
//            }
//            if (data?.statusNotification == StatusNotificationType.NEW.value) {
//                binding.imgUnread.visibility = View.VISIBLE
//                binding.tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.color_text))
//                binding.tvDes.setTextColor(ContextCompat.getColor(mContext, R.color.color_text))
//            } else {
//                binding.imgUnread.visibility = View.GONE
//                binding.tvTitle.setTextColor(
//                    ContextCompat.getColor(
//                        mContext,
//                        R.color.color_body_text
//                    )
//                )
//                binding.tvDes.setTextColor(
//                    ContextCompat.getColor(
//                        mContext,
//                        R.color.color_body_text
//                    )
//                )
//            }
//        }
//    }
//
//    interface INotificationCallback {
//        fun itemOptionsClick(id: String?, isRead: String?, position: Int)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//    }
//}