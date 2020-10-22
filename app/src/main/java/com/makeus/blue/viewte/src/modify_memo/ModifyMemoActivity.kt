package com.makeus.blue.viewte.src.modify_memo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.request.RequestOptions
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.GlideApp
import com.makeus.blue.viewte.src.modify_memo.interfaces.ModifyMemoAPI
import com.makeus.blue.viewte.src.modify_memo.models.RequestPatchMemo
import com.makeus.blue.viewte.src.modify_memo.models.ResponsePatchMemo
import com.makeus.blue.viewte.src.prev_interview.interfaces.GetInterviewAPI
import com.makeus.blue.viewte.src.prev_interview.models.ResponseInterview
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModifyMemoActivity : BaseActivity() {

    private lateinit var mTvQuestion:TextView
    private lateinit var mLlNext:LinearLayout
    private lateinit var mTvTitle:TextView
    private lateinit var mTvDate:TextView
    private lateinit var mEtStt:EditText
    private var mMemoNo = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_memo)

        mTvQuestion = findViewById(R.id.modify_memo_tv_question)
        mTvTitle = findViewById(R.id.modify_memo_tv_title)
        mTvDate = findViewById(R.id.modify_memo_tv_date)
        mLlNext = findViewById(R.id.modify_memo_ll_next)
        mEtStt = findViewById(R.id.modify_memo_et_stt)

        init()

        mLlNext.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                if (mEtStt.text.toString() == "") {
                    showCustomToast("답변을 입력해주세요")
                }
                else {
                    patchMemo()
                }
            }
        })
    }

    private fun init() {
        mTvQuestion.text = intent.getStringExtra("question")
        mTvTitle.text = intent.getStringExtra("interview_title")
        mTvDate.text = intent.getStringExtra("interview_date")
        mEtStt.setText(intent.getStringExtra("memo"))
        mMemoNo = intent.getIntExtra("memoNo", -1)
    }

    private fun patchMemo() {
        showProgressDialog()
        val api = ModifyMemoAPI.create()

        api.patchMemo(RequestPatchMemo(mMemoNo, mEtStt.text.toString())).enqueue(object :
            Callback<ResponsePatchMemo> {
            override fun onResponse(call: Call<ResponsePatchMemo>, response: Response<ResponsePatchMemo>) {
                var responsePatchMemo = response.body()

                if (responsePatchMemo!!.IsSuccess() && responsePatchMemo.getCode() == 200) {
                    showCustomToast(responsePatchMemo.getMessage())
                    var intent = Intent()
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                else {
                    hideProgressDialog()
                    showCustomToast(responsePatchMemo.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponsePatchMemo>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
                t.printStackTrace()
            }
        })
    }
}