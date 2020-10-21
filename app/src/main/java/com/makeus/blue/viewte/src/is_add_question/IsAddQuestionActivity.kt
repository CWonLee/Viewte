package com.makeus.blue.viewte.src.is_add_question

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.end_interview.EndInterviewActivity
import com.makeus.blue.viewte.src.interview.InterviewActivity
import com.makeus.blue.viewte.src.interview.interfaces.QuestionAPI
import com.makeus.blue.viewte.src.interview.models.RequestQuestion
import com.makeus.blue.viewte.src.interview.models.RequestQuestionInfo
import com.makeus.blue.viewte.src.interview.models.ResponseQuestion
import com.makeus.blue.viewte.src.is_add_question.interfaces.AddMemoAPI
import com.makeus.blue.viewte.src.is_add_question.models.RequestAddMemo
import com.makeus.blue.viewte.src.is_add_question.models.RequestAddMemoInfo
import com.makeus.blue.viewte.src.is_add_question.models.ResponseAddMemo
import com.makeus.blue.viewte.src.login.LoginActivity
import com.makeus.blue.viewte.src.main.MainActivity
import com.makeus.blue.viewte.src.main.interfaces.AddCategoryAPI
import com.makeus.blue.viewte.src.main.models.RequestAddCategory
import com.makeus.blue.viewte.src.main.models.ResponseAddCategory
import com.makeus.blue.viewte.src.prev_interview.models.ResponseInterviewResultQuestion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IsAddQuestionActivity : BaseActivity() {

    private lateinit var mClYes : ConstraintLayout
    private lateinit var mClNo : ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_is_add_question)

        mClYes = findViewById(R.id.is_add_question_cl_yes)
        mClNo = findViewById(R.id.is_add_question_cl_no)

        mClYes.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                var nextIntent = Intent(this@IsAddQuestionActivity, InterviewActivity::class.java)
                nextIntent.putExtra("questionList", intent.getSerializableExtra("questionList") as ArrayList<ResponseInterviewResultQuestion>)
                nextIntent.putExtra("interviewNo", intent.getIntExtra("interviewNo", 0))
                nextIntent.putExtra("interviewTitle", intent.getStringExtra("interviewTitle"))
                nextIntent.putExtra("interviewDate", intent.getStringExtra("interviewDate"))
                nextIntent.putExtra("answerList", intent.getStringArrayListExtra("answerList"))
                nextIntent.putExtra("isPlus", 1)
                startActivity(nextIntent)
            }
        })

        mClNo.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                postAddMemo()
            }
        })
    }

    private fun postAddMemo() {
        showProgressDialog()
        val api = AddMemoAPI.create()

        var arraylist : ArrayList<RequestAddMemoInfo> = ArrayList<RequestAddMemoInfo>()
        for ((cnt, i) in (intent.getSerializableExtra("questionList") as ArrayList<ResponseInterviewResultQuestion>).withIndex()) {
            arraylist.add(RequestAddMemoInfo(i.getQuestionNo(), intent.getStringArrayListExtra("answerList")[cnt]))
        }

        api.postAddMemo(RequestAddMemo(arraylist)).enqueue(object : Callback<ResponseAddMemo> {
            override fun onResponse(call: Call<ResponseAddMemo>, response: Response<ResponseAddMemo>) {
                var responseAddMemo = response.body()

                if (responseAddMemo!!.IsSuccess() && responseAddMemo.getCode() == 200) {

                    showCustomToast(responseAddMemo.getMessage())

                    var nextIntent = Intent(this@IsAddQuestionActivity, EndInterviewActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(nextIntent)
                }
                else {
                    hideProgressDialog()
                    showCustomToast(responseAddMemo.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponseAddMemo>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
            }
        })
    }
}