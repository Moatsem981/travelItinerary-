plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.travelappcw"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.travelappcw"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // AndroidX Core Dependencies
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Navigation Component (Manually Added)
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // Firebase Firestore & Core SDK (Manually Added)
    implementation("com.google.firebase:firebase-firestore:25.1.1")
    implementation("com.google.firebase:firebase-core:21.1.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")

    // Firebase Database (Manually Added)
    implementation("com.google.firebase:firebase-database-ktx:20.3.1")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")

    // RecyclerView (Manually Added)
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Firebase In-App Messaging (Manually Added)
    implementation("com.google.firebase:firebase-inappmessaging-display:20.3.1")

    implementation ("com.squareup.picasso:picasso:2.71828")

    // AndroidX Media Library (Manually Added)
    implementation("androidx.media3:media3-common:1.3.1")

    // AndroidX Annotation (Manually Added)
    implementation("androidx.annotation:annotation:1.7.0")

    // Lifecycle Components (Manually Added)
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    // GridLayout Dependency (Manually Added)
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    implementation("com.google.firebase:firebase-auth:22.2.0") // Firebase Authentication
    implementation("com.google.firebase:firebase-firestore:25.1.1") // Firestore Database
    implementation("androidx.gridlayout:gridlayout:1.0.0")

    // Unit Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

apply(plugin = "com.google.gms.google-services")  // Correct Kotlin DSL plugin application
