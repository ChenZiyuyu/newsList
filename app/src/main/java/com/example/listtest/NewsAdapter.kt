package com.example.listtest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NewsAdapter(private var newsList: MutableList<NewsItem>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val sourceTextView: TextView = itemView.findViewById(R.id.sourceTextView)
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

    fun updateData(newData: List<NewsItem>) {
        newsList.clear()
        newsList.addAll(newData)
        notifyDataSetChanged()
    }

    fun addData(newData: List<NewsItem>) {
        val startPosition = newsList.size
        newsList.addAll(newData)
        notifyItemRangeInserted(startPosition, newData.size)
    }
}