package com.makeus.blue.viewte.src.interview

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
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
import com.makeus.blue.viewte.src.interview.interfaces.QuestionAPI
import com.makeus.blue.viewte.src.interview.models.RequestQuestion
import com.makeus.blue.viewte.src.interview.models.RequestQuestionInfo
import com.makeus.blue.viewte.src.interview.models.ResponseQuestion
import com.makeus.blue.viewte.src.introduce.IntroduceActivity
import com.makeus.blue.viewte.src.is_add_question.IsAddQuestionActivity
import com.makeus.blue.viewte.src.main.MainActivity
import com.makeus.blue.viewte.src.prev_interview.models.ResponseInterviewResultQuestion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*


class InterviewActivity : BaseActivity(), RecognitionListener {

    private var mPage : Int = 0
    private lateinit var mTvQuestion : TextView
    private lateinit var mTvPage : TextView
    private lateinit var mTvTitle : TextView
    private lateinit var mTvDate : TextView
    private lateinit var mLlPrev : LinearLayout
    private lateinit var mLlNext : LinearLayout
    private lateinit var mEtSTT : EditText
    private lateinit var mLlReply : LinearLayout
    private lateinit var mClRecord: ConstraintLayout
    private lateinit var mIvPlay: ImageView
    private lateinit var mQuestionList : ArrayList<ResponseInterviewResultQuestion>
    private var mInterviewNo:Int = 0
    private var mInterviewTitle : String = ""
    private var mInterviewDate : String = ""
    private var mPlay : Boolean = false
    private val PERMISSIONS_REQUEST_RECORD_AUDIO = 1
    private var mResultString: ArrayList<String> = ArrayList()
    private var speech: SpeechRecognizer? = null
    private var recognizerIntent: Intent? = null
    private lateinit var mScrollView: ScrollView
    private var mIsPlus: Int = 0
    private var mPlusAnswer = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interview)

        mTvQuestion = findViewById(R.id.interview_tv_question)
        mTvPage = findViewById(R.id.interview_tv_page)
        mLlPrev = findViewById(R.id.interview_ll_prev)
        mLlNext = findViewById(R.id.interview_ll_next)
        mEtSTT = findViewById(R.id.interview_et_stt)
        mLlReply = findViewById(R.id.interview_ll_reply)
        mClRecord = findViewById(R.id.interview_cl_record)
        mTvTitle = findViewById(R.id.interview_tv_title)
        mTvDate = findViewById(R.id.interview_tv_date)
        mScrollView = findViewById(R.id.interview_scrollview)
        mIvPlay = findViewById(R.id.interview_iv_mic)

        init()

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

        mClRecord.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                if (!mPlay) {
                    mIvPlay.setImageResource(R.drawable.ic_stop)
                    showCustomToast("음성인식 시작")
                    mPlay = true
                    mEtSTT.isEnabled = false
                    setRecogniserIntent();
                    speech!!.startListening(recognizerIntent)
                }
                else {
                    mIvPlay.setImageResource(R.drawable.ic_record_mic)
                    showCustomToast("음성인식 완료")
                    mPlay = false
                    mEtSTT.isEnabled = true
                }
            }
        })

        mEtSTT.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (mPlay) {
                    mScrollView.post{
                        mScrollView.fullScroll(View.FOCUS_DOWN)
                    }
                }
                else {
                    if (mIsPlus == 1) {
                        mPlusAnswer = s.toString()
                    }
                    else {
                        mResultString[mPage] = s.toString()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        mLlReply.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                mIvPlay.setImageResource(R.drawable.ic_record_mic)
                showCustomToast("초기화")
                mPlay = false
                mEtSTT.isEnabled = true
                if (mIsPlus == 1) {
                    mPlusAnswer = ""
                }
                else {
                    mResultString[mPage] = ""
                }
                mEtSTT.setText("")
            }
        })

        mLlNext.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                if (mIsPlus == 0) {
                    if (!mPlay && mPage < mQuestionList.size - 1) {
                        mPage++
                        mTvPage.text = (mPage + 1).toString() + "/" + mQuestionList.size
                        mTvQuestion.text = mQuestionList[mPage].getQuestion()
                        mEtSTT.setText(mResultString[mPage])
                    }
                    else if (!mPlay && mPage == mQuestionList.size - 1) {
                        var bo = true
                        for (i in mResultString) {
                            if (i == "") {
                                bo = false
                                break
                            }
                        }
                        if (!bo) {
                            showCustomToast("모든 질문을 완료해주세요")
                        }
                        else {
                            var nextIntent = Intent(this@InterviewActivity, IsAddQuestionActivity::class.java)
                            nextIntent.putExtra("questionList", intent.getSerializableExtra("questionList") as ArrayList<ResponseInterviewResultQuestion>)
                            nextIntent.putExtra("interviewNo", intent.getIntExtra("interviewNo", 0))
                            nextIntent.putExtra("interviewTitle", intent.getStringExtra("interviewTitle"))
                            nextIntent.putExtra("interviewDate", intent.getStringExtra("interviewDate"))
                            nextIntent.putExtra("answerList", mResultString)
                            startActivity(nextIntent)
                        }
                    }
                    else if (mPlay) {
                        showCustomToast("녹음을 완료해주세요")
                    }
                }
                else {
                    if (mPlusAnswer == "") {
                        showCustomToast("모든 질문을 완료해주세요")
                    }
                    else {
                        postQuestion()
                    }
                }
            }
        })

        mLlPrev.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                if (!mPlay && mPage > 0) {
                    mPage--
                    mTvPage.text = (mPage + 1).toString() + "/" + mQuestionList.size
                    mTvQuestion.text = mQuestionList[mPage].getQuestion()
                    mEtSTT.setText(mResultString[mPage])
                }
                else if (mPlay) {
                    showCustomToast("녹음을 완료해주세요")
                }
            }
        })
    }

    private fun init() {
        mIsPlus = intent.getIntExtra("isPlus", 0)

        if (mIsPlus == 1) {
            mQuestionList = intent.getSerializableExtra("questionList") as ArrayList<ResponseInterviewResultQuestion>
            mInterviewNo = intent.getIntExtra("interviewNo", 0)
            mInterviewTitle = intent.getStringExtra("interviewTitle")
            mInterviewDate = intent.getStringExtra("interviewDate")
            mResultString = intent.getStringArrayListExtra("answerList")

            mTvQuestion.text = "추가질문"
            mTvPage.text = mQuestionList.size.toString() + "+1"
            mLlPrev.visibility = View.GONE
            mTvTitle.text = mInterviewTitle
            mTvDate.text = mInterviewDate
        }
        else {
            mQuestionList = intent.getSerializableExtra("questionList") as ArrayList<ResponseInterviewResultQuestion>
            mInterviewNo = intent.getIntExtra("interviewNo", 0)
            mInterviewTitle = intent.getStringExtra("interviewTitle")
            mInterviewDate = intent.getStringExtra("interviewDate")

            mTvQuestion.text = mQuestionList[0].getQuestion()
            mTvPage.text = "1/" + mQuestionList.size
            mTvTitle.text = mInterviewTitle
            mTvDate.text = mInterviewDate

            for (i in 1..mQuestionList.size) {
                mResultString.add("")
            }
        }
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
                finish()
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
        if (mIsPlus == 0) {
            if (mPlay) {
                mResultString[mPage] += "\n" + text
                mEtSTT.setText(mResultString[mPage])
            }
        }
        else {
            if (mPlay) {
                mPlusAnswer += "\n" + text
                mEtSTT.setText(mPlusAnswer)
            }
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
        resetSpeechRecognizer();
        speech!!.startListening(recognizerIntent);
    }

    override fun onResults(results: Bundle?) {
        val matches = results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        var text = matches!![0]

        if (mIsPlus == 0) {
            if (mPlay) {
                mResultString[mPage] += "\n" + text
                mEtSTT.setText(mResultString[mPage])
                speech!!.startListening(recognizerIntent)
            }
        }
        else {
            if (mPlay) {
                mPlusAnswer += "\n" + text
                mEtSTT.setText(mPlusAnswer)
                speech!!.startListening(recognizerIntent)
            }
        }
    }

    private fun postQuestion() {
        showProgressDialog()
        val api = QuestionAPI.create()

        var arraylist : ArrayList<RequestQuestionInfo> = ArrayList<RequestQuestionInfo>()
        for ((cnt, i) in (intent.getSerializableExtra("questionList") as ArrayList<ResponseInterviewResultQuestion>).withIndex()) {
            arraylist.add(RequestQuestionInfo(i.getQuestion(), intent.getStringArrayListExtra("answerList")[cnt]))
        }
        arraylist.add(RequestQuestionInfo("추가질문", mPlusAnswer))

        api.postQuestion(RequestQuestion(intent.getIntExtra("interviewNo", 0), arraylist)).enqueue(object :
            Callback<ResponseQuestion> {
            override fun onResponse(call: Call<ResponseQuestion>, response: Response<ResponseQuestion>) {
                var responseAddCategory = response.body()

                if (responseAddCategory!!.IsSuccess() && responseAddCategory.getCode() == 200) {

                    showCustomToast(responseAddCategory.getMessage())

                    var nextIntent = Intent(this@InterviewActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(nextIntent)
                }
                else {
                    hideProgressDialog()
                    showCustomToast(responseAddCategory.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponseQuestion>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
            }
        })
    }
}