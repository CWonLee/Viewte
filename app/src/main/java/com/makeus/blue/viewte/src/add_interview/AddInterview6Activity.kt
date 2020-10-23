package com.makeus.blue.viewte.src.add_interview

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import kotlinx.android.synthetic.main.activity_add_interview5.*
import kotlinx.android.synthetic.main.activity_add_interview6.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddInterview6Activity : BaseActivity() {

    private lateinit var mRecyclerAdapter: AddInterview6RecyclerAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mBtnNext: Button
    private var mAnswerList: ArrayList<String> = ArrayList()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_interview6)

        mRecyclerView = findViewById(R.id.add_interview6_rv)

        for (i in intent.getStringArrayListExtra("questionList")) {
            mAnswerList.add("")
        }

        mRecyclerAdapter = AddInterview6RecyclerAdapter(this, intent.getStringArrayListExtra("questionList"), mAnswerList)
        mBtnNext = findViewById(R.id.add_interview6_btn_next)

        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mRecyclerAdapter
        mRecyclerView.isNestedScrollingEnabled = false

        add_interview6_iv_back.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                finish()
            }
        })
        add_interview6_cl_home.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview6Activity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        })
        add_interview6_cl_search.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                showSearchDialog()
            }
        })
        add_interview6_cl_mic.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview6Activity, RecordActivity::class.java)
                startActivity(intent)
            }
        })
        add_interview6_cl_trash.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview6Activity, TrashActivity::class.java)
                startActivity(intent)
            }
        })
        add_interview6_cl_setting.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview6Activity, SettingActivity::class.java)
                startActivity(intent)
            }
        })

        mBtnNext.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                var bo : Boolean = true
                for (i in mRecyclerAdapter.getAnswer()) {
                    if (i == "") {
                        bo = false
                        break
                    }
                }
                if (!bo) {
                    showCustomToast("답변을 입력해주세요")
                }
                else {
                    var intent = Intent(this@AddInterview6Activity, AddInterview7Activity::class.java)

                    intent.putExtra("categoriesNo", getIntent().getIntExtra("categoriesNo", 0))
                    intent.putExtra("i_title", getIntent().getStringExtra("i_title"))
                    intent.putExtra("purpose", getIntent().getStringExtra("purpose"))
                    intent.putExtra("date", getIntent().getStringExtra("date"))
                    intent.putExtra("time", getIntent().getStringExtra("time"))
                    intent.putExtra("location", getIntent().getStringExtra("location"))
                    intent.putStringArrayListExtra("questionList", getIntent().getStringArrayListExtra("questionList"))
                    intent.putStringArrayListExtra("answerList", mRecyclerAdapter.getAnswer())
                    intent.putExtra("imageUrl", getIntent().getStringExtra("imageUrl"))

                    startActivity(intent)
                }
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

                    var intent = Intent(this@AddInterview6Activity, SearchResultActivity::class.java)
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