package com.makeus.blue.viewte.src.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.AppPreferences
import com.makeus.blue.viewte.src.ApplicationClass
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.login.LoginActivity
import com.makeus.blue.viewte.src.main.MainActivity

class SplashActivity : BaseActivity() {

    val SPLASH_TIME_OUT: Long = 1500 //3초간 보여 주고 넘어 간다.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            //어떤 액티비티로 넘어 갈지 설정 - 당연히 메인액티비로 넘어 가면 된다.

            println("preference = " + ApplicationClass.prefs.myEditText.toString())

            if (ApplicationClass.prefs.myEditText.toString() == "") {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            else {
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        }, SPLASH_TIME_OUT)
    }
}
