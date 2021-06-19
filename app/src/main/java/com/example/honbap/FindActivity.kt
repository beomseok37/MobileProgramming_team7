package com.example.honbap

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.honbap.databinding.ActivityFindBinding
import com.google.firebase.database.*

class FindActivity : AppCompatActivity() {
    lateinit var binding3: ActivityFindBinding
    lateinit var rdb: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding3 = ActivityFindBinding.inflate(layoutInflater)
        setContentView(binding3.root)
        init3()
    }
    fun init3(){
        var useremail:String?
        rdb= FirebaseDatabase.getInstance().getReference("information")
        var count:Int=0
        rdb.child("auto").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                count=snapshot.value.toString().toInt()
            }

        })
        binding3.apply{
//            returnbtn.setOnClickListener {
//                finish()
//            }
            confirmIDbtn.setOnClickListener {
                //firebase에서 해당 이메일이 있음을 확인해본다.
                val findemail=findemail.text.toString()
                checkemail(count,findemail)
            }
            confirmPasswordbtn.setOnClickListener {
                //firebase에서 해당 이메일의 아이디가 있을 경우 비밀번호를 출력해준다.
                val findemail=findemail.text.toString()
                checkpassword(count,findemail)
            }
        }
    }
    fun AlertDig(str:String){
        val builder = AlertDialog.Builder(this)
        builder.setMessage(str)
            .setPositiveButton("OK"){
                    _, _->
            }
        val dlg = builder.create()
        dlg.show()
    }
    fun checkemail(count:Int,userid:String){
        var flag=false
        rdb.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in 0..(count-1)){
                    val tempemail=snapshot.child("$i").child("userEmail").value.toString()
                    if(userid==tempemail){
                        flag=true
                        break
                    }
                }
                if(flag){
                    AlertDig("해당 이메일은 가입되어 있습니다.")
                }else{
                    AlertDig("해당 이메일 정보가 없습니다.")
                }
            }
        })
    }
    fun checkpassword(count:Int,userid:String){
        var flag=false
        var showpassword=""
        rdb.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in 0..(count-1)){
                    val tempemail=snapshot.child("$i").child("userEmail").value.toString()
                    val temppassword=snapshot.child("$i").child("userPassword").value.toString()
                    if(userid==tempemail){
                        flag=true
                        showpassword=temppassword
                        break
                    }
                }
                val length=showpassword.length
                var newpassword=showpassword.slice(IntRange(0,length/2-1))
                for(i in (showpassword.length/2)..length-1)newpassword=newpassword+"*"
                if(flag){
                    AlertDig("해당 이메일의 비밀번호는 ${newpassword}입니다.")
                }else{
                    AlertDig("해당 이메일 정보가 없습니다.")
                }
            }
        })
    }
}