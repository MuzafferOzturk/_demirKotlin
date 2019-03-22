package com.rookiesoft.demir

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Element
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class dataFireBase{
    companion object {
        var categoryList        = ArrayList<dataFireBase.Category>()
        var newsList            = ArrayList<dataFireBase.News>()
        var productList         = ArrayList<dataFireBase.Product>()
        var priceList           = ArrayList<dataFireBase.Price>()
        var installmentList     = ArrayList<dataFireBase.Installment>()
        var advertisementList   = ArrayList<dataFireBase.Advertisement>()
        var shippingList        = ArrayList<dataFireBase.Shipping>()
        var whatsAppPhone = "905327776859"
        var mail = "info@fkmmalzeme.com.tr"
        const val currencyUrl ="http://www.tcmb.gov.tr/kurlar/today.xml"
        var usdAlis = ""
        var usdSatis = ""
        var eurAlis =""
        var eurSatis=""
    }
    @IgnoreExtraProperties
    data class Users(
//            var rec_date        : Long = 0,
            var name_surname    : String = "",
            var company         : String ="",
            var mail            : String = "",
            var phone           : String = ""
    )
    @IgnoreExtraProperties
    data class Price(
            var cat_code        : Int = 0,
            var history         : HashMap<String,ArrayList<price_history>> = hashMapOf(),
            var price           : String = "",  //(product_no,price;procut_no,price)
            var product_codes   : String = "",
            var rec_update      : String = ""
    )
    data class price_history(
            var city_code       : String ="",
            var date            : List<date> = listOf()
    )
    data class date(
            var price           : String ="",
            var rec_date        : String= ""
    )
    @IgnoreExtraProperties
    data class Category(
            var cat_code        : Int = 0,
            var parent_cat      : Int = 0,
            var cat_name        : String = ""
    )
    @IgnoreExtraProperties
    data class News(
            var title           : String = "",
            var content         : String = "",
            var rec_date        : String = ""
    )
    @IgnoreExtraProperties
    data class Product(
            var pro_no          : Int = 0,
            var index           : Int = 0,
            var pro_name        : String = ""
    )
    @IgnoreExtraProperties
    data class Installment(
            var bank_name       : String = "",
            var install_option  : String = ""
    )
    @IgnoreExtraProperties
    data class Shipping(
            var outlet          : String = "",
            var shipping        : String = ""
    )
    @IgnoreExtraProperties
    data class Advertisement(
            var base64          : String = ""
    )
}

var preferences         : SharedPreferences?=null
var catBool             : Boolean = false
var newsBool            : Boolean = false
var productBool         : Boolean = false
var priceBool           : Boolean = false
var installmentBool     : Boolean = false
var advertisementBool   : Boolean = false
var shippingBool        : Boolean = false
var database        = FirebaseDatabase.getInstance()
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init()
        //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.DEBUG)
        val toolbar: android.support.v7.widget.Toolbar = findViewById(R.id.mainToolbar)
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        actionbar.setDisplayShowTitleEnabled(false)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val mDrawerLayout : DrawerLayout= findViewById(R.id.drawerLayout)

        navigationView.setNavigationItemSelectedListener{
            displayScreen(it.itemId)
            mDrawerLayout.closeDrawer(GravityCompat.START)
            true
        }
//        bottomNavigationView.setOnNavigationItemSelectedListener {
//            displayScreen(it.itemId)
//            mDrawerLayout.closeDrawer(GravityCompat.START)
//            true
//        }
        refreshButton.setOnClickListener {
            runOnUiThread {
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                progressBarMain.bringToFront()
                progressBarMain.visibility = View.VISIBLE
            }
//            database.goOnline()
//            getData()

            runOnUiThread {
                displayScreen(R.id.fiyat)
                    progressBarMain.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                Toast.makeText(applicationContext!!,"Veri Alındı",Toast.LENGTH_SHORT).show()
            }
//            database.goOnline()
//            refresh(it)
        }
        Thread {
            getData()
        }.start()
//region DateTime
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSSS")
//        var date = Date()
//        var formattedDate = dateFormat.format(date)
//        println("DATE <><><><><> $formattedDate")
//        date = Date()
//        var cal = Calendar.getInstance()
//        cal.add(Calendar.DAY_OF_MONTH,Calendar.DAY_OF_MONTH-Calendar.DAY_OF_MONTH-1)
//        formattedDate = dateFormat.format(cal.time)
//        println("DATE <><><><><> $formattedDate")

