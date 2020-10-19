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
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity


class RecordActivity : BaseActivity(), RecognitionListener {

    private val PERMISSIONS_REQUEST_RECORD_AUDIO = 1
    private lateinit var mClStart: ConstraintLayout
    private lateinit var mEtSTT: EditText
    var play: Boolean = false
    var mResultString: String = ""
    private var speech: SpeechRecognizer? = null
    private var recognizerIntent: Intent? = null
    private val LOG_TAG = "VoiceRecognitionActivity"
    private lateinit var mIvPlay : ImageView
    private lateinit var mScrollView: ScrollView
    private lateinit var mllRefresh: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        mClStart = findViewById(R.id.record_cl_start)
        mEtSTT = findViewById(R.id.record_et)
        mIvPlay = findViewById(R.id.record_iv_play)
        mScrollView = findViewById(R.id.record_scroll)
        mllRefresh = findViewById(R.id.record_ll_refresh)

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

    @SuppressLint("LongLogTag")
    private fun resetSpeechRecognizer() {
        if (speech != null) speech!!.destroy()
        speech = SpeechRecognizer.createSpeechRecognizer(this)
        Log.i(
            LOG_TAG,
            "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(this)
        )
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

        println(text)
        mEtSTT.setText(mResultString + "\n" + text)

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


        println(text)
        mResultString += "\n" + text
        mEtSTT.setText(mResultString)
        if (play) {
            speech!!.startListening(recognizerIntent)
        }
        else {
            //mIvPlay.setImageResource(R.drawable.ic_record_mic)
        }
    }
}
