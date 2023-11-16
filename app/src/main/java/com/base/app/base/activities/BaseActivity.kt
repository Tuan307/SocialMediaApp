package com.base.app.base.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import android.view.*
import android.webkit.MimeTypeMap
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.base.app.R
import com.base.app.base.dialogs.ConfirmDialog
import com.base.app.base.dialogs.ErrorDialog
import com.base.app.base.dialogs.NotifyDialog
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.common.CommonUtils
import com.base.app.common.EventObserver
import com.base.app.common.RESULT_CODE_UPDATE_APP
import com.esafirm.imagepicker.features.ImagePickerConfig
import com.esafirm.imagepicker.features.ImagePickerMode
import com.esafirm.imagepicker.features.ImagePickerSavePath
import com.esafirm.imagepicker.features.toFiles
import com.esafirm.imagepicker.model.Image
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.InstallStatus
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import java.lang.reflect.Field
import java.lang.reflect.Method

abstract class BaseActivity<BINDING : ViewDataBinding> :
    AppCompatActivity() {

    lateinit var binding: BINDING
    var loadingDialog: AlertDialog? = null
    private val DOUBLE_PRESS_INTERVAL: Long = 1000

    private var mLastClickTime: Long = 0
    private var appUpdateManager: AppUpdateManager? = null
    val imagesPicker = arrayListOf<Image>()
    private val appUpdatedListener: InstallStateUpdatedListener by lazy {
        object : InstallStateUpdatedListener {
            override fun onStateUpdate(installState: InstallState) {
                when {
                    installState.installStatus() == InstallStatus.DOWNLOADED -> {
                        popupSnackbarForCompleteUpdate()
                    }
                    installState.installStatus() == InstallStatus.INSTALLED -> appUpdateManager?.unregisterListener(
                        this
                    )
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
        fixTimeOut()
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = DataBindingUtil.setContentView(this, getContentLayout())
        setContentView(binding.root)
        loadingDialog = setupProgressDialog()
        initView()
        initListener()
        observerLiveData()
        showCustomUI()
        //transparentStatusBar()
    }

    abstract fun getContentLayout(): Int

    abstract fun initView()

    abstract fun initListener()

    abstract fun observerLiveData()

    open fun isDoubleClick(): Boolean {
        if (SystemClock.elapsedRealtime() - mLastClickTime < DOUBLE_PRESS_INTERVAL) {
            return true
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        return false
    }

    private fun setUpTransparentStatusBar() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        )
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    @SuppressLint("DiscouragedPrivateApi")
    open fun fixTimeOut() {
        try {
            val clazz = Class.forName("java.lang.Daemons\$FinalizerWatchdogDaemon")
            @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val method: Method =
                clazz.superclass.getDeclaredMethod("stop")
            method.isAccessible = true
            val field: Field = clazz.getDeclaredField("INSTANCE")
            field.isAccessible = true
            method.invoke(field.get(null))
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }


    private fun setupProgressDialog(): AlertDialog? {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.CustomDialog)
        builder.setCancelable(false)

        val myLayout = LayoutInflater.from(this)
        val dialogView: View = myLayout.inflate(R.layout.fragment_progress_dialog, null)

        builder.setView(dialogView)

        val dialog: AlertDialog = builder.create()
        val window: Window? = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams
        }
        return dialog
    }

    private fun showError(errorMessage: String) {
        val errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG)
        errorSnackbar.setAction("", null)
        errorSnackbar.show()
    }

    fun setLightIconStatusBar(isLight: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (isLight) {
                window.decorView.windowInsetsController?.setSystemBarsAppearance(
                    0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                window.decorView.windowInsetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }
        } else {
            var flags: Int = window.decorView.systemUiVisibility
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                flags = if (isLight) {
                    flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
            window.decorView.systemUiVisibility = flags
        }
    }

    private fun setColorForStatusBar(color: Int) {
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, color)
    }

    private fun showError(@StringRes id: Int) {
        val errorSnackbar = Snackbar.make(binding.root, id, Snackbar.LENGTH_LONG)
        errorSnackbar.setAction("", null)
        errorSnackbar.show()
    }

    protected fun paddingStatusBar(view: View) {
        view.setPadding(
            view.paddingStart,
            CommonUtils.getStatusBarHeight(this),
            view.paddingEnd,
            view.paddingBottom
        )
    }

    fun transparentStatusBar() {
        window.statusBarColor = 0x000000
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    open fun showLoading(isShow: Boolean) {
        if (isShow) {
            loadingDialog?.show()
        } else {
            loadingDialog?.dismiss()
        }
    }

    open fun showErrorDialog(message: String) {
        val errorDialog = ErrorDialog(this, message)
        errorDialog.show()
        errorDialog.window?.setGravity(Gravity.CENTER)
        errorDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    open fun showNotifyDialog(
        titleResourceId: Int,
        messageResourceId: Int,
        textButtonResourceId: Int = -1
    ) {
        val title = getString(titleResourceId)
        val message = getString(messageResourceId)
        val textButton = if (textButtonResourceId == -1) null else getString(textButtonResourceId)
        showNotifyDialog(message, title, textButton)
    }

    open fun showNotifyDialog(message: String, title: String, textButton: String? = null) {
        val notifyDialog = NotifyDialog(this, title, message, textButton)
        notifyDialog.show()
        notifyDialog.window?.setGravity(Gravity.CENTER)
        notifyDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    open fun showConfirmDialog(
        titleResourceId: Int,
        messageResourceId: Int = -1,
        positiveTitleResourceId: Int,
        negativeTitleResourceId: Int,
        textButtonResourceId: Int = -1,
        callback: ConfirmDialog.ConfirmCallback
    ) {
        val title = getString(titleResourceId)
        val message = if (messageResourceId != -1) getString(messageResourceId) else null
        val negativeButtonTitle = getString(negativeTitleResourceId)
        val positiveButtonTitle = getString(positiveTitleResourceId)
        val textButton = if (textButtonResourceId == -1) null else getString(textButtonResourceId)

        showConfirmDialog(
            title,
            message,
            negativeButtonTitle,
            positiveButtonTitle,
            textButton,
            callback
        )
    }

    open fun showConfirmDialog(
        title: String,
        message: String?,
        positiveButtonTitle: String,
        negativeButtonTitle: String,
        textButton: String?,
        callback: ConfirmDialog.ConfirmCallback
    ) {
        val confirmDialog = ConfirmDialog(
            context = this,
            title = title,
            message = message,
            positiveButtonTitle = positiveButtonTitle,
            negativeButtonTitle = negativeButtonTitle,
            callback = callback
        )
        confirmDialog.show()
        confirmDialog.window?.setGravity(Gravity.CENTER)
        confirmDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    // Displays the snackbar notification and call to action.
    private fun popupSnackbarForCompleteUpdate() {
        Snackbar.make(
            binding.root,
            getString(R.string.str_download_complete),
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction(getString(R.string.str_setup)) {
                appUpdateManager?.completeUpdate()
            }
            setActionTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.primaryColor
                )
            )
            show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_CODE_UPDATE_APP) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(
                    this,
                    getString(R.string.str_update_fail),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
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

    override fun onResume() {
        super.onResume()
        appUpdateManager
            ?.appUpdateInfo
            ?.addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate()
                }
            }
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showCustomUI() {
//        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        val decorView = window.decorView
        decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    protected fun registerObserverLoadingEvent(
        viewModel: BaseViewModel,
        viewLifecycleOwner: LifecycleOwner
    ) {
        Log.d("CheckLoading", viewModel.isLoading.value.toString())
        viewModel.isLoading.observe(viewLifecycleOwner) { isShow ->
            showLoading(isShow)
        }
    }

    protected fun registerObserverErrorEvent(
        viewModel: BaseViewModel,
        viewLifecycleOwner: LifecycleOwner
    ) {
        viewModel.baseNetworkException.observe(viewLifecycleOwner, EventObserver {
            showLoading(false)
            Toast.makeText(this, it.responseMessage.toString(), Toast.LENGTH_SHORT).show()
        })
        viewModel.networkException.observe(viewLifecycleOwner, EventObserver {
            showLoading(false)
            Toast.makeText(this, it.responseMessage.toString(), Toast.LENGTH_SHORT).show()
        })
    }

    fun getFileExtension(uri: Uri): String? {
        val contentResolver = contentResolver
        val map = MimeTypeMap.getSingleton()
        return map.getExtensionFromMimeType(contentResolver.getType(uri))
    }
}