//endregion

//region Firebase add
//        val database = FirebaseDatabase.getInstance()
//        val myRef = database.getReference("Price")
//        myRef.removeValue()
//        (1..6).forEach{cat_code->
//            var id =myRef.push().key
//            (0..30).forEach {date->
//                var str : String = "{ \"Prices\" :"
//                (1..16).forEach {city_code->
//                    str += ""
//                }
//                str += "}"
//                println("<><><><>$str")
//            }
//        }
//        (1..16).forEach {city_code->
//            var id =myRef.push().key
//            (0..30).forEach {date->
//                (4..5).forEach {cat_code->
//                    id =myRef.push().key
//                    cal = Calendar.getInstance()
//                    cal.add(Calendar.DAY_OF_MONTH,Calendar.DAY_OF_MONTH-Calendar.DAY_OF_MONTH-date)
//                    formattedDate = dateFormat.format(cal.time)
//                    myRef.child(id!!).setValue(dataFireBase.Price(cat_code,city_code,city_code,"2,4000;3,1000",formattedDate))
//                    println("$city_code $date $cat_code")
//                }
//            }
//
//        }
        //endregion

//region widgetTest
        /*test.setCustomEventListener(object : OnCustomEventListener {
            override fun onEvent() {
                println("<><><><><><><><><><><><")
            }

            override fun onEvent2() {
                println("onEvent2")
                button.text="oneventtt4"
            }
        })
       // test.deneme("test345")
        test.text="123123123"*/
