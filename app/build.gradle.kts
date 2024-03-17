plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "local.defaults.properties"
}

android {
    namespace = "ru.vaihdass.aikataulus"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.vaihdass.aikataulus"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_BASE_URL", "\"https://aikataulus.ru/\"")

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )

            }
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

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    val calendarLibraryVersion = "2.5.0"
    implementation("com.kizitonwose.calendar:view:${calendarLibraryVersion}")

    val constraintLayoutVersion = "2.1.4"
    implementation("androidx.constraintlayout:constraintlayout:${constraintLayoutVersion}")

    val fragmentVersion = "1.6.2"
    implementation("androidx.fragment:fragment-ktx:${fragmentVersion}")

    val coreCtxVersion = "1.12.0"
    implementation("androidx.core:core-ktx:$coreCtxVersion")

    val appcompatVersion = "1.6.1"
    implementation("androidx.appcompat:appcompat:$appcompatVersion")

    val androidMaterialVersion = "1.11.0"
    implementation("com.google.android.material:material:$androidMaterialVersion")

    val viewBindingDelegateVersion = "1.5.9"
    implementation("com.github.kirich1409:viewbindingpropertydelegate-noreflection:$viewBindingDelegateVersion")

    val navigationVersion = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")

    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    "kapt"("androidx.room:room-compiler:$roomVersion")

    val coroutinesVersion = "1.7.1"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    val lifecycleVersion = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${lifecycleVersion}")

    val gsonVersion = "2.10.1"
    implementation("com.google.code.gson:gson:$gsonVersion")

    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-jackson:$retrofitVersion")

    val loggingVersion = "4.12.0"
    implementation("com.squareup.okhttp3:logging-interceptor:$loggingVersion")

    val daggerVersion = "2.51"
    implementation("com.google.dagger:dagger:$daggerVersion")
    "kapt"("com.google.dagger:dagger-compiler:$daggerVersion")

    val googleTasksVersion = "v1-rev71-1.25.0"
    implementation("com.google.apis:google-api-services-tasks:$googleTasksVersion")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}