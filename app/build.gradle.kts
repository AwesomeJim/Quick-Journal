@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id(libs.plugins.com.google.devtools.ksp.get().pluginId)
    alias(libs.plugins.com.google.dagger.hilt)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
    id(libs.plugins.kotlin.parcelize.get().pluginId)
}

android {
    compileSdk = libs.versions.compileSdkVersion.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdkVersion.get().toInt()
        targetSdk = libs.versions.targetSdkVersion.get().toInt()
        applicationId = "com.jim.quickjournal"
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        //   testInstrumentationRunner 'androidx.test.runner.AndroidJUnitRunner'
        testInstrumentationRunner = "com.jim.quickjournal.HiltTestRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
        }
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = true
        //disable("ContentDescription")
    }
    namespace = "com.jim.quickjournal"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    // Core with Ktx
    implementation(libs.ktxCore)

    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.mutidex)
    implementation(libs.androidx.legacySupportV4)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.googleMaterial)
    implementation(libs.androidx.vectorDrawable)
    implementation(libs.androidx.browser)

    implementation(libs.androidx.mutidex)
    //navigation
    implementation(libs.androidx.navigationFragmentKtx)
    implementation(libs.androidx.navigationUiKtx)

    implementation(libs.androidx.lifecyclelivedataKtx)
    implementation(libs.androidx.lifecyclelivedataKtx)

    // --------Hilt Dependency Injection------------------------------
    implementation(libs.daggerHilt)
    kapt(libs.daggerHiltCompiler)

    implementation(libs.timber)


    //Room Local Database implementation------------------------------------------------------------
    implementation(libs.androidx.roomDb)
    annotationProcessor(libs.androidx.roomDbCompiler)

    // To use Kotlin annotation processing tool (KSP)
    ksp(libs.androidx.roomDbCompiler)


    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.roomDbKtx)


    //LifeCycle
    implementation(libs.androidx.lifecycleRuntimeKtx)
    implementation(libs.androidx.lifecycleExtensions)
    annotationProcessor(libs.androidx.lifecycleCompiler)


    //Firebase dependencies-------------------------------------------------------------------------
    implementation(libs.firebaseCore) //firebase core
//  implementation 'com.firebaseui:firebase-ui-firestore:8.0.0' //FirebaseUI for Cloud Firestore
//  implementation 'com.firebaseui:firebase-ui-auth:8.0.0' //FirebaseUI for Firebase Auth

    implementation(libs.picasso) //Load photos
    implementation(libs.circleimageview)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.espresso)
    //    local unit test
    testImplementation(libs.truth)
//    instrumentation test
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.espresso)
    androidTestImplementation(libs.hiltAndroidTesting)
    kaptAndroidTest(libs.hiltAndroidCompiler)
    androidTestImplementation(libs.testingcore)
    androidTestImplementation(libs.coroutinesTest)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.truth)

}
