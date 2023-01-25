package com.base.app.common

import android.content.ContextWrapper
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat


fun View.getParentActivity(): AppCompatActivity? {
    var context = this.context
    while (context is ContextWrapper) {
        if (context is AppCompatActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun EditText.setCursorToEnd() {
    this.setSelection(this.text.length)
}

fun Double.formatMoney(): String {
    val moneyFormat = DecimalFormat("###,###,###,###.###")
    return moneyFormat.format(this).toString().replace(",",".") + "đ"
}

fun String.formatMoney(): String {
    val moneyFormat = DecimalFormat("###,###,###,###.###")
    return moneyFormat.format(this.toDouble()).toString().replace(",",".")
}
