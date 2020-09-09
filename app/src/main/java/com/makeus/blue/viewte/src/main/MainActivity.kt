package com.makeus.blue.viewte.src.main

import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.makeus.blue.viewte.R
import com.makeus.blue.viewte.src.BaseActivity

class MainActivity : BaseActivity() {

    private lateinit var mDl: DrawerLayout
    private lateinit var mIvMenu: ImageView
    private lateinit var mIvNavProfile: ImageView


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDl = findViewById(R.id.main_dl)
        mIvMenu = findViewById(R.id.main_iv_menu)
        mIvNavProfile = findViewById(R.id.main_iv_nav_profile)

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

        mIvMenu.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                mDl.openDrawer(GravityCompat.START)
            }
        })

    }
}
