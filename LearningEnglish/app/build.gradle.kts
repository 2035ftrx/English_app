plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.learningenglish"
    compileSdk = 34
    flavorDimensions += "versionCode"

    defaultConfig {
        applicationId = "com.example.learningenglish"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }

    }

    productFlavors {

        create("QA") {
            applicationIdSuffix = ".qa"
            buildConfigField("String", "BuildType", "\"QA\"")
        }

        create("DEV") {
            applicationIdSuffix = ".dev"
            buildConfigField("String", "BuildType", "\"Dev\"")
        }

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("androidx.compose.material:material:1.6.8")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.8")

    val paging_version = "3.3.0"
    implementation("androidx.paging:paging-runtime:$paging_version")
    implementation("androidx.paging:paging-compose:$paging_version")

    val viewmodel_version = "2.8.1"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${viewmodel_version}")
    implementation("androidx.lifecycle:lifecycle-viewmodel:${viewmodel_version}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${viewmodel_version}")

    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // logger
    api("com.jakewharton.timber:timber:5.0.1")
    implementation("io.coil-kt:coil:2.6.0")
    implementation("io.coil-kt:coil-compose:2.6.0")

    val room_version = "2.6.1"
    // To use Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:$room_version")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.room:room-paging:$room_version")

    // network
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:${retrofitVersion}@jar")
    implementation("com.squareup.retrofit2:converter-gson:${retrofitVersion}@jar")
    val okHttpVersion = "4.12.0"
    implementation("com.squareup.okhttp3:okhttp:${okHttpVersion}")
    implementation("com.squareup.okhttp3:logging-interceptor:${okHttpVersion}@jar")
    // gson
    implementation("com.google.code.gson:gson:2.10.1")

    val workVersion = "2.9.0"
    implementation("androidx.work:work-runtime:${workVersion}")
    implementation("androidx.work:work-runtime-ktx:${workVersion}")
    implementation("androidx.work:work-multiprocess:${workVersion}")

}