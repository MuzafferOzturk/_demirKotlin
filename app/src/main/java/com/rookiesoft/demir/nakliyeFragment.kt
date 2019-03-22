package com.rookiesoft.demir

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import com.rookiesoft.demir.R
import kotlinx.android.synthetic.main.fragment_nakliye.view.*
import org.json.JSONArray
import org.json.JSONObject

class nakliyeFragment :android.support.v4.app.Fragment{
    constructor() : super()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView : View = inflater!!.inflate(R.layout.fragment_nakliye,null)
        var bigaTable = rootView.findViewById(R.id.tlNakliyeBiga) as TableLayout
        var karabükTable = rootView.findViewById(R.id.tlNakliyeKarabük) as TableLayout
        var izmirTable = rootView.findViewById(R.id.tlNakliyeIzmir) as TableLayout
        var payasTable = rootView.findViewById(R.id.tlNakliyePayas) as TableLayout
        bigaTable.removeAllViews()
        karabükTable.removeAllViews()
        izmirTable.removeAllViews()
        payasTable.removeAllViews()
        dataFireBase.shippingList.forEach {
            val shipping = it
            val jObj : JSONObject
            var jsonObj = JSONObject(shipping.shipping)
            var jsonArray : JSONArray = jsonObj.getJSONArray("shipping")
            var rowList = createTableRow(jsonArray)
            when{
                shipping.outlet.contains("BİGA")->
                    rowList.forEach{
                        rootView.tlNakliyeBiga.addView(it)
                    }

                shipping.outlet.contains("KARABÜK")->
                    rowList.forEach{
                        rootView.tlNakliyeKarabük.addView(it)
                    }

                shipping.outlet.contains("İZMİR")->
                    rowList.forEach{
                        rootView.tlNakliyeIzmir.addView(it)
                    }

                shipping.outlet.contains("PAYAS")->
                    rowList.forEach{
                        rootView.tlNakliyePayas.addView(it)
                    }
            }
        }

        return rootView
    }
    fun createTableRow(jObj : JSONArray) : ArrayList<TableRow>{
        val tableRowList : ArrayList<TableRow> = arrayListOf()
        var index=-1
        var colorIndex = 0
        var tableRow : TableRow = TableRow(context)
        repeat(3){
            var textView = android.support.v7.widget.AppCompatTextView(context)
            textView.setPadding(10,5,0,0)
            textView.gravity = Gravity.CENTER
            textView.setTextColor(ContextCompat.getColor(context as Activity, R.color.black)) //resources.getColor(R.color.headerText)
            textView.text = "ŞEHİR"
            textView.textSize = 13f
            textView.typeface = Typeface.DEFAULT_BOLD
            textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            var parent = textView.parent as? ViewGroup
            parent?.removeView(textView)
            tableRow.addView(textView)
            textView = android.support.v7.widget.AppCompatTextView(context)
            textView.setPadding(10,5,0,0)
            textView.gravity = Gravity.CENTER
            textView.setTextColor(ContextCompat.getColor(context as Activity, R.color.black)) //resources.getColor(R.color.headerText)
            textView.text = "FİYAT"
            textView.textSize = 13f
            textView.typeface = Typeface.DEFAULT_BOLD
            textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            parent = textView.parent as? ViewGroup
            parent?.removeView(textView)
            (tableRow.parent as? ViewGroup)?.removeView(tableRow)
            tableRow.addView(textView)
        }
        tableRowList.add(tableRow)
        tableRow  = TableRow(context)
        repeat(jObj.length()){
            index++
            if(index>=3){
                (tableRow.parent as? ViewGroup)?.removeView(tableRow)
                tableRowList.add(tableRow)
                tableRow = TableRow(context)
                index=0
            }
            var textView = android.support.v7.widget.AppCompatTextView(context)
//                textView.setPadding(10,15,0,0)
            textView.text = cityEnum.values()[jObj.getJSONObject(it).getInt("city_code")-1].toString()
            textView.textSize = 13.0F
            textView.typeface = Typeface.DEFAULT_BOLD
            textView.gravity = Gravity.LEFT
            textView.setTextColor(Color.BLACK)
            (textView.parent as? ViewGroup)?.removeView(textView)
            tableRow.addView(textView)
            textView = android.support.v7.widget.AppCompatTextView(context)
//                textView.setPadding(10,15,0,0)
            textView.text = jObj.getJSONObject(it).getString("price")
            textView.textSize = 13.0F
            textView.typeface = Typeface.DEFAULT_BOLD
            textView.gravity = Gravity.CENTER
            if(colorIndex!=0){
                tableRow.setBackgroundColor(ContextCompat.getColor(context as Activity, R.color.column1))
                colorIndex=0
            }
            else{
                tableRow.setBackgroundColor(ContextCompat.getColor(context as Activity, R.color.column2))
                colorIndex++
            }

            (textView.parent as? ViewGroup)?.removeView(textView)
            tableRow.addView(textView)
            if(it == jObj.length()-1 && index<3){
                (tableRow.parent as? ViewGroup)?.removeView(tableRow)
                tableRowList.add(tableRow)
                tableRow = TableRow(context)
            }
        }



        return tableRowList
    }
}