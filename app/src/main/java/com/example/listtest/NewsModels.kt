// file: app/src/main/java/com/example/listtest/NewsModels.kt

package com.example.listtest

import com.google.gson.annotations.SerializedName

/**
 * 这是根据服务器返回的真实 JSON 结构重写的全新数据模型
 */

// 1. JSON 的最外层，它只包含一个 "result" 对象
data class TopLevelResponse(
    @SerializedName("result") val result: ApiResult
)

// 2. "result" 对象里的内容
data class ApiResult(
    @SerializedName("data") val data: NewsDataContainer
)

// 3. 第一个 "data" 对象里的内容，它包含了广告和第二个 "data" 数组
data class NewsDataContainer(
    // 我们只关心新闻列表，广告部分忽略。
    // 注意：JSON里的数组名叫 "data"，我们用 @SerializedName 把它映射到 "newsList" 变量上
    @SerializedName("data") val newsList: List<NewsItem>
)

// 4. 新闻列表里的每一项
data class NewsItem(
    // JSON里的 "content" 字段，映射到我们的 "title" 变量
    @SerializedName("content") val title: String?,

    // JSON里没有 "source"，但有 "view_num"，我们把它当作来源，或者直接忽略
    @SerializedName("view_num") val source: String?,

    // JSON里的 "ctime" 字段，映射到我们的 "time" 变量
    @SerializedName("ctime") val time: String?

    // 注意：实际的 JSON 里没有 "imageUrl"，所以我们在这里不再定义它，
    // 或者你可以保留它，但必须是可空类型并有默认值: val imageUrl: String? = null
)