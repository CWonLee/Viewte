package com.makeus.blue.viewte.src.add_interview

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.add_interview.interfaces.PostInterviewAPI
import com.makeus.blue.viewte.src.add_interview.models.RequestInterview
import com.makeus.blue.viewte.src.add_interview.models.RequestInterviewQuestionInfo
import com.makeus.blue.viewte.src.add_interview.models.ResponseInterview
import com.makeus.blue.viewte.src.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddInterview7Activity : BaseActivity() {

    private lateinit var mTvTitle: TextView
    private lateinit var mRecyclerAdapter: AddInterview7RecyclerAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mBtnNext: Button
    private var mKeywordList: ArrayList<String> = ArrayList()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_interview7)

        mTvTitle = findViewById(R.id.add_interview7_tv_title)
        mRecyclerView = findViewById(R.id.add_interview7_rv)
        mBtnNext = findViewById(R.id.add_interview7_btn_next)

        for (i in intent.getStringArrayListExtra("questionList")) {
            mKeywordList.add("")
        }

        var titleString: String = "질문별 꼭 필요한\n키워드, 키 프레이즈를\n알려주세요!"
        var ssb = SpannableStringBuilder(titleString)
        ssb.setSpan(ForegroundColorSpan(Color.parseColor("#456EFF")), 10, 21, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        mTvTitle.text = ssb

        mRecyclerAdapter = AddInterview7RecyclerAdapter(this, intent.getStringArrayListExtra("questionList"), intent.getStringArrayListExtra("answerList"), mKeywordList)
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mRecyclerAdapter
        mRecyclerView.isNestedScrollingEnabled = false

        mBtnNext.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var bo : Boolean = true
                for (i in mRecyclerAdapter.getKeyword()) {
                    if (i == "") {
                        bo = false
                        break
                    }
                }
                if (!bo) {
                    showCustomToast("답변을 입력해주세요")
                }
                else {
                    addInterview()
                }
            }
        })
    }

    private fun addInterview() {
        showProgressDialog()

        var requestInterviewQuestionInfo = ArrayList<RequestInterviewQuestionInfo>()
        var questionList = intent.getStringArrayListExtra("questionList")
        var answerList = intent.getStringArrayListExtra("answerList")
        var keywordList = mRecyclerAdapter.getKeyword()
        for ((cnt, i) in questionList.withIndex()) {
            requestInterviewQuestionInfo.add(RequestInterviewQuestionInfo(cnt + 1, questionList[cnt], answerList[cnt], keywordList[cnt]))
        }

        var requestInterview = RequestInterview(intent.getIntExtra(
            "categoriesNo", 0),
            intent.getStringExtra("i_title"),
            intent.getStringExtra("purpose"),
            intent.getStringExtra("date"),
            intent.getStringExtra("time"),
            intent.getStringExtra("location"),
            "image Url",
            requestInterviewQuestionInfo
        )

        requestInterview.printAll()

        val api = PostInterviewAPI.create()

        api.postInterview(requestInterview).enqueue(object : Callback<ResponseInterview> {
            override fun onResponse(call: Call<ResponseInterview>, response: Response<ResponseInterview>) {
                hideProgressDialog()
                var responseInterview = response.body()

                if (responseInterview!!.isSuccess() && responseInterview.getCode() == 200) {
                    var myIntent = Intent(this@AddInterview7Activity, MainActivity::class.java)
                    showCustomToast(responseInterview.getMessage())
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(myIntent)
                }
                else {
                    showCustomToast(responseInterview.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponseInterview>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
            }
        })
    }
}