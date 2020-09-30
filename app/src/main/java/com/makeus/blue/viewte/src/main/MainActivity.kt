package com.makeus.blue.viewte.src.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.aakira.expandablelayout.ExpandableRelativeLayout
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.main.interfaces.AddCategoryAPI
import com.makeus.blue.viewte.src.main.interfaces.GetCategoryAPI
import com.makeus.blue.viewte.src.main.interfaces.MainActivityView
import com.makeus.blue.viewte.src.main.models.CategoryItem
import com.makeus.blue.viewte.src.main.models.RequestAddCategory
import com.makeus.blue.viewte.src.main.models.ResponseAddCategory
import com.makeus.blue.viewte.src.main.models.ResponseGetCategory
import com.makeus.blue.viewte.src.record.RecordActivity
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.Holder
import com.orhanobut.dialogplus.ViewHolder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity(), MainActivityView {

    private lateinit var mDl: DrawerLayout
    private lateinit var mIvMenu: ImageView
    private lateinit var mIvNavProfile: ImageView
    private lateinit var mIvProfile: ImageView
    private lateinit var mRvCategoryTop: RecyclerView
    private lateinit var mRvCategoryBottom: RecyclerView
    private lateinit var mMainCategoryAdapter : MainCategoryAdapter
    private lateinit var mMainCategoryBottomAdapter : MainCategoryAdapter
    private var mTempCategoryItem : ArrayList<CategoryItem> = ArrayList()
    private var mTempCategoryItemBottom : ArrayList<CategoryItem> = ArrayList()
    private var mCategoryItemTop : ArrayList<CategoryItem> = ArrayList()
    private var mCategoryItemBottom : ArrayList<CategoryItem> = ArrayList()
    private lateinit var mElCategory : ExpandableRelativeLayout
    private lateinit var mClCategoryExpand : ConstraintLayout
    private var mCategoryExpand : Boolean = false
    private lateinit var mClSearch: ConstraintLayout
    private lateinit var mClRecord: ConstraintLayout
    private lateinit var mIvAddCategory: ImageView

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
        mElCategory = findViewById(R.id.main_el_category)
        mClCategoryExpand = findViewById(R.id.main_cl_category_expand)
        mClSearch = findViewById(R.id.main_cl_search)
        mClRecord = findViewById(R.id.main_cl_mic)
        mIvAddCategory = findViewById(R.id.main_iv_plus)

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
        mElCategory.collapse()


        mMainCategoryAdapter = MainCategoryAdapter(mCategoryItemTop, this)
        mRvCategoryTop.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        mRvCategoryTop.setHasFixedSize(true)
        mRvCategoryTop.adapter = mMainCategoryAdapter
        mRvCategoryTop.isNestedScrollingEnabled = false


        mMainCategoryBottomAdapter = MainCategoryAdapter(mCategoryItemBottom, this)
        mRvCategoryBottom.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        mRvCategoryBottom.setHasFixedSize(true)
        mRvCategoryBottom.adapter = mMainCategoryBottomAdapter
        mRvCategoryBottom.isNestedScrollingEnabled = false

        /*
        for (i in 1.. 3) {
            mCategoryItemTop.add(CategoryItem("qq", 0, 0, 0))
        }
        for (i in 1.. 2) {
            mCategoryItemBottom.add(CategoryItem("qq", 0, 0, 0))
        }

         */

        getCategory()

        for (i in mTempCategoryItem) {
            mCategoryItemTop.add((i))
        }
        for (i in mTempCategoryItemBottom) {
            mCategoryItemBottom.add(i)
        }

        mMainCategoryAdapter.notifyDataSetChanged()
        mMainCategoryBottomAdapter.notifyDataSetChanged()

        mIvMenu.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                mDl.openDrawer(GravityCompat.START)
            }
        })
        mClCategoryExpand.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                if (mCategoryExpand) {
                    mElCategory.collapse()
                    mCategoryExpand = false
                }
                else {
                    mElCategory.expand()
                    mCategoryExpand = true
                }
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
                var dialogAddCategory = DialogAddCategory(this@MainActivity, this@MainActivity)
                dialogAddCategory.show()
                val window = dialogAddCategory.getWindow()
                val width = (resources.displayMetrics.widthPixels * 0.80).toInt()
                window?.setLayout(width, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                window?.setBackgroundDrawable(resources.getDrawable(R.drawable.theme_grey2_radius20))
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
        }
        builder.create().show()
    }

    override fun addCategory(name: String) {
        showProgressDialog()
        val api = AddCategoryAPI.create()

        api.postLogin(RequestAddCategory(name)).enqueue(object : Callback<ResponseAddCategory> {
            override fun onResponse(call: Call<ResponseAddCategory>, response: Response<ResponseAddCategory>) {
                hideProgressDialog()
                var responseAddCategory = response.body()

                if (responseAddCategory!!.IsSuccess() && responseAddCategory.getCode() == 200) {
                    showCustomToast(responseAddCategory.getMessage())
                }
                else {
                    showCustomToast(responseAddCategory.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponseAddCategory>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
            }
        })
    }

    private fun getCategory() {
        showProgressDialog()
        val api = GetCategoryAPI.create()

        api.getCategory().enqueue(object : Callback<ResponseGetCategory> {
            override fun onResponse(call: Call<ResponseGetCategory>, response: Response<ResponseGetCategory>) {
                hideProgressDialog()
                var responseGetCategory = response.body()

                if (responseGetCategory!!.IsSuccess() && responseGetCategory.getCode() == 200) {
                    var cnt : Int = 0
                    for (i in responseGetCategory.getResult()) {
                        if (cnt < 3) {
                            mTempCategoryItem.add(CategoryItem(i.getC_title(), 0, 0, i.getCategoriesNo()))
                        }
                        else {
                            mTempCategoryItemBottom.add(CategoryItem(i.getC_title(), 0, 0, i.getCategoriesNo()))
                        }
                        cnt++
                    }
                    if (responseGetCategory.getResult().size < 3) {
                        for (i in 1 .. (3-responseGetCategory.getResult().size)) {
                            mTempCategoryItem.add(CategoryItem("새 카테고리를 만들어주세요", 0, 0, -1))
                        }
                    }
                    else if (responseGetCategory.getResult().size == 3) {
                        mTempCategoryItemBottom.add(CategoryItem("새 카테고리를 만들어주세요", 0, 0, -1))
                    }
                    //mMainCategoryAdapter.notifyDataSetChanged()
                    //mMainCategoryBottomAdapter.notifyDataSetChanged()
                }
                else {
                    showCustomToast(responseGetCategory.getMessage())
                }
            }

            override fun onFailure(call: Call<ResponseGetCategory>, t: Throwable) {
                hideProgressDialog()
                showCustomToast(resources.getString(R.string.network_error))
            }
        })
    }
}
