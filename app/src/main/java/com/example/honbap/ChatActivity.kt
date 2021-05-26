package com.example.honbap

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatActivity : AppCompatActivity() {

    lateinit var adapter: ChatAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var Firebase: FirebaseDatabase
    lateinit var chatref: DatabaseReference

    lateinit var nick:String
    lateinit var id:String


    var data: ArrayList<Message> = ArrayList()
    var room_code=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        var group_info =intent.getStringExtra("which_grouproom")

        if(group_info!=null){
           room_code="GroupChat/"+group_info
            var mess=intent.getStringExtra("message")
            Toast.makeText(this,mess,Toast.LENGTH_LONG).show( )

            nick=intent.getStringExtra("nick").toString()
            id= intent.getStringExtra("id").toString()

        }

        //그룹 채팅시 입장메세지 + intent 정보 물려받기


        Firebase = FirebaseDatabase.getInstance()
        chatref = Firebase.getReference(room_code)

        initrecyclerview()

        val button = findViewById<Button>(R.id.send)
        button.setOnClickListener {
            send()

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
        adapter = ChatAdapter(data)

        adapter.itemClickListener=object :ChatAdapter.OnItemClickListener{
            override fun OnItemClick(holder: ChatAdapter.ViewHolder, view: View, data: Message, position: Int) {

                Toast.makeText(this@ChatActivity,data.id,Toast.LENGTH_SHORT).show()




            }
        }

        recyclerView.adapter = adapter

    }
    //리사이클러뷰
}