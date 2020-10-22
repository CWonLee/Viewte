package com.makeus.blue.viewte.src.record

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.login.LoginActivity
import com.makeus.blue.viewte.src.main.MainActivity
import com.makeus.blue.viewte.src.main.interfaces.AddCategoryAPI
import com.makeus.blue.viewte.src.main.models.RequestAddCategory
import com.makeus.blue.viewte.src.main.models.ResponseAddCategory
import com.makeus.blue.viewte.src.record.interfaces.RecordAPI
import com.makeus.blue.viewte.src.record.models.RequestRecord
import com.makeus.blue.viewte.src.record.models.ResponseRecord
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RecordActivity : BaseActivity(), RecognitionListener {

    private val PERMISSIONS_REQUEST_RECORD_AUDIO = 1
    private lateinit var mClStart: ConstraintLayout
    private lateinit var mEtSTT: EditText
    var play: Boolean = false
    var mResultString: String = ""
    private var speech: SpeechRecognizer? = null
    private var recognizerIntent: Intent? = null
    private lateinit var mIvPlay : ImageView
    private lateinit var mScrollView: ScrollView
    private lateinit var mllRefresh: LinearLayout
    private lateinit var mTvSave: TextView
    private lateinit var mEtTitle: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        mClStart = findViewById(R.id.record_cl_start)
        mEtSTT = findViewById(R.id.record_et)
        mIvPlay = findViewById(R.id.record_iv_play)
        mScrollView = findViewById(R.id.record_scroll)
        mllRefresh = findViewById(R.id.record_ll_refresh)
        mTvSave = findViewById(R.id.record_tv_save)
        mEtTitle = findViewById(R.id.record_et_title)

        // start speech recogniser
        resetSpeechRecognizer()
        // check for permission

        // check for permission
        val permissionCheck = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.RECORD_AUDIO
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                PERMISSIONS_REQUEST_RECORD_AUDIO
            )
            return
        }

        mTvSave.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                if (play) {
                    showCustomToast("녹음을 완료해주세요")
                }
                else {
                    if (mEtSTT.text.toString() == "") {
                        showCustomToast("녹음을 완료해주세요")    
                    }
                    else {
                        if (mEtTitle.text.toString() == "") {
                            showCustomToast("제목을 입력해주세요")
                        }
                        else {
                            postRecord()
                        }
                    }
                }
            }
        })
        mClStart.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                if (!play) {
                    mIvPlay.setImageResource(R.drawable.ic_stop)
                    showCustomToast("음성인식 시작")
                    play = true
                    mEtSTT.isEnabled = false
                    setRecogniserIntent();
                    speech!!.startListening(recognizerIntent)
                }
                else {
                    mIvPlay.setImageResource(R.drawable.ic_record_mic)
                    showCustomToast("음성인식 완료")
                    play = false
                    mEtSTT.isEnabled = true
                }
            }
        })

        mEtSTT.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (play) {
                    mScrollView.post{
                        mScrollView.fullScroll(View.FOCUS_DOWN)
                    }
                }
                else {
                    mResultString = s.toString()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        mllRefresh.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                mIvPlay.setImageResource(R.drawable.ic_record_mic)
                showCustomToast("초기화")
                play = false
                mEtSTT.isEnabled = true
                mResultString = ""
                mEtSTT.setText("")
            }
        })
    }

    private fun resetSpeechRecognizer() {
        if (speech != null) speech!!.destroy()
        speech = SpeechRecognizer.createSpeechRecognizer(this)
        if (SpeechRecognizer.isRecognitionAvailable(this))
            speech!!.setRecognitionListener(this)
        else finish()
    }

    private fun setRecogniserIntent() {
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent!!.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
            "en"
        )
        recognizerIntent!!.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        recognizerIntent!!.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                speech!!.startListening(recognizerIntent);
            } else {
                showCustomToast("Permission Denied!")
                finish();
            }
        }
    }

    override fun onReadyForSpeech(params: Bundle?) {

    }

    override fun onRmsChanged(rmsdB: Float) {

    }

    override fun onBufferReceived(buffer: ByteArray?) {

    }

    override fun onPartialResults(partialResults: Bundle?) {
        val matches = partialResults
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        var text = ""
        text = matches!![0]

        if (play) {
            mResultString += "\n" + text
            mEtSTT.setText(mResultString)
        }
    }

    override fun onEvent(eventType: Int, params: Bundle?) {

    }

    override fun onBeginningOfSpeech() {

    }

    override fun onEndOfSpeech() {
        speech!!.stopListening()
    }

    override fun onError(error: Int) {
        // rest voice recogniser
        resetSpeechRecognizer();
        speech!!.startListening(recognizerIntent);
    }

    override fun onResults(results: Bundle?) {
        val matches = results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        var text = matches!![0]

        if (play) {
            mResultString += "\n" + text
            mEtSTT.setText(mResultString)
            speech!!.startListening(recognizerIntent)
        }
    }

    private fun postRecord() {
        showProgressDialog()

        val api = RecordAPI.create()

        api.postRecord(RequestRecord(mEtTitle.text.toString(), mEtSTT.text.toString())).enqueue(object :
            Callback<ResponseRecord> {
            override fun onResponse(call: Call<ResponseRecord>, response: Response<ResponseRecord>) {
                var responseRecord = response.body()

                hideProgressDialog()
                if (responseRecord!!.IsSuccess() && responseRecord.getCode() == 200) {
                    showCustomToast(responseRecord.getMessage())

                    var intent = Intent(this@RecordActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                else {
                    hideProgressDialog()
                    showCustomToast(responseRecord.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponseRecord>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
            }
        })
    }
}
