package com.example.honbap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.honbap.databinding.ActivityMainBinding
import com.google.android.gms.maps.MapFragment
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val groupfrag=GroupFragment()
    val singlefrag=SingleFragment()
    val settingfrag=SettingFragment()
    val resfrag= RestaurantFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {


            Groupchat.setOnClickListener {

                val frag = supportFragmentManager.beginTransaction()
                frag.replace(R.id.frameLayout, groupfrag)
                frag.commit()

            }

            Singlechat.setOnClickListener {

                val frag = supportFragmentManager.beginTransaction()
                frag.replace(R.id.frameLayout, singlefrag)
                frag.commit()

            }

            Map.setOnClickListener {

                val frag=supportFragmentManager.beginTransaction()
                frag.replace(R.id.frameLayout,resfrag)
                frag.commit()

            }

            Setting.setOnClickListener {
                val frag = supportFragmentManager.beginTransaction()
                frag.replace(R.id.frameLayout, settingfrag)
                frag.commit()

            }
        }


    }


}