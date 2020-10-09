package com.makeus.blue.viewte.src.add_interview

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.roundToInt

class AddInterview5Activity : BaseActivity() {

    private lateinit var mQuestionArea:  LinearLayout
    private lateinit var mAddQuestion:  LinearLayout
    private var mQuestionCnt: Int = 1
    private val sNextGeneratedId =
        AtomicInteger(1)
    private var mQuestionEtId: ArrayList<Int> = ArrayList()
    private lateinit var mTvQuestionCnt: TextView
    private lateinit var mBtnNext: Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_interview5)

        mQuestionArea = findViewById(R.id.add_interview5_ll_question_area)
        mAddQuestion = findViewById(R.id.add_interview5_ll_add_question)
        mTvQuestionCnt = findViewById(R.id.add_interview5_tv_question_count)
        mBtnNext = findViewById(R.id.add_interview5_btn_next)

        mAddQuestion.setOnClickListener {
            addQuestion()
        }

        mBtnNext.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var list : ArrayList<String> = ArrayList()
                var bo : Boolean = true
                for (i in mQuestionEtId) {
                    var editText : EditText = findViewById(i)
                    if (editText.text.toString() == "") {
                        bo = false
                        break
                    }
                    list.add(editText.text.toString())
                }
                if (mQuestionEtId.isEmpty()){
                    bo = false
                }
                if (bo) {
                    var intent = Intent(this@AddInterview5Activity, AddInterview6Activity::class.java)
                    intent.putStringArrayListExtra("questionList", list)
                    startActivity(intent)
                }
                else {
                    showCustomToast("질문을 입력해주세요")
                }
            }
        })
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun addQuestion() {
        val dm = resources.displayMetrics

        val rootLinearLayout = LinearLayout(this)
        rootLinearLayout.id = generateViewId()
        rootLinearLayout.orientation = LinearLayout.VERTICAL
        val paramsRoot = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        paramsRoot.topMargin = (27 * dm.density).roundToInt()

        val firstTv = TextView(this)
        firstTv.id = generateViewId()
        firstTv.textSize = 12F
        firstTv.text = "질문 $mQuestionCnt"
        firstTv.setTextColor(resources.getColor(R.color.colorWhite))
        val typeFace = resources.getFont(R.font.notosanskr_regular)
        firstTv.typeface = typeFace
        firstTv.includeFontPadding = false
        val paramsFirstTv = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        rootLinearLayout.addView(firstTv, paramsFirstTv)

        var newContext= ContextThemeWrapper(baseContext, R.style.EditTheme);
        var firstEt = EditText(newContext)
        firstEt.id = generateViewId()
        mQuestionEtId.add(firstEt.id)
        firstEt.textSize = 16F
        firstEt.setTextColor(resources.getColor(R.color.colorWhite))
        firstEt.typeface = typeFace
        firstEt.includeFontPadding = false
        firstEt.inputType = InputType.TYPE_CLASS_TEXT
        val paramsFirstEt = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        rootLinearLayout.addView(firstEt, paramsFirstEt)

        var secondTv = TextView(this)
        secondTv.id = generateViewId()
        secondTv.textSize = 12F
        secondTv.text = "자기소개를 부탁드리면 늘 좋은 인사이트가 나와요!"
        secondTv.setTextColor(resources.getColor(R.color.colorWhite))
        secondTv.typeface = typeFace
        secondTv.includeFontPadding = false
        val paramsSecondTv = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        rootLinearLayout.addView(secondTv, paramsSecondTv)

        mQuestionArea.addView(rootLinearLayout, paramsRoot)

        mQuestionCnt++
        mTvQuestionCnt.text = "질문 $mQuestionCnt"
    }

    private fun generateViewId(): Int {
        while (true) {
            val result: Int =
                sNextGeneratedId.get()
            var newValue = result + 1
            if (newValue > 0x00FFFFFF) newValue = 1
            if (sNextGeneratedId.compareAndSet(
                    result,
                    newValue
                )
            ) {
                return result
            }
        }
    }
}
