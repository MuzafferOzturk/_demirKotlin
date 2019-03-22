package com.rookiesoft.demir

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class haberlerAdapter(private val context: Context,private val listArray : ArrayList<haberLvClass>) : BaseAdapter() {

    override fun getViewTypeCount(): Int {
        return count
    }

    override fun getItemViewType(position: Int): Int {

        return position
    }

    override fun getCount(): Int {
        return listArray.size
    }

    override fun getItem(position: Int): Any {
        return listArray[position]
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
            convertView = inflater.inflate(R.layout.haberler_layout, null, false)

            holder.tvBaslik = convertView!!.findViewById(R.id.tvBaslikHaberler) as TextView
            holder.tvIcerik = convertView!!.findViewById(R.id.tvIcerikHaberler) as TextView
            holder.iv = convertView.findViewById(R.id.ivHaberlerLayout) as ImageView

            convertView.tag = holder
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = convertView.tag as ViewHolder
        }
        holder.tvBaslik!!.text = listArray[position].baslik
        holder.tvIcerik!!.text = listArray[position].icerik
        holder.iv!!.setImageBitmap(listArray[position].image)

        return convertView
    }

    private inner class ViewHolder {
        var tvBaslik: TextView? = null
        var tvIcerik: TextView? = null
        internal var iv: ImageView? = null
    }

}