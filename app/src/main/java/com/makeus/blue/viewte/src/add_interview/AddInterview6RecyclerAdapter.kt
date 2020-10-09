package com.makeus.blue.viewte.src.add_interview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.category.CategoryActivity
import com.makeus.blue.viewte.src.main.MainCategoryAdapter
import com.makeus.blue.viewte.src.main.models.CategoryItem
import kotlinx.android.synthetic.main.item_add_interview6_recycler.view.*
import kotlinx.android.synthetic.main.item_main_category_recycler.view.*

class AddInterview6RecyclerAdapter(private val context: Context, private val items: ArrayList<String>):
    RecyclerView.Adapter<AddInterview6RecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddInterview6RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_add_interview6_recycler, parent, false)
        return AddInterview6RecyclerAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: AddInterview6RecyclerAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position], position + 1)
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bindItems(data : String, position: Int){
            itemView.item_add_interview6_tv_title.text = "$position. $data"
        }
    }

}