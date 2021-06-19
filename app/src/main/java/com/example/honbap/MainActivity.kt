package com.example.honbap

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.honbap.databinding.ActivityMainBinding
import com.google.android.gms.location.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.type.LatLng
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val groupfrag=GroupFragment()
    val singlefrag=SingleFragment()
    val settingfrag=SettingFragment()
    val resfrag= RestaurantFragment()
    lateinit var LoginDBHelper: LoginDBAdapter

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    var isAvailable by Delegates.notNull<Boolean>()
    lateinit var id:String
    lateinit var nick:String
    lateinit var bundle:Bundle

    lateinit var Firebase: FirebaseDatabase
    var flag =false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        LoginDBHelper = LoginDBAdapter(this)
//        val data=intent.getSerializableExtra("logininfo") as UserInDB
//        Log.i("useremail",data.UserEmail)
        autologin()


        id=intent.getStringExtra("id").toString()


        autologin()
        getuserid()
        bundle= Bundle()
        bundle.putString("id",id)
        settingfrag.arguments = bundle

        initLocation()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,locationCallback, Looper.getMainLooper()
        )

        binding.bottomNavigationView.selectedItemId=R.id.Setting

        changeFragment(settingfrag)
        binding.apply {
            bottomNavigationView.setOnNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.Groupchat->{
                        val frag = supportFragmentManager.beginTransaction()
                        bundle= Bundle()
                        bundle.putString("id",id)
                        bundle.putString("nick",nick)
                        groupfrag.arguments = bundle
                        changeFragment(groupfrag)

                    }
                    R.id.Singlechat->{
                        bundle= Bundle()
                        bundle.putString("id",id)
                        bundle.putString("nick",nick)
                        singlefrag.arguments = bundle
                        changeFragment(singlefrag)

                    }
                    R.id.Map->{
//                        bundle= Bundle()
//                        bundle.putBoolean("available",isAvailable)
//                        resfrag.arguments=bundle
                        changeFragment(resfrag)
                    }
                    R.id.Setting->{
                        bundle= Bundle()
                        bundle.putString("id",id)
                        bundle.putString("nick",nick)
                        settingfrag.arguments = bundle
                        changeFragment(settingfrag)
                    }
                }
                true
            }

        }


    }
    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout,fragment)
            .commit()
    }
    fun autologin(){
        if(LoginDBHelper.loginfun()>=2){
            //자동로그인으로 로그인할 경우
            val successintent = Intent(this,MainActivity::class.java)
            val temp=LoginDBHelper.logininfo()
            id=temp.UserID.toString()
            Log.i("useremail",temp.UserEmail)
        }
    }

    fun getuserid() {
        Firebase = FirebaseDatabase.getInstance()
        val myref = Firebase.getReference()

        myref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val test = snapshot.child("information")

                for (ds in test.children) {
                    if (ds.key == id) {
                        nick = ds.child("userNickname").value as String
                        flag = true
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun initLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create().apply {
            interval = 15000
            fastestInterval = 15000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
         locationCallback=object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                //val konkuk=LatLng(37.5408, 127.0793)
                val konkuk=Location(LocationManager.GPS_PROVIDER)
                konkuk.latitude=37.5408
                konkuk.longitude=127.0793

                val loc=Location(LocationManager.GPS_PROVIDER)
                loc.latitude=p0.locations[p0.locations.size-1].latitude
                loc.longitude=p0.locations[p0.locations.size-1].longitude
                val distance=loc.distanceTo(konkuk)
                val dis2 =distance/1000

                if(distance>4000){
                    val dialog = AlertDialog.Builder(this@MainActivity)
                    dialog.setTitle("거리 초과")
                    dialog.setMessage("건국대로부터 ${dis2}km 떨어져있습니다.")
                    dialog.setIcon(R.drawable.ic_baseline_place_24)
                    dialog.setNegativeButton("무시 후 계속 사용", DialogInterface.OnClickListener { dialog, which ->
                    })
                    dialog.setPositiveButton("종료", DialogInterface.OnClickListener { dialog, which ->
                        finish()
                    })
                    dialog.show()
                }
            }
        }
    }

}

