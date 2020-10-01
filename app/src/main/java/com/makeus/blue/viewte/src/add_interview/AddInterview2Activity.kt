package com.makeus.blue.viewte.src.add_interview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import java.text.SimpleDateFormat
import java.util.*

class AddInterview2Activity : BaseActivity() {

    private lateinit var mTvDate: TextView
    private lateinit var mCalendarView: CalendarView
    private lateinit var mWeekDay: String
    private lateinit var mMonth: String
    private lateinit var mDay: String
    private lateinit var mIvBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_interview2)

        mTvDate = findViewById(R.id.add_interview2_tv_date)
        mCalendarView = findViewById(R.id.add_interview2_calendar)
        mIvBack = findViewById(R.id.add_interview2_iv_back)

        val currentTime = Calendar.getInstance().time
        val weekdayFormat = SimpleDateFormat("EE", Locale.getDefault())
        val dayFormat = SimpleDateFormat("dd", Locale.getDefault())
        val monthFormat = SimpleDateFormat("MM", Locale.getDefault())

        mWeekDay = weekdayFormat.format(currentTime)
        mMonth = monthFormat.format(currentTime)
        mDay = dayFormat.format(currentTime)

        mTvDate.text = mMonth + "월" + mDay + "일" + mWeekDay + "요일"

        mCalendarView.setOnDayClickListener(object : OnDayClickListener {
            override fun onDayClick(eventDay: EventDay) {
                val calendar = eventDay.calendar
                val date = calendar.time
                when {
                    date.day == 0 -> mWeekDay = "일"
                    date.day == 1 -> mWeekDay = "월"
                    date.day == 2 -> mWeekDay = "화"
                    date.day == 3 -> mWeekDay = "수"
                    date.day == 4 -> mWeekDay = "목"
                    date.day == 5 -> mWeekDay = "금"
                    date.day == 6 -> mWeekDay = "토"
                }
                mMonth = (date.month + 1).toString()
                mDay = date.date.toString()

                mTvDate.text = mMonth + "월" + mDay + "일" + mWeekDay + "요일"
            }
        })

        mIvBack.setOnClickListener(object: OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                finish()
            }
        })
    }
}
