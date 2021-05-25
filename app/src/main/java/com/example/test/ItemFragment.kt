package com.example.test

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStream

/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment() {

    private var columnCount = 1
    val datalist= ArrayList<Data>()
    val scope= CoroutineScope(Dispatchers.IO)
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
                addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                //adapter = MyItemRecyclerViewAdapter(datalist)
            }
            view.adapter=adapter
        }
        loadInfo()
        return view
    }

    private fun loadInfo() {
        scope.launch{
            try{
                val inputStream=activity!!.assets.open("info.json")
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                val str=String(buffer, Charsets.UTF_8)

                val json= JSONObject(str)
                val data=json.getJSONArray("DATA")
                for (i in 0 until data.length()) {
                    //영업상태인 음식점의 이름, 전화번호, 주소 가져옴
                    if (data.getJSONObject(i).getString("dtlstatenm") == "영업")
                        adapter.values.add(Data(data.getJSONObject(i).getString("bplcnm"),data.getJSONObject(i).getString("sitetel"),
                            data.getJSONObject(i).getString("sitewhladdr")))
                }
                withContext(Dispatchers.Main) {
                    adapter.notifyDataSetChanged()
                }

            } catch (e: JSONException){
                e.printStackTrace()
            }
        }


    }
}