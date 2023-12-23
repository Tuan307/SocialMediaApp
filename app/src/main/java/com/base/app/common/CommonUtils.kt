package com.base.app.common

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.net.http.SslError
import android.os.*
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.webkit.SslErrorHandler
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.base.app.R
import com.base.app.ui.custom.InputEdittextCustom
import id.zelory.compressor.Compressor
import org.json.JSONObject
import java.io.*
import java.nio.charset.StandardCharsets
import java.security.spec.KeySpec
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object CommonUtils {

     fun Activity.downloadImageByUri(fileName: String, postImage: String) {
        try {
            val downloadManager: DownloadManager =
                this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(postImage)
            val request = DownloadManager.Request(uri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(fileName)
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    File.separator + fileName + ".jpg"
                )
            downloadManager.enqueue(request)
            Toast.makeText(
                this,
                resources.getString(R.string.str_success), Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                this,
                resources.getString(R.string.str_error), Toast.LENGTH_SHORT
            ).show()
        }
    }


    fun Fragment.downloadImageByUri(fileName: String, postImage: String) {
        try {
            val downloadManager: DownloadManager =
                this.requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(postImage)
            val request = DownloadManager.Request(uri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(fileName)
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    File.separator + fileName + ".jpg"
                )
            downloadManager.enqueue(request)
            Toast.makeText(
                this.requireContext(),
                resources.getString(R.string.str_success), Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                this.requireContext(),
                resources.getString(R.string.str_error), Toast.LENGTH_SHORT
            ).show()
        }
    }

    var isFirstLogin = true

    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    @SuppressLint("HardwareIds")
    fun getID(context: Context): String? {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    fun versionName(context: Context): String? {
        try {
            val pInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    private fun getActivityRoot(window: Window): View {
        return (window.decorView.rootView.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(
            0
        )
    }

    fun getDPtoPX(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp,
            context.resources.displayMetrics
        ).toInt()
    }

    private const val KEYBOARD_VISIBLE_THRESHOLD_DP = 100f


    open interface KeyboardVisibilityEventListener {
        fun onVisibilityChanged(isOpen: Boolean)
    }


    fun getHeightScreen(context: Context): Int {
        val displayMetrics = DisplayMetrics()
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val display = context.display
            display?.getRealMetrics(displayMetrics)
        } else {
            (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        }
        return displayMetrics.heightPixels
    }

    fun setEventListener(
        activity: Activity,
        listener: KeyboardVisibilityEventListener?,
        window: Window
    ) {
        if (listener == null) {
            return
        }

        val activityRoot = getActivityRoot(window) ?: return
        activityRoot!!.viewTreeObserver
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {

                private val r = Rect()

                private val visibleThreshold = Math.round(
                    getDPtoPX(
                        activity,
                        KEYBOARD_VISIBLE_THRESHOLD_DP
                    ).toFloat()
                )

                private var wasOpened = false

                override fun onGlobalLayout() {
                    activityRoot.getWindowVisibleDisplayFrame(r)

                    val heightDiff = activityRoot.rootView.height - r.height()

                    val isOpen = heightDiff > visibleThreshold
                    if (isOpen == wasOpened) {
                        return
                    }
                    wasOpened = isOpen
                    listener.onVisibilityChanged(isOpen)
                }
            })
    }

    fun getImageUri(context: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 50, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, inImage, "f5", null)
        return Uri.parse(path)
    }

    fun setImageFormUrl(imageView: ImageView, url: String?) {
        val context = imageView.context
        Glide.with(context)
            .load(url)
            .transition(GenericTransitionOptions.with(R.anim.fade_in))
            .into(imageView)
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun convertMillieToHMmSs(millie: Long): String? {
        val seconds = millie / 1000
        val second = seconds % 60
        val minute = seconds / 60 % 60
        val hour = seconds / (60 * 60) % 24
        val result = ""
        return if (hour > 0) {
            String.format("%02d:%02d:%02d", hour, minute, second)
        } else {
            String.format("%02d:%02d", minute, second)
        }
    }

    fun setStatusBarColor(activity: Activity) {
        val window: Window = activity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(activity, R.color.black)
        }
    }

    fun setStatusColorIcon(view: View) {
        view.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    fun showCustomUI(activity: Activity) {
        activity.window.statusBarColor = 0x00000000
        activity.window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    fun pxFromDp(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp,
            context.resources.displayMetrics
        ).toInt()
    }


    fun setViewGroupCenter(viewGroup: ViewGroup) {

        Handler(Looper.getMainLooper()).postDelayed({
            val layoutParams =
                viewGroup.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.topMargin =
                (getHeightScreen(viewGroup.context) - viewGroup.measuredHeight - getStatusBarHeight(
                    viewGroup.context
                )) / 2
            viewGroup.layoutParams = layoutParams
        }, INIT_VIEW_DELAY)
    }


    fun Activity.hideSoftKeyboard() {
        currentFocus?.let {
            val inputMethodManager = ContextCompat.getSystemService(
                this,
                InputMethodManager::class.java
            )
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun Bitmap?.persistImage(name: String, context: Context): File {
        val filesDir: File = context.filesDir
        val imageFile = File(filesDir, "$name.png")
        val os: OutputStream
        try {
            os = FileOutputStream(imageFile)
            this?.compress(Bitmap.CompressFormat.PNG, 100, os)
            os.flush()
            os.close()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Error writing bitmap", e)
        }
        return imageFile
    }

    fun getRandomString(): String {
        val random = Random()
        val sb = StringBuilder(20)
        for (i in 0 until 20)
            sb.append(ALLOWED_CHARACTERS[random.nextInt(ALLOWED_CHARACTERS.length)])
        return sb.toString() + System.currentTimeMillis()
    }

    private fun rotateImage(source: Bitmap, angle: Float, activity: Activity): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        val bitmap = Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
        val bitmapDrawable = BitmapDrawable(activity.resources, bitmap)
        val width: Int = bitmapDrawable.intrinsicWidth
        val height: Int = bitmapDrawable.intrinsicHeight
        val ratioImage = width.toDouble() / height
        val b = bitmapDrawable.bitmap

        var widthResize = width
        var heightResize = height
        if (ratioImage > 1) { // ảnh ngang
            if (width > 1600) {
                widthResize = 1600
                heightResize = (widthResize / ratioImage).toInt()
            }
        } else { // ảnh dọc
            if (height > 1600) {
                heightResize = 1600
                widthResize = (heightResize * ratioImage).toInt()
            }
        }
        return Bitmap.createScaledBitmap(b, widthResize, heightResize, false)
    }

    fun createRotatedFile(filePath: String, activity: Activity): File {
        val ei = ExifInterface(filePath)
        val orientation: Int = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        val bitmap = BitmapFactory.decodeFile(filePath)

        val rotatedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f, activity)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f, activity)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f, activity)
            else -> rotateImage(bitmap, 0f, activity)
        }
        return rotatedBitmap.persistImage(getRandomString(), activity)
    }

    fun formatAccountNumber(number: String?): String {
        if (number?.length ?: EMPTY_STRING.length > 3) {
            val characters = 3
            val firstThreeDigits = number?.substring(0, 3)
            var rest = number?.substring(3)
            var accountNumber = "$firstThreeDigits"
            rest = correctAccountNumber(rest)
            for (i in rest.indices) {
                if (i % characters == 0) {
                    accountNumber = accountNumber + " " + rest[i]
                } else {
                    accountNumber += rest[i]
                }
            }
            return accountNumber
        } else {
            return number ?: EMPTY_STRING
        }

    }

    private fun correctAccountNumber(number: String?): String {
        return number?.replace("[0-9]".toRegex(), "*") ?: EMPTY_STRING
    }


    val CRYPTO_SALT: String = "bytepay@123"
    val CRYPTO_MASTER_KEY = "a0a3ffedc70409da75f1c749de1e11b3"
    val CRYPTO_IV = "4afd4ff379d763d9"
    fun encrypt(strToEncrypt: String): String {
        try {
            val iv = CRYPTO_IV.toByteArray(StandardCharsets.UTF_8)
            val ivspec = IvParameterSpec(iv)
            val factory: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            val spec: KeySpec =
                PBEKeySpec(CRYPTO_MASTER_KEY.toCharArray(), CRYPTO_SALT.toByteArray(), 2145, 256)
            val tmp: SecretKey = factory.generateSecret(spec)
            val secretKey = SecretKeySpec(tmp.encoded, "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(strToEncrypt.toByteArray(StandardCharsets.UTF_8)))
            } else {
                return android.util.Base64.encodeToString(
                    cipher.doFinal(
                        strToEncrypt.toByteArray(
                            StandardCharsets.UTF_8
                        )
                    ), 0
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return EMPTY_STRING
    }

    fun encrypt2(strToEncrypt: String) {
        try {
            val iv = CRYPTO_IV.toByteArray(StandardCharsets.UTF_8)
            val ivspec = IvParameterSpec(iv)
            val factory: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val spec: KeySpec =
                PBEKeySpec(CRYPTO_MASTER_KEY.toCharArray(), CRYPTO_SALT.toByteArray(), 2145, 128)
            val tmp: SecretKey = factory.generateSecret(spec)
            val secretKey = SecretKeySpec(tmp.encoded, "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d(
                    "0fsdfsd", Base64.getEncoder()
                        .encodeToString(cipher.doFinal(strToEncrypt.toByteArray(StandardCharsets.UTF_8)))
                )
            } else {
                Log.d(
                    "0fsdfsd", "android 7 " +
                            android.util.Base64.encodeToString(
                                cipher.doFinal(
                                    strToEncrypt.toByteArray(StandardCharsets.UTF_8)
                                ), 0
                            )
                )
            }
        } catch (e: Exception) {
            Log.d(
                "0fsdfsd", "Lỗi"
            )
            e.printStackTrace()
        }
    }

    fun isValidIndianMobileNumber(s: String): Boolean {
        val p = Pattern.compile("((09|03|07|08|05)+([0-9]{8})\\b)")
        val m = p.matcher(s)
        return m.find() && m.group() == s
    }

    fun getFirstDayOfWeek(): String? {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val date = calendar.time
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return dateFormat.format(date)
    }

    fun getLastDayOfWeek(): String? {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.add(Calendar.DATE, 6)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val date = calendar.time
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return dateFormat.format(date)
    }

    fun getFirstDayOfMonth(): String? {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val date = calendar.time
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return dateFormat.format(date)
    }

    fun getLastDayOfMonth(): String? {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DATE))
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val date = calendar.time
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return dateFormat.format(date)
    }

    fun getFirstDayOfYear(): String? {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_YEAR, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val date = calendar.time
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        return dateFormat.format(date)
    }

    fun getLastDayOfYear(): String? {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, 11)
        calendar.set(Calendar.DATE, 31)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val date = calendar.time
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        return dateFormat.format(date)
    }

    fun compareTwoDates(date1: String, date2: String): Boolean {
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val d1 = simpleDateFormat.parse(date1)
        val d2 = simpleDateFormat.parse(date2)
        return when {
            d1 > d2 -> {
                false
            }
            else -> {
                true
            }
        }
    }

    fun setupUI(view: View?, activity: Activity?) {
        if (view !is EditText) {
            view?.setOnTouchListener { v, _ ->
                v?.performClick()
                activity?.hideSoftKeyboard()
                false
            }
        }


        if (view is ViewGroup && view !is InputEdittextCustom) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView, activity)
            }
        }
    }

    fun formatMoney(edt: EditText) {
        edt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                edt.removeTextChangedListener(this)
                try {
                    var originalString = s.toString()
                    if (originalString.contains(".")) {
                        originalString = originalString.replace(".", "")
                    }
                    val longValue = originalString.toLong()
                    val formatter =
                        NumberFormat.getInstance(Locale.US) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    val formattedString = formatter.format(longValue)
                    edt.setText(formattedString.replace(",", "."))
                    edt.setSelection(edt.text.length)

                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
                edt.addTextChangedListener(this)
            }
        })
    }

    fun showAlertErrorSSl(context: Context?, handler: SslErrorHandler?, error: SslError?) {
        var message = "SSL Certificate error."
        when (error?.primaryError) {
            SslError.SSL_UNTRUSTED -> message = "The certificate authority is not trusted."
            SslError.SSL_EXPIRED -> message = "The certificate has expired."
            SslError.SSL_IDMISMATCH -> message = "The certificate Hostname mismatch."
            SslError.SSL_NOTYETVALID -> message = "The certificate is not yet valid."
        }
        message += " Do you want to continue anyway?"
        val builder = context?.let {
            AlertDialog.Builder(
                it
            )
        }
        builder?.setTitle("SSL Certificate Error")
        builder?.setMessage(message)
        builder?.setPositiveButton(
            "continue"
        ) { _: DialogInterface?, _: Int -> handler?.proceed() }
        builder?.setNegativeButton(
            "cancel"
        ) { _: DialogInterface?, _: Int -> handler?.cancel() }
        val dialog = builder?.create()
        dialog?.show()
    }

    fun convertToAlphabet(str: String): String {
        var str = str
        str = str.replace(
            "à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ|À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ".toRegex(),
            "a"
        )
        str = str.replace("è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ|È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ".toRegex(), "e")
        str = str.replace("ì|í|ị|ỉ|ĩ|Ì|Í|Ị|Ỉ|Ĩ".toRegex(), "i")
        str = str.replace(
            "ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ|Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ".toRegex(),
            "o"
        )
        str = str.replace("ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ".toRegex(), "u")
        str = str.replace("ỳ|ý|ỵ|ỷ|ỹỲ|Ý|Ỵ|Ỷ|Ỹ".toRegex(), "y")
        str = str.replace("đ|Đ".toRegex(), "d")
        return str
    }

    var bankMap = HashMap<String, String>()
    fun getBankInfoFromJson(activity: Activity): Map<String, String> {
        if (bankMap.isEmpty()) {
            try {
                val jsonFileContent = readFile("list_bank.json", activity)
                val obj = JSONObject(jsonFileContent)
                val jsonArray = obj.getJSONArray("bank")
                for (i in 0 until jsonArray.length()) {
                    val jsonObj = jsonArray.getJSONObject(i)
                    val id = jsonObj.getString("id")
                    val name = jsonObj.getString("bank_name")
                    bankMap[id] = name
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return bankMap

    }

    private fun readFile(fileName: String, activity: Activity): String {
        var content: String = ""
        try {
            val bufferedReader =
                BufferedReader(InputStreamReader(activity.assets.open(fileName), "UTF-8"))
            var line: String = bufferedReader.readLine()
            while (line != null) {
                content += line
                line = bufferedReader.readLine()
            }
        } catch (e: Exception) {
        }
        return content
    }

    private val DOUBLE_PRESS_INTERVAL: Long = 200
    private var mLastClickTime: Long = 0
    open fun isDoubleClick(): Boolean {
        if (SystemClock.elapsedRealtime() - mLastClickTime < DOUBLE_PRESS_INTERVAL) {
            return true
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        return false
    }

    fun compressedImage(context: Context, file: File): File {
        return Compressor(context)
            .setMaxWidth(640)
            .setMaxHeight(480)
            .setQuality(95)
            .setCompressFormat(Bitmap.CompressFormat.WEBP)
            .setDestinationDirectoryPath(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES
                ).absolutePath
            )
            .compressToFile(file)
    }

}

