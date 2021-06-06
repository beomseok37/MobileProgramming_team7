package com.example.honbap

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.honbap.RestaurantFragment.Companion.datalist
import com.google.android.gms.maps.model.LatLng

class ListFragment : Fragment() {

    private var columnCount = 1
    lateinit var adapter: ListRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            adapter= ListRecyclerViewAdapter(datalist)
            with(view) {
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
        adapter.itemClickListener=object :ListRecyclerViewAdapter.OnitemClickListener{
            override fun OnItemClick(
                holder: ListRecyclerViewAdapter.ViewHolder,
                view: View,
                data: ResData
            ) {
                getloc(data)
            }

        }
    }

    private fun getloc(data: ResData) {
        val geocoder:Geocoder=Geocoder(context) //주소를 좌표로 변환하기 위함
        var addressList:List<Address>?=null
        addressList=geocoder.getFromLocationName(data.address,5)
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