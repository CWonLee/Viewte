package com.makeus.blue.viewte.src.ice_break

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.introduce.IntroduceActivity
import com.makeus.blue.viewte.src.prev_interview.models.ResponseInterviewResultQuestion

class IceBreakActivity : BaseActivity() {

    private lateinit var mCl1 : ConstraintLayout
    private lateinit var mCl2 : ConstraintLayout
    private lateinit var mCl3 : ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ice_break)

        mCl1 = findViewById(R.id.ice_break_cl_1)
        mCl2 = findViewById(R.id.ice_break_cl_2)
        mCl3 = findViewById(R.id.ice_break_cl_3)

        for (i in intent.getSerializableExtra("questionList") as ArrayList<ResponseInterviewResultQuestion>) {
            println(i.getQuestion())
        }

        mCl1.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                var nextIntent = Intent(this@IceBreakActivity, IntroduceActivity::class.java)
                nextIntent.putExtra("questionList", intent.getSerializableExtra("questionList") as ArrayList<ResponseInterviewResultQuestion>)
                nextIntent.putExtra("interviewNo", intent.getIntExtra("interviewNo", 0))
                nextIntent.putExtra("interviewTitle", intent.getStringExtra("interviewTitle"))
                nextIntent.putExtra("interviewDate", intent.getStringExtra("interviewDate"))
                startActivity(nextIntent)
                finish()
            }
        })
        mCl2.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                var nextIntent = Intent(this@IceBreakActivity, IntroduceActivity::class.java)
                nextIntent.putExtra("questionList", intent.getSerializableExtra("questionList") as ArrayList<ResponseInterviewResultQuestion>)
                nextIntent.putExtra("interviewNo", intent.getIntExtra("interviewNo", 0))
                nextIntent.putExtra("interviewTitle", intent.getStringExtra("interviewTitle"))
                nextIntent.putExtra("interviewDate", intent.getStringExtra("interviewDate"))
                startActivity(nextIntent)
                finish()
            }
        })
        mCl3.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                var nextIntent = Intent(this@IceBreakActivity, IntroduceActivity::class.java)
                nextIntent.putExtra("questionList", intent.getSerializableExtra("questionList") as ArrayList<ResponseInterviewResultQuestion>)
                nextIntent.putExtra("interviewNo", intent.getIntExtra("interviewNo", 0))
                nextIntent.putExtra("interviewTitle", intent.getStringExtra("interviewTitle"))
                nextIntent.putExtra("interviewDate", intent.getStringExtra("interviewDate"))
                startActivity(nextIntent)
                finish()
            }
        })
    }
}