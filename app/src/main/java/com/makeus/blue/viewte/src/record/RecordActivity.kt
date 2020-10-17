package com.makeus.blue.viewte.src.record

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.kakao.sdk.newtoneapi.SpeechRecognizeListener
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import com.vikramezhil.droidspeech.DroidSpeech
import com.vikramezhil.droidspeech.OnDSListener


class RecordActivity : BaseActivity(), OnDSListener {

    private val REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE: Int = 10000
    private lateinit var mClStart: ConstraintLayout
    private lateinit var mEtSTT: EditText
    var play: Boolean = false
    var resultString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        mClStart = findViewById(R.id.record_cl_start)
        mEtSTT = findViewById(R.id.record_et)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE)
            }
            else {
                showCustomToast("권한이 없습니다")
            }
        }
        else {
            startUsingSpeechSDK()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.size > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startUsingSpeechSDK()
                } else {
                    finish()
                }
            }
        }
    }


    private fun startUsingSpeechSDK(){
        //SDK 초기화
        SpeechRecognizerManager.getInstance().initializeLibrary(this)
        val droidSpeech = DroidSpeech(this, null)
        droidSpeech.setOnDroidSpeechListener(this)

        mClStart.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                if (play) {
                    showCustomToast("음성인식 끝")
                    play = false
                    droidSpeech.closeDroidSpeechOperations()
                }
                else {
                    showCustomToast("음성인식 시작")
                    play = true
                    droidSpeech.startDroidSpeechRecognition()
                }
            }
        })
    }

    override fun onDroidSpeechRmsChanged(rmsChangedValue: Float) {

    }

    override fun onDroidSpeechSupportedLanguages(
        currentSpeechLanguage: String?,
        supportedSpeechLanguages: MutableList<String>?
    ) {

    }

    override fun onDroidSpeechError(errorMsg: String?) {

    }

    override fun onDroidSpeechClosedByUser() {
    }

    override fun onDroidSpeechLiveResult(liveSpeechResult: String?) {
        mEtSTT.setText(resultString + liveSpeechResult)
    }

    override fun onDroidSpeechFinalResult(finalSpeechResult: String?) {
        resultString += finalSpeechResult + "\n"
        mEtSTT.setText(resultString)
    }
}
