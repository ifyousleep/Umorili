package com.ifyou.umorili.adapters

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ifyou.umorili.R

class DrawerAdapter(val mNavTitles: Array<String>, val mIcons: List<Drawable>) : RecyclerView.Adapter<DrawerAdapter.ViewHolder>() {

    var selected_item = 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var rowText: TextView? = null
        internal var rowIcon: ImageView? = null
        internal var nav_holder: LinearLayout? = null

        init {
            itemView.isClickable = true
            rowText = itemView.findViewById(R.id.rowText) as TextView
            rowIcon = itemView.findViewById(R.id.rowIcon) as ImageView
            nav_holder = itemView.findViewById(R.id.nav_holder) as LinearLayout
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerAdapter.ViewHolder? {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_drawer_item, parent, false)
        val vhItem = ViewHolder(v)
        return vhItem
    }

    override fun onBindViewHolder(holder: DrawerAdapter.ViewHolder, position: Int) {
        if (position == selected_item) {
            holder.nav_holder?.setBackgroundResource(R.color.colorBlu)
        }
        else {
            holder.nav_holder?.setBackgroundResource(R.color.colorCard)
        }
        holder.rowText?.text = mNavTitles[position]
        holder.rowIcon?.setImageDrawable(mIcons[position])
    }

    override fun getItemCount(): Int {
        return mNavTitles.size
    }
}