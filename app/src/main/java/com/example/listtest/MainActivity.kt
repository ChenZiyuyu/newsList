package com.example.listtest

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listtest.util.DateUtil
import com.google.gson.Gson
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val TAG = "MyAppDebug"

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var refreshLayout: SmartRefreshLayout

    private val client = OkHttpClient()
    private val gson = Gson()

    private var lastNewsId: String? = null
    private var lastDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        newsAdapter = NewsAdapter(mutableListOf())
        recyclerView.adapter = newsAdapter

        recyclerView.addItemDecoration(StickyDateHeaderDecoration(newsAdapter))

        refreshLayout = findViewById(R.id.refreshLayout)

        refreshLayout.setOnRefreshListener {
            Log.d(TAG, "下拉刷新被触发")
            fetchNews(isRefresh = true)
        }
        refreshLayout.setOnLoadMoreListener {
            Log.d(TAG, "上拉加载被触发")
            fetchNews(isRefresh = false)
        }

        Log.d(TAG, "MainActivity onCreate: 准备自动刷新")
        refreshLayout.autoRefresh()
    }

    private fun fetchNews(isRefresh: Boolean) {
        Log.d(TAG, "fetchNews: 开始获取新闻, isRefresh = $isRefresh")

        if (isRefresh) {
            lastNewsId = null
            lastDate = null
        }

        val url = if (isRefresh) {
            "https://news.cj.sina.cn/app/v1/news724/list?from=hmwidget&version=8.16.0&deviceId=0062738dda096582bead16efd7d0dd72&is_important=0&tag=0&nonce=72000382&ts=1752547529&sign=c5cc82cb675340e53ccb43130a8389e1&apptype=10&id=&dire=f&up=0&num=20&deviceid=0062738dda096582bead16efd7d0dd72"
        } else {
            "https://news.cj.sina.cn/app/v1/news724/list?from=hmwidget&version=8.16.0&deviceId=0062738dda096582bead16efd7d0dd72&is_important=0&tag=0&nonce=72000382&ts=1752547529&sign=c5cc82cb675340e53ccb43130a8389e1&apptype=10&id=${lastNewsId ?: ""}&dire=b&up=0&num=20&deviceid=0062738dda096582bead16efd7d0dd72"
        }

        Log.d(TAG, "fetchNews: 请求的 URL: $url")

        val userAgent = "sinafinancehmscar__1.1.11__android__f41eb02ce388f1c086eb8d24a821c18c__12__OCE-AN50"
        val request = Request.Builder().url(url).header("User-Agent", userAgent).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 网络请求失败
                Log.e(TAG, "onFailure: 网络请求失败!", e)
                runOnUiThread {
                    Toast.makeText(applicationContext, "网络请求失败: ${e.message}", Toast.LENGTH_LONG).show()
                    if (isRefresh) refreshLayout.finishRefresh(false) else refreshLayout.finishLoadMore(false)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val onFinally = {
                    runOnUiThread {
                        if (isRefresh) refreshLayout.finishRefresh() else refreshLayout.finishLoadMore()
                    }
                }

                if (!response.isSuccessful) {
                    Log.e(TAG, "onResponse: 响应失败, code = ${response.code}")
                    onFinally()
                    return
                }

                val responseBody = response.body?.string()
                if (responseBody != null) {
                    Log.d(TAG, "onResponse: 成功获取到响应体，内容长度: ${responseBody.length}")

                    try {
                        val topLevelResponse = gson.fromJson(responseBody, TopLevelResponse::class.java)
                        val newsList = topLevelResponse.result.data.newsList
                        Log.d(TAG, "onResponse: JSON 解析成功，获取到 newsList 大小: ${newsList.size}")

                        if (newsList.isNotEmpty()) {
                            lastNewsId = newsList.last().id
                        }

                        val displayableItems = processNewsData(newsList)
                        Log.d(TAG, "onResponse: processNewsData 处理完毕，生成 displayableItems 大小: ${displayableItems.size}")

                        runOnUiThread {
                            if (displayableItems.isEmpty() && isRefresh) {
                                Log.w(TAG, "警告: 刷新后没有获取到任何可显示的数据。")
                            }
                            Log.d(TAG, "runOnUiThread: 准备更新 Adapter...")
                            if (isRefresh) {
                                newsAdapter.updateData(displayableItems)
                            } else {
                                if (newsList.isEmpty()) {
                                    refreshLayout.finishLoadMoreWithNoMoreData()
                                    Toast.makeText(applicationContext, "没有更多历史记录了", Toast.LENGTH_SHORT).show()
                                } else {
                                    newsAdapter.addData(displayableItems)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "onResponse: 解析 JSON 或处理数据时发生异常!", e)
                    } finally {
                        onFinally()
                    }
                } else {
                    Log.e(TAG, "onResponse: 响应成功，但响应体 response.body 为 null")
                    onFinally()
                }
            }
        })
    }

    private fun processNewsData(newsList: List<NewsItem>): List<DisplayableItem> {
        val displayableItems = mutableListOf<DisplayableItem>()
        newsList.forEach { newsItem ->
            val date = DateUtil.getFormattedDate(newsItem.time)
            if (date != lastDate) {
                displayableItems.add(DisplayableItem.DateHeader(date))
                lastDate = date
            }
            displayableItems.add(DisplayableItem.News(newsItem))
        }
        return displayableItems
    }
}