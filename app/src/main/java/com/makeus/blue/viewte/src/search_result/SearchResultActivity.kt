package com.makeus.blue.viewte.src.search_result

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.category.CategoryAdapter
import com.makeus.blue.viewte.src.main.models.ResponseSearchResult
import com.makeus.blue.viewte.src.record_list.RecordListRecyclerAdapter

class SearchResultActivity : BaseActivity() {

    private lateinit var mIvBack: ImageView
    private lateinit var mRecyclerView : RecyclerView
    private lateinit var mRecyclerViewAdapter : SearchResultAdapter

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        mIvBack = findViewById(R.id.search_result_back)
        mRecyclerView = findViewById(R.id.search_result_rv)

        mRecyclerViewAdapter = SearchResultAdapter(intent.getSerializableExtra("SearchResult") as ArrayList<ResponseSearchResult>, this)
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        mRecyclerView.adapter = mRecyclerViewAdapter

        mIvBack.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                finish()
            }
        })
    }
}