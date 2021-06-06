package com.example.honbap

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LoginDBAdapter(val context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object{
        val DB_NAME="logindb.db"
        val DB_VERSION=1
        val TABLE_NAME="USER"
        val UID="uid"
        val USEREMAIL="useremail"
        val USERPASSWORD="userpassword"
        val SAVEID="saveid"
        val AUTOLOGIN="autologin"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val create_table = "create table if not exists $TABLE_NAME("+
                "$UID integer, "+
                "$USEREMAIL text, "+
                "$USERPASSWORD text," +
                "$SAVEID integer," +
                "$AUTOLOGIN integer);"
        db!!.execSQL(create_table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val drop_table = "drop table if exists $TABLE_NAME"
        db!!.execSQL(drop_table)
        onCreate(db)
    }
    fun loginbefore():Boolean{
        val strsql="select * from $TABLE_NAME;"
        val db=readableDatabase
        val cursor = db.rawQuery(strsql,null)
        val flag=cursor.count!=0
        cursor.close()
        db.close()
        return flag
    }
    fun insertProduct(user:UserInDB):Boolean{
        val values = ContentValues()
        values.put(UID,user.UserID)
        values.put(USEREMAIL,user.UserEmail)
        values.put(USERPASSWORD,user.UserPassword)
        values.put(SAVEID,user.SaveID)
        values.put(AUTOLOGIN,user.AutoLogin)
        val db = writableDatabase
        val flag=db.insert(TABLE_NAME,null,values)>0
        db.close()
        return flag
    }
    fun updateProduct(user: UserInDB): Boolean {
        val uid=user.UserID
        val strsql="select * from $TABLE_NAME where $UID='$uid';"
        val db=writableDatabase
        val cursor = db.rawQuery(strsql,null)
        val flag=cursor.count!=0
        if(flag){
            cursor.moveToFirst()
            val values= ContentValues()
            values.put(USEREMAIL,user.UserEmail)
            values.put(USERPASSWORD,user.UserPassword)
            values.put(SAVEID,user.SaveID)
            values.put(AUTOLOGIN,user.AutoLogin)
            db.update(TABLE_NAME,values,"$UID=?",arrayOf(uid.toString()))
        }
        cursor.close()
        db.close()
        return flag
    }
    fun loginfun():Int{
        val strsql="select * from $TABLE_NAME;"
        val db=readableDatabase
        val cursor = db.rawQuery(strsql,null)
        cursor.moveToFirst()
        val saveemail=cursor.getInt(3)
        val autologin=cursor.getInt(4)
        cursor.close()
        db.close()
        return saveemail+autologin*2
    }
    fun getEmail():String{
        val strsql="select * from $TABLE_NAME;"
        val db=readableDatabase
        val cursor = db.rawQuery(strsql,null)
        cursor.moveToFirst()
        val email=cursor.getString(1)
        cursor.close()
        db.close()
        return email
    }

    fun getInfo():Int{
        val strsql="select * from $TABLE_NAME;"
        val db=readableDatabase
        val cursor = db.rawQuery(strsql,null)
        cursor.moveToFirst()
        val info=cursor.getInt(0)
        cursor.close()
        db.close()
        return info
    }
}