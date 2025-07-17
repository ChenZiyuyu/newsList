package com.example.listtest

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter

    // 创建 OkHttpClient 和 Gson 实例
    private val client = OkHttpClient()
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 初始化适配器
        newsAdapter = NewsAdapter(emptyList())
        recyclerView.adapter = newsAdapter

        // 获取新闻数据
        fetchNews()
    }


    private fun fetchNews() {

        val url = "https://news.cj.sina.cn/app/v1/news724/list?from=hmwidget&version=8.16.0&deviceId=0062738dda096582bead16efd7d0dd72&is_important=0&tag=0&nonce=72000382&ts=1752547529&sign=c5cc82cb675340e53ccb43130a8389e1&apptype=10&id=&dire=f&up=0&num=20&deviceid=0062738dda096582bead16efd7d0dd72"     //        // 这是必须的 User-Agent
        val userAgent =
            "sinafinancehmscar__1.1.11__android__f41eb02ce388f1c086eb8d24a821c18c__12__OCE-AN50"

        // 创建一个 Request，并添加 User-Agent 请求头
        val request = Request.Builder()
            .url(url)
            .header("User-Agent", userAgent)
            .build()

        // 异步执行请求
        client.newCall(request).enqueue(object : Callback {
            // 请求失败时的回调
            override fun onFailure(call: Call, e: IOException) {
                // 确保在 UI 线程上显示 Toast
                runOnUiThread {
                    Toast.makeText(applicationContext, "网络请求失败: ${e.message}", Toast.LENGTH_LONG).show()
                }
                Log.e("MainActivity", "OkHttp Failed", e)
            }

            // 请求成功时的回调
            override fun onResponse(call: Call, response: Response) {
                // 检查响应是否成功
                if (!response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "请求失败，状态码: ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                    return
                }

                // 获取响应体
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    Log.d("JSON_RESPONSE", "收到的原始JSON: $responseBody")
                    try {
                        // 使用 Gson 解析 JSON
                        val topLevelResponse = gson.fromJson(responseBody, TopLevelResponse::class.java)
                        // 根据新的数据结构层层深入，获取新闻列表
                        val newsList = topLevelResponse.result.data.newsList

                        // 切换回 UI 线程来更新 RecyclerView
                        runOnUiThread {
                            newsAdapter.updateData(newsList)
                        }
                    } catch (e: Exception) {
                        Log.e("MainActivity", "JSON 解析失败", e)
                        runOnUiThread {
                            Toast.makeText(applicationContext, "数据解析错误", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }
}
