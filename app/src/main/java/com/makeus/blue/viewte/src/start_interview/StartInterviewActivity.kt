package com.makeus.blue.viewte.src.start_interview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.ApplicationClass
import com.makeus.blue.viewte.src.interview.InterviewActivity
import com.makeus.blue.viewte.src.login.LoginActivity
import com.makeus.blue.viewte.src.main.MainActivity
import com.makeus.blue.viewte.src.prev_interview.models.ResponseInterviewResultQuestion

class StartInterviewActivity : AppCompatActivity() {

    val SPLASH_TIME_OUT: Long = 1500 //3초간 보여 주고 넘어 간다.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_interview)

        Handler().postDelayed({
            //어떤 액티비티로 넘어 갈지 설정 - 당연히 메인액티비로 넘어 가면 된다.

            var nextIntent = Intent(this@StartInterviewActivity, InterviewActivity::class.java)
            nextIntent.putExtra("questionList", intent.getSerializableExtra("questionList") as ArrayList<ResponseInterviewResultQuestion>)
            nextIntent.putExtra("interviewNo", intent.getIntExtra("interviewNo", 0))
            nextIntent.putExtra("interviewTitle", intent.getStringExtra("interviewTitle"))
            nextIntent.putExtra("interviewDate", intent.getStringExtra("interviewDate"))
            nextIntent.putExtra("isPlus", 0)
            startActivity(nextIntent)

            finish()
        }, SPLASH_TIME_OUT)
    }
}