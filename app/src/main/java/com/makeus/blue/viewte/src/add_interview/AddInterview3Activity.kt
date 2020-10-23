package com.makeus.blue.viewte.src.add_interview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.main.MainActivity
import com.makeus.blue.viewte.src.main.interfaces.SearchAPI
import com.makeus.blue.viewte.src.main.models.ResponseSearch
import com.makeus.blue.viewte.src.record.RecordActivity
import com.makeus.blue.viewte.src.search_result.SearchResultActivity
import com.makeus.blue.viewte.src.setting.SettingActivity
import com.makeus.blue.viewte.src.trash.TrashActivity
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.Holder
import com.orhanobut.dialogplus.ViewHolder
import kotlinx.android.synthetic.main.activity_add_interview2.*
import kotlinx.android.synthetic.main.activity_add_interview3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddInterview3Activity : BaseActivity() {

    private lateinit var mIvBack: ImageView
    private lateinit var mBtnNext: Button
    private lateinit var mTimePicker: TouchInterceptorTimePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_interview3)

        mIvBack = findViewById(R.id.add_interview3_iv_back)
        mBtnNext = findViewById(R.id.add_interview3_btn_next)
        mTimePicker = findViewById(R.id.add_interview3_time_picker)

        add_interview3_cl_home.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview3Activity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        })
        add_interview3_cl_search.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                showSearchDialog()
            }
        })
        add_interview3_cl_mic.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview3Activity, RecordActivity::class.java)
                startActivity(intent)
            }
        })
        add_interview3_cl_trash.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview3Activity, TrashActivity::class.java)
                startActivity(intent)
            }
        })
        add_interview3_cl_setting.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview3Activity, SettingActivity::class.java)
                startActivity(intent)
            }
        })

        mIvBack.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                finish()
            }
        })
        mBtnNext.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview3Activity, AddInterview4Activity::class.java)

                var hour = mTimePicker.hour.toString()
                var minute = mTimePicker.minute.toString()
                if (hour.length == 1) {
                    hour = "0$hour"
                }
                if (minute.length == 1) {
                    minute = "0$minute"
                }
                var timeString = "$hour:$minute"

                intent.putExtra("categoriesNo", getIntent().getIntExtra("categoriesNo", 0))
                intent.putExtra("i_title", getIntent().getStringExtra("i_title"))
                intent.putExtra("purpose", getIntent().getStringExtra("purpose"))
                intent.putExtra("date", getIntent().getStringExtra("date"))
                intent.putExtra("time", timeString)
                intent.putExtra("imageUrl", getIntent().getStringExtra("imageUrl"))

                startActivity(intent)
            }
        })
    }

    private fun showSearchDialog() {
        val searchGravity: Int = Gravity.TOP
        val holder: Holder

        holder = ViewHolder(R.layout.dialog_search)

        val builder = DialogPlus.newDialog(this).apply {
            setContentHolder(holder)
            isCancelable = true
            setGravity(searchGravity)
            setOnClickListener { dialog, view ->
                var editText: EditText = dialog.holderView.findViewById(R.id.search_edit_text)

                if (view.id == R.id.search_iv) {
                    if (editText.text.toString() != "") {
                        getSearch(editText.text.toString())
                    }
                }
            }
        }
        builder.create().show()
    }

    private fun getSearch(content: String) {
        val api = SearchAPI.create()

        api.getSearch(content).enqueue(object : Callback<ResponseSearch> {
            override fun onResponse(call: Call<ResponseSearch>, response: Response<ResponseSearch>) {
                var responseSearch = response.body()

                if (responseSearch!!.IsSuccess() && responseSearch.getCode() == 200) {

                    var intent = Intent(this@AddInterview3Activity, SearchResultActivity::class.java)
                    intent.putExtra("SearchResult", responseSearch.getResult())
                    startActivity(intent)
                }
                else {
                    hideProgressDialog()
                    showCustomToast(responseSearch.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
            }
        })
    }
}
