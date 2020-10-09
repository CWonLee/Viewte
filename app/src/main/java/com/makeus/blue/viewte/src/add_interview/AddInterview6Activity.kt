package com.makeus.blue.viewte.src.add_interview

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity

class AddInterview6Activity : BaseActivity() {

    private lateinit var mRecyclerAdapter: AddInterview6RecyclerAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mBtnNext: Button
    private var mAnswerList: ArrayList<String> = ArrayList()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_interview6)

        mRecyclerView = findViewById(R.id.add_interview6_rv)

        for (i in intent.getStringArrayListExtra("questionList")) {
            mAnswerList.add("")
        }

        mRecyclerAdapter = AddInterview6RecyclerAdapter(this, intent.getStringArrayListExtra("questionList"), mAnswerList)
        mBtnNext = findViewById(R.id.add_interview6_btn_next)

        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mRecyclerAdapter
        mRecyclerView.isNestedScrollingEnabled = false

        mBtnNext.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                for (i in mRecyclerAdapter.getAnswer()) {
                    println(i)
                }
            }
        })
    }
}