package com.makeus.blue.viewte.src.record

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity


class RecordActivity : BaseActivity(), RecognitionListener {

    private val REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE: Int = 10000
    private val PERMISSIONS_REQUEST_RECORD_AUDIO = 1
    private lateinit var mClStart: ConstraintLayout
    private lateinit var mEtSTT: EditText
    var play: Boolean = false
    var mResultString: String = ""
    private var speech: SpeechRecognizer? = null
    private var recognizerIntent: Intent? = null
    private val LOG_TAG = "VoiceRecognitionActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        mClStart = findViewById(R.id.record_cl_start)
        mEtSTT = findViewById(R.id.record_et)

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

        setRecogniserIntent();
        speech!!.startListening(recognizerIntent)
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
        mEtSTT.setText(mResultString + text)
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
        mResultString += text
        mEtSTT.setText(mResultString)
        speech!!.startListening(recognizerIntent)
    }
}
