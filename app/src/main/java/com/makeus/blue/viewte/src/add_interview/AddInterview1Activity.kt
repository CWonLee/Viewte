package com.makeus.blue.viewte.src.add_interview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity

class AddInterview1Activity : BaseActivity() {

    private lateinit var mBtnNext: Button
    private lateinit var mIvBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_interview1)

        mBtnNext = findViewById(R.id.add_interview1_btn_next)
        mIvBack = findViewById(R.id.add_interview1_iv_back)

        mBtnNext.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview1Activity, AddInterview2Activity::class.java)
                startActivity(intent)
            }
        })
        mIvBack.setOnClickListener(object: OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                finish()
            }
        })

    }
}
