plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.listtest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.listtest"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
                      proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

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
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.appcompat:appcompat:1.0.0")      // 必须 1.0.0 以上

    implementation("io.github.scwang90:refresh-layout-kernel:3.0.0-alpha")  // 核心必须依赖
    implementation("io.github.scwang90:refresh-header-classics:3.0.0-alpha") // 经典刷新头
    implementation("io.github.scwang90:refresh-header-radar:3.0.0-alpha")      // 雷达刷新头
    implementation("io.github.scwang90:refresh-header-falsify:3.0.0-alpha")   // 虚拟刷新头
    implementation("io.github.scwang90:refresh-header-material:3.0.0-alpha")  // 谷歌刷新头
    implementation("io.github.scwang90:refresh-header-two-level:3.0.0-alpha") // 二级刷新头
    implementation("io.github.scwang90:refresh-footer-ball:3.0.0-alpha")      // 球脉冲加载
    implementation("io.github.scwang90:refresh-footer-classics:3.0.0-alpha")  // 经典加载
}
