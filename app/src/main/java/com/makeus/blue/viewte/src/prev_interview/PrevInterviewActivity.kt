package com.makeus.blue.viewte.src.prev_interview

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.GlideApp
import com.makeus.blue.viewte.src.ice_break.IceBreakActivity
import com.makeus.blue.viewte.src.interview.InterviewActivity
import com.makeus.blue.viewte.src.prev_interview.interfaces.GetInterviewAPI
import com.makeus.blue.viewte.src.prev_interview.models.ResponseInterview
import com.makeus.blue.viewte.src.prev_interview.models.ResponseInterviewResultQuestion
import kotlinx.android.synthetic.main.item_category_recycler.view.*
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
    private lateinit var mIvMain: ImageView
    private var mQuestionList: ArrayList<ResponseInterviewResultQuestion> = ArrayList()
    private var mDateString:String = ""
    private var mInterviewName:String = ""

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prev_interview)

        mRecyclerView = findViewById(R.id.prev_interview_rv)
        mTvTitle = findViewById(R.id.prev_interview_tv_title)
        mTvDate = findViewById(R.id.prev_interview_tv_date)
        mClStartInterview = findViewById(R.id.prev_interview_cl_record)
        mIvBackBtn = findViewById(R.id.prev_interview_iv_back)
        mIvMain = findViewById(R.id.prev_interview_iv_main)

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
                var nextIntent = Intent(this@PrevInterviewActivity, IceBreakActivity::class.java)
                nextIntent.putExtra("questionList", mQuestionList)
                nextIntent.putExtra("interviewNo", intent.getIntExtra("interviewNo", 0))
                nextIntent.putExtra("interviewTitle", mInterviewName)
                nextIntent.putExtra("interviewDate", mDateString)
                startActivity(nextIntent)
            }
        })
    }

    private fun getInterview() {
        showProgressDialog()
        val api = GetInterviewAPI.create()

        println("categoryNo : " + intent.getIntExtra("categoriesNo", 0))
        println("interviewNo : " + intent.getIntExtra("interviewNo", 0))
        api.getInterview(intent.getIntExtra("categoriesNo", 0), intent.getIntExtra("interviewNo", 0)).enqueue(object :
            Callback<ResponseInterview> {
            override fun onResponse(call: Call<ResponseInterview>, response: Response<ResponseInterview>) {
                var responseGetInterview = response.body()

                if (responseGetInterview!!.getIsSuccess() && responseGetInterview.getCode() == 200) {
                    hideProgressDialog()

                    mRecyclerViewAdapter = PrevInterviewAdapter(responseGetInterview.getResult().getQuestionList())
                    mRecyclerView.adapter = mRecyclerViewAdapter

                    for (i in responseGetInterview.getResult().getQuestionList()) {
                        mQuestionList.add(i)
                    }

                    mInterviewName = responseGetInterview.getResult().getInterviewList()[0].getITitle()
                    mTvTitle.text = responseGetInterview.getResult().getInterviewList()[0].getITitle()
                    mDateString += responseGetInterview.getResult().getInterviewList()[0].getDate()[0]
                    mDateString += responseGetInterview.getResult().getInterviewList()[0].getDate()[1]
                    mDateString += responseGetInterview.getResult().getInterviewList()[0].getDate()[2]
                    mDateString += responseGetInterview.getResult().getInterviewList()[0].getDate()[3]
                    mDateString += "."
                    mDateString += responseGetInterview.getResult().getInterviewList()[0].getDate()[5]
                    mDateString += responseGetInterview.getResult().getInterviewList()[0].getDate()[6]
                    mDateString += "."
                    mDateString += responseGetInterview.getResult().getInterviewList()[0].getDate()[8]
                    mDateString += responseGetInterview.getResult().getInterviewList()[0].getDate()[9]
                    mDateString += " "
                    mDateString += responseGetInterview.getResult().getInterviewList()[0].getTime()[0]
                    mDateString += responseGetInterview.getResult().getInterviewList()[0].getTime()[1]
                    mDateString += ":"
                    mDateString += responseGetInterview.getResult().getInterviewList()[0].getTime()[3]
                    mDateString += responseGetInterview.getResult().getInterviewList()[0].getTime()[4]
                    mTvDate.text = mDateString

                    GlideApp.with(this@PrevInterviewActivity).load(responseGetInterview.getResult().getInterviewList()[0].getImageUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(mIvMain)
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