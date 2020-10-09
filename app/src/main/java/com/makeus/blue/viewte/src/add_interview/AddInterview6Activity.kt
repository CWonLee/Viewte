package com.makeus.blue.viewte.src.add_interview

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makeus.blue.viewte.R

class AddInterview6Activity : AppCompatActivity() {

    private lateinit var mRecyclerAdapter: AddInterview6RecyclerAdapter
    private lateinit var mRecyclerView: RecyclerView

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_interview6)

        mRecyclerView = findViewById(R.id.add_interview6_rv)
        mRecyclerAdapter = AddInterview6RecyclerAdapter(this, intent.getStringArrayListExtra("questionList"))

        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mRecyclerAdapter
        mRecyclerView.isNestedScrollingEnabled = false
    }
}