package com.makeus.blue.viewte.src.add_interview

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
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
import com.makeus.blue.viewte.src.main.MainActivity
import com.makeus.blue.viewte.src.main.interfaces.SearchAPI
import com.makeus.blue.viewte.src.main.models.ResponseSearch
import com.makeus.blue.viewte.src.record.RecordActivity
import com.makeus.blue.viewte.src.search_result.SearchResultActivity
import com.makeus.blue.viewte.src.setting.SettingActivity
import com.makeus.blue.viewte.src.trash.TrashActivity
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.Holder
import com.orhanobut.dialogplus.ViewHolder
import kotlinx.android.synthetic.main.activity_add_interview1.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

        add_interview1_cl_home.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview1Activity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        })
        add_interview1_cl_search.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                showSearchDialog()
            }
        })
        add_interview1_cl_mic.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview1Activity, RecordActivity::class.java)
                startActivity(intent)
            }
        })
        add_interview1_cl_trash.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview1Activity, TrashActivity::class.java)
                startActivity(intent)
            }
        })
        add_interview1_cl_setting.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview1Activity, SettingActivity::class.java)
                startActivity(intent)
            }
        })

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

    private fun showSearchDialog() {
        val searchGravity: Int = Gravity.TOP
        val holder: Holder

        holder = ViewHolder(R.layout.dialog_search)

        val builder = DialogPlus.newDialog(this).apply {
            setContentHolder(holder)
            isCancelable = true
            setGravity(searchGravity)
            setOnClickListener { dialog, view ->
                var editText: EditText = dialog.holderView.findViewById(R.id.search_edit_text)

                if (view.id == R.id.search_iv) {
                    if (editText.text.toString() != "") {
                        getSearch(editText.text.toString())
                    }
                }
            }
        }
        builder.create().show()
    }

    private fun getSearch(content: String) {
        val api = SearchAPI.create()

        api.getSearch(content).enqueue(object : Callback<ResponseSearch> {
            override fun onResponse(call: Call<ResponseSearch>, response: Response<ResponseSearch>) {
                var responseSearch = response.body()

                if (responseSearch!!.IsSuccess() && responseSearch.getCode() == 200) {

                    var intent = Intent(this@AddInterview1Activity, SearchResultActivity::class.java)
                    intent.putExtra("SearchResult", responseSearch.getResult())
                    startActivity(intent)
                }
                else {
                    hideProgressDialog()
                    showCustomToast(responseSearch.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
            }
        })
    }
}
