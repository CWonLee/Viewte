package com.makeus.blue.viewte.src.add_interview

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.add_interview.interfaces.PostInterviewAPI
import com.makeus.blue.viewte.src.add_interview.models.RequestInterview
import com.makeus.blue.viewte.src.add_interview.models.RequestInterviewQuestionInfo
import com.makeus.blue.viewte.src.add_interview.models.ResponseInterview
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
import kotlinx.android.synthetic.main.activity_add_interview6.*
import kotlinx.android.synthetic.main.activity_add_interview7.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class AddInterview7Activity : BaseActivity() {

    private lateinit var mTvTitle: TextView
    private lateinit var mRecyclerAdapter: AddInterview7RecyclerAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mBtnNext: Button
    private var mKeywordList: ArrayList<String> = ArrayList()
    private var mStorageRef: StorageReference? = null
    private var mImageUrl: String = ""

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

        add_interview7_iv_back.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                finish()
            }
        })
        add_interview7_cl_home.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview7Activity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        })
        add_interview7_cl_search.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                showSearchDialog()
            }
        })
        add_interview7_cl_mic.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview7Activity, RecordActivity::class.java)
                startActivity(intent)
            }
        })
        add_interview7_cl_trash.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview7Activity, TrashActivity::class.java)
                startActivity(intent)
            }
        })
        add_interview7_cl_setting.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview7Activity, SettingActivity::class.java)
                startActivity(intent)
            }
        })

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
                    mStorageRef = FirebaseStorage.getInstance().reference;
                    val ref: StorageReference = mStorageRef!!.child(
                        "images/" + UUID.randomUUID().toString())



                    ref.putFile(intent.getStringExtra("imageUrl").toUri()).addOnSuccessListener(
                        OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot -> // Get a URL to the uploaded content
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            mImageUrl = it.toString()
                            addInterview()
                        }
                    })
                        .addOnFailureListener(OnFailureListener {
                            // Handle unsuccessful uploads
                            // ...
                            showCustomToast(resources.getString(R.string.network_error))
                        })
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
            mImageUrl,
            requestInterviewQuestionInfo
        )

        println("ImageUrl == $mImageUrl")

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

                    var intent = Intent(this@AddInterview7Activity, SearchResultActivity::class.java)
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