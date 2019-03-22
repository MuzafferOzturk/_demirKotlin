package com.rookiesoft.demir

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import java.net.URLEncoder

class fiyatalFragment :android.support.v4.app.Fragment{
    constructor() : super()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView : View = inflater!!.inflate(R.layout.fragment_fiyatal,null)
        val btnCall =  rootView.findViewById(R.id.btnCall) as Button
        btnCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+" + dataFireBase.whatsAppPhone))
            startActivity(intent)
        }

        val btnMail =  rootView.findViewById(R.id.btnMail) as Button
        btnMail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${dataFireBase.mail}"))
            intent.putExtra(Intent.EXTRA_SUBJECT,"Fiyat Teklifi")
            intent.putExtra(Intent.EXTRA_TEXT,"Lütfen çap listenizi ve teslim yerinizi gönderiniz")
            startActivity(intent)
        }
        val btnWhatsApp = rootView.findViewById(R.id.btnWhatsApp) as Button
        btnWhatsApp.setOnClickListener {
//            val intent = Intent(Intent.ACTION_SEND)
//            intent.putExtra(Intent.EXTRA_TEXT,"Lütfen çap listenizi ve teslim yerinizi gönderiniz")
//            intent.putExtra("jid",dataFireBase.whatsAppPhone+"@s.whatsapp.net")
//            intent.setType("text/plain")
//            intent.setPackage("com.whatsapp")
//            startActivity(intent)
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
        val btnSms = rootView.findViewById(R.id.btnSms) as Button
        btnSms.setOnClickListener {
            val uri = Uri.parse("smsto:+${dataFireBase.whatsAppPhone}")
            val intent = Intent(Intent.ACTION_SENDTO,uri)
            intent.putExtra("sms_body","Lütfen çap listenizi ve teslim yerinizi gönderiniz")
            startActivity(intent)
        }
        return rootView
    }
}