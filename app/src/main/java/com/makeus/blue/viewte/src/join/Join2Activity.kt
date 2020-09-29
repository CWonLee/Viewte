package com.makeus.blue.viewte.src.join

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.ApplicationClass
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.join.api.JoinAPI
import com.makeus.blue.viewte.src.join.models.RequestJoin
import com.makeus.blue.viewte.src.join.models.ResponseJoin
import com.makeus.blue.viewte.src.login.api.LoginAPI
import com.makeus.blue.viewte.src.login.models.RequestLogin
import com.makeus.blue.viewte.src.login.models.ResponseLogin
import com.makeus.blue.viewte.src.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Join2Activity : BaseActivity() {

    lateinit var mTvHello: TextView
    lateinit var mIvNext: ImageView
    lateinit var mEtCategory: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join2)

        mTvHello = findViewById(R.id.join2_tv_hello)
        mIvNext = findViewById(R.id.join2_iv_next)
        mEtCategory = findViewById(R.id.join2_et_category)

        mTvHello.setText("좋아요! " + intent.getStringExtra("name") + "님\n" +
                "그럼 첫 번째 인터뷰\n" +
                "카테고리를 만들어 주세요!")
        mIvNext.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                if (mEtCategory.text.toString() == "") {
                    showCustomToast("카테고리를 입력해주세요")
                }
                else {
                    join()
                }
            }
        })
    }

    fun join() {
        showProgressDialog()

        val joinApi = JoinAPI.create()

        println("join2 = " + intent.getStringExtra("oauthid"))
        joinApi.postJoin(RequestJoin(intent.getStringExtra("name"), intent.getStringExtra("oauthid"), mEtCategory.text.toString())).enqueue(object : Callback<ResponseJoin> {
            override fun onFailure(call: Call<ResponseJoin>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
            }

            override fun onResponse(call: Call<ResponseJoin>, response: Response<ResponseJoin>) {
                val responseJoin = response.body()
                if (responseJoin!!.IsSuccess() && responseJoin.getCode() == 200) {
                    showCustomToast(responseJoin.getMessage())
                    ApplicationClass.prefs.myEditText = responseJoin.getJwt()
                    var intent = Intent(this@Join2Activity, MainActivity::class.java)
                    startActivity(intent)
                }
                else {
                    hideProgressDialog()
                    showCustomToast(responseJoin.getMessage())
                }
            }
        })
    }
}
