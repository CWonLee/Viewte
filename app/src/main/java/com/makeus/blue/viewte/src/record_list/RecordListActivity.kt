package com.makeus.blue.viewte.src.record_list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.record_list.interfaces.DeleteRecordAPI
import com.makeus.blue.viewte.src.record_list.interfaces.GetRecordListAPI
import com.makeus.blue.viewte.src.record_list.interfaces.RecordListActivityView
import com.makeus.blue.viewte.src.record_list.models.ResponseDeleteRecord
import com.makeus.blue.viewte.src.record_list.models.ResponseGetRecordList
import com.makeus.blue.viewte.src.record_list.models.ResponseGetRecordListResult
import com.makeus.blue.viewte.src.trash.models.ResponseGetTrash
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecordListActivity : BaseActivity(), RecordListActivityView {

    private lateinit var mIvBack: ImageView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerViewAdapter: RecordListRecyclerAdapter
    private var mRecordList: ArrayList<ResponseGetRecordListResult> = ArrayList()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_list)

        mIvBack = findViewById(R.id.record_list_back)
        mRecyclerView = findViewById(R.id.record_list_rv)

        mRecyclerViewAdapter = RecordListRecyclerAdapter(mRecordList, this, this)
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        mRecyclerView.adapter = mRecyclerViewAdapter

        getRecordList()

        mIvBack.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                finish()
            }
        })
    }

    private fun getRecordList() {
        showProgressDialog()
        val api = GetRecordListAPI.create()

        api.getRecord().enqueue(object :
            Callback<ResponseGetRecordList> {
            override fun onResponse(call: Call<ResponseGetRecordList>, response: Response<ResponseGetRecordList>) {
                var responseGetRecordList = response.body()

                if (responseGetRecordList!!.IsSuccess() && responseGetRecordList.getCode() == 200) {
                    hideProgressDialog()

                    mRecordList.clear()
                    for (i in responseGetRecordList.getResult()) {
                        mRecordList.add(i)
                    }
                    mRecyclerViewAdapter.notifyDataSetChanged()

                }
                else {
                    hideProgressDialog()
                    showCustomToast(responseGetRecordList.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponseGetRecordList>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
                t.printStackTrace()
            }
        })
    }

    override fun deleteRecord(noiseNo: Int) {
        showProgressDialog()
        val api = DeleteRecordAPI.create()

        api.deleteRecord(noiseNo).enqueue(object :
            Callback<ResponseDeleteRecord> {
            override fun onResponse(call: Call<ResponseDeleteRecord>, response: Response<ResponseDeleteRecord>) {
                var responseDeleteRecord = response.body()

                if (responseDeleteRecord!!.IsSuccess() && responseDeleteRecord.getCode() == 200) {
                    hideProgressDialog()

                    getRecordList()

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