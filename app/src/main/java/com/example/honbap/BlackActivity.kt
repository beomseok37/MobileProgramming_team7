package com.example.honbap

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.PrintStream
import java.lang.Exception
import java.util.*

class BlackActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: BlackAdapter

    var black_user:ArrayList<ublack> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_black)

        recyclerView=findViewById(R.id.blackrecycler)
        init()
        initrecycler()
    }

    fun init(){

        black_user.clear()

        try {
            val scan = Scanner(openFileInput("black.txt"))
            while(scan.hasNextLine()){
                val id=scan.nextLine()
                black_user.add(ublack(id))
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
                var file = File("/data/data/com.example.honbap/files/black.txt")
                file.delete()

                val output= PrintStream(openFileOutput("black.txt", Context.MODE_APPEND))

                for(i in black_user){
                    output.println(i.uid)
                    output.close()

                }
                Toast.makeText(this@BlackActivity,"블랙리스트에서 삭제되었습니다",Toast.LENGTH_SHORT).show()
                adapter.notifyDataSetChanged()
            }




        }
        recyclerView.adapter=adapter



    }






}