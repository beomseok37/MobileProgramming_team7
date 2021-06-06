package com.example.honbap

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {
    lateinit var database: DatabaseReference
    lateinit var adapter: ChatAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var Firebase: FirebaseDatabase
    lateinit var chatref: DatabaseReference

    var data: ArrayList<Message> = ArrayList()
    var room_code=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        var group_info =intent.getStringExtra("data")

        if(group_info!=null){
           room_code=group_info
            var mess=intent.getStringExtra("message")
            Toast.makeText(this,mess,Toast.LENGTH_LONG).show()
        }

        Firebase = FirebaseDatabase.getInstance()
        chatref = Firebase.getReference(room_code)

        initrecyclerview()

        val button = findViewById<Button>(R.id.send)
        button.setOnClickListener {
            send()

            Firebase = FirebaseDatabase.getInstance()
            chatref=FirebaseDatabase.getInstance().getReference()


            val qw = findViewById<EditText>(R.id.context)
            var text= qw.text.toString()
            var name ="원유현"
            val tett =Message(name,text)
            chatref.child("chat").push().setValue(tett)

        }


        chatref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                var asd = snapshot.child("name")
                var name= asd.value.toString()
                var asd2= snapshot.child("message")
                var message =asd2.value.toString()
                data.add(Message(name,message))

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


    fun send() {

        Firebase = FirebaseDatabase.getInstance()
        chatref = FirebaseDatabase.getInstance().getReference()


        val qw = findViewById<EditText>(R.id.context)
        var text = qw.text.toString()
        var name = "원유현"
        val tett = Message(name, text)
        chatref.child(room_code).push().setValue(tett)


    }

    private fun initrecyclerview() {
        recyclerView = findViewById<RecyclerView>(R.id.ChatView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = ChatAdapter(data)

        recyclerView.adapter = adapter

    }
}