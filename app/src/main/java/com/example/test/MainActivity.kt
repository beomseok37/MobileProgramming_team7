package com.example.test
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isGone

import com.example.test.databinding.ActivityMainBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val itemFragment=ItemFragment()
    val MapsFragment=MapsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    private fun init() {
        val fragment=supportFragmentManager.beginTransaction()
        fragment.replace(R.id.frameLayout,MapsFragment)
        fragment.commit()
        binding.listBtn.setOnClickListener {
            if(!itemFragment.isVisible) {
                val fragment = supportFragmentManager.beginTransaction()
                fragment.addToBackStack(null)
                fragment.replace(R.id.frameLayout, itemFragment)
                fragment.commit()
            //    binding.listBtn.visibility=View.INVISIBLE

            }
            else{
                val fragment=supportFragmentManager.beginTransaction()
                fragment.replace(R.id.frameLayout,MapsFragment)
                fragment.commit()
            }

        }

    }





}