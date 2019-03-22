package com.rookiesoft.demir

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.rookiesoft.demir.R

class taksitAdapter(private val context: Context,private val imageList : ArrayList<Int>,private val strList : ArrayList<String>) : BaseAdapter() {

    override fun getViewTypeCount(): Int {
        return count
    }

    override fun getItemViewType(position: Int): Int {

        return position
    }

    override fun getCount(): Int {
        return strList.size
    }

    override fun getItem(position: Int): Any {
        return strList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            holder = ViewHolder()
            /*val inflater = context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater*/
            val inflater = LayoutInflater.from(context)
            convertView = inflater.inflate(R.layout.taksitlv_layout, null, false)

            holder.tvname = convertView!!.findViewById(R.id.tvTaksitLayout) as TextView
            holder.iv = convertView.findViewById(R.id.ivTaksitLayout) as ImageView

            convertView.tag = holder
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = convertView.tag as ViewHolder
        }
        holder.tvname!!.text = "${strList[position]} TAKSİT"
        holder.iv!!.setImageResource(imageList[position])


        return convertView
    }

    private inner class ViewHolder {
        var tvname: TextView? = null
        internal var iv: ImageView? = null
    }

}