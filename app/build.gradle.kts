plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.film_api"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.film_api"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    testImplementation("junit:junit:4.14-SNAPSHOT")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.google.android.material:material:1.11.0")

    // Room components
    implementation ("androidx.room:room-runtime:2.4.3")
    ksp("androidx.room:room-compiler:2.4.3")
    implementation ("androidx.room:room-ktx:2.4.3")
    androidTestImplementation ("androidx.room:room-testing:2.4.3")

    // Lifecycle components
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-common-java8:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.6.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.6.0")

    //Kotlin
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.72")
    api ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
    api ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.5")
    implementation ("androidx.activity:activity-ktx:1.8.2")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.12.0")

    //Hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    ksp("com.google.dagger:dagger-compiler:2.48.1")
    ksp("com.google.dagger:hilt-android-compiler:2.48.1")
    androidTestImplementation ("com.google.dagger:hilt-android-testing:2.41")
    kspAndroidTest ("com.google.dagger:hilt-android-compiler:2.42")
}