package com.base.app.ui.study

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.app.R

class MainActivity3 : AppCompatActivity(), StudyAdapter.clickDelete {
    lateinit var adapter: StudyAdapter
    lateinit var text1: EditText
    var a = 5
    lateinit var text2: EditText
    var editPosition = -1
    lateinit var btn: Button
    lateinit var btnUpdate: Button
    lateinit var recycler: RecyclerView
    private var list: ArrayList<Person> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        initView()
        getData()
    }

    private fun getData() {
        btn.setOnClickListener {
            val person = Person(text1.text.toString(), text2.text.toString())
            list.add(person)
            adapter.notifyDataSetChanged()
        }


    }

    private fun initView() {
        btnUpdate = findViewById(R.id.btnUpdate)
        btnUpdate.isEnabled = false
        btnUpdate.isClickable = false
        text1 = findViewById(R.id.edtName)
        text2 = findViewById(R.id.edtAddress)
        btn = findViewById(R.id.btnAdd)
        recycler = findViewById(R.id.recyBT)
        recycler.layoutManager = LinearLayoutManager(this@MainActivity3)
        recycler.setHasFixedSize(true)
        adapter = StudyAdapter(this@MainActivity3, list, this@MainActivity3)
        recycler.adapter = adapter
    }

    override fun deleteItem(item: Int) {
        list.removeAt(item)
        adapter.notifyDataSetChanged()
    }

    override fun clickItem(item: Int) {
        editPosition = item
        text1.setText(list[item].name)
        text2.setText(list[item].address)
        btnUpdate.isEnabled = true
        btnUpdate.setOnClickListener {
            if (editPosition != -11) {
                val person = Person(text1.text.toString(), text2.text.toString())
                list[editPosition] = person
                adapter.notifyDataSetChanged()
                btnUpdate.isEnabled = false

            }
        }

    }
}