plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.moviematch"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.moviematch"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // --- ДОБАВЛЕННЫЕ БИБЛИОТЕКИ ДЛЯ MOVIEMATCH ---

    // Retrofit для связи с базой данных Supabase по REST API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // CardStackView для реализации механики свайпов (как в Tinder)
    implementation("com.github.yuyakaido:CardStackView:v2.3.4")

    // Glide для плавной загрузки и отображения постеров фильмов
    implementation("com.github.bumptech.glide:glide:4.16.0")
}