package com.makeus.blue.viewte.src.add_interview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TimePicker
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity

class AddInterview3Activity : BaseActivity() {

    private lateinit var mIvBack: ImageView
    private lateinit var mBtnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_interview3)

        mIvBack = findViewById(R.id.add_interview3_iv_back)
        mBtnNext = findViewById(R.id.add_interview3_btn_next)

        mIvBack.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                finish()
            }
        })
        mBtnNext.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview3Activity, AddInterview4Activity::class.java)
                startActivity(intent)
            }
        })
    }
}
