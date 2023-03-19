import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin(Plugins.multiplatform)
    // alias(libs.plugins.multiplatform)
    kotlin(Plugins.nativeCocoaPods)
    // alias(libs.plugins.nativeCocoapod)
    id(Plugins.androidLibrary)
    alias(libs.plugins.kotlinX.serialization.plugin)
    alias(libs.plugins.kmp.nativeCoroutines.plugin)
    // alias(libs.plugins.sqlDelight.plugin)
    alias(libs.plugins.buildKonfig)
}

android {
    compileSdk = AndroidSdk.compileSdkVersion
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = AndroidSdk.minSdkVersion
        targetSdk = AndroidSdk.targetSdkVersion
    }
}

kotlin {
    android()

    jvm()

    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget = when {
        System.getenv("SDK_NAME")?.startsWith("iphoneos") == true -> ::iosArm64
        System.getenv("NATIVE_ARCH")?.startsWith("arm") == true -> ::iosSimulatorArm64
        else -> ::iosX64
    }
    iosTarget("iOS") {}
    version = "1"
    cocoapods {
        // version = "1"

        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        // ios.deploymentTarget = "14.1"
        podfile = project.file("../iOSNotflix/Podfile")

        framework {
            baseName = "shared"
            isStatic = false
        }
    }

    sourceSets {
        sourceSets["commonMain"].dependencies {
            implementation(libs.kotlinX.coroutines)

            api(libs.koin.core)

            api(libs.ktor.core)
            api(libs.ktor.cio)
            implementation(libs.ktor.contentNegotiation)
            implementation(libs.ktor.json)
            implementation(libs.ktor.logging)

            implementation(libs.kotlinX.serializationJson)

            implementation(libs.multiplatformSettings.noArg)
            implementation(libs.multiplatformSettings.coroutines)

            api(libs.napier)

            implementation(libs.kotlinX.dateTime)
        }

        sourceSets["commonTest"].dependencies {
            implementation(kotlin("test"))
            implementation(libs.ktor.mock)
            implementation(libs.kotlinX.testResources)
            implementation(libs.kotlinX.coroutines.test)
            implementation(libs.multiplatformSettings.test)
        }

        sourceSets["androidMain"].dependencies {
        }

        sourceSets["androidTest"].dependencies {}

        sourceSets["iOSMain"].dependencies {
        }

        sourceSets["iOSTest"].dependencies {}

        sourceSets["jvmMain"].dependencies {
        }

        sourceSets["jvmTest"].dependencies {}
    }
}

buildkonfig {
    packageName = "com.vickikbt.shared"

    defaultConfigs {
        buildConfigField(STRING, "API_KEY", properties["api_key"].toString())
    }
}
