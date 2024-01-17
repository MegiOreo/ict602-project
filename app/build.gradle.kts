plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.groupprojectict602"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.groupprojectict602"
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

    buildFeatures { viewBinding= true }
}

dependencies {

    implementation("com.journeyapps:zxing-android-embedded:4.3.0")// Use the latest version
    implementation ("com.google.zxing:core:3.4.1")
    implementation ("com.google.mlkit:barcode-scanning:17.2.0") // Ensure you use the latest version
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation ("com.google.firebase:firebase-firestore:24.10.0")  // Use the latest version
    implementation ("androidx.cardview:cardview:1.0.0")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}