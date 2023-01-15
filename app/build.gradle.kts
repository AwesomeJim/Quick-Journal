plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinParcelize)
    id(BuildPlugins.kotlinKapt)
    id(BuildPlugins.daggerHilt)
//  id 'com.google.gms.google-services'
}
android {
    compileSdkVersion(AndroidSdk.compileSdkVersion)
    defaultConfig {
        minSdkVersion(AndroidSdk.minSdkVersion)
        targetSdkVersion(AndroidSdk.targetSdkVersion)
        applicationId = "com.jim.quickjournal"
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        //   testInstrumentationRunner 'androidx.test.runner.AndroidJUnitRunner'
        testInstrumentationRunner = "com.jim.quickjournal.HiltTestRunner"
    }
    buildTypes {
        getByName("release") {
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
    implementation(Libraries.ktxCore)

    implementation(Libraries.AndroidXAppCompat)
    implementation(Libraries.AndroidXMedia)
    implementation(Libraries.AndroidXlegacySupportV4)
    implementation(Libraries.AndroidXconstraintLayout)
    implementation(Libraries.googleMaterial)
    implementation(Libraries.AndroidXvectorDrawable)
    implementation(Libraries.AndroidXbrowser)

    implementation(Libraries.AndroidXmutidex)
    //navigation
    implementation(Libraries.AndroidXnavigationFragmentKtx)
    implementation(Libraries.AndroidXnavigationUiKtx)

    implementation(Libraries.AndroidXlifecyclelivedataKtx)
    implementation(Libraries.AndroidXlifecyclelivedataKtx)

    // --------Hilt Dependency Injection------------------------------
    implementation(Libraries.daggerHilt)
    kapt(Libraries.daggerHiltCompiler)

    implementation(Libraries.timber)


    //Room Local Database implementation------------------------------------------------------------
    implementation(Libraries.AndroidXRoomDb)
    annotationProcessor(Libraries.AndroidXRoomDbCompiler)

    // To use Kotlin annotation processing tool (kapt)
    kapt(Libraries.AndroidXRoomDbCompiler)


    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(Libraries.AndroidXRoomDbKtx)


    //LifeCycle
    implementation(Libraries.AndroidXlifecycleRuntimeKtx)
    implementation(Libraries.AndroidXlifecycleExtensions)
    annotationProcessor(Libraries.AndroidXlifecycleCompiler)


    //Firebase dependencies-------------------------------------------------------------------------
    implementation(Libraries.firebaseCore) //firebase core
//  implementation 'com.firebaseui:firebase-ui-firestore:8.0.0' //FirebaseUI for Cloud Firestore
//  implementation 'com.firebaseui:firebase-ui-auth:8.0.0' //FirebaseUI for Firebase Auth

    implementation(Libraries.picasso) //Load photos
    implementation(Libraries.circleimageview)

    testImplementation(TestLibraries.junit4)
    androidTestImplementation(TestLibraries.junit)
    androidTestImplementation(TestLibraries.espresso)
    //    local unit test
    testImplementation(TestLibraries.truth)
//    instrumentation test
    androidTestImplementation(TestLibraries.junit)
    androidTestImplementation(TestLibraries.espresso)
    androidTestImplementation(TestLibraries.hiltAndroidTesting)
    kaptAndroidTest(TestLibraries.hiltAndroidCompiler)
    androidTestImplementation(TestLibraries.core)
    androidTestImplementation(TestLibraries.coroutinesTest)
    androidTestImplementation(TestLibraries.junit)
    androidTestImplementation(TestLibraries.truth)

}
