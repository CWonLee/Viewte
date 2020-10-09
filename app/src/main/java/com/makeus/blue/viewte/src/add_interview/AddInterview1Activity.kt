package com.makeus.blue.viewte.src.add_interview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity

class AddInterview1Activity : BaseActivity() {

    private lateinit var mBtnNext: Button
    private lateinit var mIvBack: ImageView
    private lateinit var mEtTitle: EditText
    private lateinit var mEtPurpose: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_interview1)

        mBtnNext = findViewById(R.id.add_interview1_btn_next)
        mIvBack = findViewById(R.id.add_interview1_iv_back)
        mEtTitle = findViewById(R.id.add_interview1_et_title)
        mEtPurpose = findViewById(R.id.add_interview1_et_purpose)

        mBtnNext.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                if (mEtTitle.text.toString() == "") {
                    showCustomToast("인터뷰 제목을 입력해주세요")
                }
                else {
                    if (mEtPurpose.text.toString() == "") {
                        showCustomToast("인터뷰 목적을 입력해주세요")
                    }
                    else {
                        var intent = Intent(this@AddInterview1Activity, AddInterview2Activity::class.java)
                        intent.putExtra("categoriesNo", getIntent().getIntExtra("categoriesNo", 0))
                        intent.putExtra("i_title", mEtTitle.text.toString())
                        intent.putExtra("purpose", mEtPurpose.text.toString())

                        startActivity(intent)
                    }
                }
            }
        })
        mIvBack.setOnClickListener(object: OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                finish()
            }
        })

    }
}
