package com.example.listtest

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout // 1. 导入 SwipeRefreshLayout
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout // 2. 声明 SwipeRefreshLayout 变量

    private val client = OkHttpClient()
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        newsAdapter = NewsAdapter(emptyList())
        recyclerView.adapter = newsAdapter

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)

        swipeRefreshLayout.setOnRefreshListener {
            fetchNews()
        }

        fetchNews()
    }

    private fun fetchNews() {
        val url = "https://news.cj.sina.cn/app/v1/news724/list?from=hmwidget&version=8.16.0&deviceId=0062738dda096582bead16efd7d0dd72&is_important=0&tag=0&nonce=72000382&ts=1752547529&sign=c5cc82cb675340e53ccb43130a8389e1&apptype=10&id=&dire=f&up=0&num=20&deviceid=0062738dda096582bead16efd7d0dd72"
        val userAgent = "sinafinancehmscar__1.1.11__android__f41eb02ce388f1c086eb8d24a821c18c__12__OCE-AN50"
        val currentTimeSeconds = System.currentTimeMillis() / 100

        val request = Request.Builder()
            .url(url)
            .header("User-Agent", userAgent)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "网络请求失败: ${e.message}", Toast.LENGTH_LONG).show()
                    // 请求失败后，隐藏刷新指示器
                    swipeRefreshLayout.isRefreshing = false
                }
                Log.e("MainActivity", "OkHttp Failed", e)
            }

            override fun onResponse(call: Call, response: Response) {
                val onFinally = {
                    runOnUiThread {
                        swipeRefreshLayout.isRefreshing = false
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
                            newsAdapter.updateData(newsList)
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