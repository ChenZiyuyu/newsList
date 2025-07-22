package com.example.listtest

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.scwang.smart.refresh.layout.SmartRefreshLayout // 1. 导入正确的 SmartRefreshLayout
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var refreshLayout: SmartRefreshLayout // 2. 声明 SmartRefreshLayout 变量

    private val client = OkHttpClient()
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化 RecyclerView 和 Adapter
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        newsAdapter = NewsAdapter(mutableListOf())
        recyclerView.adapter = newsAdapter

        // 初始化 SmartRefreshLayout
        refreshLayout = findViewById(R.id.refreshLayout)

        // 3. 设置 SmartRefreshLayout 的刷新监听
        refreshLayout.setOnRefreshListener {
            fetchNews(isRefresh = true)
        }
        refreshLayout.setOnLoadMoreListener {
            fetchNews(isRefresh = false)
        }
        refreshLayout.autoRefresh()
    }

    private fun fetchNews(isRefresh: Boolean) {
        val url = "https://news.cj.sina.cn/app/v1/news724/list?from=hmwidget&version=8.16.0&deviceId=0062738dda096582bead16efd7d0dd72&is_important=0&tag=0&nonce=72000382&ts=1752547529&sign=c5cc82cb675340e53ccb43130a8389e1&apptype=10&id=&dire=f&up=0&num=20&deviceid=0062738dda096582bead16efd7d0dd72"
        val userAgent = "sinafinancehmscar__1.1.11__android__f41eb02ce388f1c086eb8d24a821c18c__12__OCE-AN50"

        val request = Request.Builder()
            .url(url)
            .header("User-Agent", userAgent)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "网络请求失败: ${e.message}", Toast.LENGTH_LONG).show()
                    if (isRefresh) {
                        refreshLayout.finishRefresh(false)
                    } else {
                        refreshLayout.finishLoadMore(false)
                    }
                }
                Log.e("MainActivity", "OkHttp Failed", e)
            }

            override fun onResponse(call: Call, response: Response) {
                val onFinally = {
                    runOnUiThread {
                        if (isRefresh) {
                            refreshLayout.finishRefresh()
                        } else {
                            refreshLayout.finishLoadMore()
                        }
                    }
                }

                if (!response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "请求失败，状态码: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                    onFinally()
                    return
                }

                val responseBody = response.body?.string()
                if (responseBody != null) {
                    try {
                        val topLevelResponse = gson.fromJson(responseBody, TopLevelResponse::class.java)
                        val newsList = topLevelResponse.result.data.newsList
                        runOnUiThread {
                            //  修正数据更新逻辑
                            if (isRefresh) {
                                newsAdapter.updateData(newsList)
                            } else {
                                newsAdapter.addData(newsList)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("MainActivity", "JSON 解析失败", e)
                        runOnUiThread {
                            Toast.makeText(applicationContext, "数据解析错误", Toast.LENGTH_SHORT).show()
                        }
                    } finally {
                        onFinally()
                    }
                } else {
                    onFinally()
                }
            }
        })
    }
}