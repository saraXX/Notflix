plugins {
    id(Plugins.androidApplication)
    kotlin(Plugins.android)

    id(Plugins.googleService)
    id(Plugins.appDistribution)
    id(Plugins.crashlytics)
    id(Plugins.performance)
}

android {
    compileSdk = AndroidSdk.compileSdkVersion

    defaultConfig {
        applicationId = AndroidSdk.applicationId

        minSdk = AndroidSdk.minSdkVersion
        targetSdk = AndroidSdk.targetSdkVersion
        versionCode = AndroidSdk.versionCode
        versionName = AndroidSdk.versionName

        testInstrumentationRunner = AndroidSdk.testInstrumentationRunner
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
}

dependencies {
    api(project(BuildModules.shared))

    implementation(libs.androidX.core)

    implementation(libs.material)

    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.tooling)
    implementation(libs.compose.runtime)
    implementation(libs.compose.util)
    implementation(libs.compose.activity)

    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.insets)
    implementation(libs.accompanist.animation)
    implementation(libs.accompanist.systemUIController)
    implementation(libs.accompanist.materialPlaceHolder)
    implementation(libs.accompanist.pagerIndicator)

    implementation(libs.lifecycle.runtime)

    // Koin-Dependency injection
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    // Compose Navigation-Navigation between various screens
    implementation(libs.navigation.compose)

    // Coil-Image Loader
    implementation(libs.coil)

    // Palette-Used to extract color palette from images
    implementation(libs.palette)

    // Gowtham Compose Rating Bar
    implementation(libs.compose.ratingBar)

    // Collapsing toolbar
    implementation(libs.oneBoneToolbar)

    // Leak Canary - Memory leaks
    debugImplementation(libs.leakCanary)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.performance)

    testImplementation(libs.jUnit)
    testImplementation(libs.googleTruth)
    testImplementation(libs.kotlinX.coroutines.test)

    androidTestImplementation(libs.test.rules)
    androidTestImplementation(libs.test.runner)
}
