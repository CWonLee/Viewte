package com.makeus.blue.viewte.src.main

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat.startActivityForResult
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.main.interfaces.MainActivityView

class DialogAddCategory(context: Context, var mainActivityView: MainActivityView, var pickImage: Boolean, var imageUrl: String, var name: String) : Dialog(context) {

    private lateinit var mTvAdd: TextView
    private lateinit var mTvCancel: TextView
    private lateinit var mEtCategory: EditText
    private lateinit var mClImage: ConstraintLayout
    private lateinit var mIvImage: ImageView
    private lateinit var mIvImageVisible: ImageView
    private var IMAGE_PICK_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.88f
        window?.attributes = layoutParams

        setContentView(R.layout.dialog_add_category)

        mTvAdd = findViewById(R.id.custom_dialog_add_category_tv_add)
        mTvCancel = findViewById(R.id.custom_dialog_add_category_tv_cancel)
        mEtCategory = findViewById(R.id.custom_dialog_add_category_et)
        mClImage = findViewById(R.id.custom_dialog_add_category_cl_photo)
        mIvImage = findViewById(R.id.custom_dialog_add_category_photo)
        mIvImageVisible = findViewById(R.id.custom_dialog_add_category_add_photo)

        if (pickImage) {
            Glide.with(context).load(imageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(mIvImage)
            mIvImageVisible.visibility = View.GONE
            mEtCategory.setText(name)
        }

        mTvCancel.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                dismiss()
            }
        })
        mTvAdd.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                if (mEtCategory.text.toString() == "") {
                    Toast.makeText(context, "카테고리를 입력해주세요", Toast.LENGTH_LONG).show()
                }
                else {
                    mainActivityView.addCategory(mEtCategory.text.toString(), imageUrl)
                    dismiss()
                }
            }
        })
        mClImage.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                mainActivityView.pickImage(mEtCategory.text.toString())
                dismiss()
            }
        })
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