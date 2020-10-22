package com.makeus.blue.viewte.src.record_detail

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.login.LoginActivity
import com.makeus.blue.viewte.src.main.MainActivity
import com.makeus.blue.viewte.src.record.interfaces.RecordAPI
import com.makeus.blue.viewte.src.record.models.RequestRecord
import com.makeus.blue.viewte.src.record.models.ResponseRecord
import com.makeus.blue.viewte.src.record_detail.interfaces.GetRecordDetailAPI
import com.makeus.blue.viewte.src.record_detail.models.ResponseRecordDetail
import com.makeus.blue.viewte.src.record_list.interfaces.DeleteRecordAPI
import com.makeus.blue.viewte.src.record_list.models.ResponseDeleteRecord
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecordDetailActivity : BaseActivity() {

    private lateinit var mIvBack: ImageView
    private lateinit var mTvTitle: TextView
    private lateinit var mIvDelete: ImageView
    private lateinit var mTvContent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_detail)

        mIvBack = findViewById(R.id.record_detail_iv_back)
        mTvTitle = findViewById(R.id.record_detail_tv_question)
        mIvDelete = findViewById(R.id.record_detail_iv_trash)
        mTvContent = findViewById(R.id.record_detail_tv_answer)

        getRecordDetail()

        mIvBack.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                finish()
            }
        })
        mIvDelete.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                deleteRecord()
            }
        })
    }

    private fun getRecordDetail() {
        showProgressDialog()

        val api = GetRecordDetailAPI.create()

        api.getRecordDetail(intent.getIntExtra("noiseNo", 0)).enqueue(object :
            Callback<ResponseRecordDetail> {
            override fun onResponse(call: Call<ResponseRecordDetail>, response: Response<ResponseRecordDetail>) {
                var responseRecord = response.body()

                hideProgressDialog()
                if (responseRecord!!.IsSuccess() && responseRecord.getCode() == 200) {
                    mTvTitle.text = responseRecord.getResult()[0].getTopic()
                    mTvContent.text = responseRecord.getResult()[0].getText()
                }
                else {
                    hideProgressDialog()
                    showCustomToast(responseRecord.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponseRecordDetail>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
            }
        })
    }

    private fun deleteRecord() {
        showProgressDialog()
        val api = DeleteRecordAPI.create()

        api.deleteRecord(intent.getIntExtra("noiseNo", 0)).enqueue(object :
            Callback<ResponseDeleteRecord> {
            override fun onResponse(call: Call<ResponseDeleteRecord>, response: Response<ResponseDeleteRecord>) {
                var responseDeleteRecord = response.body()

                if (responseDeleteRecord!!.IsSuccess() && responseDeleteRecord.getCode() == 200) {
                    hideProgressDialog()

                    showCustomToast(responseDeleteRecord.getMessage())
                    var intent = Intent(this@RecordDetailActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                }
                else {
                    hideProgressDialog()
                    showCustomToast(responseDeleteRecord.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponseDeleteRecord>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
                t.printStackTrace()
            }
        })
    }
}