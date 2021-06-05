package com.example.honbap

data class userinfo(var email:String,var password:String, var age:Int,var sex:String, var nickname:String) {
    constructor():this("NONE","NONE",0,"NONE","NONE")
}