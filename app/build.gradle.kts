import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kserialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

android {
    namespace = "com.rk.pace"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.rk.pace"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "GOOGLE_MAPS_API_KEY",
            "\"${properties.getProperty("GOOGLE_MAPS_API_KEY")}\""
        )

        manifestPlaceholders[
            "GOOGLE_MAPS_API_KEY"
        ] = properties.getProperty("GOOGLE_MAPS_API_KEY")
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
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.material.icons.extended)

    ksp(libs.hilt)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation)
    ksp(libs.androidx.hilt)

    implementation(libs.coroutines)
    implementation(libs.coroutines.android)
    implementation(libs.androidx.room.rt)
    ksp(libs.androidx.room)
    implementation(libs.androidx.room.ktx)

    implementation(libs.androidx.navigation)
    implementation(libs.kserialization.json)

    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.service)

    implementation(libs.google.maps)
    implementation(libs.p.services.maps)
    implementation(libs.p.services.location)
    implementation(libs.c.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}