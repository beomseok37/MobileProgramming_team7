package com.example.honbap

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.honbap.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.HashMap
import kotlinx.coroutines.*

class SignUpActivity : AppCompatActivity() {
    lateinit var binding2: ActivitySignUpBinding
    lateinit var rdb: DatabaseReference
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding2 = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding2.root)
        auth = FirebaseAuth.getInstance()
        init2()
    }
    fun builddialog(text:String,context: Context){
        val builder = AlertDialog.Builder(context)
        builder.setMessage("$text").setTitle("$text")
        val dlg = builder.create()
        dlg.show()
    }
    fun init2(){
        rdb= FirebaseDatabase.getInstance().getReference("information")
        binding2.apply{
            var useremail:String
            var userpassword:String
            var userpasswordcertificate:String
            var userage:Int
            var usersex:String?=null
            var usernickname:String
            var certificateflag:Boolean=false
            var auto:Int=0
            val forauto=rdb.child("auto").addListenerForSingleValueEvent(object:
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    //
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    auto=snapshot.value.toString().toInt()
                }

            })
            emailtextinputedittext.addTextChangedListener {
                if(it.toString().contains("@konkuk.ac.kr")){
                    emailtextinputlayout.error = null
                    emailcertificatebtn.isEnabled=true
                }
                else{
                    emailtextinputlayout.error = "건국대학교 이메일이 아닙니다."
                }
                certificateflag=false
            }
            emailcertificatebtn.setOnClickListener {
                //이메일 인증

                val checkemail=emailtextinputedittext.text.toString()
//                val userpassword = passwordedittext.text.toString()
                emailindatabase(auto,checkemail)
                createUserId(checkemail,"password")
                certificateflag=true
            }
            sexradiogroup.setOnCheckedChangeListener { _, checkedId ->
                when(checkedId){
                    R.id.manradiobtn->{
                        usersex="남"
                    }
                    R.id.womanradiobtn->{
                        usersex="여"
                    }
                }
            }
            joinbtn.setOnClickListener {
                if(!certificateflag){
                    Toast.makeText(this@SignUpActivity,"이메일이 인증되지 않았습니다.",Toast.LENGTH_SHORT).show()
                }
                else if(passwordedittext.text.isEmpty()){
                    Toast.makeText(this@SignUpActivity,"비밀번호가 빠졌습니다",Toast.LENGTH_SHORT).show()
                }
                else if(passwordconfirmedittext.text.isEmpty()){
                    Toast.makeText(this@SignUpActivity,"비밀번호 확인이 빠졌습니다",Toast.LENGTH_SHORT).show()
                }
                else if(passwordedittext.text.toString()!=passwordconfirmedittext.text.toString()){
                    Toast.makeText(this@SignUpActivity,"비밀번호와 비밀번호 확인이 일치하지 않습니다",Toast.LENGTH_SHORT).show()
                }
                else if(ageedittext.text.isEmpty()){
                    Toast.makeText(this@SignUpActivity,"나이가 빠졌습니다",Toast.LENGTH_SHORT).show()
                }
                else if(usersex==null){
                    Toast.makeText(this@SignUpActivity,"성별이 빠졌습니다",Toast.LENGTH_SHORT).show()
                }
                else if(nickname.text.isEmpty()){
                    Toast.makeText(this@SignUpActivity,"닉네임이 빠졌습니다",Toast.LENGTH_SHORT).show()
                }
                else {
                    auth.currentUser?.reload()
                    useremail = emailtextinputedittext.text.toString()
                    userpassword = passwordedittext.text.toString()
                    userage = ageedittext.text.toString().toInt()
                    usernickname = nickname.text.toString()
                    //firebase에 넘겨야겠지..
                    if(verifyEmail2()) {
                        val loginrdb = rdb.child("$auto")
                        loginrdb.child("uid").setValue(auto)
                        loginrdb.child("userEmail").setValue(useremail)
                        loginrdb.child("userPassword").setValue(userpassword)
                        loginrdb.child("userage").setValue(userage)
                        loginrdb.child("userSex").setValue(usersex)
                        loginrdb.child("userNickname").setValue(usernickname)
                        val hashmap: HashMap<String, Any> = HashMap<String, Any>()
                        hashmap.put("auto", "${auto + 1}")
                        val addauto = rdb.updateChildren(hashmap)
                        finish()
                    } else {
                        Toast.makeText(this@SignUpActivity,"이메일 인증을 해주세요",Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

    fun createUserId(id:String, pw:String) {
        Log.v("1", "1")
        auth.createUserWithEmailAndPassword(id,pw).addOnCompleteListener { task->
            if(task.isSuccessful) {
                Log.v("success", "success")
                verifyEmail()
            } else {
                Log.v("asd","Asd")
                Log.w("createUserWithEmail:failure", task.exception)
//                Toast.makeText(this@SignUpActivity,"회갑실패",Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun verifyEmail() {
        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
            if(it.isSuccessful) {
                Log.v("emailsend2", "Email sent.2")
                Toast.makeText(this@SignUpActivity,"이메일이 전송 되었습니다.",Toast.LENGTH_SHORT).show()
            } else {
                Log.v("emailsend2", "Email sent failed.2")
                Toast.makeText(this@SignUpActivity,"이메일 전송이 실패했습니다.",Toast.LENGTH_SHORT).show()
                auth.currentUser!!.delete()
            }
        }
    }
    fun verifyEmail2():Boolean {
        runBlocking {
            val job = GlobalScope.launch {
                auth.currentUser?.reload()
            }
            job.join()
        }
        Toast.makeText(this@SignUpActivity,"이메일 인증 확인중입니다.",Toast.LENGTH_SHORT).show()
        Thread.sleep(2000L)
        return auth.currentUser!!.isEmailVerified
    }
    fun emailindatabase(count:Int,userid:String){
        var flag=false
        rdb.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                //
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in 0..(count-1)){
                    val tempemail=snapshot.child("$i").child("userEmail").value.toString()
//                    Log.i("email",tempemail)
                    if(userid==tempemail){
                        flag=true
                        break
                    }

                }
                if(flag){
                    AlertDig("해당 이메일은 이미 가입되어 있습니다.")
                }else{
                    AlertDig("해당 이메일은 사용가능합니다.")
                }
            }
        })
    }
    fun AlertDig(str:String){
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setMessage(str)
            .setPositiveButton("OK"){
                    _, _->
            }
        val dlg = builder.create()
        dlg.show()
    }
}

