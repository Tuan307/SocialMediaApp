package com.base.app.ui.custom_view_study

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class CutomsViewActivity : AppCompatActivity() {
    private lateinit var canvaCustomViewStudy: CustomViewStudy
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        canvaCustomViewStudy = CustomViewStudy(this)
        setContentView(canvaCustomViewStudy)
    }
}