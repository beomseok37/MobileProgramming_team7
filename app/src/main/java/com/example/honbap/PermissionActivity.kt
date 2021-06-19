package com.example.honbap

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
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
                //ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),REQUEST)
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),REQUEST)
            }
        val dlg = builder.create()
        dlg.show()
    }

    fun Action() {
        val loginIntent = Intent(this, SignInActivity::class.java)
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            AlertDig()
        } else {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                AlertDig()
            } else {
                if (!checkLocationServicesStatus()) {
                    showLocationServicesSetting()
                } else {
                    startActivity(loginIntent)

                    finish()
                }
            }

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

    private fun checkLocationServicesStatus(): Boolean{
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        //GPS가 사용가능한지 , 사용가능->true
    }

    private fun showLocationServicesSetting() {
        val builder= AlertDialog.Builder(this)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage(
            "앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"+
                    "위치 설정을 허용하겠습니까?"
        )
        builder.setPositiveButton("설정", DialogInterface.OnClickListener { dialog, id ->
            val GpsSettingIntent= Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(GpsSettingIntent,1000)
        })
        builder.setNegativeButton("취소",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
            })
        builder.create().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            1000->{
                if(checkLocationServicesStatus()){
                    Toast.makeText(this,"GPS 활성화 되었음",Toast.LENGTH_SHORT).show()
                    Action()
                }
            }
        }
    }
}