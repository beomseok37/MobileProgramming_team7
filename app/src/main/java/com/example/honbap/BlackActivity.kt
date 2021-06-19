package com.example.honbap

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.*
import java.io.File
import java.io.PrintStream
import java.lang.Exception
import java.util.*

class BlackActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: BlackAdapter
    lateinit var txt:String


    var black_user:ArrayList<ublack> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_black)

        var id2= intent.getStringExtra("id").toString()
        txt="black"+id2+".txt"

        recyclerView=findViewById(R.id.blackrecycler)
        init()
        initrecycler()
    }

    fun init(){

        black_user.clear()

        try {
            val scan = Scanner(openFileInput(txt))
            while(scan.hasNextLine()){
                val id=scan.nextLine()
                val nick=scan.nextLine()
                black_user.add(ublack(id,nick))
            }
            scan.close()
        }catch (e: Exception){

        }


    }

    fun initrecycler(){

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        adapter=BlackAdapter(black_user)

        adapter.itemClickListener=object :BlackAdapter.OnItemClickListener{
            override fun OnItemClick(holder: BlackAdapter.ViewHolder, view: View, data: ublack, position: Int) {

                black_user.remove(data)
                var path ="/data/data/com.example.honbap/files/"+txt
                var file = File(path)
                file.delete()

                val output= PrintStream(openFileOutput(txt, Context.MODE_APPEND))

                for(i in black_user){
                    output.println(i.uid)
                    output.println(i.nick)
                    output.close()

                }
                Toast.makeText(this@BlackActivity,data.nick+"을 블랙리스트에서 삭제되었습니다",Toast.LENGTH_SHORT).show()
                adapter.notifyDataSetChanged()


            }




        }
        recyclerView.adapter=adapter



    }





}