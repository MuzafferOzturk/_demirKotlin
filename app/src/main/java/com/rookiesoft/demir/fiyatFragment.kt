package com.rookiesoft.demir

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.util.Base64
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_fiyat.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.net.URLEncoder


class fiyatFragment() : android.support.v4.app.Fragment() {
    protected lateinit var tableLayout : TableLayout
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView : View = inflater!!.inflate(R.layout.fragment_fiyat,null)
        tableLayout = rootView.findViewById(R.id.tlFiyatFragment)
        val spinner : Spinner = rootView.findViewById(R.id.spinner) as Spinner
        val tv = rootView.findViewById(R.id.tvCurreny) as TextView
        if(dataFireBase.usdAlis!="" && dataFireBase.usdSatis!="" && dataFireBase.eurAlis!="" && dataFireBase.eurSatis!=""){
            activity!!.runOnUiThread {
                tv.text = "USD ALIŞ:${dataFireBase.usdAlis} USD SATIŞ:${dataFireBase.usdSatis} \nEURO ALIŞ:${dataFireBase.eurAlis} EURO SATIŞ:${dataFireBase.eurSatis}"
            }
        }
        else activity!!.runOnUiThread {
            tv.visibility = View.GONE
        }
//        var baos = ByteArrayOutputStream()
//        val bitmap = BitmapFactory.decodeResource(resources,R.drawable.axess)
//        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos)
//        val imageBytes = baos.toByteArray()
//        val imageString = Base64.encodeToString(imageBytes,Base64.DEFAULT)
//        println("<><><>>>>$imageString")

        var imageStr : String =""
        try{
            imageStr = dataFireBase.advertisementList[0].base64
        }
        catch (e :Exception){
            println("<><><>>>>ImageExcept ${e.toString()}")
            var baos = ByteArrayOutputStream()
            val bitmap = BitmapFactory.decodeResource(resources,R.drawable.logo2)
            bitmap.compress(Bitmap.CompressFormat.PNG,100,baos)
            val imageBytes = baos.toByteArray()
            imageStr = Base64.encodeToString(imageBytes,Base64.DEFAULT)
        }


