package com.example.listtest.util

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    // 定义API返回的日期时间格式
    private val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    /**
     * 从 "yyyy-MM-dd HH:mm:ss" 中提取出日期 "yyyy-MM-dd"
     */
    fun getFormattedDate(dateTimeString: String?): String {
        if (dateTimeString.isNullOrEmpty()) return ""
        // 更简单、更高效的方法是直接截取字符串
        return dateTimeString.split(" ").firstOrNull() ?: ""
    }

    /**
     * 从 "yyyy-MM-dd HH:mm:ss" 中提取出时间 "HH:mm:ss"
     */
    fun getFormattedTime(dateTimeString: String?): String {
        if (dateTimeString.isNullOrEmpty()) return ""
        // 同样，直接截取字符串
        return dateTimeString.split(" ").getOrNull(1) ?: ""
    }
}