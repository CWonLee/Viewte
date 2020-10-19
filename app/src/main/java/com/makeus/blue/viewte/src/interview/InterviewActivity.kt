package com.makeus.blue.viewte.src.interview

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import java.io.*


class InterviewActivity : BaseActivity() {

    private val REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE: Int = 10000
    private lateinit var mClRecord: ConstraintLayout
    private lateinit var mEtStt: EditText
    private val mAudioSource = MediaRecorder.AudioSource.MIC
    private val mSampleRate = 44100
    private val mChannelCount: Int = android.media.AudioFormat.CHANNEL_IN_STEREO
    private val mAudioFormat: Int = android.media.AudioFormat.ENCODING_PCM_16BIT
    private val mBufferSize =
        AudioTrack.getMinBufferSize(mSampleRate, mChannelCount, mAudioFormat)
    private var mAudioRecord: AudioRecord? = null
    private var mAudioTrack: AudioTrack? = null
    private var mRecordThread: Thread? = null
    private var mPlayThread: Thread? = null
    private var isRecording = false
    private var isPlaying = false
    private lateinit var mFilepath: String
    private var resultString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interview)

        mClRecord = findViewById(R.id.interview_cl_record)
        mEtStt = findViewById(R.id.interview_et_stt)
    }
}