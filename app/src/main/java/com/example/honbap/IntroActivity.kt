package com.example.honbap

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.honbap.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply{

            IntroButton.setOnClickListener {
                val id= binding.ID.text.toString()
                val nick =binding.Nick.text.toString()

                val intent = Intent(this@IntroActivity,MainActivity::class.java)

                intent.putExtra("id",id)
                intent.putExtra("nick",nick)
                startActivity(intent)


            }
        }


    }
}