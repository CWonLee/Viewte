package com.makeus.blue.viewte.src.record

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kakao.sdk.newtoneapi.SpeechRecognizeListener
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity

class RecordActivity : BaseActivity() {

    private val REQUEST_CODE_AUDIO_AND_WRITE_EXTERNAL_STORAGE: Int = 10000
    private lateinit var mClStart: ConstraintLayout
    private lateinit var mEtSTT: EditText

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

        mClStart.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                // 소리재생
                /*
                mediaPlayer = MediaPlayer.create(this, R.raw.android_latest)
                mediaPlayer?.start()

                 */

                //클라이언트 생성
                val builder = SpeechRecognizerClient.Builder().setServiceType(SpeechRecognizerClient.SERVICE_TYPE_WEB)
                val client = builder.build()

                //Callback
                client.setSpeechRecognizeListener(object : SpeechRecognizeListener {
                    //콜백함수들
                    override fun onReady() {
                        Handler(Looper.getMainLooper()).postDelayed({
                            showCustomToast("녹음을 시작합니다")
                        }, 0)
                        /*
                        Handler mHandler = new Handler(Looper.getMainLooper());
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // 사용하고자 하는 코드
                            }
                        }, 0);

                         */
                        println("모든 하드웨어 및 오디오 서비스가 준비되었습니다.")
                    }

                    override fun onBeginningOfSpeech() {
                        println("사용자가 말을 하기 시작했습니다.")
                    }

                    override fun onEndOfSpeech() {
                        Handler(Looper.getMainLooper()).postDelayed({
                            showCustomToast("녹음이 끝났습니다")
                        }, 0)
                        println("사용자의 말하기가 끝이 났습니다. 데이터를 서버로 전달합니다.")
                    }

                    override fun onPartialResult(partialResult: String?) {
                        //현재 인식된 음성테이터 문자열을 출력해 준다. 여러번 호출됨. 필요에 따라 사용하면 됨.
                        //Log.d(TAG, "현재까지 인식된 문자열:" + partialResult)
                        mEtSTT.setText(partialResult)
                    }

                    /*
                    최종결과 - 음성입력이 종료 혹은 stopRecording()이 호출되고 서버에 질의가 완료되고 나서 호출됨
                    Bundle에 ArrayList로 값을 받음. 신뢰도가 높음 것 부터...
                     */
                    override fun onResults(results: Bundle?) {
                        val texts = results?.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS)
                        val confs = results?.getIntegerArrayList(SpeechRecognizerClient.KEY_CONFIDENCE_VALUES)

                        //정확도가 높은 첫번째 결과값을 텍스트뷰에 출력
                        runOnUiThread {
                            //mEtSTT.setText(texts?.get(0))
                        }


                    }

                    override fun onAudioLevel(audioLevel: Float) {
                        //Log.d(TAG, "Audio Level(0~1): " + audioLevel.toString())
                    }

                    override fun onError(errorCode: Int, errorMsg: String?) {
                        //에러 출력 해 봄
                        println("Error: " + errorMsg)
                    }
                    override fun onFinished() {
                    }
                })

                //음성인식 시작함
                client.startRecording(true)
            }
        })
    }
}
