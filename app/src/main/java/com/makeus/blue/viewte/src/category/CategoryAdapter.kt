package com.makeus.blue.viewte.src.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.category.models.CategoryItem
import kotlinx.android.synthetic.main.item_category_recycler.view.*

class CategoryAdapter(private val items: ArrayList<CategoryItem>): RecyclerView.Adapter<CategoryAdapter.ViewHolder>()  {

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
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bindItems(data : CategoryItem){
            itemView.item_category_tv_title.text = data.getName()
            itemView.item_category_tv_date.text = data.getDate()
            // 이미지 glide로 넣기
        }
    }
}