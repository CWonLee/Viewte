package com.makeus.blue.viewte.src.add_interview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makeus.blue.viewte.R
import kotlinx.android.synthetic.main.item_add_interview7_recycler.view.*

class AddInterview7RecyclerAdapter(private val context: Context, private val questionItems: ArrayList<String>, private val answerItem: ArrayList<String>, private val keywordItem: ArrayList<String>):
    RecyclerView.Adapter<AddInterview7RecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_add_interview7_recycler, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return questionItems.size
    }

    fun updateItem(changeString: String, position: Int) {
        keywordItem[position] = changeString
    }

    fun getKeyword(): ArrayList<String> {
        return keywordItem
    }

    override fun onBindViewHolder(holder: AddInterview7RecyclerAdapter.ViewHolder, position: Int) {
        holder.bindItems(questionItems[position], answerItem[position], position + 1)
    }


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bindItems(data : String, answerData: String, position: Int){
            itemView.item_add_interview7_tv_title.text = "$position. $data"
            itemView.item_add_interview7_tv_answer.text = answerData
            itemView.item_add_interview7_et.addTextChangedListener(object : TextWatcher {
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