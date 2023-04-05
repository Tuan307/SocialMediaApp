package com.base.app.ui.study

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.databinding.ActivityMain3Binding


class MainActivity2 : BaseActivity<ActivityMain3Binding>(), StudyAdapter.clickDelete {

    private lateinit var adapter: StudyAdapter
    val list = ArrayList<Person>()
    override fun getContentLayout(): Int {
        return R.layout.activity_main3
    }

    override fun initView() {

        val intent = Intent()
        intent.putExtra("result", "yes")
        setResult(Activity.RESULT_OK, intent)

        list.add(Person("Kotlin", "Kotlin"))

        adapter = StudyAdapter(this@MainActivity2, list, this@MainActivity2)
        binding.rcvStudy.layoutManager = LinearLayoutManager(this@MainActivity2)
        binding.rcvStudy.setHasFixedSize(true)
        binding.rcvStudy.adapter = adapter
    }

    override fun initListener() {

    }

    override fun observerLiveData() {
    }

    override fun deleteItem(item: Int) {

    }

    override fun clickItem(item: Int) {

    }
}