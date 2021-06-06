package com.example.honbap

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.honbap.databinding.ActivitySignInBinding
import com.google.firebase.database.*

class SignInActivity : AppCompatActivity() {
    lateinit var binding4 : ActivitySignInBinding
    lateinit var rdb: DatabaseReference
    var notdialog=false
    var saveemailflag=0
    var autologinflag=0
    lateinit var LoginDBHelper: LoginDBAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding4 = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding4.root)
        LoginDBHelper = LoginDBAdapter(this)
        checkDB()
        init4()
    }
    fun checkDB(){
        if(LoginDBHelper.loginbefore()){
            if(LoginDBHelper.loginfun()==1){
                //이메일저장
                binding4.IDpageID.setText(LoginDBHelper.getEmail())
                binding4.saveID.isChecked=true
            }else if(LoginDBHelper.loginfun()>=2){
                //자동로그인
                val successintent = Intent(this,MainActivity::class.java)
                startActivity(successintent)
                finish()
            }
        }
    }
    fun init4(){
        rdb= FirebaseDatabase.getInstance().getReference("information")
        var userid:String?
        var userpassword:String?
        var count:Int=0
        rdb.child("auto").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                count=snapshot.value.toString().toInt()
            }

        })
        binding4.apply{
            loginbtn.setOnClickListener {
                if(saveID.isChecked()) {
                    //저장되었던 userid를 IDpageID에 옮겨놓는다.
                    saveemailflag = 1
                }else{
                    saveemailflag = 0
                }
                if(autologin.isChecked()){
                    //다음부터는 자동로그인
                    autologinflag=1
                }else{
                    autologinflag=0
                }
                notdialog=true
                if(IDpageID.text.isEmpty()||IDpagePassword.text.isEmpty()){
                    AlertDig()
                }else {
                    userid = IDpageID.text.toString()
                    userpassword = IDpagePassword.text.toString()
                    //id와 password를 firebase에 넘겨 확인한다.
                    checklogin(userid!!,userpassword!!)
                }
            }
            signupbtn.setOnClickListener {
                val signupIntent= Intent(this@SignInActivity,SignUpActivity::class.java)
                startActivity(signupIntent)
                notdialog=false
            }
            findbtn.setOnClickListener {
                val findIntent= Intent(this@SignInActivity,FindActivity::class.java)
                startActivity(findIntent)
                notdialog=false
            }
        }
    }

    fun AlertDig(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("사용자 정보가 일치하지 않습니다.")
            .setPositiveButton("OK"){
                    _, _->
            }
        val dlg = builder.create()
        dlg.show()
    }
    fun checklogin(userid:String,userpassword:String){
        var flag=false
        var count:Int=0
        var tempid:Int=0
        rdb.child("auto").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                count=snapshot.value.toString().toInt()
            }

        })
        rdb.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in 0..(count-1)){
                    tempid=i
                    val tempemail=snapshot.child("$i").child("userEmail").value.toString()
                    val temppassword=snapshot.child("$i").child("userPassword").value.toString()
                    Log.i("email",tempemail)
                    Log.i("password",temppassword)
                    if(userid==tempemail && userpassword==temppassword){
                        flag=true
                        break
                    }

                }
                if(notdialog) {
                    if (flag) {
                        if(LoginDBHelper.loginbefore()){
                            Log.i("login","update")
                            LoginDBHelper.updateProduct(UserInDB(tempid,userid,userpassword,saveemailflag,autologinflag))
                        }else{
                            Log.i("login","insert")
                            LoginDBHelper.insertProduct(UserInDB(tempid,userid,userpassword,saveemailflag,autologinflag))
                        }
                        Toast.makeText(this@SignInActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                        val successintent= Intent(this@SignInActivity,MainActivity::class.java)
                        startActivity(successintent)
                        finish()
                    } else {
                        AlertDig()
                    }
                }
            }
        })
    }
}