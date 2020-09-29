package com.makeus.blue.viewte.src.join

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import kotlinx.android.synthetic.main.activity_join.*

class JoinActivity : BaseActivity() {

    private lateinit var mIvNext: ImageView
    private lateinit var mEtName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        this.mIvNext = findViewById(R.id.join_iv_next)
        this.mEtName = findViewById(R.id.join_et_name)

        mIvNext.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                if (mEtName.text.toString() == "") {
                    showCustomToast("이름을 입력해주세요")
                }
                else {
                    if (mEtName.text.toString().length > 4) {
                        showCustomToast("최대 네글자까지 가능합니다")
                    }
                    else {
                        var intent = Intent(this@JoinActivity, Join2Activity::class.java)
                        intent.putExtra("oauthid", getIntent().getStringExtra("oauthid"))
                        intent.putExtra("name", mEtName.text.toString())
                        println("join1에서 oauth = " + getIntent().getStringExtra("oauthid"))
                        startActivity(intent)
                    }
                }
            }
        })
    }
}
