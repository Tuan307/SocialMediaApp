package com.base.app.common.extension


import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import java.io.*
import java.text.DecimalFormat


const val TAG = "FileUtils"
private const val DEBUG = false // Set to true to enable logging


const val MIME_TYPE_IMAGE = "image/*"
const val AUTHORITY = "vn.touchspace.news.utils.extension.documents"


const val HIDDEN_PREFIX = "."

/**
 * Gets the extension of a file name, like ".png" or ".jpg".
 *
 * @param uri
 * @return Extension including the dot("."); "" if there is no extension;
 * null if uri was null.
 */
fun getExtension(uri: String?): String? {
    if (uri == null) {
        return null
    }
    val dot = uri.lastIndexOf(".")
    return if (dot >= 0) {
        uri.substring(dot)
    } else {
        // No extension.
        ""
    }
}

/**
 * @return Whether the URI is a local one.
 */
fun isLocal(url: String?): Boolean {
    return url != null && !url.startsWith("http://") && !url.startsWith("https://")
}

/**
 * @return True if Uri is a MediaStore Uri.
 * @author paulburke
 */
fun isMediaUri(uri: Uri?): Boolean {
    return "media".equals(uri?.authority, ignoreCase = true)
}

/**
 * Convert File into Uri.
 *
 * @param file
 * @return uri
 */
fun getUri(file: File?): Uri? {
    return if (file != null) {
        Uri.fromFile(file)
    } else null
}

/**
 * Returns the path only (without file name).
 *
 * @param file
 * @return
 */
fun getPathWithoutFilename(file: File?): File? {
    return if (file != null) {
        if (file.isDirectory) {
            // no file to be split off. Return everything
            file
        } else {
            val filename: String = file.name
            val filepath: String = file.absolutePath

            // Construct path without file name.
            var pathwithoutname = filepath.substring(0,
                    filepath.length - filename.length)
            if (pathwithoutname.endsWith("/")) {
                pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length - 1)
            }
            File(pathwithoutname)
        }
    } else null
}

/**
 * @return The MIME type for the given file.
 */
fun getMimeType(file: File): String? {
    val extension = getExtension(file.name)
    return if (extension!!.isNotEmpty()) MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1)) else "application/octet-stream"
}

/**
 * @return The MIME type for the give Uri.
 */

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is [LocalStorageProvider].
 * @author paulburke
 */
fun isLocalStorageDocument(uri: Uri): Boolean {
    return AUTHORITY == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is ExternalStorageProvider.
 * @author paulburke
 */
fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 * @author paulburke
 */
fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 * @author paulburke
 */
fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 */
fun isGooglePhotosUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.content" == uri.authority
}

/**
 * Get the value of the data column for this Uri. This is useful for
 * MediaStore Uris, and other file-based ContentProviders.
 *
 * @param context The context.
 * @param uri The Uri to query.
 * @param selection (Optional) Filter used in the query.
 * @param selectionArgs (Optional) Selection arguments used in the query.
 * @return The value of the _data column, which is typically a file path.
 * @author paulburke
 */
fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                  selectionArgs: Array<String>?): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(
            column
    )
    try {
        cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs,
                null)
        if (cursor != null && cursor.moveToFirst()) {
            if (DEBUG) DatabaseUtils.dumpCursor(cursor)
            val column_index: Int = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(column_index)
        }
    } finally {
        cursor?.close()
    }
    return null
}

/**
 * Get the file size in a human-readable string.
 *
 * @param size
 * @return
 * @author paulburke
 */
fun getReadableFileSize(size: Int): String? {
    val BYTES_IN_KILOBYTES = 1024
    val dec = DecimalFormat("###.#")
    val KILOBYTES = " KB"
    val MEGABYTES = " MB"
    val GIGABYTES = " GB"
    var fileSize = 0f
    var suffix = KILOBYTES
    if (size > BYTES_IN_KILOBYTES) {
        fileSize = size / BYTES_IN_KILOBYTES.toFloat()
        if (fileSize > BYTES_IN_KILOBYTES) {
            fileSize /= BYTES_IN_KILOBYTES
            if (fileSize > BYTES_IN_KILOBYTES) {
                fileSize /= BYTES_IN_KILOBYTES
                suffix = GIGABYTES
            } else {
                suffix = MEGABYTES
            }
        }
    }
    return (dec.format(fileSize).toString() + suffix)
}


@SuppressLint("Recycle")
fun getFileName(context: Context, uri: Uri): String? {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        } finally {
            cursor!!.close()
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result!!.lastIndexOf('/')
        if (cut != -1) {
            result = result.substring(cut + 1)
        }
    }
    return result
}

fun convertBitmapToFile(context: Context, bitmap: Bitmap, size: Int): File {
    val out: Bitmap
    out = if (bitmap.height > size || bitmap.width > size) {
        val max = bitmap.height.coerceAtLeast(bitmap.width)
        val ratio = size.toFloat() / max.toFloat()
        Bitmap.createScaledBitmap(bitmap,
                (bitmap.width * ratio).toInt(),
                (bitmap.height * ratio).toInt(),
                true)
    } else {
        bitmap
    }
    val file = File(context.cacheDir, "thumb.jpeg")
    val fOut: FileOutputStream
    try {
        fOut = FileOutputStream(file)
        out.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
        fOut.flush()
        fOut.close()
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return file
}




fun getUriFromPath(filePath: String): Uri{
    return if (Build.VERSION.SDK_INT >=  VERSION_CODES.N) {
        Uri.parse(filePath);
    } else{
        Uri.fromFile(File(filePath));
    }
}

fun getBitmapFormPath(path: String): Bitmap {
    val image = File(path)
    val bmOptions = BitmapFactory.Options()
    return BitmapFactory.decodeFile(image.absolutePath, bmOptions)
}