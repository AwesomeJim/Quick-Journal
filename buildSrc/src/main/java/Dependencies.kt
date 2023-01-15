const val kotlinVersion = "1.7.20"

object BuildPlugins {

    object Versions {
        const val buildToolsVersion = "7.4.0"
        const val hilt = "2.44.2"
        const val googleGms = "4.3.14"

    }

    /*
    id 'com.android.application'
    id 'kotlin-android'
    id "kotlin-parcelize"
    id "kotlin-kapt"
    id 'dagger.hilt.android.plugin'
     */
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.buildToolsVersion}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    const val hiltAndroidGradlePlugin =
        "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "kotlin-android"
    const val kotlinKapt = "kotlin-kapt"
    const val kotlinParcelize = "kotlin-parcelize"
    const val daggerHilt = "dagger.hilt.android.plugin"
    const val kotlinAndroidExtensions = "kotlin-android-extensions"
    const val googleGms = "com.google.gms:google-services:${Versions.googleGms}"
}

object AndroidSdk {
    const val minSdkVersion = 21
    const val targetSdkVersion = 33
    const val compileSdkVersion = targetSdkVersion
    const val buildToolsVersion = "33.0.0"
}

object Libraries {
    private object Versions {
        object AndroidX {
            const val core = "1.9.0"
            const val appcompat = "1.6.0"
            const val media = "1.6.0"
            const val legacySupportV4 = "1.0.0"
            const val constraintlayout = "2.1.4"
            const val vectorDrawable = "1.1.0"
            const val browser = "1.4.0"
            const val multidex = "2.0.1"
            const val navigationFragmentKtx = "2.5.3"
            const val lifecycle = "2.5.1"
            const val roomDb = "2.4.3"
            const val lifecycleExtensions = "2.2.0"
        }

        object Google {
            const val material = "1.7.0"
            const val gson = "2.9.0"
            const val firebaseCore = "21.1.1"
            const val gms = ""
        }

        const val picasso = "2.71828"
        const val circleimageview = "3.1.0"
        const val moshi = "1.9.3"
        const val navigation = "1.0.0"
        const val retrofit = "2.9.0"
        const val retrofit_coroutines_adapter = "0.9.2"
        const val recyclerview = "1.2.1"
        const val room_db = "2.3.0"
        const val material = "1.5.0"
        const val timber = "5.0.1"
        const val httplogging = "5.0.0-alpha.6"
    }

    const val ktxCore = "androidx.core:core-ktx:${Versions.AndroidX.core}"
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
    const val AndroidXAppCompat = "androidx.appcompat:appcompat:${Versions.AndroidX.appcompat}"
    const val AndroidXMedia = "androidx.media:media:${Versions.AndroidX.media}"
    const val AndroidXlegacySupportV4 =
        "androidx.legacy:legacy-support-v4:${Versions.AndroidX.legacySupportV4}"

    const val AndroidXconstraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.AndroidX.constraintlayout}"
    const val googleMaterial =
        "com.google.android.material:material:${Versions.Google.material}"

    const val AndroidXvectorDrawable =
        "androidx.vectordrawable:vectordrawable-animated:${Versions.AndroidX.vectorDrawable}"
    const val AndroidXbrowser = "androidx.browser:browser:${Versions.AndroidX.browser}"

    const val AndroidXmutidex = "androidx.multidex:multidex:${Versions.AndroidX.multidex}"
    const val AndroidXnavigationFragmentKtx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.AndroidX.navigationFragmentKtx}"
    const val AndroidXnavigationUiKtx =
        "androidx.navigation:navigation-ui-ktx:${Versions.AndroidX.navigationFragmentKtx}"
    const val AndroidXlifecyclelivedataKtx =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.AndroidX.lifecycle}"

    const val AndroidXRoomDb = "androidx.room:room-runtime:${Versions.AndroidX.roomDb}"

    const val AndroidXRoomDbCompiler = "androidx.room:room-compiler:${Versions.AndroidX.roomDb}"
    const val AndroidXRoomDbKtx = "androidx.room:room-ktx:${Versions.AndroidX.roomDb}"
    const val AndroidXlifecycleRuntimeKtx =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.AndroidX.lifecycle}"

    const val AndroidXlifecycleExtensions =
        "androidx.lifecycle:lifecycle-extensions:${Versions.AndroidX.lifecycleExtensions}"
    const val AndroidXlifecycleCompiler =
        "androidx.lifecycle:lifecycle-compiler:${Versions.AndroidX.lifecycle}"
    const val daggerHilt = "com.google.dagger:hilt-android:${BuildPlugins.Versions.hilt}"
    const val daggerHiltCompiler = "com.google.dagger:hilt-compiler:${BuildPlugins.Versions.hilt}"

    const val firebaseCore = "com.google.firebase:firebase-core:${Versions.Google.firebaseCore}"
    const val circleimageview = "de.hdodenhof:circleimageview:${Versions.circleimageview}"
    const val picasso = "com.squareup.picasso:picasso:${Versions.picasso}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"


}

object TestLibraries {
    private object Versions {
        const val junit4 = "4.13.2"
        const val junit = "1.1.5"
        const val espresso = "3.5.1"
        const val truth = "1.1.3"
        const val hilt = "2.44.2"
        const val core = "2.1.0"
        const val coroutines = "1.6.4"
    }

    const val junit4 = "junit:junit:${Versions.junit4}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val junit = "androidx.test.ext:junit:${Versions.junit}"
    const val truth = "com.google.truth:truth:${Versions.truth}"
    const val hiltAndroidTesting = "com.google.dagger:hilt-android-testing:${Versions.hilt}"
    const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val core = "androidx.arch.core:core-testing:${Versions.core}"
    const val coroutinesTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"

}