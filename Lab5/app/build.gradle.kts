plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.sensor"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.sensor"
        minSdk = 24
        targetSdk = 33
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation ("androidx.activity:activity-compose:1.7.2")
    implementation ("androidx.compose.material3:material3")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:1.7.2")

    implementation ("androidx.compose.material3:material3:1.3.0")
    implementation ("androidx.compose.material3:material3-window-size-class:1.3.0")
    implementation ("androidx.compose.material3:material3-adaptive-navigation-suite:1.4.0-alpha01")

    implementation ("androidx.compose.ui:ui")
    //implementation ("androidx.compose.ui:ui:1.5.0")
    implementation ("androidx.compose.foundation:foundation:1.7.2")
    implementation ("androidx.compose.ui:ui:1.5.4")

    debugImplementation("androidx.compose.ui:ui-tooling:1.7.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}