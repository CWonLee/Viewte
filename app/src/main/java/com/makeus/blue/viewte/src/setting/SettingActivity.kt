package com.makeus.blue.viewte.src.setting

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.ApplicationClass
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.GlideApp
import com.makeus.blue.viewte.src.login.LoginActivity
import com.makeus.blue.viewte.src.main.DialogAddCategory
import com.makeus.blue.viewte.src.main.interfaces.GetCategoryAPI
import com.makeus.blue.viewte.src.main.interfaces.GetUserAPI
import com.makeus.blue.viewte.src.main.models.CategoryItem
import com.makeus.blue.viewte.src.main.models.ResponseGetCategory
import com.makeus.blue.viewte.src.main.models.ResponseUser
import com.makeus.blue.viewte.src.setting.interfaces.PatchUserAPI
import com.makeus.blue.viewte.src.setting.interfaces.SettingActivityView
import com.makeus.blue.viewte.src.setting.models.RequestPatchUser
import com.makeus.blue.viewte.src.setting.models.ResponsePatchUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SettingActivity : BaseActivity(), SettingActivityView {

    private lateinit var mIvProfile: ImageView
    private lateinit var mIvBack: ImageView
    private var mProfileUrl: String = ""
    private var mUserName: String = ""
    private lateinit var mClChangeUser: ConstraintLayout
    private var mDialogUserName:String = ""
    private var PERMISSION_PICK_IMAGE = 1
    private var IMAGE_PICK_CODE = 2
    private var mStorageRef: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        mIvProfile = findViewById(R.id.setting_iv_profile)
        mIvBack = findViewById(R.id.setting_iv_back_btn)

        init()

        mIvBack.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                finish()
            }
        })
        mIvProfile.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                var dialogAddCategory = DialogChangeUser(this@SettingActivity, this@SettingActivity, mProfileUrl, mUserName)
                dialogAddCategory.show()
                val window = dialogAddCategory.getWindow()
                val width = (resources.displayMetrics.widthPixels * 0.80).toInt()
                window?.setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                window?.setBackgroundDrawable(resources.getDrawable(R.drawable.theme_grey2_radius20))
            }
        })
    }

    private fun init() {
        getUserInfo()
    }

    private fun getUserInfo() {
        showProgressDialog()
        val api = GetUserAPI.create()

        api.getUser().enqueue(object : Callback<ResponseUser> {
            override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
                hideProgressDialog()
                var responseUser = response.body()

                if (responseUser!!.IsSuccess() && responseUser.getCode() == 200) {
                    if (responseUser.getResult()[0].getProfileUrl() != null) {
                        GlideApp.with(this@SettingActivity).load(responseUser.getResult()[0].getProfileUrl())
                            .apply(RequestOptions.circleCropTransform())
                            .into(mIvProfile)
                    }
                    mUserName = responseUser.getResult()[0].getName()
                    mProfileUrl = responseUser.getResult()[0].getProfileUrl()
                }
                else {
                    showCustomToast(responseUser.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
            }
        })
    }

    override fun changeUser(name: String, imageUrl: String) {
        showProgressDialog()

        if (imageUrl.length >= 4 && imageUrl[0] == 'h' && imageUrl[1] == 't' && imageUrl[2] == 't' && imageUrl[3] == 'p') {
            patchUser(name, imageUrl)
            return
        }

        mStorageRef = FirebaseStorage.getInstance().reference;
        val ref: StorageReference = mStorageRef!!.child(
            "images/" + UUID.randomUUID().toString())



        ref.putFile(imageUrl.toUri()).addOnSuccessListener(
            OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot -> // Get a URL to the uploaded content
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                    patchUser(name, it.toString())
                }
            })
            .addOnFailureListener(OnFailureListener {
                // Handle unsuccessful uploads
                // ...
                showCustomToast(resources.getString(R.string.network_error))
                hideProgressDialog()
            })


    }

    override fun pickImage(name: String) {
        mDialogUserName = name
        val permissionCheck = ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.RECORD_AUDIO
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this@SettingActivity,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_PICK_IMAGE
            )
            return
        }
        else {
            pickImageFromGallery()
        }
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
            var dialogChangeUser = DialogChangeUser(this@SettingActivity, this@SettingActivity, uri.toString(), mDialogUserName)
            dialogChangeUser.show()
            val window = dialogChangeUser.getWindow()
            val width = (resources.displayMetrics.widthPixels * 0.80).toInt()
            window?.setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawable(resources.getDrawable(R.drawable.theme_grey2_radius20))
        }
    }

    private fun patchUser(name: String, imageUrl: String) {
        val api = PatchUserAPI.create()

        println("name = $name")
        println("imageUrl = $imageUrl")

        api.patchUser(RequestPatchUser(imageUrl, name)).enqueue(object : Callback<ResponsePatchUser> {
            override fun onResponse(call: Call<ResponsePatchUser>, response: Response<ResponsePatchUser>) {
                hideProgressDialog()
                var responsePatchUser = response.body()

                if (responsePatchUser!!.IsSuccess() && responsePatchUser.getCode() == 200) {
                    showCustomToast(responsePatchUser.getMessage())
                    GlideApp.with(this@SettingActivity).load(imageUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(mIvProfile)
                }
                else {
                    showCustomToast(responsePatchUser.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponsePatchUser>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
            }
        })
    }
}