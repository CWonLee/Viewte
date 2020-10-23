package com.makeus.blue.viewte.src.add_interview

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.main.MainActivity
import com.makeus.blue.viewte.src.main.interfaces.SearchAPI
import com.makeus.blue.viewte.src.main.models.ResponseSearch
import com.makeus.blue.viewte.src.record.RecordActivity
import com.makeus.blue.viewte.src.search_result.SearchResultActivity
import com.makeus.blue.viewte.src.setting.SettingActivity
import com.makeus.blue.viewte.src.trash.TrashActivity
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.Holder
import com.orhanobut.dialogplus.ViewHolder
import kotlinx.android.synthetic.main.activity_add_interview1.*
import kotlinx.android.synthetic.main.activity_add_interview2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AddInterview2Activity : BaseActivity() {

    private lateinit var mTvDate: TextView
    private lateinit var mCalendarView: CalendarView
    private lateinit var mWeekDay: String
    private lateinit var mMonth: String
    private lateinit var mDay: String
    private lateinit var mIvBack: ImageView
    private lateinit var mBtnNext: Button
    private lateinit var mYear: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_interview2)

        mTvDate = findViewById(R.id.add_interview2_tv_date)
        mCalendarView = findViewById(R.id.add_interview2_calendar)
        mIvBack = findViewById(R.id.add_interview2_iv_back)
        mBtnNext = findViewById(R.id.add_interview2_btn_next)

        add_interview2_cl_home.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview2Activity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        })
        add_interview2_cl_search.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                showSearchDialog()
            }
        })
        add_interview2_cl_mic.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview2Activity, RecordActivity::class.java)
                startActivity(intent)
            }
        })
        add_interview2_cl_trash.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview2Activity, TrashActivity::class.java)
                startActivity(intent)
            }
        })
        add_interview2_cl_setting.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview2Activity, SettingActivity::class.java)
                startActivity(intent)
            }
        })

        val currentTime = Calendar.getInstance().time
        val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        val weekdayFormat = SimpleDateFormat("EE", Locale.getDefault())
        val dayFormat = SimpleDateFormat("dd", Locale.getDefault())
        val monthFormat = SimpleDateFormat("MM", Locale.getDefault())


        mWeekDay = weekdayFormat.format(currentTime)
        mMonth = monthFormat.format(currentTime)
        mDay = dayFormat.format(currentTime)
        mYear = yearFormat.format(currentTime)

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

        mBtnNext.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview2Activity, AddInterview3Activity::class.java)

                var dateString: String = "$mYear-$mMonth-$mDay"

                intent.putExtra("categoriesNo", getIntent().getIntExtra("categoriesNo", 0))
                intent.putExtra("i_title", getIntent().getStringExtra("i_title"))
                intent.putExtra("purpose", getIntent().getStringExtra("purpose"))
                intent.putExtra("date", dateString)
                intent.putExtra("imageUrl", getIntent().getStringExtra("imageUrl"))

                startActivity(intent)
            }
        })
    }

    private fun showSearchDialog() {
        val searchGravity: Int = Gravity.TOP
        val holder: Holder

        holder = ViewHolder(R.layout.dialog_search)

        val builder = DialogPlus.newDialog(this).apply {
            setContentHolder(holder)
            isCancelable = true
            setGravity(searchGravity)
            setOnClickListener { dialog, view ->
                var editText: EditText = dialog.holderView.findViewById(R.id.search_edit_text)

                if (view.id == R.id.search_iv) {
                    if (editText.text.toString() != "") {
                        getSearch(editText.text.toString())
                    }
                }
            }
        }
        builder.create().show()
    }

    private fun getSearch(content: String) {
        val api = SearchAPI.create()

        api.getSearch(content).enqueue(object : Callback<ResponseSearch> {
            override fun onResponse(call: Call<ResponseSearch>, response: Response<ResponseSearch>) {
                var responseSearch = response.body()

                if (responseSearch!!.IsSuccess() && responseSearch.getCode() == 200) {

                    var intent = Intent(this@AddInterview2Activity, SearchResultActivity::class.java)
                    intent.putExtra("SearchResult", responseSearch.getResult())
                    startActivity(intent)
                }
                else {
                    hideProgressDialog()
                    showCustomToast(responseSearch.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
            }
        })
    }
}
