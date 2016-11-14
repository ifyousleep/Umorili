package com.ifyou.umorili.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ifyou.umorili.R
import com.ifyou.umorili.net.PostModel
import kotlinx.android.synthetic.main.adapter_main_item.view.*

class MainAdapter(val context: Context, val data: List<Any>, val size: Float) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_main_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MainAdapter.ViewHolder, position: Int) {
        holder.bindData(data[position] as PostModel, size)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindData(item: PostModel, size: Float) {
            val result: Spanned
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                result = Html.fromHtml(item.elementPureHtml, Html.FROM_HTML_MODE_LEGACY)
            } else {
                @Suppress("DEPRECATION")
                result = Html.fromHtml(item.elementPureHtml)
            }

            itemView.itemText.textSize = size
            itemView.itemText.text = result

            itemView.card.setOnClickListener(
                    { view ->

                    }
            )
        }
    }
}