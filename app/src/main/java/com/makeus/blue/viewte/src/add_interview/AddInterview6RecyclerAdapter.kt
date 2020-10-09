package com.makeus.blue.viewte.src.add_interview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.makeus.blue.viewte.R
import kotlinx.android.synthetic.main.item_add_interview6_recycler.view.*


class AddInterview6RecyclerAdapter(private val context: Context, private val items: ArrayList<String>, private val answerItem: ArrayList<String>):
    RecyclerView.Adapter<AddInterview6RecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_add_interview6_recycler, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItem(changeString: String, position: Int) {
        answerItem[position] = changeString
    }

    fun getAnswer(): ArrayList<String> {
        return answerItem
    }

    override fun onBindViewHolder(holder: AddInterview6RecyclerAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position], position + 1)
    }


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bindItems(data : String, position: Int){
            itemView.item_add_interview6_tv_title.text = "$position. $data"
            itemView.item_add_interview6_et.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    updateItem(s.toString(), position - 1)
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

            })
        }
    }

}