package com.makeus.blue.viewte.src.main

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.category.CategoryActivity
import com.makeus.blue.viewte.src.main.models.CategoryItem
import kotlinx.android.synthetic.main.item_main_category_recycler.view.*

class MainCategoryAdapter(private val items: ArrayList<CategoryItem>, private val context: Context):RecyclerView.Adapter<MainCategoryAdapter.ViewHolder>()  {

    //아이템의 갯수
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCategoryAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_main_category_recycler, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: MainCategoryAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position])
        holder.itemView.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                val intent = Intent(context, CategoryActivity::class.java)
                context.startActivity(intent)
            }
        })
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bindItems(data : CategoryItem){
            itemView.item_main_category_tv_title.text = data.getName()
            itemView.item_main_category_tv_info.text = data.getCount().toString() + "개, " + data.getMinute().toString() + "분"
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