//endregion

    }

    private fun getCurrency(){
        try {
            val url = URL(dataFireBase.currencyUrl)
            val conn = url.openConnection() as HttpURLConnection
            if(conn.responseCode == HttpURLConnection.HTTP_OK){
                val stream = BufferedInputStream(conn.inputStream)
                val documentBuilderFac = DocumentBuilderFactory.newInstance()
                val documentBuilder = documentBuilderFac.newDocumentBuilder()
                val document = documentBuilder.parse(stream)
                println("<><><>>>>${document.toString()}")
                val doviz = document.getElementsByTagName("Currency")
//                    Toast.makeText(this,doviz.toString(),Toast.LENGTH_SHORT).show()
                if(doviz != null){
                    for(i in 0..doviz.length-1){
                        if((doviz.item(i) as Element).getAttribute("Kod").equals("USD")){
                            dataFireBase.usdAlis = (doviz.item(i) as Element).getElementsByTagName("ForexBuying").item(0).firstChild.nodeValue
                            dataFireBase.usdSatis = (doviz.item(i) as Element).getElementsByTagName("ForexSelling").item(0).firstChild.nodeValue
                        }
                        else if((doviz.item(i) as Element).getAttribute("Kod").equals("EUR")){
                            dataFireBase.eurAlis = (doviz.item(i) as Element).getElementsByTagName("ForexBuying").item(0).firstChild.nodeValue
                            dataFireBase.eurSatis = (doviz.item(i) as Element).getElementsByTagName("ForexSelling").item(0).firstChild.nodeValue
                        }
                        if(dataFireBase.usdAlis!="" && dataFireBase.eurSatis!="" && dataFireBase.eurAlis!="" && dataFireBase.usdSatis!="") break
                    }
                }
            }
            else{
                Toast.makeText(this,"Merkez Bankasına Bağlanılamadı.",Toast.LENGTH_SHORT).show()
            }
        }
        catch (e: java.lang.Exception){
            println("<><><>>>Currency Error ${e.toString()}")
        }
    }
    private fun refresh(v:View){
        getData()
    }

    private fun loadFragment(fragment : android.support.v4.app.Fragment){
            runOnUiThread {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainConstraintLayout,fragment)
                    .commit()
            }
    }

    private fun displayScreen(menuId : Int){
        Thread{
            var fragment : android.support.v4.app.Fragment? = null
            when(menuId){
//                R.id.hakkimizda->{
//                    fragment  = hakkimizdaFragment()
//                }
                R.id.taksit->{
                    fragment  = taksitFragment()
                }
                R.id.Nakliye->{
                    fragment  = nakliyeFragment()
                }
                R.id.teknikBilgiler->{
                    fragment  = teknikFragment()
                }
                R.id.iletisim->{
                    fragment  = iletisimFragment()
                }
                R.id.fiyat->{
                    fragment  = fiyatFragment()
                }
                R.id.haber->{
                    fragment  = haberlerFragment()
                }
                R.id.fiyatAl->{
                    fragment  = fiyatalFragment()
                }
            }
            if(fragment!=null)
                loadFragment(fragment!!)
        }.start()

    }

    private fun getData(){
        Thread{
            try{
                runOnUiThread {
                    window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    progressBarMain.bringToFront()
                    progressBarMain.visibility = View.VISIBLE
                }
                getCurrency()
                while(!catBool or !newsBool or !priceBool or !productBool or !installmentBool or !shippingBool or !advertisementBool){
                    Thread.sleep(50)
                }
//                println("Mod Offline <><><><>")
//                database.goOffline()
                catBool             = false
                newsBool            = false
                priceBool           = false
                productBool         = false
                installmentBool     = false
                advertisementBool   = false
                shippingBool        = false
                runOnUiThread {
                    displayScreen(R.id.fiyat)
//                    progressBarMain.visibility = View.GONE
//                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
            }
            catch (e: Exception){
                println("getData Thread Except <><><>${e.message}")
            }
            finally {
                runOnUiThread {
                    progressBarMain.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
            }

        }.start()
        onDataChange("Price")
        onDataChange("Category")
        onDataChange("News")
        onDataChange("Product")
        onDataChange("Installment")
        onDataChange("Shipping")
        onDataChange("Advertisement")
    }

    private fun onDataChange(className : String){
        try {
            val _categoryList       = ArrayList<dataFireBase.Category>()
            val _newsList           = ArrayList<dataFireBase.News>()
            val _productList        = ArrayList<dataFireBase.Product>()
            val _priceList          = ArrayList<dataFireBase.Price>()
            val _shippingList       = ArrayList<dataFireBase.Shipping>()
            val _advertisementList  = ArrayList<dataFireBase.Advertisement>()
            val myRef = database.getReference(className)
            myRef.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.children.count()>0){
                        when(className){
                            "Price" ->{
                                _priceList.clear()
                                p0.children.mapNotNullTo(_priceList){
                                    it.getValue(dataFireBase.Price::class.java)
                                }
                                dataFireBase.priceList.clear()
                                dataFireBase.priceList = _priceList.sortedBy {
                                    it.rec_update
                                }.reversed().mapNotNullTo(dataFireBase.priceList){
                                    it
                                }
//                                var list = _priceList.groupBy {
//                                    it.cat_code
//                                }
//                                list = list.toSortedMap()
//                                var map : MutableMap<Int,List<dataFireBase.Price>> = mutableMapOf()
//                                repeat(list.size){
//
//                                }
                                println(dataFireBase.priceList)

                                priceBool = true
                            }
                            "Category" ->{
                                _categoryList.clear()
                                p0.children.mapNotNullTo(_categoryList){
                                    it.getValue(dataFireBase.Category::class.java)
                                }
                                dataFireBase.categoryList.clear()
                                _categoryList.sortedBy { it.cat_code }.mapNotNullTo(dataFireBase.categoryList){it}
                                catBool = true
                            }
                            "News" -> {
                                _newsList.clear()
                                p0.children.mapNotNullTo(_newsList){
                                    it.getValue(dataFireBase.News::class.java)
                                }
                                dataFireBase.newsList.clear()
                                _newsList.sortedBy { it.rec_date }.reversed().mapNotNullTo(dataFireBase.newsList){it}
//                                dataFireBase.newsList.forEach {
//                                    println("<><><><>${it.rec_date}")
//                                }
                                newsBool = true
                            }
                            "Product" ->{
                                _productList.clear()
                                p0.children.mapNotNullTo(_productList){
                                    it.getValue(dataFireBase.Product::class.java)
                                }
                                dataFireBase.productList.clear()
                                _productList.sortedBy { it.index }.mapNotNullTo(dataFireBase.productList){it}
                                productBool = true
                            }
                            "Installment"->{
                                dataFireBase.installmentList.clear()
                                p0.children.mapNotNullTo(dataFireBase.installmentList){
                                    it.getValue(dataFireBase.Installment::class.java)
                                }
                                installmentBool = true
                            }
                            "Shipping"->{
                                _shippingList.clear()
                                dataFireBase.shippingList.clear()
                                p0.children.mapNotNullTo(dataFireBase.shippingList){
                                    it.getValue(dataFireBase.Shipping::class.java)
                                }
                                shippingBool = true
                            }
                            "Advertisement"->{
                                dataFireBase.advertisementList.clear()
                                p0.children.mapNotNullTo(dataFireBase.advertisementList){
                                    it.getValue(dataFireBase.Advertisement::class.java)
                                }
                                advertisementBool = true
                            }
                        }
                    }
                    else{
                        when(className){
                            "Price" -> priceBool = true
                            "Category" -> catBool = true
                            "News" -> newsBool = true
                            "Product" -> productBool = true
                            "Advertisement"-> advertisementBool = true
                            "Shipping"-> shippingBool = true
                        }
                    }
//region liste ekleme
//                        p0.children.forEach {
//                            val price =it.getValue(Price::class.java)
//                            priceList.add(Price(price!!.cat_code
//                                    ,price.city_code
//                                    ,price.index
//                                    ,price.price
//                                    ,price.rec_update))
//                        }
//                        println("COUNT <><>><> ${priceList.size}")
//                        val editor = preferences!!.edit()
//                        editor.putString("Price", priceList.toString()).commit()
//                        println("BITTI")
//                    }
                    //endregion
//                    runOnUiThread {
//                        progressBarMain.visibility = View.GONE
//                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
//                    }
//region JSON okuma
//                    priceList.clear()
//                    var strJson = preferences!!.getString("Price","")

//                    if(!strJson.isNullOrEmpty()){
//                        strJson = strJson.substring(12, endIndex = strJson.length)
//                        var jsonObj = JSONObject(strJson)
//                        jsonObj = JSONObject(jsonObj.getString("value"))
//                        val keys = jsonObj.keys()
//                        keys.forEach {
//                            //println("Element->$it  ${jsonObj.getString(it)}")
//                            val jsonObject = JSONObject(jsonObj.getString(it))
//                            priceList.add(Price(jsonObject.getInt("cat_code"),jsonObject.getInt("city_code"),
//                                    jsonObject.getInt("index"),jsonObject.getInt("price"),jsonObject.getString("rec_update")))
//                            //println("${jsonObject.getString("price")}")
//                        }
//                        Toast.makeText(applicationContext!!, "Veriler Alındı.", Toast.LENGTH_SHORT).show()
//                        println("Count <><> ${priceList.size}")
//                        fun selector(p: Price) : Int = p.index
//                        val newList = priceList.sortedBy { it.index
//                        }
//                        println("$newList")
//                        println("Max<><>< ${priceList.maxBy { it.index }}")
//                    }
//endregion
                }
                override fun onCancelled(p0: DatabaseError) {
                    println("onCancelled :$className <><> $ ${p0.toException()}")
//                    runOnUiThread {
//                        progressBarMain.visibility = View.GONE
//                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
//                    }
                }
            })
//region FirebaseFireStore
            val db = FirebaseFirestore.getInstance()
            /*for(i in 0..1000){
                db.collection("Price")
                        .add(Price(i,i,i,i))
                        .addOnSuccessListener { documentReference ->
                            Log.d("DEBUG", "ID " + documentReference.id)
                        }
                        .addOnFailureListener { e ->
                            Log.w("DEBUG", "Error", e)
                        }
            }*/
            /*Thread{
                for(i in 0..1000)
                    db.collection("Price")
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    Log.d("TEST", document.id + " => " + document.data)
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.w("TEST", "Error getting documents.", exception)
                            }

            }.start()*/
            //endregion

        }
        catch (e:Exception){
            println("Except onDataChange <><> ${e.message}")
//            runOnUiThread {
//                progressBarMain.visibility = View.GONE
//                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
//            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val mDrawerLayout : DrawerLayout= findViewById(R.id.drawerLayout)
                mDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun rowClick(v:View){
        Toast.makeText(this,v.tag.toString(),Toast.LENGTH_SHORT).show()
    }
}
