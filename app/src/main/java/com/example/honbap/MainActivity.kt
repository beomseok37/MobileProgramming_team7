package com.example.honbap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.honbap.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var id:String
    lateinit var nick:String

    val groupfrag=GroupFragment()
    val singlefrag=SingleFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        id= intent.getStringExtra("id").toString()
        nick=intent.getStringExtra("nick").toString()
        val bundle=Bundle()
        bundle.putString("id",id)
        bundle.putString("nick",nick)



        binding.apply {


            Groupchat.setOnClickListener {

                val frag = supportFragmentManager.beginTransaction()
                groupfrag.arguments=bundle
                frag.replace(R.id.frameLayout, groupfrag)
                frag.commit()

            }

            Singlechat.setOnClickListener {

                val frag = supportFragmentManager.beginTransaction()
                singlefrag.arguments=bundle
                frag.replace(R.id.frameLayout, singlefrag)
                frag.commit()

            }

            Setting.setOnClickListener {


            }
        }


    }
}