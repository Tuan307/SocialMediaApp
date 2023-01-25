package com.base.app.base.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.base.app.R
import com.base.app.base.dialogs.ProgressDialog
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.common.CommonUtils
import com.base.app.common.EventObserver
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.ImagePickerMode
import com.esafirm.imagepicker.features.ImagePickerSavePath
import com.esafirm.imagepicker.features.toFiles
import com.esafirm.imagepicker.model.Image

abstract class BaseFragment<BINDING : ViewDataBinding> :
    Fragment() {

    lateinit var binding: BINDING
    lateinit var loadingDialog: ProgressDialog
    val imagesPicker = arrayListOf<Image>()
    private var mLastClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = ProgressDialog.newInstance()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, getContentLayout(), container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        observerLiveData()
    }

    abstract fun getContentLayout(): Int

    abstract fun initView()

    abstract fun initListener()

    abstract fun observerLiveData()

    protected fun paddingStatusBar(view: View) {
        view.setPadding(0, CommonUtils.getStatusBarHeight(context!!), 0, 0)
    }

    open fun isDoubleClick(DOUBLE_PRESS_INTERVAL: Long): Boolean {
        if (SystemClock.elapsedRealtime() - mLastClickTime < DOUBLE_PRESS_INTERVAL) {
            return true
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        return false
    }

    protected fun navigateToPage(actionId: Int) {
        findNavController().navigate(actionId)
    }

    protected fun registerObserverLoadingMoreEvent(
        viewModel: BaseViewModel,
        viewLifecycleOwner: LifecycleOwner
    ) {
        viewModel.isLoadingMore.observe(viewLifecycleOwner, EventObserver { isShow ->
            showLoadingMore(isShow)
        })
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    protected fun showLoadingMore(isShow: Boolean) {

    }

    private fun getDefaultNotifyTitle(): String {
        return getString(R.string.default_notify_title)
    }
    fun createConfig(isSingleMode: Boolean, limitImage: Int): ImagePickerConfig {
        return ImagePickerConfig {

            mode = if (isSingleMode) {
                ImagePickerMode.SINGLE
            } else {
                ImagePickerMode.MULTIPLE // multi mode (default mode)
            }

            arrowColor = Color.WHITE // set toolbar arrow up color
            folderTitle = "Folder" // folder selection title
            imageTitle = getString(R.string.app_name) // image selection title
            doneButtonText = getString(R.string.str_done) // done button text
            showDoneButtonAlways = true // Show done button always or not
            limit = limitImage // max images can be selected (99 by default)
            isShowCamera = true // show camera or not (true by default)
            savePath =
                ImagePickerSavePath("Camera") // captured image directory name ("Camera" folder by default)
            savePath = ImagePickerSavePath(
                Environment.getExternalStorageDirectory().path,
                isRelative = false
            ) // can be a full path
            excludedImages =
                imagesPicker.toFiles() // don't show anything on this selected images
        }
    }
}