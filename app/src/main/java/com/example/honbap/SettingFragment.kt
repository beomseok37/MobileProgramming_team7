package com.example.honbap

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.honbap.databinding.FragmentSettingBinding
import com.google.firebase.database.*

class SettingFragment : Fragment() {
    var binding: FragmentSettingBinding?=null
    lateinit var rdb: DatabaseReference
    lateinit var LoginDBHelper: LoginDBAdapter
    private lateinit var emailText: EditText
    private lateinit var ageText: EditText
    private lateinit var nameText: EditText
    private lateinit var radioButton: RadioButton
    private lateinit var radioGroup: RadioGroup
    private lateinit var button: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val intent = requireActivity() as MainActivity
        binding = FragmentSettingBinding.inflate(layoutInflater)
        LoginDBHelper = LoginDBAdapter(intent)
        val v:ViewGroup = inflater.inflate(R.layout.fragment_setting, container, false) as ViewGroup
        rdb= FirebaseDatabase.getInstance().getReference("information")
        var auto:Int=LoginDBHelper.getInfo()
        rdb.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                //
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val i = auto
                Log.i("0",i.toString())
                val userEmail=snapshot.child("$i").child("userEmail").value.toString()
                val userName=snapshot.child("$i").child("userNickname").value.toString()
                val userSex=snapshot.child("$i").child("userSex").value.toString()
                val userAge=snapshot.child("$i").child("userage").value.toString()

                emailText = v.findViewById(R.id.emailtextinputedittext)
                emailText.setText(userEmail)
                ageText = v.findViewById(R.id.ageedittext)
                ageText.setText(userAge)
                nameText = v.findViewById(R.id.nickname)
                nameText.setText(userName)
                when(userSex){
                    "남"->{
                        radioButton = v.findViewById(R.id.manradiobtn)
                        radioButton.isChecked = true
                    }
                    "여"->{
                        radioButton = v.findViewById(R.id.womanradiobtn)
                        radioButton.isChecked = true
                    }
                }

//                for(i in 0..(auto-1)){
//
//                }
                binding.apply{
                    var useremail:String
                    var userpassword:String
                    var userpasswordcertificate:String
                    var userage:Int
                    var usersex:String?=null
                    var usernickname:String
                    var certificateflag:Boolean=false
                    //var auto:Int=LoginDBHelper.getInfo()
                    val forauto=rdb.child("auto").addListenerForSingleValueEvent(object:
                        ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            //
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            auto=snapshot.value.toString().toInt()
                        }

                    })
                    radioGroup = v.findViewById(R.id.sexradiogroup)
                    radioGroup.setOnCheckedChangeListener { _, checkedId ->
                        when(checkedId){
                            R.id.manradiobtn->{
                                usersex="남"
                            }
                            R.id.womanradiobtn->{
                                usersex="여"
                            }
                        }
                    }
                    button = v.findViewById(R.id.joinbtn)

                    button.setOnClickListener {
                        Log.i("button","button")

//                if(!certificateflag){
//                    Toast.makeText(intent,"이메일이 인증되지 않았습니다.", Toast.LENGTH_SHORT).show()
//                    Log.i("join","email")
//                }
                        if(ageText.text.isEmpty()){
                            Toast.makeText(intent,"나이가 빠졌습니다", Toast.LENGTH_SHORT).show()
                            Log.i("join","age")
                        }
                        else if(radioGroup==null){
                            Toast.makeText(intent,"성별이 빠졌습니다", Toast.LENGTH_SHORT).show()
                            Log.i("join","sex")
                        }
                        else if(nameText.text.isEmpty()){
                            Toast.makeText(intent,"닉네임이 빠졌습니다", Toast.LENGTH_SHORT).show()
                            Log.i("join","nickname")
                        }
                        else {
                            Log.i("join","else")
                            useremail = emailText.text.toString()
                            userage = ageText.text.toString().toInt()
                            usernickname = nameText.text.toString()
                            //firebase에 넘겨야겠지..
                            Log.i("join",useremail+userage+usersex+usernickname)

                            val loginrdb = rdb.child(i.toString())
                            Log.i("1", i.toString())

                            loginrdb.child("uid").setValue(i)
                            Log.i("2", i.toString())

                            //loginrdb.child("userEmail").setValue(useremail)
                            loginrdb.child("userage").setValue(userage)
                            loginrdb.child("userSex").setValue(usersex)
                            loginrdb.child("userNickname").setValue(usernickname)
                            val hashmap: HashMap<String, Any> = HashMap<String, Any>()
                            hashmap.put("auto", "${auto}")
                            Log.i("3", i.toString())

                            val addauto = rdb.updateChildren(hashmap)
                            Log.i("finish","finish")

                            //finish()
                        }
                    }

                }
            }
        })
        val logoutbtn=v.findViewById<Button>(R.id.logoutbtn)
        logoutbtn.setOnClickListener {
            Log.i("logout","logoutbefore")
            LoginDBHelper.logout()
            val signinintent= Intent(getActivity()!!.getApplicationContext(),SignInActivity::class.java)
            startActivity(signinintent)
            Log.i("logout","logoutafter")
            getActivity()!!.finish()
        }
        return v
        //return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onResume() {
        super.onResume()
    }
    fun builddialog(text:String,context: Context){
        val builder = AlertDialog.Builder(context)
        builder.setMessage("$text").setTitle("$text")
        val dlg = builder.create()
        dlg.show()
    }

    fun AlertDig(str:String){
        val intent = requireActivity() as MainActivity
        val builder = androidx.appcompat.app.AlertDialog.Builder(intent)
        builder.setMessage(str)
            .setPositiveButton("OK"){
                    _, _->
            }
        val dlg = builder.create()
        dlg.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}