package com.makeus.blue.viewte.src.join

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity

class Join2Activity : BaseActivity() {

    lateinit var mTvHello: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join2)

        mTvHello = findViewById(R.id.join2_tv_hello)

        mTvHello.setText("좋아요! " + intent.getStringExtra("name") + "님\n" +
                "그럼 첫 번째 인터뷰\n" +
                "카테고리를 만들어 주세요!")
    }
}
