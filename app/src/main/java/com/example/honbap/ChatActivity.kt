package com.example.honbap

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.io.PrintStream
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    lateinit var adapter: ChatAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var Firebase: FirebaseDatabase
    lateinit var chatref: DatabaseReference

    lateinit var id:String
    lateinit var nick:String

    var data: ArrayList<Message> = ArrayList()
    var black_user:ArrayList<Int> = ArrayList()
    var room_code=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        var group_info =intent.getStringExtra("which_grouproom")

        if(group_info!=null){
            room_code="GroupChat/"+group_info
            var mess=intent.getStringExtra("message")
            Toast.makeText(this,mess,Toast.LENGTH_LONG).show( )
            id= intent.getStringExtra("id").toString()
            nick= intent.getStringExtra("nick").toString()

        }
        //그룹 채팅시 입장메세지 + intent 정보 물려받기


        Firebase = FirebaseDatabase.getInstance()
        chatref = Firebase.getReference(room_code)


        initrecyclerview()
        read_black()


        val button = findViewById<Button>(R.id.send)
        button.setOnClickListener {
            send()

            recyclerView.scrollToPosition(data.size)

            var imm: InputMethodManager?=null
            imm=getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(button.windowToken,0)


        }



        chatref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                var db_name = snapshot.child("name")
                var name= db_name.value.toString()
                var db_message= snapshot.child("message")
                var message =db_message.value.toString()
                var db_userid= snapshot.child("id")
                var user_id =db_userid.value.toString()
                var db_time= snapshot.child("time")
                var time =db_time.value.toString()
                data.add(Message(name,message,user_id,time))

                adapter.notifyDataSetChanged()
                recyclerView.scrollToPosition(adapter.itemCount-1)

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // TODO("Not yet implemented")
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                // TODO("Not yet implemented")
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //  TODO("Not yet implemented")
            }
            override fun onCancelled(error: DatabaseError) {
                // TODO("Not yet implemented")
            }


        })
    }
    //채팅방에 데이터 추가시 갱신


    fun send() {

        Firebase = FirebaseDatabase.getInstance()
        chatref = FirebaseDatabase.getInstance().getReference()


        val tosend = findViewById<EditText>(R.id.context)
        var text = tosend.text.toString()
        var name = nick
        var user_id =id

        var time_now =LocalDateTime.now()
        val format =DateTimeFormatter.ofPattern("HH:mm")
        val time= time_now.format(format)

        val tett = Message(name, text,user_id,time)
        chatref.child(room_code).push().setValue(tett)
        tosend.setText("")


    }//보낼때

    private fun initrecyclerview() {
        recyclerView = findViewById<RecyclerView>(R.id.ChatView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = ChatAdapter(data,id)

        adapter.itemClickListener=object :ChatAdapter.OnItemClickListener{
            override fun OnItemClick(holder: ChatAdapter.ViewHolder, view: View, data: Message, position: Int) {
                if (id != data.id) {
                    val dialog = AlertDialog.Builder(this@ChatActivity)
                    dialog.setTitle("1:1채팅")
                    dialog.setMessage(data.name + "님 과 1:1채팅을 하시겠습니까?")
                    dialog.setIcon(R.drawable.ic_baseline_connect_without_contact_24)
                    dialog.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->

                        val intent = Intent(this@ChatActivity, ChatActivity2::class.java)
                        intent.putExtra("other_id", data.id)
                        intent.putExtra("other_nick", data.name)
                        intent.putExtra("mynick", nick)
                        intent.putExtra("myid", id)

                        addSingleChatinfo(id, data.id)

                        startActivity(intent)
                        finish()
                    })

                    dialog.setNeutralButton("차단하기", DialogInterface.OnClickListener { dialog, which ->
                        var flag= false
                       for(i in black_user){

                          if(i==data.id.toInt()) {
                              Toast.makeText(
                                  this@ChatActivity,
                                  "이미 차단된 사용자입니다.",
                                  Toast.LENGTH_LONG
                              ).show()
                              flag=true
                              break;
                          }
                       }
                        if(flag==false) {
                            black_user.add(data.id.toInt())
                            val output =
                                PrintStream(openFileOutput("black.txt", Context.MODE_APPEND))

                            output.println(data.id)
                            output.println(data.name)
                            output.close()

                            Toast.makeText(
                                this@ChatActivity,
                                data.name + "을 차단하였습니다.",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    })


                    dialog.setNegativeButton("취소", DialogInterface.OnClickListener { dialog, which ->
                        Toast.makeText(this@ChatActivity, data.name + "님 과 1:1채팅을 취소하였습니다.", Toast.LENGTH_SHORT).show()

                    })

                    dialog.show()


                }
            }


        }

        recyclerView.adapter = adapter


    }
    //리사이클러뷰

    fun addSingleChatinfo(id1:String, id2:String){
        chatref = Firebase.getReference("SingleRoom_Info")
        var id_1=chatref.child(id1)
        id_1.child(id2).setValue("true")
        var id_2=chatref.child(id2)
        id_2.child(id1).setValue("true")

    }
    fun read_black(){

        try {
            val scan2 = Scanner(openFileInput("black.txt"))
            readFileScan(scan2)
        }catch (e: Exception){

        }


    }
    fun readFileScan(scan:Scanner){
        while(scan.hasNextLine()){
            val id=scan.nextLine()
            black_user.add(id.toInt())
        }
        scan.close()
    }

}