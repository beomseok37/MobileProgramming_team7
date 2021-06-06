package com.example.honbap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter (val items:ArrayList<Message>): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(holder: ViewHolder, view: View, data: Message, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null


    fun moveItem(oldPos: Int, newPos: Int) {
        val item = items[oldPos]
        items.removeAt(oldPos)
        items.add(newPos, item)

        notifyItemMoved(oldPos, newPos)

    }

    fun removeItem(oldPos: Int) {

        items.removeAt(oldPos)
        notifyItemRemoved(oldPos)


    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.my_name)
        val message: TextView = itemView.findViewById(R.id.my_msg)

//        init{
//
//            itemView.setOnClickListener{
//
//                itemClickListener?.OnItemClick(this,it,items[adapterPosition],adapterPosition)
//

//            }
//
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view2 = LayoutInflater.from(parent.context).inflate(R.layout.otherchat, parent, false)

        return ViewHolder(view2)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name.text =items[position].name
        holder.message.text=items[position].message

    }

    override fun getItemCount(): Int {
        return items.size
    }
}

