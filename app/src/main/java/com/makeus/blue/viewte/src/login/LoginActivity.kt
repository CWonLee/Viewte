package com.makeus.blue.viewte.src.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.ApplicationClass
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.join.JoinActivity
import com.makeus.blue.viewte.src.login.api.LoginAPI
import com.makeus.blue.viewte.src.login.models.RequestLogin
import com.makeus.blue.viewte.src.login.models.ResponseLogin
import com.makeus.blue.viewte.src.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {

    private var callback: SessionCallback = SessionCallback()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Session.getCurrentSession().addCallback(callback);
    }

    @SuppressLint("MissingSuperCall")
    override fun onDestroy() {
        Session.getCurrentSession().removeCallback(callback);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.i("Log", "session get current session")
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private inner class SessionCallback : ISessionCallback {
        override fun onSessionOpenFailed(exception: KakaoException?) {
            Log.e("Log", "Session Call back :: onSessionOpenFailed: ${exception?.message}")
        }

        override fun onSessionOpened() {
            UserManagement.getInstance().me(object : MeV2ResponseCallback() {

                override fun onFailure(errorResult: ErrorResult?) {
                    Log.i("Log", "Session Call back :: on failed ${errorResult?.errorMessage}")
                }

                override fun onSessionClosed(errorResult: ErrorResult?) {
                    Log.i("Log", "Session Call back :: onSessionClosed ${errorResult?.errorMessage}")

                }

                override fun onSuccess(result: MeV2Response?) {
                    Log.i("Log", "아이디 : ${result!!.id}")
                    Log.i("Log", "이메일 : ${result.kakaoAccount.email}")
                    Log.i("Log", "프로필 이미지 : ${result.profileImagePath}")
                    val token = Session.getCurrentSession().accessToken
                    println(token)
                    login(token)

                    checkNotNull(result) { "session response null" }
                }
            })
        }
    }

    fun login(token: String) {
        showProgressDialog()
        val api = LoginAPI.create()

        api.postLogin(RequestLogin(token)).enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                hideProgressDialog()
                val responseLogin = response.body()

                if (responseLogin!!.IsSuccess() && responseLogin.getCode() == 201) {
                    var intent = Intent(this@LoginActivity, JoinActivity::class.java)
                    intent.putExtra("oauthid", responseLogin.getOauthid())
                    startActivity(intent)
                }
                else if (responseLogin!!.IsSuccess() && responseLogin.getCode() == 200) {
                    var intent = Intent(this@LoginActivity, MainActivity::class.java)
                    ApplicationClass.prefs.myEditText = responseLogin.getJwt()
                    showCustomToast(responseLogin.getMessage())
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
            }
        })
    }
}
