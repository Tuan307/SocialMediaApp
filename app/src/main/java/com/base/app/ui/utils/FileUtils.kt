package com.base.app.ui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity

/**
 * @author tuanpham
 * @since 4/24/2024
 */
object FileUtils {

    fun ActivityResultLauncher<Intent>.openMultipleFile(title: String, type: String = "*/*") {
        Intent().also {
            it.setType(type).action = Intent.ACTION_OPEN_DOCUMENT
            it.addCategory(Intent.CATEGORY_OPENABLE)
            it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }.let {
            launch(Intent.createChooser(it, title))
        }
    }

    fun ActivityResultLauncher<Intent>.openImageFile(title: String, type: String = "image/*") {
        Intent().also {
            it.setType(type).action = Intent.ACTION_GET_CONTENT
            it.addCategory(Intent.CATEGORY_OPENABLE)
        }.let {
            launch(Intent.createChooser(it, title))
        }
    }

    fun FragmentActivity.getFileExtension(uri: Uri): String? {
        val contentResolver = this.contentResolver
        val map = MimeTypeMap.getSingleton()
        return map.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    fun Context.grantUriPermission(uri: Uri) {
        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        contentResolver.takePersistableUriPermission(uri, takeFlags)
    }
}