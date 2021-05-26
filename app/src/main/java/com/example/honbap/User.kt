package com.example.honbap

data class User(val name:String,val password:String)

class Message(var name:String, var message: String, var id:String, var time:String)

data class Room(var name:String, var firebase_position:String)


