package com.rookiesoft.demir

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.rookiesoft.demir.R

class taksitFragment :android.support.v4.app.Fragment{
    constructor() : super()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView : View = inflater!!.inflate(R.layout.fragment_taksit,null)
        val listView : ListView = rootView.findViewById(R.id.lvTaksitFragment)
        var strList  : ArrayList<String> = ArrayList()
        var imageList  : ArrayList<Int> = ArrayList()
        dataFireBase.installmentList.forEach {
            strList.add(it.install_option)
            when(it.bank_name){
                "axess"     ->imageList.add(R.drawable.axess)
                "bonus"     ->imageList.add(R.drawable.bonuscard)
                "cardFinans"->imageList.add(R.drawable.card_finans)
                "maximum"   ->imageList.add(R.drawable.maximum)
                "world"     ->imageList.add(R.drawable.world_card_logo)
                else->{

                }
            }
        }
        val taksitAdapter : taksitAdapter? = taksitAdapter(activity!!,imageList, strList)
        listView.adapter = taksitAdapter
        return rootView
    }
}