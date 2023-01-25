package com.base.app.ui.custom

import android.text.method.PasswordTransformationMethod
import android.view.View
import com.base.app.common.EMPTY_STRING


class AsteriskTransformMethod : PasswordTransformationMethod() {
    override fun getTransformation(source: CharSequence?, view: View?): CharSequence {
        return PasswordCharSequence(source ?: EMPTY_STRING, source?.length ?: 0)
    }

    private inner class PasswordCharSequence(val mSource: CharSequence, override val length: Int) :
        CharSequence {
        override fun get(index: Int): Char {
            return '*'
        }

        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
            return mSource.subSequence(startIndex, endIndex) // Return default
        }
    }
}