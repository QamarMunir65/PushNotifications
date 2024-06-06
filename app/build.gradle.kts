plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    id ("kotlin-kapt")
}

android {
    namespace = "com.example.pushnotifications"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pushnotifications"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures{
        dataBinding=true
        viewBinding=true
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.messaging)
    implementation(libs.androidx.work.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))
    //noinspection UseTomlInstead
    implementation("com.google.firebase:firebase-analytics")
    implementation (libs.kpermissions)

    // Lifecycle components
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)

    // Room components
    implementation (libs.androidx.room.runtime)
    kapt (libs.androidx.room.compiler)
    implementation (libs.androidx.room.ktx)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)

    //    dimens library for dp
    implementation (libs.sdp.android)
    //    dimens library for sp
    implementation (libs.ssp.android)


}