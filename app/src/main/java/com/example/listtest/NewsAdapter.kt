package com.example.listtest
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listtest.NewsItem

class NewsAdapter(private var newsList: List<NewsItem>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val sourceTextView: TextView = itemView.findViewById(R.id.sourceTextView)
        val newsImageView: ImageView = itemView.findViewById(R.id.newsImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentNews = newsList[position]
        holder.titleTextView.text = currentNews.title
        holder.timeTextView.text = currentNews.time
        holder.sourceTextView.text = currentNews.source

    }

    override fun getItemCount(): Int {
        return newsList.size
    }
    fun updateData(newNewsList: List<NewsItem>) {
        // 1. 更新内部的数据列表引用
        this.newsList = newNewsList
        // 2. 通知 RecyclerView 数据集已经完全改变，需要重绘整个列表
        notifyDataSetChanged()
    }

}

