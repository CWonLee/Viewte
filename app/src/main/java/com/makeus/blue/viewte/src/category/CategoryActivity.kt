package com.makeus.blue.viewte.src.category

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
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
import com.makeus.blue.viewte.src.add_interview.AddInterview1Activity
import com.makeus.blue.viewte.src.category.interfaces.GetInterviewAPI
import com.makeus.blue.viewte.src.category.models.CategoryItem
import com.makeus.blue.viewte.src.category.models.ResponseInterview
import com.makeus.blue.viewte.src.category.models.ResponseInterviewResult
import com.makeus.blue.viewte.src.main.interfaces.AddCategoryAPI
import com.makeus.blue.viewte.src.main.models.RequestAddCategory
import com.makeus.blue.viewte.src.main.models.ResponseAddCategory
import com.makeus.blue.viewte.src.record.RecordActivity
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.Holder
import com.orhanobut.dialogplus.ViewHolder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        mIvNavProfile.clipToOutline = true
        collapse(mClExpandArea)

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
}
