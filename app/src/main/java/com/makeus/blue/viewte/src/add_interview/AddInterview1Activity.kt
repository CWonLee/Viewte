package com.makeus.blue.viewte.src.add_interview

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.kakao.auth.StringSet.file
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import java.util.*


class AddInterview1Activity : BaseActivity() {

    private lateinit var mBtnNext: Button
    private lateinit var mIvBack: ImageView
    private lateinit var mEtTitle: EditText
    private lateinit var mEtPurpose: EditText
    private lateinit var mClPhoto: ConstraintLayout
    private lateinit var mIvAddPhoto: ImageView
    private lateinit var mIvPhoto: ImageView
    private var PERMISSION_PICK_IMAGE = 1
    private var IMAGE_PICK_CODE = 2
    private var mStorageRef: StorageReference? = null
    private var mImageUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_interview1)

        mBtnNext = findViewById(R.id.add_interview1_btn_next)
        mIvBack = findViewById(R.id.add_interview1_iv_back)
        mEtTitle = findViewById(R.id.add_interview1_et_title)
        mEtPurpose = findViewById(R.id.add_interview1_et_purpose)
        mClPhoto = findViewById(R.id.add_interview1_cl_photo)
        mIvAddPhoto = findViewById(R.id.add_interview1_add_photo)
        mIvPhoto = findViewById(R.id.add_interview1_photo)

        mBtnNext.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                if (mEtTitle.text.toString() == "") {
                    showCustomToast("인터뷰 제목을 입력해주세요")
                }
                else {
                    if (mEtPurpose.text.toString() == "") {
                        showCustomToast("인터뷰 목적을 입력해주세요")
                    }
                    else {
                        if (mImageUrl == "") {
                            showCustomToast("이미지를 선택해주세요")
                        }
                        else {
                            var intent = Intent(this@AddInterview1Activity, AddInterview2Activity::class.java)
                            intent.putExtra("categoriesNo", getIntent().getIntExtra("categoriesNo", 0))
                            intent.putExtra("i_title", mEtTitle.text.toString())
                            intent.putExtra("purpose", mEtPurpose.text.toString())
                            intent.putExtra("imageUrl", mImageUrl)

                            startActivity(intent)
                        }
                    }
                }
            }
        })
        mIvBack.setOnClickListener(object: OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                finish()
            }
        })

        mClPhoto.setOnClickListener(object: OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                println("눌리긴하나")
                val permissionCheck = ContextCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.RECORD_AUDIO
                )
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        this@AddInterview1Activity,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        PERMISSION_PICK_IMAGE
                    )
                    return
                }
                else {
                    pickImageFromGallery()
                }
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_PICK_IMAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                println("요기?")
                pickImageFromGallery()
            } else {
                showCustomToast("권한이 필요합니다")
            }
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            var uri = data.data
            println("uri = $uri")
            mImageUrl = uri.toString()
            mIvAddPhoto.visibility = View.GONE
            Glide.with(this).load(uri)
                .apply(RequestOptions.circleCropTransform())
                .into(mIvPhoto)
        }
    }
}
