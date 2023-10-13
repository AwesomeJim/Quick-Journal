@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.com.google.dagger.hilt)
    id(libs.plugins.com.google.devtools.ksp.get().pluginId)
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
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()

    }

    kotlinOptions {
        jvmTarget = libs.versions.jvm.target.get()
        freeCompilerArgs += listOf("-opt-in=kotlin.RequiresOptIn")
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = libs.versions.jvm.target.get()
            suppressWarnings = true
        }
    }

    hilt {
        enableAggregatingTask = true
        enableExperimentalClasspathAggregation = true
    }


    testOptions {
        packaging {
            jniLibs {
                useLegacyPackaging = true
            }
        }
        unitTests.isReturnDefaultValues = true
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
    // -------AndroidX and Jetpack Core --------------
    implementation(libs.bundles.androidx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewModelCompose)

    // --------Hilt Dependency Injection--------------
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)

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

    //------------Timber logging----------
    implementation(libs.timber)

    //--------Room Local Database implementation----------
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.kotlinx.coroutines.android)


    //Firebase dependencies-------------------------------------------------------------------------
    implementation(libs.firebaseCore) //firebase core
//  implementation 'com.firebaseui:firebase-ui-firestore:8.0.0' //FirebaseUI for Cloud Firestore
//  implementation 'com.firebaseui:firebase-ui-auth:8.0.0' //FirebaseUI for Firebase Auth

    implementation(libs.picasso) //Load photos
    implementation(libs.circleimageview)


    //-----------local unit test-----------
    testImplementation(libs.test.truth)
    testImplementation(libs.test.junit4)
    testImplementation(libs.turbine)
    testImplementation(libs.mock.android)
    testImplementation(libs.mock.agent)
    testImplementation(libs.test.truth)
    testImplementation(libs.test.coroutines.test)
    testImplementation(libs.turbine)

    //-----------instrumentation test-----------
    //androidTestImplementation(libs.espresso)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)
    androidTestImplementation(libs.test.testingcore)
    androidTestImplementation(libs.test.coroutines.test)
    androidTestImplementation(libs.test.truth)
    androidTestImplementation(libs.turbine)
    androidTestImplementation(libs.mock.android)
    androidTestImplementation(libs.mock.agent)

}
