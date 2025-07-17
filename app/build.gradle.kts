plugins {
    // 声明这是 Android 应用程序插件
    id("com.android.application")
    // 声明使用 Kotlin Android 插件
    id("org.jetbrains.kotlin.android")
}

android {
    // 为项目设置命名空间，这是新版 Gradle 插件的要求
    namespace = "com.example.listtest"
    // 设置编译项目的 SDK 版本
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.listtest"
        // 设置应用支持的最低 Android 版本
        minSdk = 24
        // 设置目标 Android 版本
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // 是否启用代码混淆
            isMinifyEnabled = false
            // 混淆规则文件
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // 设置 Java 语言的编译选项
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // 设置 Kotlin 语言的编译选项
    kotlinOptions {
        jvmTarget = "1.8"
    }

    // 启用 View Binding，可以更方便地访问视图（可选，但推荐）
    // 如果你不想用，可以删除这个 buildFeatures 代码块
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // --- 核心 AndroidX 库 ---
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // --- 我们项目需要的第三方库 ---

    // RecyclerView 用于显示列表
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // OkHttp 用于网络请求
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Gson 用于解析 JSON 数据
    implementation("com.google.code.gson:gson:2.10.1")


    // --- 测试相关的库 ---
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
