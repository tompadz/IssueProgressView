package com.dapadz.issueprogressview.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.dapadz.issueprogressview.data.Issue

class SegmentProgressView @JvmOverloads constructor(
    context:Context,
    attributeSet : AttributeSet? = null
) : ViewGroup(context, attributeSet) {

    private val TAG = "SegmentProgressView"
    private val lineHeight = 25f
    private val margin = 5

    var itemsCount = 0
    private set

    fun setIssues(data:List<Issue>) {
        itemsCount = data.size
        data.forEach {
            val line = ProgressLine(context).apply {
                progressId = it.id
                color = it.type.color
                lineWidth = it.workTime.toFloat()
            }
            addView(line)
        }
        invalidate()
    }

    fun showCurrentIssues(data:List<Issue>) {
        val ids = mutableListOf<Int>()
        data.forEach {
            ids.add(it.id)
        }
        children.forEach {
            val item = it as ProgressLine
            val alpha = if (ids.contains(it.progressId)) 1f else 0.1f
            item.animate().alpha(alpha)
        }
    }

    private fun measureChild(child:View, widthMeasureSpec : Int) {
        val childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        child.measure(widthMeasureSpec, childHeightSpec)
    }

    private fun checkChildType() {
        children.forEach {
            if (it !is ProgressLine) {
                throw Exception("Incorrect child format")
            }
        }
    }

    private fun getAllChildWidth():Int {
        var w = 0
        children.forEach {
            w += it.measuredWidth
        }
        return w
    }

    private fun resizeChild(childWidth:Int, parentWidth:Int) {
        if (parentWidth > childWidth) {
            resizeAllChildToMax(childWidth, parentWidth)
        }else {
            resizeAllChildToMin(childWidth, parentWidth)
        }
    }

    private fun resizeAllChildToMax(childWidth:Int, parentWidth:Int) {
        val sizeDiff = parentWidth - childWidth
        val itemAddWidth = (sizeDiff / childCount) - margin
        children.forEach {
            (it as ProgressLine).lineWidth += itemAddWidth
        }
    }

    private fun resizeAllChildToMin(childWidth:Int, parentWidth:Int) {
        val sizeDiff = childWidth - parentWidth
        val itemAddWidth = (sizeDiff / childCount) + margin
        var tryCount = 0
        children.forEach {
            val item = (it as ProgressLine)
            val updateW = item.lineWidth - itemAddWidth
            if (updateW > item.minWidth) {
                item.lineWidth = updateW
            }else {
                tryCount++
            }
        }
        for (index in 0..tryCount) {
            children.forEach {
                val item = (it as ProgressLine)
                item.lineWidth -= itemAddWidth / (childCount - tryCount)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec : Int, heightMeasureSpec : Int) {

        if (childCount == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

        checkChildType()

        children.forEach {
            measureChild(it, widthMeasureSpec)
        }

        val measureHeight  = MeasureSpec.makeMeasureSpec(lineHeight.toInt(), MeasureSpec.UNSPECIFIED)
        val measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        val allChildWidth = getAllChildWidth()

        Log.e(TAG, allChildWidth.toString())
        Log.e(TAG, measureWidth.toString())

        resizeChild(
            childWidth = allChildWidth,
            parentWidth = measureWidth,
        )

        children.forEach {
            measureChild(it, widthMeasureSpec)
        }

        setMeasuredDimension(widthMeasureSpec, measureHeight)
    }

    override fun onLayout(changed : Boolean, left : Int, top : Int, right : Int, bottom : Int) {
        for (index in 0 until childCount) {
            val isFirstChild = index == 0
            val view = getChildAt(index)
            val rightMargin = if (index != childCount - 1) margin else 0
            if (isFirstChild) {
                view.layout(
                    0,0, view.measuredWidth + rightMargin, bottom
                )
            }else {
                val prevView = getChildAt(index - 1)
                view.layout(
                    prevView.right,
                    0,
                    prevView.right + view.measuredWidth + rightMargin,
                    bottom,
                )
            }
        }
    }
}

