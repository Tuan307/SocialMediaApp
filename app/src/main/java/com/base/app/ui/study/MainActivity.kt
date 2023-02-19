package com.base.app.ui.study


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.databinding.ActivityMain2Binding
import java.util.*

class MainActivity : BaseActivity<ActivityMain2Binding>(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    override fun getContentLayout(): Int {
        return R.layout.activity_main2
    }

    override fun initView() {
    }

    override fun initListener() {
        binding.apply {
            btnDate.setOnClickListener {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    this@MainActivity,
                    this@MainActivity,
                    year,
                    month,
                    day
                )
                datePickerDialog.show()
            }
            btnTime.setOnClickListener {
                val c = Calendar.getInstance()
                val hour = c.get(Calendar.HOUR_OF_DAY)
                val minute = c.get(Calendar.MINUTE)

                val timePickerDialog = TimePickerDialog(
                    this@MainActivity, this@MainActivity,
                    hour, minute, true
                )
                timePickerDialog.show()
            }
        }

    }

    override fun observerLiveData() {
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        val a = "$p1/${p2 + 1}/$p3"
        binding.txtTime.text = a
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        val a = "$p1:$p2"
        binding.txtTime.text = a
    }

}