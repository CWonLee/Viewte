package com.makeus.blue.viewte.src.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.main.models.CategoryItem
import kotlinx.android.synthetic.main.item_main_category_recycler.view.*

class MainCategoryAdapter(private val items: ArrayList<CategoryItem>):RecyclerView.Adapter<MainCategoryAdapter.ViewHolder>()  {

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
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bindItems(data : CategoryItem){
            itemView.item_main_category_tv_title.text = data.getName()
            itemView.item_main_category_tv_info.text = data.getCount().toString() + "개, " + data.getMinute().toString() + "분"
        }
    }
}