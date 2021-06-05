package com.example.test


import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView



class MyItemRecyclerViewAdapter(
     val values: ArrayList<Data>
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.name
        val info=item.tel+"\n"+item.address
        holder.contentView.text = info
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_number)
        val contentView: TextView = view.findViewById(R.id.content)
        init{
            itemView.setOnClickListener {
                itemClickListener?.OnItemClick(this,it,values[absoluteAdapterPosition])
            }

        }

    }
    interface OnitemClickListener{
        fun OnItemClick(holder: ViewHolder,view: View,data:Data)
    }
    var itemClickListener:OnitemClickListener?=null
}