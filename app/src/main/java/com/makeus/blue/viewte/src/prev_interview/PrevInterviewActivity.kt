package com.makeus.blue.viewte.src.prev_interview

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.category.CategoryAdapter
import com.makeus.blue.viewte.src.interview.InterviewActivity
import com.makeus.blue.viewte.src.prev_interview.interfaces.GetInterviewAPI
import com.makeus.blue.viewte.src.prev_interview.models.ResponseInterview
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PrevInterviewActivity : BaseActivity() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerViewAdapter: PrevInterviewAdapter
    private lateinit var mTvTitle: TextView
    private lateinit var mTvDate: TextView
    private lateinit var mIvBackBtn: ImageView
    private lateinit var mClStartInterview: ConstraintLayout

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prev_interview)

        mRecyclerView = findViewById(R.id.prev_interview_rv)
        mTvTitle = findViewById(R.id.prev_interview_tv_title)
        mTvDate = findViewById(R.id.prev_interview_tv_date)
        mClStartInterview = findViewById(R.id.prev_interview_cl_record)
        mIvBackBtn = findViewById(R.id.prev_interview_iv_back)

        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.isNestedScrollingEnabled = false

        getInterview()

        mIvBackBtn.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                finish()
            }
        })
        mClStartInterview.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                var intent = Intent(this@PrevInterviewActivity, InterviewActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun getInterview() {
        showProgressDialog()
        val api = GetInterviewAPI.create()

        api.getInterview(intent.getIntExtra("categoriesNo", 0), intent.getIntExtra("interviewNo", 0)).enqueue(object :
            Callback<ResponseInterview> {
            override fun onResponse(call: Call<ResponseInterview>, response: Response<ResponseInterview>) {
                var responseGetInterview = response.body()

                if (responseGetInterview!!.getIsSuccess() && responseGetInterview.getCode() == 200) {
                    hideProgressDialog()

                    mRecyclerViewAdapter = PrevInterviewAdapter(responseGetInterview.getResult().getQuestionList())
                    mRecyclerView.adapter = mRecyclerViewAdapter

                    mTvTitle.text = responseGetInterview.getResult().getInterviewList()[0].getITitle()
                    mTvDate.text = responseGetInterview.getResult().getInterviewList()[0].getDate() + ", " + responseGetInterview.getResult().getInterviewList()[0].getTime()
                }
                else {
                    hideProgressDialog()
                    showCustomToast(responseGetInterview.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponseInterview>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
                t.printStackTrace()
            }
        })
    }
}