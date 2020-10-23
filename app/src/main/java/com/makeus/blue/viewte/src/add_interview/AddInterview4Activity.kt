package com.makeus.blue.viewte.src.add_interview

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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
import kotlinx.android.synthetic.main.activity_add_interview3.*
import kotlinx.android.synthetic.main.activity_add_interview4.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class AddInterview4Activity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mEtSearch: EditText
    private lateinit var mMap: GoogleMap
    private lateinit var mGeocoder : Geocoder
    private lateinit var mMarkerOption :MarkerOptions
    private lateinit var mScrollView : ScrollView
    private lateinit var mTvLocation : TextView
    private lateinit var mBtnNext : Button
    private lateinit var mIvBack : ImageView
    private var REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val GPS_ENABLE_REQUEST_CODE = 2001
    private val PERMISSIONS_REQUEST_CODE = 100

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_interview4)

        mEtSearch = findViewById(R.id.add_interview4_et_search)
        mScrollView = findViewById(R.id.add_interview4_scrollview)
        mTvLocation = findViewById(R.id.add_interview4_tv_locate)
        mBtnNext = findViewById(R.id.add_interview4_btn_next)
        mIvBack = findViewById(R.id.add_interview4_iv_back)

        val scrollableMapFragment = supportFragmentManager
            .findFragmentById(R.id.add_interview4_fm_map) as ScrollableMapFragment
        scrollableMapFragment.getMapAsync(this)

        scrollableMapFragment.setListener(object : ScrollableMapFragment.OnTouchListener {
            override fun onActionDown() {
                mScrollView.requestDisallowInterceptTouchEvent(true)
            }

            override fun onActionUp() {
                mScrollView.requestDisallowInterceptTouchEvent(true)
            }
        })

        add_interview4_cl_home.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview4Activity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        })
        add_interview4_cl_search.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                showSearchDialog()
            }
        })
        add_interview4_cl_mic.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview4Activity, RecordActivity::class.java)
                startActivity(intent)
            }
        })
        add_interview4_cl_trash.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview4Activity, TrashActivity::class.java)
                startActivity(intent)
            }
        })
        add_interview4_cl_setting.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View) {
                var intent = Intent(this@AddInterview4Activity, SettingActivity::class.java)
                startActivity(intent)
            }
        })

        mBtnNext.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {

                if (mTvLocation.text.toString() == "") {
                    showCustomToast("주소를 선택해주세요")
                }
                else {
                    var intent = Intent(this@AddInterview4Activity, AddInterview5Activity::class.java)

                    intent.putExtra("categoriesNo", getIntent().getIntExtra("categoriesNo", 0))
                    intent.putExtra("i_title", getIntent().getStringExtra("i_title"))
                    intent.putExtra("purpose", getIntent().getStringExtra("purpose"))
                    intent.putExtra("date", getIntent().getStringExtra("date"))
                    intent.putExtra("time", getIntent().getStringExtra("time"))
                    intent.putExtra("location", mTvLocation.text.toString())
                    intent.putExtra("imageUrl", getIntent().getStringExtra("imageUrl"))

                    startActivity(intent)
                }
            }
        })

        mIvBack.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View) {
                finish()
            }
        })

        mEtSearch.setOnTouchListener { _, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (mEtSearch.right - mEtSearch.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    val point = getPointFromGeoCoder(mEtSearch.text.toString())
                    mMap.clear()
                    mMarkerOption.position(LatLng(point.y, point.x))
                    mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(point.y, point.x),
                            16f
                        )
                    )
                    mMap.addMarker(mMarkerOption)
                    event.action = MotionEvent.ACTION_CANCEL
                    var tempLocation = getCurrentAddress(mMarkerOption.position.latitude, mMarkerOption.position.longitude)
                    var splitTempLocation = tempLocation.split(" ")
                    var tempLocation2 = ""
                    for (i in splitTempLocation.indices) {
                        if (i == 0) continue
                        else tempLocation2 += splitTempLocation[i] + " "
                    }
                    mTvLocation.text = tempLocation2
                    true
                }
            }
            false
        }
    }

    private fun getCurrentAddress(latitude: Double, longitude: Double): String {

        //지오코더... GPS를 주소로 변환
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>?
        try {

            addresses = geocoder.getFromLocation(
                latitude,
                longitude,
                7
            )
        } catch (ioException: IOException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show()
            return "지오코더 서비스 사용불가"
        } catch (illegalArgumentException: IllegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show()
            return "잘못된 GPS 좌표"
        }
        if (addresses == null || addresses.size == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show()
            return "주소 미발견"
        }
        val address = addresses[0]
        return address.getAddressLine(0).toString()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()
        } else {
            checkRunTimePermission()
        }
        mMap = googleMap
        mGeocoder = Geocoder(this)
        mMarkerOption = MarkerOptions()

        val gpsTracker = GpsTracker(this@AddInterview4Activity)
        var lat = gpsTracker.getLatitude()
        var lon = gpsTracker.getLongitude()
        if (lat == 0.0 && lon == 0.0) {
            lat = 37.552302
            lon = 126.992189
        }
        mMarkerOption.position(LatLng(lat!!, lon!!))
        googleMap.addMarker(mMarkerOption)
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat!!, lon!!), 16f))
        var tempLocation = getCurrentAddress(lat!!, lon!!)
        var splitTempLocation = tempLocation.split(" ")
        var tempLocation2 = ""
        for (i in splitTempLocation.indices) {
            if (i == 0) continue
            else tempLocation2 += splitTempLocation[i] + " "
        }
        mTvLocation.text = tempLocation2

        mMap.setOnMapClickListener { latLng ->
            googleMap.clear()
            val latitude = latLng.latitude // 위도
            val longitude = latLng.longitude // 경도
            println("위도 : $latitude, 경도 : $longitude")
            mMarkerOption.position(LatLng(latitude, longitude))
            googleMap.addMarker(mMarkerOption)
            var tempLocation = getCurrentAddress(latitude, longitude)
            var splitTempLocation = tempLocation.split(" ")
            var tempLocation2 = ""
            for (i in splitTempLocation.indices) {
                if (i == 0) continue
                else tempLocation2 += splitTempLocation[i] + " "
            }
            mTvLocation.text = tempLocation2
            mEtSearch.setText("")
        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private fun showDialogForLocationServiceSetting() {
        val builder = AlertDialog.Builder(this@AddInterview4Activity)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n" + "위치 설정을 수정하실래요?")
        builder.setCancelable(true)
        builder.setPositiveButton("설정") { dialog, id ->
            val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
        }
        builder.setNegativeButton(
            "취소"
        ) { dialog, id -> dialog.cancel() }
        builder.create().show()
    }

    private fun checkLocationServicesStatus(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onRequestPermissionsResult(
        permsRequestCode: Int,
        permissions: Array<String>,
        grandResults: IntArray
    ) {
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.size == REQUIRED_PERMISSIONS.size) {
            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            var checkResult = true
            // 모든 퍼미션을 허용했는지 체크합니다.
            for (result in grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    checkResult = false
                    break
                }
            }
            if (checkResult) {
            }//위치 값을 가져올 수 있음
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[0]
                    ) || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[1]
                    )
                ) {
                    Toast.makeText(
                        this@AddInterview4Activity,
                        "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@AddInterview4Activity,
                        "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun checkRunTimePermission() {
        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this@AddInterview4Activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this@AddInterview4Activity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음
        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@AddInterview4Activity,
                    REQUIRED_PERMISSIONS[0]
                )
            ) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(
                    this@AddInterview4Activity,
                    "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                    Toast.LENGTH_LONG
                ).show()
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(
                    this@AddInterview4Activity, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(
                    this@AddInterview4Activity, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }

    /**
     * 지오코더(구글꺼)에서 좌표를 가져온다.
     */
    private fun getPointFromGeoCoder(addr: String): Point {
        val point = Point()
        point.addr = addr

        val geocoder = Geocoder(this@AddInterview4Activity)
        val listAddress: List<Address>
        try {
            listAddress = geocoder.getFromLocationName(addr, 1)
        } catch (e: IOException) {
            e.printStackTrace()
            point.havePoint = false
            return point
        }

        if (listAddress.isEmpty()) {
            point.havePoint = false
            return point
        }

        point.havePoint = true
        point.x = listAddress[0].longitude
        point.y = listAddress[0].latitude
        return point
    }

    internal inner class Point {
        // 위도
        var x: Double = 0.toDouble()
        // 경도
        var y: Double = 0.toDouble()
        var addr: String? = null
        // 포인트를 받았는지 여부
        var havePoint: Boolean = false

        override fun toString(): String {
            val builder = StringBuilder()
            builder.append("x : ")
            builder.append(x)
            builder.append(" y : ")
            builder.append(y)
            builder.append(" addr : ")
            builder.append(addr)

            return builder.toString()
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

                    var intent = Intent(this@AddInterview4Activity, SearchResultActivity::class.java)
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
