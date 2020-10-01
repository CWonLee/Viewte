package com.makeus.blue.viewte.src.category

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.aakira.expandablelayout.ExpandableRelativeLayout
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity
import com.makeus.blue.viewte.src.category.models.CategoryItem
import com.makeus.blue.viewte.src.record.RecordActivity
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.Holder
import com.orhanobut.dialogplus.ViewHolder

class CategoryActivity : BaseActivity() {

    private lateinit var mDl: DrawerLayout
    private lateinit var mIvMenu: ImageView
    private lateinit var mIvNavProfile: ImageView
    private lateinit var mRvCategoryTop: RecyclerView
    private lateinit var mRvCategoryBottom: RecyclerView
    private lateinit var mMainCategoryAdapter : CategoryAdapter
    private lateinit var mMainCategoryBottomAdapter : CategoryAdapter
    private lateinit var mElCategory : ExpandableRelativeLayout
    private lateinit var mClCategoryExpand: ConstraintLayout
    private lateinit var mIvBack: ImageView
    private var mCategoryExpand : Boolean = false
    private var mCategoryItemTop : ArrayList<CategoryItem> = ArrayList()
    private var mCategoryItemBottom : ArrayList<CategoryItem> = ArrayList()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        mDl = findViewById(R.id.category_dl)
        mIvMenu = findViewById(R.id.category_iv_menu)
        mIvNavProfile = findViewById(R.id.category_iv_nav_profile)
        mRvCategoryTop = findViewById(R.id.category_rv_top)
        mRvCategoryBottom = findViewById(R.id.category_rv_bottom)
        mElCategory = findViewById(R.id.category_el)
        mClCategoryExpand = findViewById(R.id.category_cl_expand)
        mIvBack = findViewById(R.id.category_iv_back)

        mIvNavProfile.clipToOutline = true
        mElCategory.collapse()

        for (i in 1..3) {
            mCategoryItemTop.add(CategoryItem("김연우 파티셰 인터뷰", "2020.07.16 월요일, 8분", ""))
        }
        for (i in 1..8) {
            mCategoryItemBottom.add(CategoryItem("김연우 파티셰 인터뷰", "2020.07.16 월요일, 8분", ""))
        }

        mMainCategoryAdapter = CategoryAdapter(mCategoryItemTop, this)
        mRvCategoryTop.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        mRvCategoryTop.setHasFixedSize(true)
        mRvCategoryTop.adapter = mMainCategoryAdapter
        mRvCategoryTop.isNestedScrollingEnabled = false

        mMainCategoryBottomAdapter = CategoryAdapter(mCategoryItemBottom, this)
        mRvCategoryBottom.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        mRvCategoryBottom.setHasFixedSize(true)
        mRvCategoryBottom.adapter = mMainCategoryBottomAdapter
        mRvCategoryBottom.isNestedScrollingEnabled = false

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
    }

    override fun onBackPressed() {
        if (mDl.isDrawerOpen(GravityCompat.START)) {
            mDl.closeDrawer(GravityCompat.START)
        }
        else
            super.onBackPressed()
    }
}
