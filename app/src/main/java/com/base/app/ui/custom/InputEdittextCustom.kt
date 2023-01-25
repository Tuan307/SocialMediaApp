package com.base.app.ui.custom

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.base.app.R
import com.base.app.common.CommonUtils
import com.base.app.common.EMPTY_STRING
import com.base.app.databinding.LayoutEdittextCustomBinding
import com.google.android.material.textfield.TextInputLayout


class InputEdittextCustom @JvmOverloads constructor(
    context: Context, attrs: AttributeSet, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private lateinit var textChangeCallBack: TextChangeCallBack
    private var isVisibilityPass = true
    private var isMultiLine = false
    private val mBinding =
        LayoutEdittextCustomBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        init(context, attrs)
    }

    fun setInfoEdt(resource: Int) {
        val lp = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        if (resource > 0) {
            mBinding.imgStart.setImageResource(resource)
            lp.setMargins(CommonUtils.pxFromDp(context, 20f), 0, 0, 0)
            mBinding.layoutName.layoutParams = lp
        } else {
            lp.setMargins(0, 0, 0, 0)
            mBinding.layoutName.layoutParams = lp
        }
    }

    fun initInterface(textChangeCallBack: TextChangeCallBack) {
        CommonUtils.formatMoney(mBinding.edtName)
        this.textChangeCallBack = textChangeCallBack
        mBinding.edtName.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                Handler(Looper.getMainLooper()).postDelayed({
                    if (s?.length ?: 0 > 0) {
                        if (s.toString() == context.getString(R.string.str_default_price)) {
                            mBinding.edtName.setText(EMPTY_STRING)
                        } else {
                            // Date: 19/09/2022
                            // Problem : Avoid user input wrong number and make exception
                            try {
                                textChangeCallBack.getTaxCallBack(
                                    mBinding.edtName.text.toString().replace(".", "").toLong()
                                )
                            } catch (e: NumberFormatException) {
                                e.printStackTrace()
                            }
                        }
                    } else {
                        textChangeCallBack.getTaxCallBack(0)
                    }
                }, 100)
            }
        })
    }

    fun getText(): Editable? {
        return mBinding.edtName.text
    }

    fun setText(text: String) {
        mBinding.edtName.setText(text)
        mBinding.edtName.requestFocus()
        mBinding.edtName.clearFocus()
    }

    fun setUnEnable() {
        mBinding.edtName.isEnabled = false
    }

    fun setEnable() {
        mBinding.edtName.isEnabled = true
    }

    fun setPadding() {
        mBinding.layoutName.setPadding(
            CommonUtils.pxFromDp(context, 20f),
            CommonUtils.pxFromDp(context, 4f),
            CommonUtils.pxFromDp(context, 20f),
            0
        )
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.InputEdittextCustom, 0, 0)

        if (typedArray.getBoolean(R.styleable.InputEdittextCustom_actionDone, false)) {
            mBinding.edtName.imeOptions = EditorInfo.IME_ACTION_DONE
        }
        if (typedArray.getInteger(R.styleable.InputEdittextCustom_maxLength, 0) > 0) {
            val fArray = arrayOfNulls<InputFilter>(1)
            fArray[0] =
                LengthFilter(typedArray.getInteger(R.styleable.InputEdittextCustom_maxLength, 0))
            mBinding.edtName.filters = fArray
        }
        if (typedArray.getBoolean(R.styleable.InputEdittextCustom_inputPhone, false)) {
            mBinding.edtName.inputType = InputType.TYPE_CLASS_PHONE
        }
        if (typedArray.getBoolean(R.styleable.InputEdittextCustom_inputNumber, false)) {
            mBinding.edtName.inputType = InputType.TYPE_CLASS_NUMBER
        }
        if (typedArray.getBoolean(R.styleable.InputEdittextCustom_multiLine, false)) {
            isMultiLine = typedArray.getBoolean(R.styleable.InputEdittextCustom_multiLine, false)
            mBinding.edtName.minHeight = CommonUtils.pxFromDp(mBinding.root.context, 60f)
            mBinding.edtName.isSingleLine = false
        }
        if (typedArray.getBoolean(R.styleable.InputEdittextCustom_inputPass, false)) {
            mBinding.imgPass.visibility = View.VISIBLE
            mBinding.edtName.transformationMethod = PasswordTransformationMethod.getInstance()
            mBinding.imgPass.setImageResource(R.drawable.ic_gone_pass)
        }
        setPaddingLayoutEdittext(
            mBinding.layoutRoot,
            mBinding.imgStart,
            mBinding.layoutName,
            mBinding.edtName,
            typedArray.getString(R.styleable.InputEdittextCustom_titleEditText) ?: EMPTY_STRING,
            typedArray.getString(R.styleable.InputEdittextCustom_hintEditText) ?: EMPTY_STRING,
            context
        )
        mBinding.imgPass.setOnClickListener {

            if (isVisibilityPass) {
                mBinding.edtName.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                mBinding.imgPass.setImageResource(R.drawable.ic_visibility_pass)
            } else {
                mBinding.edtName.transformationMethod = PasswordTransformationMethod.getInstance()
                mBinding.imgPass.setImageResource(R.drawable.ic_gone_pass)
            }
            isVisibilityPass = !isVisibilityPass
        }
        mBinding.edtName.requestFocus()
        mBinding.edtName.clearFocus()

    }

    private fun setPaddingLayoutEdittext(
        layout: ConstraintLayout,
        imageStart: ImageView,
        layoutEdittext: TextInputLayout,
        edittext: EditText,
        title: String,
        hint: String,
        context: Context
    ) {
        layoutEdittext.setPadding(
            CommonUtils.pxFromDp(context, 20f),
            0,
            CommonUtils.pxFromDp(context, 20f),
            CommonUtils.pxFromDp(context, 6f)
        )
        layoutEdittext.hint =
            HtmlCompat.fromHtml(
                hint,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

        edittext.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                imageStart.setColorFilter(ContextCompat.getColor(context, R.color.black))
                layout.setBackgroundResource(if (isMultiLine) R.drawable.bg_edittext_focus_12dp else R.drawable.bg_edittext_focus)
                layoutEdittext.setPadding(
                    CommonUtils.pxFromDp(context, 20f),
                    CommonUtils.pxFromDp(context, 4f),
                    CommonUtils.pxFromDp(context, 20f),
                    0
                )
                layoutEdittext.hint = HtmlCompat.fromHtml(
                    title,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            } else {
                imageStart.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.color_icon_unfocus
                    )
                )
                layout.setBackgroundResource(if (isMultiLine) R.drawable.bg_edittext_unfocus_12dp else R.drawable.bg_edittext_unfocus)
                if (edittext.text?.length ?: 0 > 0) {
                    layoutEdittext.setPadding(
                        CommonUtils.pxFromDp(context, 20f),
                        CommonUtils.pxFromDp(context, 4f),
                        CommonUtils.pxFromDp(context, 20f), CommonUtils.pxFromDp(context, 4f)
                    )
                    layoutEdittext.hint = HtmlCompat.fromHtml(
                        title,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                } else {
                    layoutEdittext.setPadding(
                        CommonUtils.pxFromDp(context, 20f),
                        0,
                        CommonUtils.pxFromDp(context, 20f),
                        CommonUtils.pxFromDp(context, 6f)
                    )
                    layoutEdittext.hint =
                        HtmlCompat.fromHtml(
                            hint,
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                }
            }
        }
    }


    interface TextChangeCallBack {
        fun getTaxCallBack(tax: Long)
    }
}

