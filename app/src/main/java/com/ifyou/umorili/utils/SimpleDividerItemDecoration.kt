package com.ifyou.umorili.utils

import android.support.v7.widget.RecyclerView
import android.content.Context
import android.graphics.Canvas
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView.*
import com.ifyou.umorili.R

class SimpleDividerItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    val divider = ContextCompat.getDrawable(context, R.drawable.line_divider)!!

    /*private fun Int.pxToDp(context: Context): Int {
        return (this / context.resources.displayMetrics.density + 0.5f).toInt()
    }*/

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density + 0.5f ).toInt()
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: State?) {

        val left = parent.paddingLeft + 16.dpToPx(parent.context)
        val right = parent.width - parent.paddingRight - 16.dpToPx(parent.context)
        val childCount = parent.childCount

        for (i in 0..childCount - 1) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + divider.intrinsicHeight

            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }
}