package com.makeus.blue.viewte.src.record_list

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.record_detail.RecordDetailActivity
import com.makeus.blue.viewte.src.record_list.interfaces.RecordListActivityView
import com.makeus.blue.viewte.src.record_list.models.ResponseGetRecordListResult
import kotlinx.android.synthetic.main.item_record_list_recycler.view.*

class RecordListRecyclerAdapter(private val items: ArrayList<ResponseGetRecordListResult>, private val context: Context, private val recordListActivityView: RecordListActivityView): RecyclerView.Adapter<RecordListRecyclerAdapter.ViewHolder>() {
    //아이템의 갯수
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordListRecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_record_list_recycler, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: RecordListRecyclerAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bindItems(data : ResponseGetRecordListResult){
            itemView.item_record_list_tv_title.text = data.getTopic()

            itemView.item_record_list_ll.setOnClickListener(object : OnSingleClickListener(){
                override fun onSingleClick(v: View) {
                    var intent = Intent(context, RecordDetailActivity::class.java)
                    intent.putExtra("noiseNo", data.getNoiseNo())
                    context.startActivity(intent)
                }
            })
            itemView.item_record_list_iv_delete.setOnClickListener(object : OnSingleClickListener(){
                override fun onSingleClick(v: View) {
                    recordListActivityView.deleteRecord(data.getNoiseNo())
                }
            })
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