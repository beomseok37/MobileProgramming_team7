package com.example.honbap

import android.Manifest
import android.content.pm.PackageManager
import android.icu.util.IslamicCalendar
import android.location.*
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
import com.google.android.gms.location.*
import com.google.android.gms.location.places.PlaceReport
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceLikelihood
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.PlacesStatusCodes
import noman.googleplaces.NRPlaces
import noman.googleplaces.PlaceType
import noman.googleplaces.PlacesException
import noman.googleplaces.PlacesListener
import java.lang.reflect.GenericArrayType
import java.lang.reflect.InvocationTargetException


class MapsFragment : Fragment(),PlacesListener {
    lateinit var loc:LatLng
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    var startupdate=false //위치정보 갱신을 start했는지의 여부
    lateinit var googleMap: GoogleMap
    lateinit var restaurantLoc:LatLng
    var clickitem=false
    lateinit var info:ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(arguments!=null){
            //리스트에서 클릭한 음식점의 주소 저장
            clickitem=true
            restaurantLoc= arguments!!.get("location") as LatLng
            info=ArrayList<String>(2)
            info.add(arguments!!.getString("name")!!)
            info.add(arguments!!.getString("tel")!!)
        }
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLocation()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync{
            googleMap=it
            if(clickitem) {
                showResLos()
            }
        }

    }

    fun showResLos(){
        val option=MarkerOptions()
        option.position(restaurantLoc)
        option.title(info[0])
        option.snippet(info[1])
        option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        googleMap.clear()
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantLoc,16.0f))
        googleMap.addMarker(option)!!.showInfoWindow()
        googleMap.addMarker(MarkerOptions()
            .position(loc)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))) //현재 위치도 나오게
        stopLocationUpdate()
    }


    fun initLocation(){
       fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(context!!)

        locationRequest= LocationRequest.create().apply {
            interval=15000
            fastestInterval=10000
            priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback=object :LocationCallback(){
            override fun onLocationResult(location: LocationResult) {
                if(location.locations.size==0) return
                //현재위치
                loc=LatLng(location.locations[location.locations.size-1].latitude,
                    location.locations[location.locations.size-1].longitude)
                if(!clickitem)
                    setCurrentLocation(loc) //위치이동하는 함수
                Log.i("location","LocationCallback()")

            }
        }
    }

    private fun startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        else{
            startupdate=true
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,locationCallback, Looper.getMainLooper()
            )
        }
    }

    //현재 위치로 위치 옮겨주는 함수
    fun setCurrentLocation(location:LatLng){
        val option=MarkerOptions()
        option.position(location)
        option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        googleMap.addMarker(option)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,16.0f))
        initPlaces()
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

    fun initPlaces(){
        if(!clickitem){
            NRPlaces.Builder()
                .listener(this)
                .key("AIzaSyCtpiZvm086B2evtw2F79PfS6ZocyY2Ey8")
                .latlng(loc.latitude,loc.longitude)
                .radius(700) //현재위치에서 700미터 이내 음식점
                .type(PlaceType.RESTAURANT)
                .build()
                .execute()
        }

    }


    override fun onPlacesFailure(e: PlacesException?) {

    }

    override fun onPlacesSuccess(places: MutableList<noman.googleplaces.Place>?) {
        activity?.runOnUiThread{
            if (places != null) {
                for(p in places){
                    val nearLoc=LatLng(p.latitude,p.longitude)
                    val option=MarkerOptions()
                    option.position(nearLoc)
                    option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                    option.title(p.name)
                    googleMap.addMarker(option)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,16.0f))
                }
            }
        }

    }

    override fun onPlacesFinished() {

    }

    override fun onPlacesStart() {

    }
}