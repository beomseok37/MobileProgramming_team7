package com.example.honbap

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class GroupFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter:RoomAdapter

    var Room_list:ArrayList<Room> = ArrayList()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Room_list.clear()
        initroom()


        val view=inflater.inflate(R.layout.fragment_group,container,false)

        recyclerView=view.findViewById(R.id.group_recycle)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        adapter=RoomAdapter(Room_list)

        adapter.itemClickListener=object :RoomAdapter.OnItemClickListener{
            override fun OnItemClick(holder: RoomAdapter.ViewHolder, view: View, data: Room, position: Int) {

                val intent = Intent(requireActivity(),ChatActivity::class.java)
                val mess =data.name+"에 오신것을 환영합니다."
                intent.putExtra("data",data.firebase_position)
                intent.putExtra("message",mess)

                startActivity(intent)





            }
            }
        recyclerView.adapter=adapter




        return view
    }


    fun initroom(){
        Room_list.add(Room("1번 방","group_1"))
        Room_list.add(Room("2번 방","group_2"))
        Room_list.add(Room("3번 방","group_3"))
        Room_list.add(Room("4번 방","group_4"))
        Room_list.add(Room("5번 방","group_5"))


    }

}