package com.example.honbap

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.honbap.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val groupfrag=GroupFragment()
    val singlefrag=SingleFragment()
    val settingfrag=SettingFragment()
    val resfrag= RestaurantFragment()
    lateinit var LoginDBHelper: LoginDBAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        LoginDBHelper = LoginDBAdapter(this)
//        val data=intent.getSerializableExtra("logininfo") as UserInDB
//        Log.i("useremail",data.UserEmail)
        autologin()
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
    fun autologin(){
        if(LoginDBHelper.loginfun()>=2){
            //자동로그인으로 로그인할 경우
            val successintent = Intent(this,MainActivity::class.java)
            val temp=LoginDBHelper.logininfo()
            Log.i("useremail",temp.UserEmail)
        }
    }
}