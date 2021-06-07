package com.example.honbap

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.honbap.databinding.FragmentRestaurantBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.grpc.InternalChannelz.id
import org.json.JSONException
import org.json.JSONObject


class RestaurantFragment : Fragment() {

    lateinit var binding: FragmentRestaurantBinding
    val listfrag=ListFragment()
    val mapfrag=MapsFragment()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadInfo()
        return inflater.inflate(R.layout.fragment_restaurant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragment=childFragmentManager.beginTransaction()
        fragment.replace(R.id.resframeLayout,mapfrag)
        fragment.commit()
        val listBtn=view.findViewById<FloatingActionButton>(R.id.listBtn)
        listBtn.setOnClickListener {
            if(!listfrag.isVisible) {
                val fragment =childFragmentManager.beginTransaction()
                fragment.replace(R.id.resframeLayout, listfrag)
                fragment.addToBackStack(null)
                fragment.commit()
            }
            else{
                val fragment=childFragmentManager.beginTransaction()
                fragment.replace(R.id.resframeLayout,mapfrag)
                fragment.commit()

            }
        }
    }
    companion object{
        val datalist=ArrayList<ResData>()
    }
    fun loadInfo(){
        try {
            val inputStream = activity!!.assets.open("info.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val str = String(buffer, Charsets.UTF_8)
            val json = JSONObject(str)
            val data = json.getJSONArray("DATA")
            for (i in 0 until data.length()) {
                //영업상태인 음식점의 이름, 전화번호, 주소 가져옴
                if (data.getJSONObject(i).getString("dtlstatenm") == "영업")
                    datalist.add(
                        ResData(
                            data.getJSONObject(i).getString("bplcnm"),
                            data.getJSONObject(i).getString("sitetel"),
                            data.getJSONObject(i).getString("sitewhladdr")
                        )
                    )
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

}