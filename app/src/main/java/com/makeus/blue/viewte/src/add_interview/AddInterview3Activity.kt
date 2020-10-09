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
    private lateinit var mTimePicker: TouchInterceptorTimePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_interview3)

        mIvBack = findViewById(R.id.add_interview3_iv_back)
        mBtnNext = findViewById(R.id.add_interview3_btn_next)
        mTimePicker = findViewById(R.id.add_interview3_time_picker)

        mIvBack.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                finish()
            }
        })
        mBtnNext.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview3Activity, AddInterview4Activity::class.java)

                var hour = mTimePicker.hour.toString()
                var minute = mTimePicker.minute.toString()
                if (hour.length == 1) {
                    hour = "0$hour"
                }
                if (minute.length == 1) {
                    minute = "0$minute"
                }
                var timeString = "$hour:$minute"

                intent.putExtra("categoriesNo", getIntent().getIntExtra("categoriesNo", 0))
                intent.putExtra("i_title", getIntent().getStringExtra("i_title"))
                intent.putExtra("purpose", getIntent().getStringExtra("purpose"))
                intent.putExtra("date", getIntent().getStringExtra("date"))
                intent.putExtra("time", timeString)

                startActivity(intent)
            }
        })
    }
}
