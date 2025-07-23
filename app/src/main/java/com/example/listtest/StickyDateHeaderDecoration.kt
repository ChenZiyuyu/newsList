package com.example.listtest

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StickyDateHeaderDecoration(private val adapter: NewsAdapter) : RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val topNewsTime = findTopVisibleNewsTime(parent) ?: return
        val headerView = createHeaderView(parent, topNewsTime)
        fixLayoutSize(parent, headerView)
        val contactPoint = headerView.bottom
        val childInContact = getChildInContact(parent, contactPoint)

        if (childInContact != null && isHeader(parent, childInContact)) {
            moveHeader(c, headerView, childInContact)
            return
        }

        drawHeader(c, headerView)
    }

    /**
     * 新增的核心逻辑：遍历屏幕上所有可见的 item，
     * 找到第一个 "新闻" 类型的 item，并返回它的时间。
     */
    private fun findTopVisibleNewsTime(parent: RecyclerView): String? {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            if (position != RecyclerView.NO_POSITION) {
                val item = adapter.getItems()[position]
                if (item is DisplayableItem.News) {
                    // 找到了第一个可见的新闻项，返回它的时间
                    return item.newsItem.time
                }
            }
        }
        return null
    }

    private fun createHeaderView(parent: RecyclerView, text: String): View {
        val headerView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_date_header, parent, false)
        val textView = headerView.findViewById<TextView>(R.id.dateHeaderTextView)
        textView.text = text
        return headerView
    }

    private fun fixLayoutSize(parent: ViewGroup, view: View) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)
        val childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, parent.paddingLeft + parent.paddingRight, view.layoutParams.width)
        val childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, parent.paddingTop + parent.paddingBottom, view.layoutParams.height)
        view.measure(childWidthSpec, childHeightSpec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    private fun getChildInContact(parent: RecyclerView, contactPoint: Int): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child.bottom > contactPoint && child.top <= contactPoint) {
                childInContact = child
                break
            }
        }
        return childInContact
    }

    private fun isHeader(parent: RecyclerView, view: View): Boolean {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return false
        return adapter.getItems()[position] is DisplayableItem.DateHeader
    }

    private fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View) {
        c.save()
        c.translate(0f, (nextHeader.top - currentHeader.height).toFloat())
        currentHeader.draw(c)
        c.restore()
    }

    private fun drawHeader(c: Canvas, header: View) {
        c.save()
        c.translate(0f, 0f)
        header.draw(c)
        c.restore()
    }
}