package com.example.honbap

import android.graphics.Color

class Message(var name:String, var message: String, var id:String, var time:String)

data class Room(var name:String, var firebase_position:String)

data class uinfo(var uid:String, var nick:String)

data class ucolor(var uid:String, var color: Int)

data class ublack(var uid:String)
