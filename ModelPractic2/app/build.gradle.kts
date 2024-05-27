plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.modelpractic2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.modelpractic2"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Test libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Including JAR files from 'libs' directory
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Specific library from a custom group
    implementation("cz.msebera.android:httpclient:4.4.1.2")

    // Project dependency (assuming you have a project module named ':jsoup-1.10.2')
    implementation(project(":jsoup-1.10.2"))
}