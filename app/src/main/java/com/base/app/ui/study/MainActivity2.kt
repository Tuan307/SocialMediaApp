package com.base.app.ui.study

import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.base.activities.BaseActivity
import com.base.app.databinding.ActivityMain3Binding


class MainActivity2 : BaseActivity<ActivityMain3Binding>() {

    private lateinit var adapter: StudyAdapter
    val list = ArrayList<String>()
    override fun getContentLayout(): Int {
        return R.layout.activity_main3
    }

    override fun initView() {
        list.add("Kotlin")
        list.add("Java")
        list.add("C++")
        list.add("Python")
        list.add("Dart")
        list.add("Swift")
        list.add("Kotlin")
        list.add("Java")
        list.add("C++")
        list.add("Python")
        list.add("Dart")
        list.add("Swift")
        adapter = StudyAdapter(list)
        binding.rcvStudy.layoutManager = LinearLayoutManager(this@MainActivity2)
        binding.rcvStudy.setHasFixedSize(true)
        binding.rcvStudy.adapter = adapter
    }

    override fun initListener() {

    }

    override fun observerLiveData() {
    }
}