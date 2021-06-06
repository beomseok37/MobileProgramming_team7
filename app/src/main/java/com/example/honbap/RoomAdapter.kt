package com.example.honbap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RoomAdapter (val items:ArrayList<Room>): RecyclerView.Adapter<RoomAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(holder: ViewHolder, view: View, data: Room, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null


//    fun moveItem(oldPos: Int, newPos: Int) {
//        val item = items[oldPos]
//        items.removeAt(oldPos)
//        items.add(newPos, item)
//
//        notifyItemMoved(oldPos, newPos)
//
//    }
//
//    fun removeItem(oldPos: Int) {
//
//        items.removeAt(oldPos)
//        notifyItemRemoved(oldPos)
//
//
//    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val room_name: TextView = itemView.findViewById(R.id.room_name)



        init{
            itemView.setOnClickListener{
                itemClickListener?.OnItemClick(this,it,items[adapterPosition],adapterPosition)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.roomlist, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.room_name.text =items[position].name


    }

    override fun getItemCount(): Int {
        return items.size
    }
}