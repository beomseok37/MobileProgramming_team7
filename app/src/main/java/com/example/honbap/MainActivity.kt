package com.example.honbap

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.honbap.databinding.ActivityMainBinding
import com.google.firebase.database.*
import kotlin.math.sin

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val groupfrag=GroupFragment()
    val singlefrag=SingleFragment()
    val settingfrag=SettingFragment()
    val resfrag= RestaurantFragment()
    lateinit var LoginDBHelper: LoginDBAdapter

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
                           changeFragment(resfrag)
                       }
                       R.id.Setting->{
                           changeFragment(settingfrag)
                       }
                   }
                    true
                }

            }


    }
    fun changeFragment(fragment:Fragment) {
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

    fun getuserid(){
        Firebase = FirebaseDatabase.getInstance()
        val myref=Firebase.getReference()

        myref.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val test =snapshot.child("information")

                for(ds in test.children){
                    if(ds.key==id) {
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
}