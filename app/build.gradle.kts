import java.util.UUID

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("io.gitlab.arturbosch.detekt")
    kotlin("kapt")
}

tasks.register("genUUID") {
    val uuid = UUID.randomUUID().toString()
    val modelDir = file("$buildDir/generated/assets/model-ru")
    val uuidFile = file("$modelDir/uuid")

    doLast {
        modelDir.mkdirs()
        uuidFile.writeText(uuid)
    }
}

android {
    namespace = "com.example.note"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.note"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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
            jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
        }
    }
    buildFeatures {
        compose = true
    }

    sourceSets {
        getByName("main") {
            assets.srcDirs("$buildDir/generated/assets")
        }
    }
}

detekt {
    autoCorrect = true
    toolVersion = "1.23.8"
    config.setFrom(files("$rootDir/detekt.yml"))
    buildUponDefaultConfig = true
    source.setFrom(
        files(
            "$projectDir/src/main/java",
            "$projectDir/src/main/kotlin"
        )
    )
}

dependencies {
    //Compose
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.androidx.navigation.compose.android)
    implementation(libs.androidx.material.icons.extended)

    //Koin
    implementation (libs.koin.android.v420)
    implementation (libs.koin.androidx.compose)

    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.material3.android)
    kapt(libs.androidx.room.compiler)

    //Google fonts
    implementation(libs.ui.text.google.fonts)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    //Vosk
    implementation(libs.vosk.android)
    implementation(libs.tensorflow.lite)
}

android {
    packaging {
        resources {
            excludes += "/META-INF/**"
        }
    }
}

tasks.named("preBuild").configure {
    dependsOn("genUUID")
}