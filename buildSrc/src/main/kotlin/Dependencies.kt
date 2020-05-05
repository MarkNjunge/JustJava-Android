object AndroidConfig {
    val minSdk = 21
    val targetSdk = 28
    val compiledSdk = 28
}

object Versions {
    val kotlin = "1.3.70"
    val coroutines = "1.3.5"
    val material = "1.0.0"
    val cardview = "1.0.0"
    val legacySupport = "1.0.0"
    val appCompat = "1.1.0"
    val recyclerview = "1.1.0-alpha06"
    val retrofit = "2.6.0"
    val retrofitCoroutinesAdapter = "0.9.2"
    val okhttpLogging = "4.0.0"
    val timber = "4.7.1"
    val room = "2.1.0"
    val constraintLayout = "2.0.0-beta2"
    val picasso = "2.71828"
    val junit = "4.12"
    val testRunner = "1.3.0-alpha01"
    val espresso = "3.3.0-alpha01"
    val testRules = "1.3.0-alpha01"
    val koin = "2.0.1"
    val firebaseMessaging = "19.0.1"
    val mockk = "1.9.2"
    val androidXTestCore = "1.0.0"

    const val viewModelKtx = "2.1.0"
    const val shimmer = "0.5.0"
    const val archCoreTesting = "2.1.0"
    const val playServices = "17.0.0"
    const val serialization = "0.20.0"
    const val retrofitSerialization = "0.4.0"
    const val places = "2.0.0"
    const val sentry = "1.7.27"
    const val sentryGradlePlugin = "1.7.27"
    const val lifecycleRuntime = "2.2.0"
}

object Dependencies {
    val androidGradlePlugin = "com.android.tools.build:gradle:3.4.1"
    val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    val googleServices = "com.google.gms:google-services:4.2.0"

    val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    val corouinesPlayServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.coroutines}"
    val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
    val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Versions.serialization}"
    val serializationPlugin = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"

    const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.viewModelKtx}"
    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntime}"

    val material = "com.google.android.material:material:${Versions.material}"
    val cardview = "androidx.cardview:cardview:${Versions.cardview}"
    val legacySupport = "androidx.legacy:legacy-support-v4:${Versions.legacySupport}"
    val appcompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    val recycerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"

    val firebaseMessaging = "com.google.firebase:firebase-messaging:${Versions.firebaseMessaging}"

    val playServicesAuth = "com.google.android.gms:play-services-auth:${Versions.playServices}"
    val playServicesMaps = "com.google.android.gms:play-services-maps:${Versions.playServices}"
    val places = "com.google.android.libraries.places:places:${Versions.places}"

    val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    val roomKotlin = "androidx.room:room-ktx:${Versions.room}"
    val roomCompiler = "androidx.room:room-compiler:${Versions.room}"


    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofitCoroutinesAdapter = "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:${Versions.retrofitCoroutinesAdapter}"
    val retrofitSerialization = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.retrofitSerialization}"
    val okhttpLogging = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttpLogging}"


    val picasso = "com.squareup.picasso:picasso:${Versions.picasso}"
    val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    val koin = "org.koin:koin-android:${Versions.koin}"
    val koinViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    val shimmer = "com.facebook.shimmer:shimmer:${Versions.shimmer}"

    val sentryGradlePlugin = "io.sentry:sentry-android-gradle-plugin:${Versions.sentryGradlePlugin}"
    val sentry = "io.sentry:sentry-android:${Versions.sentry}"

    val mockk = "io.mockk:mockk:${Versions.mockk}"
    val mockAndroid = "io.mockk:mockk-android:${Versions.mockk}"

    val junit = "junit:junit:${Versions.junit}"
    val androidXTestCore = "androidx.test:core:${Versions.androidXTestCore}"
    val archCoreTesting = "androidx.arch.core:core-testing:${Versions.archCoreTesting}"
    val testRunner = "androidx.test:runner:${Versions.testRunner}"
    val testRules = "androidx.test:rules:${Versions.testRules}"
    val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    val espressoIntents = "androidx.test.espresso:espresso-intents:${Versions.espresso}"
    val espressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
}