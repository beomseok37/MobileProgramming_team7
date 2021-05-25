package com.example.test

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsFragment : Fragment() {
    var loc=LatLng(37.5408, 127.0793) //초기위치
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    var startupdate=false //위치정보 갱신을 start했는지의 여부
    lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLocation()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync{
            googleMap=it
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,16.5f))
            googleMap.setMinZoomPreference(10.0f)
            googleMap.setMaxZoomPreference(18.0f)
            val option = MarkerOptions()
            option.position(loc)
            option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            val mk1 = googleMap.addMarker(option)
            mk1.showInfoWindow()
        }
    }

    fun initLocation(){
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(context)

        locationRequest= LocationRequest.create().apply {
            interval=10000
            fastestInterval=5000
            priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback=object :LocationCallback(){
            override fun onLocationResult(location: LocationResult) {
                if(location.locations.size==0) return
                loc=LatLng(location.locations[location.locations.size-1].latitude,
                    location.locations[location.locations.size-1].longitude)
                setCurrentLocation(loc)
                Log.i("location","LocationCallback()")

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            1000->{
                if(checkLocationServicesStatus()){
                    Toast.makeText(context,"GPS 활성화 되었음",Toast.LENGTH_SHORT).show()
                    startLocationUpdates()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==100){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //허용한경우
                startLocationUpdates()
            }else{
                Toast.makeText(context,"위치정보를 제공하셔야 합니다.", Toast.LENGTH_SHORT).show()
                setCurrentLocation(loc)
            }
        }
    }

    private fun startLocationUpdates() {
        if(context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED){
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ),100
                )
            }

        }else{
            if(!checkLocationServicesStatus()){
                showLocationServicesSetting()
            }else{
                startupdate=true
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,locationCallback, Looper.getMainLooper()
                )
                Log.i("location","startLocationUpdates()")
            }
        }
    }
    private fun showLocationServicesSetting() {
        val builder= AlertDialog.Builder(context!!)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage(
            "앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"+
                    "위치 설정을 허용하겠습니까?"
        )
        builder.setPositiveButton("설정", DialogInterface.OnClickListener { dialog, id ->
            //위치정보 세팅하는 설정 창으로 이동하기 위한 action
            val GpsSettingIntent= Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(GpsSettingIntent,1000)
        })
        builder.setNegativeButton("취소",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
                setCurrentLocation(loc)
            })
        builder.create().show()
    }

    private fun checkLocationServicesStatus(): Boolean{
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        //GPS가 사용가능한지 , 사용가능->true
    }

    fun setCurrentLocation(location:LatLng){
        val option=MarkerOptions()
        option.position(location)
        option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        googleMap.addMarker(option)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,16.0f))
    }

    private fun stopLocationUpdate(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        startupdate=false
    }


    override fun onResume() {
        super.onResume()
        if(!startupdate)
            startLocationUpdates()

    }
    override fun onPause() {
        super.onPause()
        stopLocationUpdate()
    }


}