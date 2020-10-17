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
import com.vikramezhil.droidspeech.DroidSpeech
import com.vikramezhil.droidspeech.OnDSListener
import java.io.*


class InterviewActivity : BaseActivity(), OnDSListener {

    private val REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE: Int = 10000
    private lateinit var mClRecord: ConstraintLayout
    private lateinit var mLlListen: LinearLayout
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
        mLlListen = findViewById(R.id.interview_ll_listen)
        mEtStt = findViewById(R.id.interview_et_stt)

        mRecordThread = Thread(Runnable {
            val readData = ByteArray(mBufferSize)
            mFilepath = Environment.getExternalStorageDirectory().absolutePath
                .toString() + "/record.pcm"
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(mFilepath)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            while (isRecording) {
                val ret = mAudioRecord!!.read(
                    readData,
                    0,
                    mBufferSize
                ) //  AudioRecord의 read 함수를 통해 pcm data 를 읽어옴
                try {
                    fos!!.write(readData, 0, mBufferSize) //  읽어온 readData 를 파일에 write 함
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            mAudioRecord!!.stop()
            mAudioRecord!!.release()
            mAudioRecord = null
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        })


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

        /*
        mClRecord.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                record()
            }
        })
        mLlListen.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                listen()
            }
        })

         */
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

    private fun startUsingSpeechSDK() {
        var droidSpeech = DroidSpeech(this, null)
        droidSpeech.setOnDroidSpeechListener(this)

        mClRecord.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                if (isRecording) {
                    isRecording = false
                    showCustomToast("녹음이 완료됨")
                    droidSpeech.closeDroidSpeechOperations()
                    mRecordThread!!.interrupt()
                }
                else {
                    isRecording = true
                    showCustomToast("녹음시작함")
                    droidSpeech.startDroidSpeechRecognition()
                    /*
                    mAudioRecord = AudioRecord(
                        mAudioSource,
                        mSampleRate,
                        mChannelCount,
                        mAudioFormat,
                        mBufferSize
                    )
                    mAudioRecord!!.startRecording()
                    mRecordThread!!.start()
                    
                     */
                }
            }
        })
    }

    fun record() {
        if (checkPermission()) {
            if (isRecording) {
                isRecording = false
                showCustomToast("녹음이 완료됨")
                //droidSpeech?.closeDroidSpeechOperations()
            }
            else {
                isRecording = true
                showCustomToast("녹음시작")
                //droidSpeech?.startDroidSpeechRecognition()

                if (mAudioRecord == null) {
                    mAudioRecord = AudioRecord(
                        mAudioSource,
                        mSampleRate,
                        mChannelCount,
                        mAudioFormat,
                        mBufferSize
                    )
                    mAudioRecord!!.startRecording()
                }
                mRecordThread = Thread(Runnable {
                    val readData = ByteArray(mBufferSize)
                    mFilepath = Environment.getExternalStorageDirectory().absolutePath
                        .toString() + "/record.pcm"
                    var fos: FileOutputStream? = null
                    try {
                        fos = FileOutputStream(mFilepath)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                    while (isRecording) {
                        val ret = mAudioRecord!!.read(
                            readData,
                            0,
                            mBufferSize
                        ) //  AudioRecord의 read 함수를 통해 pcm data 를 읽어옴
                        try {
                            fos!!.write(readData, 0, mBufferSize) //  읽어온 readData 를 파일에 write 함
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                    mAudioRecord!!.stop()
                    mAudioRecord!!.release()
                    mAudioRecord = null
                    try {
                        fos!!.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                })
                mRecordThread!!.start()


            }
        }
        else {
            showCustomToast("권한이 없습니다")
        }
    }

    fun listen() {
        if (isPlaying) {
            isPlaying = false
        } else {
            isPlaying = true
            if (mAudioTrack == null) {
                mAudioTrack = AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    mSampleRate,
                    mChannelCount,
                    mAudioFormat,
                    mBufferSize,
                    AudioTrack.MODE_STREAM
                )
            }
            mPlayThread = Thread(Runnable {
                val writeData = ByteArray(mBufferSize)
                var fis: FileInputStream? = null
                try {
                    fis = FileInputStream(mFilepath)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                val dis = DataInputStream(fis)
                mAudioTrack!!.play() // write 하기 전에 play 를 먼저 수행해 주어야 함
                while (isPlaying) {
                    try {
                        val ret: Int = dis.read(writeData, 0, mBufferSize)
                        if (ret <= 0) {
                            this@InterviewActivity.runOnUiThread(Runnable
                            // UI 컨트롤을 위해
                            {
                                isPlaying = false
                            })
                            break
                        }
                        mAudioTrack!!.write(writeData, 0, ret) // AudioTrack 에 write 를 하면 스피커로 송출됨
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                mAudioTrack!!.stop()
                mAudioTrack!!.release()
                mAudioTrack = null
                try {
                    dis.close()
                    fis!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            })
            mPlayThread!!.start()
        }
    }

    private fun checkPermission() : Boolean {
        val TAG = "Storage Permission"
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.RECORD_AUDIO) === PackageManager.PERMISSION_GRANTED
            ) {
                Log.v(TAG, "Permission is granted")
                true
            } else {
                Log.v(TAG, "Permission is revoked")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO),
                    1
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted")
            true
        }
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
        mEtStt.setText(resultString + liveSpeechResult)
        println("호출")
    }

    override fun onDroidSpeechFinalResult(finalSpeechResult: String?) {
        resultString += finalSpeechResult + "\n"
        mEtStt.setText(resultString)
        println("호출호출")
    }
}