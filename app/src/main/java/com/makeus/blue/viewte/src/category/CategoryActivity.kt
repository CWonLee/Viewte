package com.makeus.blue.viewte.src.category

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.add_interview.AddInterview1Activity
import com.makeus.blue.viewte.src.category.interfaces.GetInterviewAPI
import com.makeus.blue.viewte.src.category.interfaces.PatchCategoryAPI
import com.makeus.blue.viewte.src.category.models.*
import com.makeus.blue.viewte.src.main.interfaces.AddCategoryAPI
import com.makeus.blue.viewte.src.main.models.RequestAddCategory
import com.makeus.blue.viewte.src.main.models.ResponseAddCategory
import com.makeus.blue.viewte.src.record.RecordActivity
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.Holder
import com.orhanobut.dialogplus.ViewHolder
import kotlinx.android.synthetic.main.item_main_category_recycler.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class CategoryActivity : BaseActivity() {

    private lateinit var mDl: DrawerLayout
    private lateinit var mIvMenu: ImageView
    private lateinit var mIvNavProfile: ImageView
    private lateinit var mRvCategoryTop: RecyclerView
    private lateinit var mRvCategoryBottom: RecyclerView
    private lateinit var mMainCategoryAdapter : CategoryAdapter
    private lateinit var mMainCategoryBottomAdapter : CategoryAdapter
    private lateinit var mClCategoryExpand: ConstraintLayout
    private lateinit var mIvBack: ImageView
    private lateinit var mClExpandArea: ConstraintLayout
    private var mCategoryExpand : Boolean = false
    private var mCategoryItemTop : ArrayList<ResponseInterviewResult> = ArrayList()
    private var mCategoryItemBottom : ArrayList<ResponseInterviewResult> = ArrayList()
    private lateinit var mClAddCategory: ConstraintLayout
    private lateinit var mIvImage: ImageView
    private lateinit var mTvCategoryTitle : TextView
    private lateinit var mClChangeImage: ConstraintLayout
    private var PERMISSION_PICK_IMAGE = 1
    private var IMAGE_PICK_CODE = 2
    private var mStorageRef: StorageReference? = null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        mDl = findViewById(R.id.category_dl)
        mIvMenu = findViewById(R.id.category_iv_menu)
        mIvNavProfile = findViewById(R.id.category_iv_nav_profile)
        mRvCategoryTop = findViewById(R.id.category_rv_top)
        mRvCategoryBottom = findViewById(R.id.category_rv_bottom)
        mClCategoryExpand = findViewById(R.id.category_cl_expand)
        mIvBack = findViewById(R.id.category_iv_back)
        mClAddCategory = findViewById(R.id.category_cl_add_interview)
        mClExpandArea = findViewById(R.id.category_cl_expand_area)
        mIvImage = findViewById(R.id.category_iv_category_image)
        mTvCategoryTitle = findViewById(R.id.category_tv_big_title)
        mClChangeImage = findViewById(R.id.category_cl_change_image)

        mTvCategoryTitle.text = intent.getStringExtra("categoryName")
        mIvNavProfile.clipToOutline = true
        collapse(mClExpandArea)

        mIvImage.setBackgroundResource(R.drawable.theme_main_category_item)
        mIvImage.clipToOutline = true
        Glide.with(this).load(intent.getStringExtra("imageUrl"))
            .centerCrop()
            .into(mIvImage)

        mMainCategoryAdapter = CategoryAdapter(mCategoryItemTop, this, intent.getIntExtra("categoriesNo", 0))
        mRvCategoryTop.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        mRvCategoryTop.setHasFixedSize(true)
        mRvCategoryTop.adapter = mMainCategoryAdapter
        mRvCategoryTop.isNestedScrollingEnabled = false

        mMainCategoryBottomAdapter = CategoryAdapter(mCategoryItemBottom, this, intent.getIntExtra("categoriesNo", 0))
        mRvCategoryBottom.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        mRvCategoryBottom.setHasFixedSize(true)
        mRvCategoryBottom.adapter = mMainCategoryBottomAdapter
        mRvCategoryBottom.isNestedScrollingEnabled = false

        getInterview()

        mClChangeImage.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                val permissionCheck = ContextCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.RECORD_AUDIO
                )
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        this@CategoryActivity,
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
        mIvMenu.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                mDl.openDrawer(GravityCompat.START)
            }
        })
        mIvBack.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                finish()
            }
        })

        mClCategoryExpand.setOnClickListener{
            if (mCategoryExpand) {
                collapse(mClExpandArea)
                mCategoryExpand = false
            }
            else {
                expand(mClExpandArea)
                mCategoryExpand = true
            }
        }

        mClAddCategory.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                var intent = Intent(this@CategoryActivity, AddInterview1Activity::class.java)
                intent.putExtra("categoriesNo", getIntent().getIntExtra("categoriesNo", 0))
                startActivity(intent)
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

    private fun getInterview() {
        showProgressDialog()
        val api = GetInterviewAPI.create()

        api.getInterview(intent.getIntExtra("categoriesNo", 0)).enqueue(object : Callback<ResponseInterview> {
            override fun onResponse(call: Call<ResponseInterview>, response: Response<ResponseInterview>) {
                var responseGetInterview = response.body()

                if (responseGetInterview!!.IsSuccess() && responseGetInterview.getCode() == 200) {
                    hideProgressDialog()
                    when {
                        responseGetInterview.getResult().size > 2 -> {
                            for ((cnt, i) in responseGetInterview.getResult().withIndex()) {
                                if (cnt < 2) {
                                    mCategoryItemTop.add(i)
                                }
                                else {
                                    mCategoryItemBottom.add(i)
                                }
                            }
                            mCategoryItemBottom
                        }
                        else -> {
                            for (i in responseGetInterview.getResult()) {
                                mCategoryItemTop.add(i)
                            }
                        }
                    }
                    mMainCategoryAdapter.notifyDataSetChanged()
                    mMainCategoryBottomAdapter.notifyDataSetChanged()
                }
                else {
                    hideProgressDialog()
                    showCustomToast(responseGetInterview.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponseInterview>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
                t.printStackTrace()
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
            showProgressDialog()
            var uri = data.data
            mStorageRef = FirebaseStorage.getInstance().reference;
            val ref: StorageReference = mStorageRef!!.child(
                "images/" + UUID.randomUUID().toString())

            ref.putFile(uri!!).addOnSuccessListener(
                OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot -> // Get a URL to the uploaded content
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                        patchCategory(it.toString())
                    }
                })
                .addOnFailureListener(OnFailureListener {
                    // Handle unsuccessful uploads
                    // ...
                    hideProgressDialog()
                    showCustomToast(resources.getString(R.string.network_error))
                })
        }
    }

    private fun patchCategory(imageUrl: String) {
        val api = PatchCategoryAPI.create()

        api.patchCategory(RequestPatchCategory(intent.getIntExtra("categoriesNo", 0), imageUrl)).enqueue(object : Callback<ResponsePatchCategory> {
            override fun onResponse(call: Call<ResponsePatchCategory>, response: Response<ResponsePatchCategory>) {
                var responsePatchCategory = response.body()

                if (responsePatchCategory!!.IsSuccess() && responsePatchCategory.getCode() == 200) {
                    hideProgressDialog()
                    mIvImage.setBackgroundResource(R.drawable.theme_main_category_item)
                    mIvImage.clipToOutline = true
                    Glide.with(this@CategoryActivity).load(imageUrl)
                        .centerCrop()
                        .into(mIvImage)
                    showCustomToast(responsePatchCategory.getMessage())
                }
                else {
                    hideProgressDialog()
                    showCustomToast(responsePatchCategory.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponsePatchCategory>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
                t.printStackTrace()
            }
        })
    }
}
