package com.dapadz.issueprogressview

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.dapadz.issueprogressview.AndroidUtils.Companion.dpf


class DividerDecorator : RecyclerView.ItemDecoration() {
    override fun onDrawOver(c : Canvas, parent : RecyclerView, state : RecyclerView.State) {
        c.save()
        parent.children.forEach {
            c.drawLine(
                47f.dpf(),
                it.bottom.toFloat(),
                it.right.toFloat(),
                it.bottom.toFloat(),
                Paint().apply {
                    color = Color.BLACK
                    style = Paint.Style.FILL
                }
            )
        }
        c.restore()
    }
}