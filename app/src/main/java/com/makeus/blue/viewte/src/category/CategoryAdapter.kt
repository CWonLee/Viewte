package com.makeus.blue.viewte.src.category

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions.with
import com.bumptech.glide.Glide.with
import com.bumptech.glide.request.RequestOptions
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.GlideApp
import com.makeus.blue.viewte.src.GlideApp.with
import com.makeus.blue.viewte.src.MyGlideApp
import com.makeus.blue.viewte.src.category.models.ResponseInterviewResult
import com.makeus.blue.viewte.src.interview_detail.InterviewDetailActivity
import com.makeus.blue.viewte.src.prev_interview.PrevInterviewActivity
import kotlinx.android.synthetic.main.item_category_recycler.view.*


class CategoryAdapter(private val items: ArrayList<ResponseInterviewResult>, private val context: Context, private val categoryNum: Int): RecyclerView.Adapter<CategoryAdapter.ViewHolder>()  {

    //아이템의 갯수
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_category_recycler, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position])

        holder.itemView.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                if (items[position].getIsMemo() == 'Y') {
                    val intent = Intent(context, InterviewDetailActivity::class.java)
                    intent.putExtra("categoriesNo", categoryNum)
                    intent.putExtra("interviewNo", items[position].getInterviewNo())
                    context.startActivity(intent)
                }
                else {
                    val intent = Intent(context, PrevInterviewActivity::class.java)
                    intent.putExtra("categoriesNo", categoryNum)
                    intent.putExtra("interviewNo", items[position].getInterviewNo())
                    context.startActivity(intent)
                }
            }
        })
    }


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bindItems(data : ResponseInterviewResult){
            itemView.item_category_tv_title.text = data.getITitle()
            var date = data.getDate()
            var newDate: String = ""
            newDate = date[0].toString() + date[1] + date[2] + date[3] + "." + date[5] + date[6] + "." + date[8] + date[9] + " "
            when {
                data.getWeek() == 0 -> {
                    newDate += "월요일"
                }
                data.getWeek() == 1 -> {
                    newDate += "화요일"
                }
                data.getWeek() == 2 -> {
                    newDate += "수요일"
                }
                data.getWeek() == 3 -> {
                    newDate += "목요일"
                }
                data.getWeek() == 4 -> {
                    newDate += "금요일"
                }
                data.getWeek() == 5 -> {
                    newDate += "토요일"
                }
                data.getWeek() == 6 -> {
                    newDate += "일요일"
                }
            }
            itemView.item_category_tv_date.text = newDate
            if (data.getIsMemo() == 'N') {
                itemView.item_category_iv_mic.visibility = View.GONE
            }
            
            
            GlideApp.with(context).load(data.getImageUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(itemView.item_category_iv)
        }
    }

    abstract inner class OnSingleClickListener : View.OnClickListener {

        //마지막으로 클릭한 시간
        private var mLastClickTime: Long = 0
        //중복클릭시간차이
        private val MIN_CLICK_INTERVAL: Long = 1000

        abstract fun onSingleClick(v: View)

        override fun onClick(v: View) {
            //현재 클릭한 시간
            val currentClickTime = SystemClock.uptimeMillis()
            //이전에 클릭한 시간과 현재시간의 차이
            val elapsedTime = currentClickTime - mLastClickTime
            //마지막클릭시간 업데이트
            mLastClickTime = currentClickTime

            //내가 정한 중복클릭시간 차이를 안넘었으면 클릭이벤트 발생못하게 return
            if (elapsedTime <= MIN_CLICK_INTERVAL)
                return
            //중복클릭시간 아니면 이벤트 발생
            onSingleClick(v)
        }
    }
}