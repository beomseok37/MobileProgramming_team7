package com.example.test

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.test.MainActivity.Companion.datalist
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStream

class ItemFragment : Fragment() {

    private var columnCount = 1
    lateinit var adapter: MyItemRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            adapter= MyItemRecyclerViewAdapter(datalist)
            with(view) {
               // addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
            }
            view.adapter=adapter
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemTouch()
    }

    fun itemTouch(){
        adapter.itemClickListener=object :MyItemRecyclerViewAdapter.OnitemClickListener{
            override fun OnItemClick(
                holder: MyItemRecyclerViewAdapter.ViewHolder,
                view: View,
                data: Data
            ) {
                getloc(data)
            }

        }
    }

    private fun getloc(data: Data) {
        val geocoder:Geocoder=Geocoder(context) //주소를 좌표로 변환하기 위함
        var addressList:List<Address>?=null
        addressList=geocoder.getFromLocationName(data.address,5)
      //  Log.i("info","${addressList.get(0)}")
        val lat=addressList.get(0).latitude
        val lng=addressList.get(0).longitude
        val mapFragment=MapsFragment()
        val bundle=Bundle()
        val loc=LatLng(lat,lng)
        bundle.putParcelable("location", loc)
        bundle.putString("name",data.name)
        bundle.putString("tel",data.tel)
        mapFragment.arguments = bundle
        activity?.supportFragmentManager!!.beginTransaction()
            .addToBackStack(null)  //지도로 음식점 위치 확인하고 뒤로가기 버튼 누르면 다시 리스트로 돌아오게 함
            .replace(R.id.frameLayout,mapFragment)
            .commit()
    }
}