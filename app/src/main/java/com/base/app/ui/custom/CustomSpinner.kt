package com.base.app.ui.custom

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import java.lang.reflect.InvocationHandler
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Proxy


@SuppressLint("AppCompatCustomView")
class CustomSpinner : Spinner {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    override fun setAdapter(orig: SpinnerAdapter) {
        val adapter = newProxy(orig)
        super.setAdapter(adapter)
        try {
            val m: Method = AdapterView::class.java.getDeclaredMethod(
                "setNextSelectedPositionInt", Int::class.javaPrimitiveType
            )
            m.setAccessible(true)
            m.invoke(this, -1)
            val n: Method = AdapterView::class.java.getDeclaredMethod(
                "setSelectedPositionInt", Int::class.javaPrimitiveType
            )
            n.setAccessible(true)
            n.invoke(this, -1)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    protected fun newProxy(obj: SpinnerAdapter): SpinnerAdapter {
        return Proxy.newProxyInstance(
            obj.javaClass.classLoader, arrayOf<Class<*>>(SpinnerAdapter::class.java),
            SpinnerAdapterProxy(obj)
        ) as SpinnerAdapter
    }

    /**
     * Intercepts getView() to display the prompt if position < 0
     */
    protected inner class SpinnerAdapterProxy(protected var obj: SpinnerAdapter) :
        InvocationHandler {
        protected var getView: Method? = null

        @Throws(Throwable::class)
        override operator fun invoke(proxy: Any?, m: Method, args: Array<Any>): Any {
            return try {
                if (m.equals(getView) && (args[0] as Int) < 0) getView(
                    args[0] as Int,
                    args[1] as View,
                    args[2] as ViewGroup
                ) else m.invoke(
                    obj, args
                )
            } catch (e: InvocationTargetException) {
                throw e.getTargetException()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }

        @Throws(IllegalAccessException::class)
        protected fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            if (position < 0) {
                val v = (context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE
                ) as LayoutInflater).inflate(
                    R.layout.simple_spinner_item, parent, false
                ) as TextView
                v.text = prompt
                return v
            }
            return obj.getView(position, convertView, parent)
        }

        init {
            try {
                getView = SpinnerAdapter::class.java.getMethod(
                    "getView",
                    Int::class.javaPrimitiveType,
                    View::class.java,
                    ViewGroup::class.java
                )
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }
}