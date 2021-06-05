package com.example.honbap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.honbap.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val groupfrag=GroupFragment()
    val singlefrag=SingleFragment()
    val settingfrag=SettingFragment()


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

            Setting.setOnClickListener {
                val frag = supportFragmentManager.beginTransaction()
                frag.replace(R.id.frameLayout, settingfrag)
                frag.commit()

            }
        }


    }
}