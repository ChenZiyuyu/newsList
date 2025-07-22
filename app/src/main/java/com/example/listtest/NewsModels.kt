package com.example.listtest

import com.google.gson.annotations.SerializedName


data class TopLevelResponse(
    @SerializedName("result") val result: ApiResult
)

data class ApiResult(
    @SerializedName("data") val data: NewsDataContainer
)

data class NewsDataContainer(
    @SerializedName("data") val newsList: List<NewsItem>
)


data class NewsItem(
    @SerializedName("content") val title: String?,
    @SerializedName("view_num") val source: String?,
    @SerializedName("ctime") val time: String?
)