package com.example.listtest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listtest.util.DateUtil

class NewsAdapter(private var items: MutableList<DisplayableItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 定义两种视图类型
    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_NEWS = 1
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val sourceTextView: TextView = itemView.findViewById(R.id.sourceTextView)
    }

    class DateHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateHeaderTextView: TextView = itemView.findViewById(R.id.dateHeaderTextView)
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is DisplayableItem.DateHeader -> TYPE_HEADER
            is DisplayableItem.News -> TYPE_NEWS
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_date_header, parent, false)
            DateHeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_news, parent, false)
            NewsViewHolder(view)
        }
    }

    // 绑定数据到对应的 ViewHolder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val currentItem = items[position]) {
            // 绑定日期数据
            is DisplayableItem.DateHeader -> {
                (holder as DateHeaderViewHolder).dateHeaderTextView.text = currentItem.date
            }
            // 绑定新闻数据
            is DisplayableItem.News -> {
                val newsHolder = holder as NewsViewHolder
                val newsItem = currentItem.newsItem
                newsHolder.titleTextView.text = newsItem.title
                newsHolder.timeTextView.text = DateUtil.getFormattedTime(newsItem.time)
                newsHolder.sourceTextView.text = newsItem.source
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getItems(): List<DisplayableItem> {
        return items
    }

    fun updateData(newData: List<DisplayableItem>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    fun addData(newData: List<DisplayableItem>) {
        val startPosition = items.size
        items.addAll(newData)
        notifyItemRangeInserted(startPosition, newData.size)
    }
}