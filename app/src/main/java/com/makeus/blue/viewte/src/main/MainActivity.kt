package com.makeus.blue.viewte.src.main

import android.annotation.SuppressLint
import android.graphics.drawable.shapes.OvalShape
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
import com.makeus.blue.viewte.src.main.models.CategoryItem
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.Holder
import com.orhanobut.dialogplus.ViewHolder

class MainActivity : BaseActivity() {

    private lateinit var mDl: DrawerLayout
    private lateinit var mIvMenu: ImageView
    private lateinit var mIvNavProfile: ImageView
    private lateinit var mIvProfile: ImageView
    private lateinit var mRvCategoryTop: RecyclerView
    private lateinit var mRvCategoryBottom: RecyclerView
    private lateinit var mMainCategoryAdapter : MainCategoryAdapter
    private lateinit var mMainCategoryBottomAdapter : MainCategoryAdapter
    private var mCategoryItemTop : ArrayList<CategoryItem> = ArrayList()
    private var mCategoryItemBottom : ArrayList<CategoryItem> = ArrayList()
    private lateinit var mElCategory : ExpandableRelativeLayout
    private lateinit var mClCategoryExpand : ConstraintLayout
    private var mCategoryExpand : Boolean = false
    private lateinit var mClSearch: ConstraintLayout

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

        for(i in 1..3) {
            mCategoryItemTop.add(CategoryItem("새 카테고리를 만들어주세요", 0, 0))
        }
        for(i in 1.. 8) {
            mCategoryItemBottom.add(CategoryItem("새 카테고리를 만들어주세요", 0, 0))
        }

        mMainCategoryAdapter = MainCategoryAdapter(mCategoryItemTop)
        mRvCategoryTop.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        mRvCategoryTop.setHasFixedSize(true)
        mRvCategoryTop.adapter = mMainCategoryAdapter
        mRvCategoryTop.isNestedScrollingEnabled = false


        mMainCategoryBottomAdapter = MainCategoryAdapter(mCategoryItemBottom)
        mRvCategoryBottom.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        mRvCategoryBottom.setHasFixedSize(true)
        mRvCategoryBottom.adapter = mMainCategoryBottomAdapter
        mRvCategoryBottom.isNestedScrollingEnabled = false

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
}
