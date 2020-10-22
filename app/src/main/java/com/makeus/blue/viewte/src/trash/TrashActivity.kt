package com.makeus.blue.viewte.src.trash

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.category.CategoryAdapter
import com.makeus.blue.viewte.src.category.interfaces.GetInterviewAPI
import com.makeus.blue.viewte.src.category.models.ResponseInterview
import com.makeus.blue.viewte.src.trash.interfaces.GetTrashAPI
import com.makeus.blue.viewte.src.trash.interfaces.TrashDeleteAPI
import com.makeus.blue.viewte.src.trash.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrashActivity : BaseActivity() {

    private lateinit var mIvBack : ImageView
    private lateinit var mClDelete : ConstraintLayout
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerViewAdapter: TrashRecyclerAdapter
    private var mTrashList : ArrayList<ResponseGetTrashResult> = ArrayList()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trash)

        mIvBack = findViewById(R.id.trash_back)
        mClDelete = findViewById(R.id.trash_cl_all_delete_btn)
        mRecyclerView = findViewById(R.id.trash_rv)

        mRecyclerViewAdapter = TrashRecyclerAdapter(mTrashList, this)
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        mRecyclerView.adapter = mRecyclerViewAdapter

        getTrash()

        mIvBack.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                finish()
            }
        })
        mClDelete.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                deleteTrash()
            }
        })
    }

    private fun getTrash() {
        showProgressDialog()
        val api = GetTrashAPI.create()

        api.getTrash().enqueue(object :
            Callback<ResponseGetTrash> {
            override fun onResponse(call: Call<ResponseGetTrash>, response: Response<ResponseGetTrash>) {
                var responseGetTrash = response.body()

                if (responseGetTrash!!.IsSuccess() && responseGetTrash.getCode() == 200) {
                    hideProgressDialog()

                    mTrashList.clear()
                    for (i in responseGetTrash.getResult()) {
                        mTrashList.add(i)
                    }
                    mRecyclerViewAdapter.notifyDataSetChanged()

                }
                else {
                    hideProgressDialog()
                    showCustomToast(responseGetTrash.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponseGetTrash>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
                t.printStackTrace()
            }
        })
    }

    private fun deleteTrash() {
        showProgressDialog()
        val api = TrashDeleteAPI.create()

        api.deleteTrash().enqueue(object :
            Callback<ResponseDeleteTrash> {
            override fun onResponse(call: Call<ResponseDeleteTrash>, response: Response<ResponseDeleteTrash>) {
                var responseDeleteTrash = response.body()

                if (responseDeleteTrash!!.IsSuccess() && responseDeleteTrash.getCode() == 200) {
                    hideProgressDialog()

                    mTrashList.clear()
                    mRecyclerViewAdapter.notifyDataSetChanged()
                    showCustomToast(responseDeleteTrash.getMessage())

                }
                else {
                    hideProgressDialog()
                    showCustomToast(responseDeleteTrash.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponseDeleteTrash>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
                t.printStackTrace()
            }
        })
    }
}