        var imageByte = Base64.decode(imageStr,Base64.DEFAULT)
//        dataFireBase.advertisementList.forEach {
//            imageByte = Base64.decode(it.base641+it.base642+it.base643+it.base644,Base64.DEFAULT)
//        }
        val decoded = BitmapFactory.decodeByteArray(imageByte,0,imageByte.size)
        try {
            activity!!.runOnUiThread {
                rootView.ivadver.setImageBitmap(decoded)
            }
        }catch (e :Exception){

        }
        fun rowFill(tag : String,spinnerKontrol : Boolean){
            var _tag = tag
//            Toast.makeText(context,tag,Toast.LENGTH_SHORT).show()
            tableLayout.removeAllViews()
            if(spinnerKontrol){
                spinner.visibility = View.GONE
                val spinnerList = ArrayList<String>()
                dataFireBase.categoryList.forEach {
                    if(it.parent_cat.toString() == _tag){
                        spinnerList.add(it.cat_name)
                    }
                }
                if(spinnerList.size>0){
                    spinner.visibility = View.VISIBLE
                    val adapter = ArrayAdapter(context,R.layout.support_simple_spinner_dropdown_item,spinnerList)
                    spinner.adapter = adapter
                }
            }
            else{
                _tag = dataFireBase.categoryList.firstOrNull { it.cat_name == _tag }!!.cat_code.toString()
                println("<><><><>$_tag")
            }
            val lastPrice = dataFireBase.priceList.firstOrNull { it.cat_code == tag.toInt() }
            val preLastPrice = dataFireBase.priceList.firstOrNull { it != lastPrice && it.cat_code == tag.toInt()  }
            if(lastPrice!=null){
                var headerString = "Bölge,"
                println(">>${lastPrice.product_codes.split(',')}")
                println("<<${dataFireBase.productList}")
                lastPrice.product_codes.split(',').forEach {
                    if(!it.isNullOrEmpty())
                        headerString+="${dataFireBase.productList.firstOrNull { product-> product.pro_no == it.toInt() }!!.pro_name},"
                }
                headerString = headerString.substring(0,headerString.length-1)
                tableLayout.addView(createTableRow(items = headerString,headerBool = true))

//                    println("<><><>${lastPrice.price}")
                var jsonObj = JSONObject(lastPrice.price)
                var jsonArray : JSONArray = jsonObj.getJSONArray("subPrice")
                repeat(jsonArray.length()){
                    tableLayout.addView(createTableRow(items = "${ cityEnum.values()[jsonArray.getJSONObject(it).getInt("city_code")-1]},${jsonArray.getJSONObject(it).getString("price")}",colorList = listOf(0,-1,0,1),preLast = jsonArray.getJSONObject(it).getString("pre_last")))
                    var bBreak = false;
                    val yData : ArrayList<Float> = arrayListOf()
                    val xData : ArrayList<String> = arrayListOf()
                    var city = cityEnum.values()[jsonArray.getJSONObject(it).getInt("city_code")-1]
                    for (price in dataFireBase.priceList) {
                        if(price.cat_code == _tag.toInt())
                        for(history in price.history) run { ->
                            for (list in history.value) {
                                if(list!=null)
                                if (list.city_code == jsonArray.getJSONObject(it).getString("city_code")) {
                                    list.date.forEach { date ->
                                        xData.add(date.rec_date)
                                        yData.add(date.price.replace("₺", "").toString().toFloat())
                                    }
                                    bBreak = true;
                                    break;
                                }
                                if (bBreak) break;
                            }
                        }
                        if(bBreak) break;
                    }
                    val barChart = createChart(yData,xData)
                    barChart.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,700)
                    barChart.tag = chartTag++
//                    println("<><><>>>>Char Tag $chartTag")
                    barChart.visibility = View.GONE
//                    barChart.tag =
                    tableLayout.addView(barChart)

                    //println("<><><><>"+jsonArray.getJSONObject(it).getString("price"))
                }
            }
                /*for(i in 0..5){
                val tableRow = TableRow(context)
                for(j in 0..3){
                    val textView = android.support.v7.widget.AppCompatTextView(context)
                    textView.text = "TESTTEXT"
                    textView.setPadding(10,0,0,0)
                    textView.gravity = Gravity.RIGHT
                    textView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_down_red_24dp,0)
                    textView.setTextColor(resources.getColor(R.color.downCash))
                    var parent = textView.parent as? ViewGroup
                    if(parent!=null) parent.removeView(textView)
                    tableRow.addView(textView)
                }
                val parent = tableRow.parent as? ViewGroup
                if(parent!=null) parent.removeView(tableRow)
                tableLayout.addView(tableRow)
            }*/
        }

        spinner.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                activity!!.runOnUiThread {
                    rowFill(spinner.selectedItem.toString(),false)
                }
            }
        }
        val tabLayout : TabLayout = rootView.findViewById(R.id.tabLayout)
        dataFireBase.categoryList.forEach {
            if(it.parent_cat == 0)
                tabLayout.addTab(tabLayout.newTab().setText(it.cat_name).setTag(it.cat_code))
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tableLayout.removeAllViews()
                activity!!.runOnUiThread {
                    if(tab!!.tag.toString()=="1") rootView.tvKdv.text = "Fiyatlara KDV Dahildir"
                    else rootView.tvKdv.text = "Fiyatlara KDV Dahil Değildir"
                    rowFill(tab!!.tag.toString(),true)
                }
            }
        })

        activity!!.runOnUiThread {
            println(">>${tabLayout.getTabAt(0)}")
            println(">>${tabLayout.getTabAt(0)?.tag}")
            println(">>${tabLayout.getTabAt(0)!!.tag.toString()}")
            rowFill(tabLayout.getTabAt(0)!!.tag.toString(),true)
        }
        rootView.imBtnTeklifAra.setOnClickListener { teklif(it) }
        rootView.imBtnTeklifWhats.setOnClickListener { teklif(it) }
        rootView.tvTeklifAra.setOnClickListener { teklif(it) }
        rootView.tvTeklifWhats.setOnClickListener { teklif(it) }
        return rootView
    }

    fun teklifAra(){
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+" + dataFireBase.whatsAppPhone))
        startActivity(intent)
    }
    fun teklifWhatsApp(){
//        try {
////            val uri = Uri.parse("smsto:" + "" + dataFireBase.whatsAppPhone + "?body=" + "")
//            val intent = Intent(Intent.ACTION_SEND)
//            intent.putExtra(Intent.EXTRA_TEXT,"Lütfen çap listenizi ve teslim yerinizi gönderiniz")
//            intent.putExtra("jid",dataFireBase.whatsAppPhone+"@s.whatsapp.net")
//            intent.setType("text/plain")
//            intent.setPackage("com.whatsapp")
//            startActivity(intent)
//        }
//        catch (e:Exception){
//            println("<><><><><>${e.toString()}")
//        }
        try {
            val sendMsg = Intent(Intent.ACTION_VIEW)
            val url = "https://api.whatsapp.com/send?phone=" + dataFireBase.whatsAppPhone + "&text=" + URLEncoder.encode("Lütfen çap listenizi ve teslim yerinizi gönderiniz", "UTF-8")
            sendMsg.setPackage("com.whatsapp")
            sendMsg.data = Uri.parse(url)
            if (sendMsg.resolveActivity(activity!!.packageManager) != null) {
                startActivity(sendMsg)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun teklif(v:View){
        when(v.id){
            R.id.imBtnTeklifAra->teklifAra()
            R.id.tvTeklifAra->teklifAra()
            R.id.imBtnTeklifWhats->teklifWhatsApp()
            R.id.tvTeklifWhats->teklifWhatsApp()
        }
    }
    var tableCounter = 0

    fun createChart(yData : ArrayList<Float>,xData : ArrayList<String>) : BarChart{
        var barChart = BarChart(context)
        val yVals1 : ArrayList<BarEntry> = arrayListOf()
        for (i in 0 until yData.size) {
            yVals1.add(BarEntry(i.toFloat(), yData[i]))
        }
        val set1 = BarDataSet(yVals1,"")
        set1.setDrawValues(false)
        val datasets : ArrayList<IBarDataSet> = arrayListOf()
        datasets.add(set1)
        set1.setColors(ColorTemplate.JOYFUL_COLORS, 255)
        val entries : List<Entry> = arrayListOf()
        val data = BarData(set1)
//        data.setValueFormatter(LargeValueFormatter())
        data.setValueTextSize(10f)
        barChart.data = data
        val xAxis = barChart.xAxis
        xAxis.isGranularityEnabled = true
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 10f
        xAxis.labelRotationAngle = 90f
        val xData2 = ArrayList<String>()
        for(dataX in xData){
            xData2.add(dataX)
        }
        xAxis.setValueFormatter(IndexAxisValueFormatter(xData2))
        barChart.description.isEnabled = false
        barChart.setDrawBarShadow(false)
        barChart.setDrawMarkers(false)
        barChart.setDrawValueAboveBar(false)
        val y =barChart.axisRight
        y.isEnabled = false
        barChart.setDrawBorders(false)
//        barChart.description.isEnabled=false
//        barChart.setDrawGridBackground(false)
//        barChart.legend.isEnabled=false
//        barChart.isScaleXEnabled = true
        return barChart
    }
    fun tableRowClick(v:View){
        for(i in 0..tableLayout.childCount){
            if((tableLayout.getChildAt(i) is BarChart)){
                if((tableLayout.getChildAt(i).tag.toString()==v.tag.toString())){
                    if(tableLayout.getChildAt(i).visibility==View.VISIBLE)
                        tableLayout.getChildAt(i).visibility = View.GONE
                    else tableLayout.getChildAt(i).visibility = View.VISIBLE
                    break
                }

            }
        }
//        Toast.makeText(context, v.tag.toString(), Toast.LENGTH_SHORT).show()
    }
    var rowTag=0
    var chartTag=0
    fun createTableRow(items : String, colorList : List<Int> = emptyList(), headerBool : Boolean = false, preLast : String = "") : TableRow{
        val tableRow = TableRow(context)
        when(headerBool){
            true->{
                tableRow.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                tableRow.isClickable = false
                items.split(',').forEach{ element ->
                    val textView = android.support.v7.widget.AppCompatTextView(context)
                    textView.setPadding(10,5,0,0)
                    textView.gravity = Gravity.CENTER
                    textView.setTextColor(ContextCompat.getColor(context as Activity, R.color.headerText)) //resources.getColor(R.color.headerText)
                    textView.text = element
                    textView.textSize = 13f
                    textView.typeface = Typeface.DEFAULT_BOLD
                    //textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    var parent = textView.parent as? ViewGroup
                    parent?.removeView(textView)
                    tableRow.addView(textView)
                }
            }
            false->{
                var columnCounter = 0
                tableRow.isClickable = true
                tableRow.setBackgroundColor(ContextCompat.getColor(context as Activity, if (tableCounter % 2 == 0) R.color.column1 else R.color.column2))
//                if(tableCounter%2 ==0)
//                    tableRow.setBackgroundColor(resources.getColor(R.color.column1))
//                else tableRow.setBackgroundColor(resources.getColor(R.color.column2))
                tableCounter++
                tableRow.tag = rowTag++
                tableRow.setOnClickListener { tableRowClick(it) }
                var lastPriceIndex=0
                var cityIndex = 0
                items.split(',').forEachIndexed {index, element ->
                    var color = 0
                    var image = 0
                    when(colorList[index]){
                        -1->{
                            color = R.color.downCash
                            image = R.drawable.ic_keyboard_arrow_down_red_24dp
                        }
                        1 ->{
                            color = R.color.upCash
                            image = R.drawable.ic_keyboard_arrow_up_green_24dp
                        }
                        else ->{
                            color = R.color.stableCash
                            image = R.drawable.ic_view_agenda_blue_24dp
                        }

                    }
                    val textView = android.support.v7.widget.AppCompatTextView(context)
                    textView.setPadding(10,15,0,0)
                    if(cityIndex==0)
                        textView.text = "$element"
                    else textView.text = "$element₺"
                    cityIndex++
                    textView.textSize = 13.0F
                    if(columnCounter == 0){
                        textView.typeface = Typeface.DEFAULT_BOLD
                        textView.gravity = Gravity.LEFT
                        textView.setTextColor(Color.BLACK)
                    }
                    else{
                        val lastPrice = preLast.split(",")
                        var color = 0
                        var image = 0
                        val currentItemInt = element.replace("₺","").replace(".","").toInt()
                        val lastItemInt = lastPrice[lastPriceIndex].replace("₺","").replace(".","").toInt()
                        if(currentItemInt>lastItemInt){
                            color = R.color.downCash
                            image = R.drawable.ic_keyboard_arrow_up_green_24dp
                        }
                        else if(currentItemInt<lastItemInt){
                            color = R.color.upCash
                            image = R.drawable.ic_keyboard_arrow_down_red_24dp
                        }
                        else{
                            color = R.color.stableCash
                            image = R.drawable.ic_view_agenda_blue_24dp
                        }
                        lastPriceIndex++
                        textView.gravity = Gravity.RIGHT
                        textView.setCompoundDrawablesWithIntrinsicBounds(0,0,image,0)
                        textView.setTextColor(resources.getColor(color))
                    }
                    //textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    (textView.parent as? ViewGroup)?.removeView(textView)
                    tableRow.addView(textView)
                    columnCounter++
                }

            }
        }
        (tableRow.parent as? ViewGroup)?.removeView(tableRow)

        return tableRow
    }
}
