package com.example.test
import android.location.Address
import android.location.Geocoder
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity()
{
    lateinit var binding: ActivityMainBinding
    val itemFragment=ItemFragment()
    val MapsFragment=MapsFragment()
    companion object{
        val datalist= ArrayList<Data>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadInfo()
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
            }
            else{
                val fragment=supportFragmentManager.beginTransaction()
                fragment.replace(R.id.frameLayout,MapsFragment)
                fragment.commit()

            }
        }
    }
    private fun loadInfo() {
        try {
            val inputStream = assets.open("info.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val str = String(buffer, Charsets.UTF_8)
            val json = JSONObject(str)
            val data = json.getJSONArray("DATA")
            for (i in 0 until data.length()) {
                //영업상태인 음식점의 이름, 전화번호, 주소 가져옴
                if (data.getJSONObject(i).getString("dtlstatenm") == "영업")
                    datalist.add(
                        Data(
                            data.getJSONObject(i).getString("bplcnm"),
                            data.getJSONObject(i).getString("sitetel"),
                            data.getJSONObject(i).getString("sitewhladdr")
                        )
                    )
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }
}


