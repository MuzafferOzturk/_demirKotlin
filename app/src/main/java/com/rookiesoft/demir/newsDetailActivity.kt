package com.rookiesoft.demir

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.rookiesoft.demir.R
import kotlinx.android.synthetic.main.activity_news_detail.*

class newsDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        val intent = intent
        val baslik = intent.getStringExtra("baslik")
        val icerik = intent.getStringExtra("icerik")
        tvDetayBaslik.text = baslik
        tvDetayIcerik.text = icerik
    }
}
