package com.makeus.blue.viewte.src.introduce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.prev_interview.models.ResponseInterviewResultQuestion
import com.makeus.blue.viewte.src.start_interview.StartInterviewActivity

class IntroduceActivity : BaseActivity() {

    private lateinit var mCl1 : ConstraintLayout
    private lateinit var mCl2 : ConstraintLayout
    private lateinit var mCl3 : ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduce)

        mCl1 = findViewById(R.id.introduce_cl_1)
        mCl2 = findViewById(R.id.introduce_cl_2)
        mCl3 = findViewById(R.id.introduce_cl_3)

        mCl1.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                var nextIntent = Intent(this@IntroduceActivity, StartInterviewActivity::class.java)
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
                var nextIntent = Intent(this@IntroduceActivity, StartInterviewActivity::class.java)
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
                var nextIntent = Intent(this@IntroduceActivity, StartInterviewActivity::class.java)
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