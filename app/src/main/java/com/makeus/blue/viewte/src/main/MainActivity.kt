package com.makeus.blue.viewte.src.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.aakira.expandablelayout.ExpandableRelativeLayout
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.ApplicationClass
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.GlideApp
import com.makeus.blue.viewte.src.login.LoginActivity
import com.makeus.blue.viewte.src.main.interfaces.*
import com.makeus.blue.viewte.src.main.models.*
import com.makeus.blue.viewte.src.record.RecordActivity
import com.makeus.blue.viewte.src.record_list.RecordListActivity
import com.makeus.blue.viewte.src.search_result.SearchResultActivity
import com.makeus.blue.viewte.src.setting.SettingActivity
import com.makeus.blue.viewte.src.trash.TrashActivity
import com.orhanobut.dialogplus.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_category_recycler.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity(), MainActivityView {

    private lateinit var mDl: DrawerLayout
    private lateinit var mIvMenu: ImageView
    private lateinit var mIvNavProfile: ImageView
    private lateinit var mIvProfile: ImageView
    private lateinit var mRvCategoryTop: RecyclerView
    private lateinit var mRvCategoryBottom: RecyclerView
    private lateinit var mMainCategoryAdapter : MainCategoryAdapter
    private lateinit var mMainCategoryBottomAdapter : MainCategoryAdapter
    private lateinit var mTvHello: TextView
    private var mCategoryItemTop : ArrayList<CategoryItem> = ArrayList()
    private var mCategoryItemBottom : ArrayList<CategoryItem> = ArrayList()
    private lateinit var mClExpandArea : ConstraintLayout
    private lateinit var mClCategoryExpand : ConstraintLayout
    private var mCategoryExpand : Boolean = false
    private lateinit var mClSearch: ConstraintLayout
    private lateinit var mClRecord: ConstraintLayout
    private lateinit var mIvAddCategory: ImageView
    private lateinit var mClLogout: ConstraintLayout
    private lateinit var mClSetting: ConstraintLayout
    private lateinit var mClNavSetting: ConstraintLayout
    private lateinit var mClNavRecordList: ConstraintLayout
    private lateinit var mTvInterviewCnt : TextView
    private lateinit var mClTrash : ConstraintLayout
    private lateinit var mClHome : ConstraintLayout
    private var PERMISSION_PICK_IMAGE = 1
    private var IMAGE_PICK_CODE = 2
    private var mDialogCategoryName = ""
    private var mStorageRef: StorageReference? = null
    private lateinit var mNavClTrash : ConstraintLayout

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDl = findViewById(R.id.main_dl)
        mIvMenu = findViewById(R.id.main_iv_menu)
        mIvNavProfile = findViewById(R.id.main_iv_nav_profile)
        mIvProfile = findViewById(R.id.main_iv_profile)
        mRvCategoryTop = findViewById(R.id.main_rv_category_top)
        mRvCategoryBottom = findViewById(R.id.main_rv_category_bottom)
        mClCategoryExpand = findViewById(R.id.main_cl_category_expand)
        mClSearch = findViewById(R.id.main_cl_search)
        mClRecord = findViewById(R.id.main_cl_mic)
        mIvAddCategory = findViewById(R.id.main_iv_plus)
        mClExpandArea = findViewById(R.id.main_cl_expand_area)
        mClLogout = findViewById(R.id.main_cl_nav_logout)
        mClSetting = findViewById(R.id.main_cl_setting)
        mClNavSetting = findViewById(R.id.main_cl_nav_set)
        mTvHello = findViewById(R.id.main_tv_hello)
        mTvInterviewCnt = findViewById(R.id.main_tv_interview_time)
        mClTrash = findViewById(R.id.main_cl_trash)
        mClNavRecordList = findViewById(R.id.main_cl_nav_record_memo)
        mClHome = findViewById(R.id.main_cl_home)
        mNavClTrash = findViewById(R.id.main_cl_nav_trash)

//        // get KeyHash
//        try {
//            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
//            val signatures = info.signingInfo.apkContentsSigners
//            val md = MessageDigest.getInstance("SHA")
//            for (signature in signatures) {
//                val md: MessageDigest
//                md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                val key = String(Base64.encode(md.digest(), 0))
//                Log.d("Hash key:", "!!!!!!!$key!!!!!!")
//            }
//        } catch(e: Exception) {
//            Log.e("name not found", e.toString())
//        }
        mIvNavProfile.clipToOutline = true
        mIvProfile.clipToOutline = true
        collapse(mClExpandArea)

        getUser()

        mMainCategoryAdapter = MainCategoryAdapter(mCategoryItemTop, this, this)
        mRvCategoryTop.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        mRvCategoryTop.setHasFixedSize(true)
        mRvCategoryTop.adapter = mMainCategoryAdapter
        mRvCategoryTop.isNestedScrollingEnabled = false


        mMainCategoryBottomAdapter = MainCategoryAdapter(mCategoryItemBottom, this, this)
        mRvCategoryBottom.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        mRvCategoryBottom.setHasFixedSize(true)
        mRvCategoryBottom.adapter = mMainCategoryBottomAdapter
        mRvCategoryBottom.isNestedScrollingEnabled = false

        getCategory()

        mClHome.setOnClickListener(object  : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@MainActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        })
        mClNavRecordList.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                var intent = Intent(this@MainActivity, RecordListActivity::class.java)
                startActivity(intent)
            }
        })
        mClTrash.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@MainActivity, TrashActivity::class.java)
                startActivity(intent)
            }
        })
        mNavClTrash.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@MainActivity, TrashActivity::class.java)
                startActivity(intent)
            }
        })
        mIvMenu.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                mDl.openDrawer(GravityCompat.START)
            }
        })
        mClCategoryExpand.setOnClickListener {
            if (mCategoryExpand) {
                collapse(mClExpandArea)
                mCategoryExpand = false
            }
            else {
                expand(mClExpandArea)
                mCategoryExpand = true
            }
        }

        mClSetting.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
            }
        })
        mClNavSetting.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
            }
        })
        mClSearch.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                showSearchDialog()
            }
        })
        mClRecord.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@MainActivity, RecordActivity::class.java)
                startActivity(intent)
            }
        })
        mIvAddCategory.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var dialogAddCategory = DialogAddCategory(this@MainActivity, this@MainActivity, false, "", "")
                dialogAddCategory.show()
                val window = dialogAddCategory.getWindow()
                val width = (resources.displayMetrics.widthPixels * 0.80).toInt()
                window?.setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                window?.setBackgroundDrawable(resources.getDrawable(R.drawable.theme_grey2_radius20))
            }
        })

        mClLogout.setOnClickListener(object: OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
                    override fun onCompleteLogout() {
                        var intent = Intent(this@MainActivity, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                })
            }
        })

    }

    override fun onBackPressed() {
        if (mDl.isDrawerOpen(GravityCompat.START)) {
            mDl.closeDrawer(GravityCompat.START)
        }
        else
            super.onBackPressed()
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
                var editText:EditText = dialog.holderView.findViewById(R.id.search_edit_text)

                if (view.id == R.id.search_iv) {
                    if (editText.text.toString() != "") {
                        getSearch(editText.text.toString())
                    }
                }
            }
        }
        builder.create().show()
    }

    private fun getCategory() {
        showProgressDialog()
        val api = GetCategoryAPI.create()

        api.getCategory().enqueue(object : Callback<ResponseGetCategory> {
            override fun onResponse(call: Call<ResponseGetCategory>, response: Response<ResponseGetCategory>) {
                hideProgressDialog()
                var responseGetCategory = response.body()

                if (responseGetCategory!!.IsSuccess() && responseGetCategory.getCode() == 200) {
                    mCategoryItemTop.clear()
                    mCategoryItemBottom.clear()
                    var cnt : Int = 0
                    for (i in responseGetCategory.getResult()) {
                        if (cnt < 3) {
                            mCategoryItemTop.add(CategoryItem(i.getC_title(), i.getCount(), 0, i.getCategoriesNo(), i.getImageUrl()))
                        }
                        else {
                            mCategoryItemBottom.add(CategoryItem(i.getC_title(), i.getCount(), 0, i.getCategoriesNo(), i.getImageUrl()))
                        }
                        cnt++
                    }
                    if (responseGetCategory.getResult().size < 3) {
                        for (i in 1 .. (3-responseGetCategory.getResult().size)) {
                            mCategoryItemTop.add(CategoryItem("새 카테고리를 만들어주세요", 0, 0, -1, ""))
                        }
                    }
                    else if (responseGetCategory.getResult().size == 3) {
                        mCategoryItemBottom.add(CategoryItem("새 카테고리를 만들어주세요", 0, 0, -1, ""))
                    }
                    mMainCategoryAdapter.notifyDataSetChanged()
                    mMainCategoryBottomAdapter.notifyDataSetChanged()
                }
                else {
                    showCustomToast(responseGetCategory.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponseGetCategory>, t: Throwable) {
                hideProgressDialog()
                //showCustomToast(resources.getString(R.string.network_error))
                ApplicationClass.prefs.myEditText = ""
                var intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        })
    }

    private fun expand(v: View) {
        val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            (v.parent as View).width,
            View.MeasureSpec.EXACTLY
        )
        val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        val targetHeight = v.getMeasuredHeight()

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.layoutParams.height = 1
        v.visibility = View.VISIBLE
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                v.getLayoutParams().height = if (interpolatedTime == 1f)
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                else
                    (targetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // Expansion speed of 1dp/ms
        a.duration =
            ((targetHeight / v.context.resources.displayMetrics.density).toInt()).toLong()
        v.startAnimation(a)
    }

    private fun collapse(v: View) {
        val initialHeight = v.measuredHeight

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // Collapse speed of 1dp/ms
        a.duration =
            ((initialHeight / v.context.resources.displayMetrics.density).toInt()).toLong()
        v.startAnimation(a)
    }

    private fun getUser() {
        showProgressDialog()
        val api = GetUserAPI.create()

        api.getUser().enqueue(object : Callback<ResponseUser> {
            override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
                hideProgressDialog()
                var responseUser = response.body()

                if (responseUser!!.IsSuccess() && responseUser.getCode() == 200) {
                    if (responseUser.getResult()[0].getProfileUrl() == null) {
                        mIvProfile.setImageResource(R.drawable.icon_profile_popup)
                    }
                    else {
                        GlideApp.with(this@MainActivity).load(responseUser.getResult()[0].getProfileUrl())
                            .apply(RequestOptions.circleCropTransform())
                            .into(mIvProfile)
                    }
                    if (responseUser.getResult()[0].getProfileUrl() == null) {
                        mIvNavProfile.setImageResource(R.drawable.icon_profile_popup)
                    }
                    else {
                        GlideApp.with(this@MainActivity).load(responseUser.getResult()[0].getProfileUrl())
                            .apply(RequestOptions.circleCropTransform())
                            .into(mIvNavProfile)
                    }
                    mTvHello.text = "안녕하세요 " + responseUser.getResult()[0].getName() + "님!"
                    mTvInterviewCnt.text = "뷰트와 함께한 인터뷰," + responseUser.getResult()[0].getCnt() + "개"
                    main_tv_nav_record_count.text = responseUser.getResult()[0].getCnt().toString()+ "개"
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

    override fun addCategory(name: String, imageUrl: String) {
        showProgressDialog()
        mStorageRef = FirebaseStorage.getInstance().reference;
        val ref: StorageReference = mStorageRef!!.child(
            "images/" + UUID.randomUUID().toString())



        ref.putFile(imageUrl.toUri()).addOnSuccessListener(
            OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot -> // Get a URL to the uploaded content
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                    postAddCategory(name, it.toString())
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
        mDialogCategoryName = name
        val permissionCheck = ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.RECORD_AUDIO
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_PICK_IMAGE
            )
            return
        }
        else {
            pickImageFromGallery()
        }
    }

    override fun makeNewCategory() {
        var dialogAddCategory = DialogAddCategory(this@MainActivity, this@MainActivity, false, "", "")
        dialogAddCategory.show()
        val window = dialogAddCategory.getWindow()
        val width = (resources.displayMetrics.widthPixels * 0.80).toInt()
        window?.setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(resources.getDrawable(R.drawable.theme_grey2_radius20))
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
            var dialogAddCategory = DialogAddCategory(this@MainActivity, this@MainActivity, true, uri.toString(), mDialogCategoryName)
            dialogAddCategory.show()
            val window = dialogAddCategory.getWindow()
            val width = (resources.displayMetrics.widthPixels * 0.80).toInt()
            window?.setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawable(resources.getDrawable(R.drawable.theme_grey2_radius20))
        }
    }

    private fun postAddCategory(name: String, imageUrl: String) {
        val api = AddCategoryAPI.create()

        api.postLogin(RequestAddCategory(name, imageUrl)).enqueue(object : Callback<ResponseAddCategory> {
            override fun onResponse(call: Call<ResponseAddCategory>, response: Response<ResponseAddCategory>) {
                var responseAddCategory = response.body()

                if (responseAddCategory!!.IsSuccess() && responseAddCategory.getCode() == 200) {
                    getCategory()
                    showCustomToast(responseAddCategory.getMessage())
                }
                else {
                    hideProgressDialog()
                    showCustomToast(responseAddCategory.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponseAddCategory>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
            }
        })
    }

    private fun getSearch(content: String) {
        val api = SearchAPI.create()

        api.getSearch(content).enqueue(object : Callback<ResponseSearch> {
            override fun onResponse(call: Call<ResponseSearch>, response: Response<ResponseSearch>) {
                var responseSearch = response.body()

                if (responseSearch!!.IsSuccess() && responseSearch.getCode() == 200) {

                    var intent = Intent(this@MainActivity, SearchResultActivity::class.java)
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
