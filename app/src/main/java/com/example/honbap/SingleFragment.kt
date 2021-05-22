package com.example.honbap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class SingleFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter:RoomAdapter

    var Room_list:ArrayList<Room> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Room_list.clear()
        initroom()


        val view=inflater.inflate(R.layout.fragment_single,container,false)

        recyclerView=view.findViewById(R.id.single_recycle)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        adapter=RoomAdapter(Room_list)

        adapter.itemClickListener=object :RoomAdapter.OnItemClickListener{
            override fun OnItemClick(holder: RoomAdapter.ViewHolder, view: View, data: Room, position: Int) {

//                val intent = Intent(requireActivity(),ChatActivity::class.java)
//                intent.putExtra("data",data.firebase_position)
//                startActivity(intent)




            }
        }
        recyclerView.adapter=adapter




        // Inflate the layout for this fragment
        return view
    }

    fun initroom(){




    }


}