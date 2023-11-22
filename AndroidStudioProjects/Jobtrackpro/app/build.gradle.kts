import java.util.Properties
plugins {
    id("com.android.application")
    id ("com.google.gms.google-services")
}

android {
    namespace = "com.NEU23FallGroup7.jobtrackpro"
    compileSdk = 33
    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.NEU23FallGroup7.jobtrackpro"
        minSdk = 27
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        Properties().apply {
            load(project.rootProject.file("local.properties").reader())
            buildConfigField("String", "API_KEY", "\"${getProperty("API_KEY")}\"")
            buildConfigField("String", "API_ID", "\"${getProperty("API_ID")}\"")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.3.1")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-database")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.code.gson:gson:2.8.9")

}