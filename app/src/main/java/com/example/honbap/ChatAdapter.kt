package com.example.honbap

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class ChatAdapter (val items:ArrayList<Message>,val id:String): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    val r =Random
    val groupcolor :ArrayList<ucolor> = ArrayList()

    val mycolor=Color.rgb(r.nextInt(255),r.nextInt(255),r.nextInt(255))



    interface OnItemClickListener {
        fun OnItemClick(holder: ViewHolder, view: View, data: Message, position: Int)
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




        init{

            itemView.setOnClickListener{

                itemClickListener?.OnItemClick(this,it,items[adapterPosition],adapterPosition)


            }

        }
    }




    override fun getItemViewType(position: Int): Int {

        var find =false
        for(i in groupcolor){
            if(i.uid==items[position].id) {
                find=true
                break
            }
        }
        if(find==false) {
            val new =ucolor(items[position].id,Color.rgb(r.nextInt(255),r.nextInt(255),r.nextInt(255)))
            groupcolor.add(new)

        }

        if(id==items[position].id)
            return 0
        else
            return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        when(viewType){

            0-> {
                val view1 = LayoutInflater.from(parent.context).inflate(R.layout.mychat, parent, false)
                return ViewHolder(view1)
            }

            else->  {
                val view2 = LayoutInflater.from(parent.context).inflate(R.layout.otherchat, parent, false)

                return ViewHolder(view2)
            }
        }



    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if(holder.itemViewType==0) {



            val mname: TextView = holder.itemView.findViewById(R.id.my_name)
            val mmessage: TextView = holder.itemView.findViewById(R.id.my_msg)
            val mtime: TextView = holder.itemView.findViewById(R.id.my_time)
           val img= holder.itemView.findViewById<ImageView>(R.id.my_imageView)


            img.setColorFilter(mycolor,PorterDuff.Mode.SRC_IN)
            mname.text =items[position].name
            mmessage.text=items[position].message
            mtime.text=items[position].time



        }

        else{

            val oname: TextView = holder.itemView.findViewById(R.id.other_name)
            val omessage: TextView = holder.itemView.findViewById(R.id.other_msg)
            val otime: TextView = holder.itemView.findViewById(R.id.other_time)
           val img= holder.itemView.findViewById<ImageView>(R.id.other_imageView)

            for(i in groupcolor)
                if(i.uid==items[position].id){
                    img.setColorFilter(i.color,PorterDuff.Mode.SRC_IN)
                    break
                }

            oname.text = items[position].name
            omessage.text = items[position].message
            otime.text = items[position].time
        }


    }



    override fun getItemCount(): Int {
        return items.size
    }
}
