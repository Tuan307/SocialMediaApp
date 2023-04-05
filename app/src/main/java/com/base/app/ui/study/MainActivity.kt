package com.base.app.ui.study


import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
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
        registerForContextMenu(binding.btnContextMenu)
    }

    override fun initListener() {
        val intent = Intent(this@MainActivity, MainActivity2::class.java)
        startActivityForResult(intent, 1000)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Toast.makeText(
                    this@MainActivity,
                    data.getStringExtra("result").toString(),
                    Toast.LENGTH_SHORT
                ).show()
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

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.post_item, menu)

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.edit) {
            Toast.makeText(this@MainActivity, "HEHEHE", Toast.LENGTH_SHORT).show()
        }
        return super.onContextItemSelected(item)
    }
}