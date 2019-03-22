package com.rookiesoft.demir

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_iletisim.view.*

class iletisimFragment() : android.support.v4.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_iletisim,null)
        val btnCall =  rootView.findViewById(R.id.fActionBtnCall) as Button
        btnCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + (rootView.findViewById(R.id.fActionBtnCall) as Button).text))
            startActivity(intent)
        }

        val btnMail =  rootView.findViewById(R.id.fActionBtnMail) as Button
        btnMail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:${(rootView.findViewById(R.id.fActionBtnMail) as Button).text}"))
            intent.putExtra(Intent.EXTRA_SUBJECT,"Fiyat Teklifi")
            intent.putExtra(Intent.EXTRA_TEXT,"Lütfen çap listenizi ve teslim yerinizi gönderiniz.")
            startActivity(intent)
        }
        rootView.fActionBtnLoc.setOnClickListener (object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val uri = Uri.parse("google.navigation:q=Oymaağaç Mahallesi+Mobilyakent+Kocasinan/Kayseri&mode=d")
                val intent = Intent(Intent.ACTION_VIEW,uri)
                intent.setPackage("com.google.android.apps.maps")
                startActivity(intent)
            }
        })

        return rootView
    }
}