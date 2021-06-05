package com.example.honbap

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class PermissionActivity : AppCompatActivity() {
    val REQUEST=100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }
    fun AlertDig(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("반드시 위치 권한이 허용되어야 앱을 이용할 수 있습니다.")
            .setTitle("권한 허용")
            .setPositiveButton("OK"){
                    _, _->
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),REQUEST)
            }
        val dlg = builder.create()
        dlg.show()
    }

    fun Action(){
        val loginIntent = Intent(this,SignInActivity::class.java)
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            AlertDig()
        }
        else{
            startActivity(loginIntent)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST->{
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"권한이 승인되었습니다.", Toast.LENGTH_SHORT).show()
                    Action()
                }else{
                    Toast.makeText(this,"권한이 승인이 거부되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun init(){
        Action()
    }
}