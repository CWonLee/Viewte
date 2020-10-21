package com.makeus.blue.viewte.src.end_interview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.interview.InterviewActivity
import com.makeus.blue.viewte.src.main.MainActivity
import com.makeus.blue.viewte.src.prev_interview.models.ResponseInterviewResultQuestion

class EndInterviewActivity : AppCompatActivity() {

    val SPLASH_TIME_OUT: Long = 1500 //3초간 보여 주고 넘어 간다.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_interview)

        Handler().postDelayed({
            //어떤 액티비티로 넘어 갈지 설정 - 당연히 메인액티비로 넘어 가면 된다.

            var nextIntent = Intent(this@EndInterviewActivity, MainActivity::class.java)

            startActivity(nextIntent)

            finish()
        }, SPLASH_TIME_OUT)
    }
}