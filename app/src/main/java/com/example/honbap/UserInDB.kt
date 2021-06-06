package com.example.honbap

import java.io.Serializable

class UserInDB(val UserID:Int,val UserEmail:String,val UserPassword:String,val SaveID:Int,val AutoLogin:Int):Serializable {
}