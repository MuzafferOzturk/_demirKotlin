package com.rookiesoft.demir

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView

class haberlerFragment :android.support.v4.app.Fragment{
    constructor() : super()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView : View = inflater!!.inflate(R.layout.fragment_haberler,null)
        val listArray : ArrayList<haberLvClass> = ArrayList()
        dataFireBase.newsList.forEach {
            listArray.add(haberLvClass(BitmapFactory.decodeResource(resources!!,R.drawable.logo2),it.title,it.content))
            println(it.title)
        }
//        listArray.add(haberLvClass(BitmapFactory.decodeResource(resources!!,R.drawable.logo),"TEST1","Mücadelede gol perdesini 8. dakikada Diego Perotti açarken, 23. dakikada ise Shick durumu 2-0'a getirdi. İlk yarıyı 2-0 önde kapatan Roma, 60. dakikada Zaniolo'nun golüyle 3-0'ı yakaladı. Roma'nın 3. golünde ise Zaniolo'ya gol pasını Cengiz Ünder verdi. Milli futbolcu, bu sezon forma giydiği resmi maçlarda 5. asistine ulaşmış oldu. Konuk ekibin tek golü ise mücadelenin 90. dakikasında Babacar'dan geldi."))
//        listArray.add(haberLvClass(BitmapFactory.decodeResource(resources!!,R.drawable.logo),"TEST2","ICERIK2"))
//        listArray.add(haberLvClass(BitmapFactory.decodeResource(resources!!,R.drawable.logo),"TEST6","ICERIK6"))
//        listArray.add(haberLvClass(BitmapFactory.decodeResource(resources!!,R.drawable.logo),"TEST3","Mücadelede gol perdesini 8. dakikada Diego Perotti açarken, 23. dakikada ise Shick durumu 2-0'a getirdi. İlk yarıyı 2-0 önde kapatan Roma, 60. dakikada Zaniolo'nun golüyle 3-0'ı yakaladı. Roma'nın 3. golünde ise Zaniolo'ya gol pasını Cengiz Ünder verdi. Milli futbolcu, bu sezon forma giydiği resmi maçlarda 5. asistine ulaşmış oldu. Konuk ekibin tek golü ise mücadelenin 90. dakikasında Babacar'dan geldi."))
//        listArray.add(haberLvClass(BitmapFactory.decodeResource(resources!!,R.drawable.logo),"TEST4","ICERIK4"))
//        listArray.add(haberLvClass(BitmapFactory.decodeResource(resources!!,R.drawable.logo),"TEST5","Mücadelede gol perdesini 8. dakikada Diego Perotti açarken, 23. dakikada ise Shick durumu 2-0'a getirdi. İlk yarıyı 2-0 önde kapatan Roma, 60. dakikada Zaniolo'nun golüyle 3-0'ı yakaladı. Roma'nın 3. golünde ise Zaniolo'ya gol pasını Cengiz Ünder verdi. Milli futbolcu, bu sezon forma giydiği resmi maçlarda 5. asistine ulaşmış oldu. Konuk ekibin tek golü ise mücadelenin 90. dakikasında Babacar'dan geldi."))

        val listView : ListView = rootView.findViewById(R.id.lvHaberlerFragment)
        val haberlerAdap : haberlerAdapter? = haberlerAdapter(activity!!,listArray)
        listView.adapter = haberlerAdap
        listView.setOnItemClickListener(object: AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val baslik = p1?.findViewById(R.id.tvBaslikHaberler) as TextView
                val icerik = p1?.findViewById(R.id.tvIcerikHaberler) as TextView
                val intent = Intent(context,newsDetailActivity::class.java)
                intent.putExtra("baslik",baslik.text)
                intent.putExtra("icerik",icerik.text)
                startActivity(intent)
            }
        })
        return rootView
    }
}
data class haberLvClass(val image : Bitmap, val baslik : String, val icerik : String)