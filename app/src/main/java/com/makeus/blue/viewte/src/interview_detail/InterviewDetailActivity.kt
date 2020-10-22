package com.makeus.blue.viewte.src.interview_detail

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.request.RequestOptions
import com.kakao.message.template.TextTemplate
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.GlideApp
import com.makeus.blue.viewte.src.interview_detail.interfaces.GetMemoAPI
import com.makeus.blue.viewte.src.interview_detail.interfaces.PostTrashAPI
import com.makeus.blue.viewte.src.interview_detail.models.RequestTrash
import com.makeus.blue.viewte.src.interview_detail.models.ResponseGetMemo
import com.makeus.blue.viewte.src.interview_detail.models.ResponseTrash
import com.makeus.blue.viewte.src.main.MainActivity
import com.makeus.blue.viewte.src.modify_memo.ModifyMemoActivity
import com.makeus.blue.viewte.src.prev_interview.interfaces.GetInterviewAPI
import com.makeus.blue.viewte.src.prev_interview.models.ResponseInterview
import com.makeus.blue.viewte.src.prev_interview.models.ResponseInterviewResultQuestion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InterviewDetailActivity : BaseActivity() {

    private lateinit var mIvMain : ImageView
    private lateinit var mTvTitle : TextView
    private lateinit var mTvDate : TextView
    private lateinit var mTvQuestion : TextView
    private lateinit var mTvAnswer : TextView
    private lateinit var mIvLeft : ImageView
    private lateinit var mIvRight : ImageView
    private lateinit var mIvBack : ImageView
    private lateinit var mIvTrash : ImageView
    private lateinit var mIvModify : ImageView
    //private lateinit var mIvShare: ImageView
    private var mPage : Int = 0
    private var mMemoNo : Int = -1
    private var mMemo : String = ""
    private var mDateString: String = ""
    private var mQuestionList: ArrayList<ResponseInterviewResultQuestion> = ArrayList()
    private val MODIFY_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interview_detail)

        mIvMain = findViewById(R.id.interview_detail_iv_main)
        mTvTitle = findViewById(R.id.interview_detail_tv_title)
        mTvDate = findViewById(R.id.interview_detail_tv_date)
        mTvQuestion = findViewById(R.id.interview_detail_tv_question)
        mTvAnswer = findViewById(R.id.interview_detail_tv_answer)
        mIvLeft = findViewById(R.id.interview_detail_iv_left)
        mIvRight = findViewById(R.id.interview_detail_iv_right)
        mIvBack = findViewById(R.id.interview_detail_iv_back)
        mIvTrash = findViewById(R.id.interview_detail_iv_trash)
        mIvModify = findViewById(R.id.interview_detail_iv_edit)
        //mIvShare = findViewById(R.id.interview_detail_iv_share)

        getInterview()

        mIvModify.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@InterviewDetailActivity, ModifyMemoActivity::class.java)
                intent.putExtra("interview_title", mTvTitle.text)
                intent.putExtra("interview_date", mTvDate.text)
                intent.putExtra("question", mQuestionList[mPage].getQuestion())
                intent.putExtra("memo", mMemo)
                intent.putExtra("memoNo", mMemoNo)
                startActivityForResult(intent, MODIFY_REQUEST_CODE)
            }
        })
        mIvTrash.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                postTrash()
            }
        })
        mIvBack.setOnClickListener(object :OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                finish()
            }
        })
        mIvLeft.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                if (mPage > 0) {
                    mPage--
                    getMemo(mQuestionList[mPage].getQuestionNo())
                    if (mPage == 0) {
                        mIvLeft.visibility = View.GONE
                    }
                    if (mPage < mQuestionList.size - 1) {
                        mIvRight.visibility = View.VISIBLE
                    }
                }
            }
        })
        mIvRight.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                if (mPage < mQuestionList.size - 1) {
                    mPage++
                    getMemo(mQuestionList[mPage].getQuestionNo())
                    if (mPage == mQuestionList.size - 1) {
                        mIvRight.visibility = View.GONE
                    }
                    if (mPage > 0) {
                        mIvLeft.visibility = View.VISIBLE
                    }
                }
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

                    for (i in responseGetInterview.getResult().getQuestionList()) {
                        mQuestionList.add(i)
                    }

                    mTvTitle.text = responseGetInterview.getResult().getInterviewList()[0].getITitle()
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

                    GlideApp.with(this@InterviewDetailActivity).load(responseGetInterview.getResult().getInterviewList()[0].getImageUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(mIvMain)

                    if (mPage == 0) {
                        mIvLeft.visibility = View.GONE
                    }
                    if (mPage == mQuestionList.size - 1) {
                        mIvRight.visibility = View.GONE
                    }

                    getMemo(responseGetInterview.getResult().getQuestionList()[0].getQuestionNo())
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

    private fun getMemo(questionNo: Int) {
        showProgressDialog()
        val api = GetMemoAPI.create()

        api.getMemo(questionNo).enqueue(object :
            Callback<ResponseGetMemo> {
            override fun onResponse(call: Call<ResponseGetMemo>, response: Response<ResponseGetMemo>) {
                var responseGetMemo = response.body()

                if (responseGetMemo!!.IsSuccess() && responseGetMemo.getCode() == 200) {
                    hideProgressDialog()
                    mTvQuestion.text = "질문 " + (mPage + 1).toString() + ". " + responseGetMemo.getResult()[0].getQuestion()
                    //mTvAnswer.text = responseGetMemo.getResult()[0].getMemo()
                    println(responseGetMemo.getResult()[0].getMemo())
                    mMemo = responseGetMemo.getResult()[0].getMemo()
                    mMemoNo = responseGetMemo.getResult()[0].getMemoNo()

                    var ssb = SpannableStringBuilder(mMemo.toString())
                    if (mQuestionList[mPage].getKeyword() != null) {
                        for (i in mMemo.indices) {
                            if (mMemo.toString()[i] == mQuestionList[mPage].getKeyword()[0]) {
                                var boolean = true
                                for (j in mQuestionList[mPage].getKeyword().indices) {
                                    if (i + j >= mMemo.toString().length) {
                                        boolean = false
                                        break
                                    }
                                    if (mMemo.toString()[i + j] != mQuestionList[mPage].getKeyword()[j]) {
                                        boolean = false
                                        break
                                    }
                                }
                                if (boolean) {
                                    ssb.setSpan(ForegroundColorSpan(Color.parseColor("#456EFF")), i, i + mQuestionList[mPage].getKeyword().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                }
                            }
                        }
                    }

                    mTvAnswer.text = ssb
                }
                else {
                    hideProgressDialog()
                    showCustomToast(responseGetMemo.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponseGetMemo>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
                t.printStackTrace()
            }
        })
    }

    private fun postTrash() {
        showProgressDialog()
        val api = PostTrashAPI.create()

        println("interviewNo = " + intent.getIntExtra("interviewNo", 0))
        api.postTrash(RequestTrash(intent.getIntExtra("interviewNo", 0))).enqueue(object :
            Callback<ResponseTrash> {
            override fun onResponse(call: Call<ResponseTrash>, response: Response<ResponseTrash>) {
                var responseTrash = response.body()

                if (responseTrash!!.IsSuccess() && responseTrash.getCode() == 200) {
                    hideProgressDialog()
                    showCustomToast(responseTrash.getMessage())
                    var intent = Intent(this@InterviewDetailActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                else {
                    hideProgressDialog()
                    showCustomToast(responseTrash.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponseTrash>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
                t.printStackTrace()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MODIFY_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) return;

            getMemo(mQuestionList[mPage].getQuestionNo())
        }
    }
}