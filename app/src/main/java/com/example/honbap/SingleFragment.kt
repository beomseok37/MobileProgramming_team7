package com.example.honbap

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class SingleFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter:RoomAdapter

    lateinit var Firebase: FirebaseDatabase
    lateinit var chatref: DatabaseReference

    lateinit var Firebase2: FirebaseDatabase
    lateinit var chatref2: DatabaseReference

    lateinit var id:String
    lateinit var nick:String

    var Room_list:ArrayList<Room> = ArrayList()
    var user_list: ArrayList<uinfo> = ArrayList()
    var black_user:ArrayList<Int> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        read_black()
        user_list.clear()
        getuserid()
        Room_list.clear()


        arguments?.let{
            id=it.getString("id").toString()
            nick=it.getString("nick").toString()
        }


        initroom()




        val view=inflater.inflate(R.layout.fragment_single,container,false)

        recyclerView=view.findViewById(R.id.single_recycle)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        adapter=RoomAdapter(Room_list)

        adapter.itemClickListener=object :RoomAdapter.OnItemClickListener{
            override fun OnItemClick(holder: RoomAdapter.ViewHolder, view: View, data: Room, position: Int) {

                val intent = Intent(requireActivity(),ChatActivity2::class.java)
                intent.putExtra("mynick",nick)
                intent.putExtra("myid",id)
                intent.putExtra("which_singleroom",data.firebase_position)
                startActivity(intent)

            }
        }
        recyclerView.adapter=adapter




        // Inflate the layout for this fragment
        return view
    }

    fun initroom(){

        Firebase = FirebaseDatabase.getInstance()
        chatref = Firebase.getReference("SingleRoom_Info")
        var searchdb= chatref.child(id)

        searchdb.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var num =snapshot.key.toString()
                var room_code=""

                var flag= false
                for(i in black_user){

                    if(i.toString()==num) {
                        flag=true
                        break;
                    }
                }

                if(num!=id) {
                    if (num.toInt() < id.toInt())
                        room_code = "SingleChat/" + num + "_" + id
                    else
                        room_code = "SingleChat/" + id + "_" + num



                    for(i in user_list){
                        if(i.uid==num) {
                            var roomnick=i.nick
                            if(flag==true){
                                roomnick+=" [차단된 사용자]"
                            }
                            Room_list.add(Room(roomnick, room_code))
                            adapter.notifyDataSetChanged()
                            break

                        }
                    }

                }

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })




    }

    fun getuserid(){
        Firebase2 = FirebaseDatabase.getInstance()
        chatref2=Firebase2.getReference()

        chatref2.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val test =snapshot.child("information")

                for(ds in test.children){
                    if(ds.key.toString()=="auto")
                        break
                    else {
                        var uid = ds.key.toString()
                        var nick = ds.child("userNickname").value as String
                        user_list.add(uinfo(uid, nick))
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    fun read_black(){

        try {
            val scan2 = Scanner(requireActivity().openFileInput("black.txt"))
            readFileScan(scan2)
        }catch (e: Exception){

        }


    }
    fun readFileScan(scan: Scanner){
        while(scan.hasNextLine()){
            val id=scan.nextLine()
            black_user.add(id.toInt())
        }
        scan.close()
    }


}